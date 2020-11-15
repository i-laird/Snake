package server.communication.message;

public class ACK extends Message{

    private String ackMessage;

    public ACK(String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage = ackMessage;
    }
}
