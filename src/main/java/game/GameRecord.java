package game;

import resources.Cell;
import resources.Snake;

import java.util.Objects;

/**
 * @author Ian Laird
 * This class implments a system for restoring a game using the memento design
 * pattern
 */
public class GameRecord {
    private Snake SnakeOneRecord, SnakeTwoRecord;
    private Cell powerUpLocation;

    /**
     * @author Ian Laird
     * Custom constructor for a Game Record
     * @param toSave-the Game will be recorded
     */
    GameRecord(Game toSave){
        SnakeOneRecord = Snake.makeSnake(toSave.getPlayerOne());
        SnakeTwoRecord = Snake.makeSnake(toSave.getPlayerTwo());
        powerUpLocation = new Cell(toSave.getPowerUp());
    }

    /**
     * @author Ian Laird
     * This function returns Snake One's record
     * @return Snake One's record
     */
    public Snake getSnakeOneRecord() {
        return SnakeOneRecord;
    }

    /**
     * @author Ian laird
     * This function returns Snake Two's record
     * @return Snake Two's record
     */
    public Snake getSnakeTwoRecord() {
        return SnakeTwoRecord;
    }

    /**
     * @author Ian Laird
     * This function returns the recorded power up location
     * @return recorded power up location
     */
    public Cell getPowerUpLocation() {
        return powerUpLocation;
    }

    /**
     * @author Ian Laird
     * An overridden equals operator
     * @param o the object to compare for equality to
     * @return if the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRecord that = (GameRecord) o;
        return Objects.equals(getSnakeOneRecord(), that.getSnakeOneRecord()) &&
                Objects.equals(getSnakeTwoRecord(), that.getSnakeTwoRecord()) &&
                Objects.equals(getPowerUpLocation(), that.getPowerUpLocation());
    }
}
