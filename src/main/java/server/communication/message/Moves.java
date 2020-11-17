package server.communication.message;

import server.Move;

import java.io.Serializable;

public class Moves extends Message implements Serializable {
    private Move[] moves;
}
