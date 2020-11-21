package communication.message;

import java.io.Serializable;

public class Register_Name extends Message implements Serializable {
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
