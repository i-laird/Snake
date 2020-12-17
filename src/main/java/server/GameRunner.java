/*
 * file: GameRunner.java
 * author: Ian Laird
 * project: Snake
 */

package server;

import communication.Move;
import communication.message.Start;
import enums.Direct;
import javafx.util.Pair;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Ian Laird
 *
 * Server for a old.game.
 *
 * While setting up the old.game is synchrnous, this step is asynchrnous.
 *
 * Repeatedly receives moves from each player and then sends it to each other player
 */
public class GameRunner extends Thread{

    private static final int GAME_WIDTH = 100;
    private static final int GAME_HEIGHT = 100;

    // holds all of the players that are in this old.game
    Set<Player> players;

    /**
     * adds a player to the old.game
     *
     * Note: this may only be run before a old.game has started
     * @param p the player to add to the old.game
     */
    public void addPlayer(Player p){
        this.players.add(p);
    }

    // the port that this old.game is running on
    private int port;

    // the async server socket for this old.game
    // this is used to accept connections
    private AsynchronousServerSocketChannel server;

    // maps players to the connections for that player
    private List<Pair<Player, AsynchronousSocketChannel>> playerToSocket = new ArrayList<>();

    // holds the moves that each player has made
    private List<Move> moves = null;

    // futures for writing moves to all clients
    List<Future<Integer>> writeMoveFutures = null;

    // futures for reading moves from the clients
    List<Future<Integer>> readMoveFutures = null;

    List<ByteBuffer> readbyteBuffers = null;

    /**
     * the run method for this thread
     *
     * manages running the old.game.
     *
     * Players will have option to play again as well. Currently all players need to
     * indicate that they want to play again in order to do so.
     */
    public void run(){

        // set up the async server for this old.game
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(null);
            port = ((InetSocketAddress)server.getLocalAddress()).getPort();
        } catch (IOException e) {

            //error unable to set up the async socket
        }

        // send start to each client
        // this step is synchronous
        this.players.forEach(x -> {
            try {
                x.getMessageHandler().sendMessage(new Start(port));
            } catch (IOException e) {}
        });

        // open a stream of communication for each client
        try {
            openConnections();
        } catch (ExecutionException | InterruptedException e) {}

        // send the player names that are in this lobby as well as the order of them
        sendPlayerNames();

