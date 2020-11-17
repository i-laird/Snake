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
                Player player = handleClientName();

                // send the lobbies that are currently open
                sendLobbies();

                // handle player's lobby selection
                handleLobby(player);

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
    private void handleLobby(Player player) throws ClassNotFoundException, IOException, MessageTypeException {
        Message m = messageHandler.receiveMessage();

        if(m instanceof Create_Lobby){
            Create_Lobby create_lobby = (Create_Lobby)m;
            String lobbyName = create_lobby.getLobbyName();

            Lobby proposedLobby = new Lobby(lobbyName);
            proposedLobby.getPlayers().add(player);

            // see if a lobby of this name already exists
            if (lobbies.add(proposedLobby)) {
                messageHandler.sendMessage(new ACK(lobbyName));
            }

            // if lobby already exists send invalid message and wait for new lobby choice
            else{
                messageHandler.sendMessage(new Error("Invalid lobby name"));
                handleLobby(player); // recursion
                return;
            }
        }

        else if( m instanceof Join_Lobby){
            Join_Lobby join_lobby = (Join_Lobby)m;
            Optional<Lobby> l = lobbies.stream().filter(x -> x.getLobbyName().equals(join_lobby.getLobbyName())).findFirst();
            if(l.isEmpty()){
                messageHandler.sendMessage(new Error("lobby does not exist"));
                handleLobby(player);
            }
            l.get().getPlayers().add(player);
        }

        else{
            throw new MessageTypeException("Error: Either CREATE_LOBBY OR JOIN_LOBBY expected");
        }
    }

    private void closeConnection() throws IOException{
        this.messageHandler.closeConnections();
        this.socket.close();
    }


}
