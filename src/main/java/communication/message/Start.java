package communication.message;

import java.io.Serializable;

public class Start extends Message implements Serializable {
    int port;

    public Start(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
