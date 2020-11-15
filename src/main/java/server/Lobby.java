package server;

import java.util.Objects;

public class Lobby {
    private String lobbyName;

    public Lobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(lobbyName, lobby.lobbyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyName);
    }
}
