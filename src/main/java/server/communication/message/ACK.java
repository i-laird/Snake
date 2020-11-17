package server.communication.message;

import java.io.Serializable;

public class ACK extends Message implements Serializable {

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
