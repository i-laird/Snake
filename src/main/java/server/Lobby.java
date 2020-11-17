package server;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Lobby {
    private String lobbyName;

    private Set<Player> players = new HashSet<>();

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

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
