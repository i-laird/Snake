package old.game;

import old.resources.Cell;
import old.resources.Snake;

import java.util.Objects;

/**
 * @author Ian Laird
 * This class implments a system for restoring a old.game using the memento design
 * pattern
 */
public class GameRecord {
    private Snake SnakeOneRecord, SnakeTwoRecord;
    private Cell powerUpLocation;

    /**
     * @param toSave-the Game will be recorded
     * @author Ian Laird
     * Custom constructor for a Game Record
     */
    GameRecord(Game toSave) {
        SnakeOneRecord = Snake.makeSnake(toSave.getPlayerOne());
        SnakeTwoRecord = Snake.makeSnake(toSave.getPlayerTwo());
        powerUpLocation = new Cell(toSave.getPowerUp());
    }

    /**
     * @return Snake One's record
     * @author Ian Laird
     * This function returns Snake One's record
     */
    public Snake getSnakeOneRecord() {
        return SnakeOneRecord;
    }

    /**
     * @return Snake Two's record
     * @author Ian laird
     * This function returns Snake Two's record
     */
    public Snake getSnakeTwoRecord() {
        return SnakeTwoRecord;
    }

    /**
     * @return recorded power up location
     * @author Ian Laird
     * This function returns the recorded power up location
     */
    public Cell getPowerUpLocation() {
        return powerUpLocation;
    }

    /**
     * @param o the object to compare for equality to
     * @return if the two objects are equal
     * @author Ian Laird
     * An overridden equals operator
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

    @Override
    public int hashCode() {

        return Objects.hash(getSnakeOneRecord(), getSnakeTwoRecord(), getPowerUpLocation());
    }
}
