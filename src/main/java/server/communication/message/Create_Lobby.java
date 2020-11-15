package server.communication.message;

public class Create_Lobby extends Message {
    private String lobbyName;

    public Create_Lobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
}
