package server;

import communication.SynchronizedMessageHandler;
import communication.message.*;
import communication.message.Error;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server extends Thread {

    /**
     * @author Ian Laird
     *
     * Used to indicate that an unexpected message has been received from the client
     */
    private static class MessageTypeException extends Throwable{
        /**
         * constructor
         */
        private MessageTypeException(){
            super();
        }

        /**
         * custom constructor
         * @param s message detailing the cause of the error
         */
        private MessageTypeException(String s){
            super(s);
        }
    }

    // indicates that a client in a old.game is not ready for the old.game to start
    public static final boolean NOT_READY = false;

    // indicates that a client in a old.game is ready for a old.game to start
    public static final boolean READY = true;

    // used to lock access to lobbies object
    // is a write is happening nothing else is allowed in
    // if a write is happening as many reads as we want can happen
    private static ReadWriteLock lobbiesRWlock = new ReentrantReadWriteLock();

    // holds all of the lobbies that are currently open in the old.game
    // once a old.game has started the lobby closes
    private static Set<Lobby> lobbies = new HashSet<>();

    // holds all of the players in the old.game
    private static Set<Player> players = new HashSet<>();

    // maps a player to the thread that is currently servicing them
    // this is useful so that we can stop threads
    private static Map<Player, Server> playerToServer = new HashMap();

    // this executes the threads that actually run games
    // one thread per old.game
    private static ExecutorService gameExecutorService = Executors.newFixedThreadPool(10);

    /**
     * This is a server for the Snake old.game
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
    // each client has its own socket
    private Socket socket = null;

    // used to read and write messages
    private SynchronizedMessageHandler messageHandler = null;

    // the name of the client that is interacting with this thread
    private String approvedClientName;

    // the player associated with this thread
    private Player player;

    // the lobby currently associated with this thread
    private Lobby lobby;

    /**
     * custom constructor
     * @param s the socket to be associated with this thread
     */
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

    /**
     * Handles a client registering with the server. Will reprompt for a new client name if the one given
     * if already taken by another client.
     *
     * @return the player that is created with the client name
     * @throws IOException if an IO exception occurs when communicating with the client
     * @throws ClassNotFoundException if the client sends an unexpected object over the wire
     * @throws MessageTypeException if the client sends the wrong type of message
     *    Allowed types of message to be sent are REGISTER_NAME
     */
    private Player handleClientName() throws IOException, ClassNotFoundException, MessageTypeException {
        Message receive = messageHandler.receiveMessage();

        // if the message is not a player name there is a communication error TERMINATE CONNECTION
        if(! (receive instanceof Register_Name)){
            throw new MessageTypeException("Error: REGISTER_NAME expected");
        }

        Register_Name clientName = (Register_Name) receive;
        Player proposedPlayer = new Player(clientName.getPlayerName(), this.socket);

        boolean flag;

        // if the name already exists prompt the player to send a new one
        synchronized (players) {
            flag = !players.add(proposedPlayer);
        }

        if(flag){
            messageHandler.sendMessage(new Error("Invalid player name"));
            return handleClientName(); // recursion
        }

        // if the name is not already in use send confirmation and use that name
        else {
            messageHandler.sendMessage(new ACK(approvedClientName));
        }
        proposedPlayer.setMessageHandler(this.messageHandler);
        return proposedPlayer;
    }

    /**
     * Helper method for telling client about the lobbies that exist
     *
     * @throws IOException if connection error
     */
    private void sendLobbies() throws IOException {
        lobbiesRWlock.readLock().lock();
        messageHandler.sendMessage(new Lobbies(new ArrayList<>(lobbies)));
        lobbiesRWlock.readLock().unlock();
    }

    /**
     * Handles Client lobby request
     *
     * Allows client to either create or join a lobby
     * @throws IOException if an IO exception occurs when communicating with the client
     * @throws ClassNotFoundException if the client sends an unexpected object over the wire
     * @throws MessageTypeException if the client sends the wrong type of message
     *    Allowed types of Message are CREATE_LOBBY | JOIN_LOBBY
     */
    private Lobby handleLobby() throws ClassNotFoundException, IOException, MessageTypeException {
        Message m = messageHandler.receiveMessage();

        if(m instanceof Create_Lobby){
            Create_Lobby create_lobby = (Create_Lobby)m;
            String lobbyName = create_lobby.getLobbyName();

            Lobby proposedLobby = new Lobby(lobbyName);
            proposedLobby.getPlayerToStatus().put(player, NOT_READY);

            // see if a lobby of this name already exists
            lobbiesRWlock.writeLock().lock();
            boolean flag = lobbies.add(proposedLobby);
            lobbiesRWlock.writeLock().unlock();

            if (flag) {
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

            lobbiesRWlock.readLock().lock();
            Optional<Lobby> l = lobbies.stream().filter(x -> x.getLobbyName().equals(join_lobby.getLobbyName())).findFirst();
            lobbiesRWlock.readLock().unlock();

            if(l.isEmpty()){
                messageHandler.sendMessage(new Error("lobby does not exist"));
                return handleLobby();
            }

            synchronized (l.get()) {
                l.get().getPlayerToStatus().put(player, NOT_READY);
            }

            return l.get();
        }

        else{
            throw new MessageTypeException("Error: Either CREATE_LOBBY OR JOIN_LOBBY expected");
        }
    }

    /**
     * method used to handle requests while a client is waiting in a lobby.
     *
     * CHANGE_LOBBY: if received the client is removed from this
     *     lobby and server goes back to get lobby step
     * READY: if received this client is marked as ready in the server
     *
     * @throws IOException if an IO exception occurs when communicating with the client
     * @throws ClassNotFoundException if the client sends an unexpected object over the wire
     * @throws MessageTypeException if the client sends the wrong type of message
     *    Allowed types of Message are CHANGE_LOBBY | READY
     */
    private void idleLobbyHandle() throws IOException, ClassNotFoundException, MessageTypeException {

        // if the thread has been interrupted it means that the old.game is starting
        if(currentThread().isInterrupted()){
            return;
        }
        Message m = messageHandler.receiveMessage();

        // if the client wants to switch to a different lobby
        if (m instanceof Change_Lobby) {

            synchronized (this.lobby) {
                this.lobby.getPlayerToStatus().remove(this.player);
            }

            // notify other clients in this lobby of the new state
            sendLobbies();

            synchronized (this.lobby) {
                if (allClientsInLobbyReady()) {
                    interruptOtherClientsInLobby();
                    initGameRunner();
                }
            }

            // get the new lobby choice
            this.lobby = handleLobby();
            idleLobbyHandle();

        } else if (m instanceof Ready) {

            boolean flag;

            // save that this player is ready
            synchronized (this.lobby) {
                this.lobby.getPlayerToStatus().put(this.player, READY);
                flag = allClientsInLobbyReady();

                if(flag){
                    this.lobby.setStart(READY);
                    interruptOtherClientsInLobby();
                    initGameRunner();
                }
            }

            if(!flag) {
                sendLobbies();
                if(!currentThread().isInterrupted()){
                    try {
                        sleep(1000);
                    }catch(InterruptedException e){}
                }
            }
        }
    }

    private void closeConnection() throws IOException{
        this.messageHandler.closeConnections();
        this.socket.close();
    }

    private boolean allClientsInLobbyReady(){
        return this.lobby.getPlayerToStatus().values().stream().reduce((x, y) -> {
            return x && y;
        }).get();
    }

    private void interruptOtherClientsInLobby(){

        // send START to each client
        // for each client in the lobby interrupt that thread
        this.lobby.getPlayerToStatus().keySet().forEach(x -> {
            playerToServer.get(x).interrupt();
        });
    }

    private void initGameRunner(){
        GameRunner newGame = new GameRunner();
        this.lobby.getPlayerToStatus().keySet().forEach(newGame::addPlayer);
        gameExecutorService.submit(newGame);
    }


}
