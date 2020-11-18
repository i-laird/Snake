package server;

import server.communication.SynchronizedMessageHandler;
import server.communication.message.*;
import server.communication.message.Error;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

    public static final boolean NOT_READY = false;
    public static final boolean READY = true;

    private static class MessageTypeException extends Throwable{
        private String message;
        private MessageTypeException(){
            super();
        }
        private MessageTypeException(String s){
            super(s);
        }
    }

    // holds all of the lobbies that are currently open in the game
    // once a game has started the lobby closes
    private static Set<Lobby> lobbies = new HashSet<>();

    // holds all of the players in the game
    private static Set<Player> players = new HashSet<>();

    private static Map<Player, Server> playerToServer = new HashMap();

    /**
     * This is a server for the Snake game
     *
     * @param args
     *  port: the port that the server is to run on
     */
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        try {

            // used in order to accept connections
            ServerSocket ss = new ServerSocket(port);

            // create thread pool
            ExecutorService gameBeginExecutorService = Executors.newFixedThreadPool(10);

            // forever accept connections
            while(true){

                // block for a connection
                Socket connection = ss.accept();
                Server server = new Server(connection);
                gameBeginExecutorService.submit(server);
            }
        }catch(IOException e){}
    }

    // to communicate with the client
    private Socket socket = null;

    private SynchronizedMessageHandler messageHandler = null;

    private String approvedClientName;

    private Player player;

    private Lobby lobby;

    private Server(Socket s){
        this.socket = s;
    }

    /**
     * Runs when a client first connects to the Server
     */
    public void run(){

        // set up the message handler
        try {
            this.messageHandler = new SynchronizedMessageHandler(this.socket);
        }catch(IOException e){}
        this.handleClient();
    }

    /**
     * Handles a client connecting to the server
     *
     * Steps:
     *      1) tell client that the connection has been established
     *      2) get the client name
     *      3) tell the client what the lobbies are
     *      4) allow the client to either open or join a lobby
     *      5) Wait for the player to ready
     */
    private void handleClient(){
        try {
            try {
                // first send confirmation that connection has been established
                messageHandler.sendMessage(new ACK("Connection Established"));

                // get the name of this player
                this.player = handleClientName();

                playerToServer.put(this.player, this);

                // send the lobbies that are currently open
                sendLobbies();

                // handle player's lobby selection
                this.lobby = handleLobby();

                // block until player readies up
                idleLobbyHandle();

            } catch (MessageTypeException e) {
                messageHandler.sendMessage(new Error(e.getMessage()));
                closeConnection();
            }
        }
        catch(IOException | ClassNotFoundException e2){}
    }

    private Player handleClientName() throws IOException, ClassNotFoundException, MessageTypeException {
        Message receive = messageHandler.receiveMessage();

        // if the message is not a player name there is a communication error TERMINATE CONNECTION
        if(! (receive instanceof Register_Name)){
            throw new MessageTypeException("Error: REGISTER_NAME expected");
        }

        Register_Name clientName = (Register_Name) receive;
        Player proposedPlayer = new Player(clientName.getPlayerName());

        // if the name already exists prompt the player to send a new one
        if(!players.add(proposedPlayer)){
            messageHandler.sendMessage(new Error("Invalid player name"));
            return handleClientName(); // recursion
        }

        // if the name is not already in use send confirmation and use that name
        else {
            messageHandler.sendMessage(new ACK(approvedClientName));
        }
        return proposedPlayer;
    }

    /**
     * Helper method for telling client about the lobbies that exist
     * @throws IOException if connection error
     */
    private void sendLobbies() throws IOException {
        messageHandler.sendMessage(new Lobbies(new ArrayList<>(lobbies)));
    }

    /**
     * Handles Client lobby request
     *
     * Allows client to either create or join a lobby
     */
    private Lobby handleLobby() throws ClassNotFoundException, IOException, MessageTypeException {
        Message m = messageHandler.receiveMessage();

        if(m instanceof Create_Lobby){
            Create_Lobby create_lobby = (Create_Lobby)m;
            String lobbyName = create_lobby.getLobbyName();

            Lobby proposedLobby = new Lobby(lobbyName);
            proposedLobby.getPlayerToStatus().put(player, NOT_READY);

            // see if a lobby of this name already exists
            if (lobbies.add(proposedLobby)) {
                messageHandler.sendMessage(new ACK(lobbyName));
                return proposedLobby;
            }

            // if lobby already exists send invalid message and wait for new lobby choice
            else{
                messageHandler.sendMessage(new Error("Invalid lobby name"));
                return handleLobby(); // recursion
            }
        }

        else if( m instanceof Join_Lobby){
            Join_Lobby join_lobby = (Join_Lobby)m;
            Optional<Lobby> l = lobbies.stream().filter(x -> x.getLobbyName().equals(join_lobby.getLobbyName())).findFirst();
            if(l.isEmpty()){
                messageHandler.sendMessage(new Error("lobby does not exist"));
                return handleLobby();
            }
            l.get().getPlayerToStatus().put(player, NOT_READY);
            return l.get();
        }

        else{
            throw new MessageTypeException("Error: Either CREATE_LOBBY OR JOIN_LOBBY expected");
        }
    }

    private void idleLobbyHandle() throws IOException, ClassNotFoundException, MessageTypeException {

        // if the thread has been interrupted it means that the game is starting
        if(currentThread().isInterrupted()){
            notifyClientStart();
            return;
        }
        Message m = messageHandler.receiveMessage();

        // if the client wants to switch to a different lobby
        if (m instanceof Change_Lobby) {

            this.lobby.getPlayerToStatus().remove(this.player);

            // notify other clients in this lobby of the new state
            sendLobbies();

            // get the new lobby choice
            this.lobby = handleLobby();
            idleLobbyHandle();

        } else if (m instanceof Ready) {

            // save that this player is ready
            this.lobby.getPlayerToStatus().put(this.player, READY);

            // if all players are ready
            if (this.lobby.getPlayerToStatus().values().stream().reduce((x, y) -> {
                return x && y;
            }).get()) {

                this.lobby.setStart(READY);

                // send START to each client
                // for each client in the lobby interrupt that thread
                this.lobby.getPlayerToStatus().keySet().forEach(x -> {
                    playerToServer.get(x).interrupt();
                });

                notifyClientStart();
                return;
            }
            else {
                sendLobbies();
                if(currentThread().isInterrupted()){
                    notifyClientStart();
                }
                else {
                    try {
                        sleep(1000);
                    }catch(InterruptedException e){
                        notifyClientStart();
                    }
                }
            }
        }


    }

    private void notifyClientStart() throws IOException{
        this.messageHandler.sendMessage(new Start());
    }

    private void closeConnection() throws IOException{
        this.messageHandler.closeConnections();
        this.socket.close();
    }


}
