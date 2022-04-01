package client;

import communication.SynchronizedMessageHandler;
import communication.message.*;
import communication.message.Error;
import exception.MessageTypeException;
import server.Lobby;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/******************************************************************************
 *
 * ClientBackend.java
 *
 * author: Ian laird
 *
 * Created 7/29/21
 *
 * © 2021
 *
 ******************************************************************************/

public class ClientBackend {

    public enum STEPS {
        INIT_CONNECTION, NAME_REGISTRATION, LOBBY_LIST, LOBBY_SELECTION, READY_UP, STARTED
    }

    private STEPS currentStep;

    private Socket socket;

    private List<Lobby> lobbies;

    // used to read and write messages
    private SynchronizedMessageHandler messageHandler = null;

    public ClientBackend(String ip, int port){
        currentStep = STEPS.INIT_CONNECTION;
        try {
            // connect to this port number
            this.socket = new Socket(ip, port);

            this.messageHandler = new SynchronizedMessageHandler(this.socket);
        }catch(IOException e) {}
    }

    public Socket getSocket() {
        return socket;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    public STEPS getCurrentStep() {
        return currentStep;
    }

    public STEPS performInitialSteps(String str, boolean flag) throws IOException, ClassNotFoundException, MessageTypeException {
        switch (currentStep){
            case INIT_CONNECTION:
                Message m = messageHandler.receiveMessage();
                if (! (m instanceof ACK)){
                    throw new MessageTypeException("Error: ACK expected");
                }
                ACK message = (ACK) m;
                if(!message.getAckMessage().equalsIgnoreCase("Connection Established")){
                    throw new MessageTypeException("Error: ACK Connection Established expected");
                }
                currentStep = STEPS.NAME_REGISTRATION;
            case NAME_REGISTRATION:
                Register_Name register_name = new Register_Name(str);
                messageHandler.sendMessage(register_name);
                Message registrationResponse = messageHandler.receiveMessage();

                // ACK means that the str is accepted
                if(registrationResponse instanceof ACK){
                    currentStep = STEPS.LOBBY_LIST;
                }
                else if((!(registrationResponse instanceof Error) || !((Error) registrationResponse).getErrorMessage().equalsIgnoreCase("Invalid player str")){
                    throw new MessageTypeException("Error: Unexpected message received from server");
                }
                return currentStep;
            case LOBBY_LIST:
                Message lobbiesMessage = messageHandler.receiveMessage();
                if(!(lobbiesMessage instanceof Lobbies)){
                    throw new MessageTypeException("Error: LOBBIES expected");
                }
                lobbies = ((Lobbies) lobbiesMessage).getLobbies();
                currentStep = STEPS.LOBBY_SELECTION;
                return currentStep;
            case LOBBY_SELECTION:
                Message lobbyNameRequest = flag ? new Create_Lobby(str) : new Join_Lobby(str);
                messageHandler.sendMessage(lobbyNameRequest);

                Message lobbyNameResponse = messageHandler.receiveMessage();
                if(lobbiesMessage instanceof ACK){
                    currentStep = STEPS.READY_UP;
                } else if(!(lobbiesMessage instanceof Error && ((Error)lobbiesMessage).getErrorMessage().equalsIgnoreCase("Invalid lobby name"))){
                    throw new MessageTypeException(("Error: Unexpected message received from the server"));
                }
                return currentStep;
            case READY_UP:
                Message readyUp = new Ready();
                messageHandler.sendMessage(readyUp);

                Message start = messageHandler.receiveMessage();
                if(!(start instanceof Start)){
                    throw new MessageTypeException(("Error: Unexpected message received from the server"));
                }
                currentStep = STEPS.STARTED;
        }
    }
}
