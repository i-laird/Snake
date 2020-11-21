package communication.message;

import communication.Move;

import java.io.Serializable;

public class Moves extends Message implements Serializable {
    private Move[] moves;
}
