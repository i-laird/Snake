package server;

import communication.SynchronizedMessageHandler;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Player {

    private String playerName;
    private Socket socket;
    private SynchronizedMessageHandler messageHandler;

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

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public SynchronizedMessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(SynchronizedMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
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
