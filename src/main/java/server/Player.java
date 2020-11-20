package server;

import java.net.Socket;
import java.util.Objects;

public class Player {

    private String playerName;
    private Socket socket;

    public Player(String playerName, Socket s) {
        this.playerName = playerName;
        this.socket = s;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(playerName, player.playerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName);
    }
}
