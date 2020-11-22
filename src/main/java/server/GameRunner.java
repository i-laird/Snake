package server;

import communication.Move;
import communication.message.Start;
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

public class GameRunner extends Thread{

    Set<Player> players;

    public void addPlayer(Player p){
        this.players.add(p);
    }

    private int port;

    private AsynchronousServerSocketChannel server;

    private List<Pair<Player, AsynchronousSocketChannel>> playerToSocket = new ArrayList<>();

    public void run(){

        // set up the async server for this game
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(null);
            port = ((InetSocketAddress)server.getLocalAddress()).getPort();
        } catch (IOException e) {

            //error unable to set up the async socket
        }

        // send start to each client
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

        // run the game
        runGame();
    }

    public void openConnections() throws ExecutionException, InterruptedException {
        List<Future<AsynchronousSocketChannel>> connectionFutures = new ArrayList<>(players.size());
        List<Pair<AsynchronousSocketChannel, ByteBuffer> > sockets = new ArrayList<>(players.size());
        List<Future<Integer>> readFutures = new ArrayList<>(players.size());

        for(int i = 0; i < players.size(); i++){
            Future<AsynchronousSocketChannel> future = server.accept();
            connectionFutures.add(future);
        }

        for(Future<AsynchronousSocketChannel> f : connectionFutures){
            sockets.add(new Pair<>(f.get(), ByteBuffer.allocate(32)));
        }

        for(Pair<AsynchronousSocketChannel, ByteBuffer> pair: sockets){
            readFutures.add(pair.getKey().read(pair.getValue()));
        }

        for(int i = 0; i < players.size(); i++){
            readFutures.get(i).get();
            String clientName = StandardCharsets.UTF_8.decode(sockets.get(i).getValue()).toString();
            Player p = players.stream().filter(x -> x.getPlayerName().equals(clientName)).findAny().get();
            playerToSocket.add(new Pair(p, sockets.get(i).getKey()));
        }
    }

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

        List<Future<Integer>> readFutures = new ArrayList<>(players.size());
        List<Future<Integer>> writeFutures = new ArrayList<>(players.size());
        List<ByteBuffer> byteBuffers = new ArrayList<>(players.size());
        List<Move> moves = new ArrayList<>(players.size());

        // allocate byte buffers for reads
        for (int i = 0; i < players.size(); i++) {
            byteBuffers.set(i, ByteBuffer.allocate(32));
        }

        while(true) {

            // get each players move
            for (int i = 0; i < players.size(); i++) {
                readFutures.set(i, playerToSocket.get(i).getValue().read(byteBuffers.get(i)));
            }

            for (int i = 0; i < players.size(); i++) {
                readFutures.get(i).get();
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(byteBuffers.get(i).array()));
                moves.set(i, new Move(in.readInt()));
            }

            // send all moves to each player
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
                writeFutures.set(i, playerToSocket.get(i).getValue().write(bb));
            }
        }
    }
}
