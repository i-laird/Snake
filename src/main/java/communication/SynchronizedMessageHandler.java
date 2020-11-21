package communication;

import communication.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SynchronizedMessageHandler {

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public SynchronizedMessageHandler(Socket s) throws IOException {
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
    }

    public void sendMessage(Message m) throws IOException{
        out.writeObject(m);
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message)in.readObject();
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void closeConnections() throws IOException{
        in.close();
        out.close();
    }
}
