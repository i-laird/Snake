package communication;

import communication.message.Message;
import enums.Direct;
import server.Player;

import java.io.Serializable;

public class Move extends Message implements Serializable {
    private Player player;
    private Direct direction;
}
