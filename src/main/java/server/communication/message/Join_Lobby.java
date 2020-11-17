package server.communication.message;

import java.io.Serializable;

public class Join_Lobby extends Message implements Serializable {
    private String lobbyName;

    public Join_Lobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
}