        // run the old.game
        try {
            runGame();
        } catch (ExecutionException e) {
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * opens a async connection with each of the clients in this lobby
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void openConnections() throws ExecutionException, InterruptedException {
        List<Future<AsynchronousSocketChannel>> connectionFutures = new ArrayList<>(players.size());
        List<Pair<AsynchronousSocketChannel, ByteBuffer> > sockets = new ArrayList<>(players.size());
        List<Future<Integer>> readFutures = new ArrayList<>(players.size());

        // get a future for a connection for each player that we expect to join
        for(int i = 0; i < players.size(); i++){
            Future<AsynchronousSocketChannel> future = server.accept();
            connectionFutures.add(future);
        }

        // for each connection future
        for(Future<AsynchronousSocketChannel> f : connectionFutures){

            // associate a new bytebuffer with each connection
            // notice the blocking get
            sockets.add(new Pair<>(f.get(), ByteBuffer.allocate(32)));
        }

        // get a future for a read for every connection
        // this read will be the name of each client
        for(Pair<AsynchronousSocketChannel, ByteBuffer> pair: sockets){
            readFutures.add(pair.getKey().read(pair.getValue()));
        }

        for(int i = 0; i < players.size(); i++){

            // blocking get
            readFutures.get(i).get();
            String clientName = StandardCharsets.UTF_8.decode(sockets.get(i).getValue()).toString();
            Player p = players.stream().filter(x -> x.getPlayerName().equals(clientName)).findAny().get();
            playerToSocket.add(new Pair(p, sockets.get(i).getKey()));
        }
    }

    /**
     * sends the names of the players in the lobby to all of the clients
     *
     * Note that the order in which the player names are sent is also
     * the order in which player moves will be sent
     */
    public void sendPlayerNames(){
        for(int i = 0; i < playerToSocket.size(); i++){
            AsynchronousSocketChannel channel = playerToSocket.get(i).getValue();

            for(int j = 0; j < playerToSocket.size(); j++){
                if(i == j){
                    continue;
                }

                ByteBuffer writeBytes = ByteBuffer.wrap(playerToSocket.get(i).getKey().getPlayerName().getBytes(StandardCharsets.UTF_8));
                channel.write(writeBytes);
            }
        }
    }

    public void runGame() throws ExecutionException, InterruptedException, IOException {

        boolean newGame = true;

        readMoveFutures = new ArrayList<>(players.size());
        writeMoveFutures = new ArrayList<>(players.size());
        readbyteBuffers = new ArrayList<>(players.size());
        moves = new ArrayList<>(players.size());

        // allocate byte buffers for reads
        for (int i = 0; i < players.size(); i++) {
            readbyteBuffers.set(i, ByteBuffer.allocate(32));
        }

        while(true) {
            if(newGame){
                newGame = false;
                sendPlayerStartPos();
            }
            if(readMovesFromPlayers()){
                readMovesFromPlayers();
                // check if all players want to play again
                boolean allPlayersWantToPlay = moves.stream().allMatch(x -> {
                    if (x.getDirection() == Direct.PLAY_AGAIN) {
                        return true;
                    }
                    return false;
                });
                if(allPlayersWantToPlay){
                    newGame = true;
                }
                else {
                    break;
                }
            }
            else {
                // send all moves to each player
                writeMovesToPlayers();
            }

        }
    }

    /**
     * reads the moves from the players
     * @return true indicates that the old.game is over
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    private boolean readMovesFromPlayers() throws ExecutionException, InterruptedException, IOException {

        boolean gameOver = false;
        // get each players move
        for (int i = 0; i < players.size(); i++) {
            readMoveFutures.set(i, playerToSocket.get(i).getValue().read(readbyteBuffers.get(i)));
        }

        for (int i = 0; i < players.size(); i++) {
            readMoveFutures.get(i).get();
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(readbyteBuffers.get(i).array()));
            Move m = new Move(in.readInt());
            moves.set(i, m);
            if(m.getDirection() == Direct.GAME_OVER){
                gameOver = true;
            }
        }
        return gameOver;
    }

    private void sendPlayerStartPos(){

        // calculate the starting position for every player
        Set<Pair<Integer, Integer>> positionSet = new HashSet<>();
        List<Pair<Integer, Integer>> positions = new ArrayList<>(players.size());
        Random rand = new Random();

        for(int i = 0; i < players.size(); i++) {
            Pair<Integer, Integer> pair = null;
            do{
                int xPos = rand.nextInt() % GAME_WIDTH;
                int yPos = rand.nextInt() % GAME_HEIGHT;
                pair = new Pair<>(xPos, yPos);
                positions.set(i, pair);
            }while(!positionSet.add(pair)); //TODO check this
        }

        // create the string that will be sent over the network
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        positions.forEach(x -> {
            try {
                out.writeInt(x.getKey());
                out.writeInt(x.getValue());
            } catch (IOException e) {}
        });

        // now send the locations over the network to each player
        for(int i =0; i < players.size(); i++){
            ByteBuffer bb = ByteBuffer.wrap(bytes.toByteArray());
            writeMoveFutures.set(i, playerToSocket.get(i).getValue().write(bb));
        }

        waitForAllWriteFutures();

    }

    private void waitForAllWriteFutures(){
        writeMoveFutures.forEach(x -> {
            try {
                x.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private void writeMovesToPlayers() throws IOException {
        for (int i = 0; i < players.size(); i++) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            for (int j = 0; j < players.size(); j++) {
                if (i == j) {
                    continue;
                }
                out.writeInt(moves.get(j).getDirection().ordinal());
            }
            ByteBuffer bb = ByteBuffer.wrap(bytes.toByteArray());
            writeMoveFutures.set(i, playerToSocket.get(i).getValue().write(bb));
        }
        waitForAllWriteFutures();
    }
}
