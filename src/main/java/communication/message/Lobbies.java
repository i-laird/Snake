package communication.message;

import server.Lobby;

import java.io.Serializable;
import java.util.List;

public class Lobbies extends Message implements Serializable {
    private List<Lobby> lobbies;

    public Lobbies(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    public void setLobbies(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }
}
