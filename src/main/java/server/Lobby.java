package server;

import java.util.*;

public class Lobby {
    private String lobbyName;

    private boolean start = false;

    private Map<Player, Boolean> playerToStatus = new HashMap<>();

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

    public Map<Player, Boolean> getPlayerToStatus() {
        return playerToStatus;
    }

    public void setPlayerToStatus(Map<Player, Boolean> playerToStatus) {
        this.playerToStatus = playerToStatus;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
