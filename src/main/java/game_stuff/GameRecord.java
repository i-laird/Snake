package game_stuff;

import resources.Cell;
import resources.Snake;

public class GameRecord {
    private Snake SnakeOneRecord, SnakeTwoRecord;
    private Cell powerUpLocation;

    /**
     * custom constructor for a Game Record
     *
     * @author Ian Laird
     * @param toSave-the Game will be recorded
     */

    GameRecord(Game toSave){
        SnakeOneRecord = Snake.makeSnake(toSave.getPlayerOne());
        SnakeTwoRecord = Snake.makeSnake(toSave.getPlayerTwo());
        powerUpLocation = new Cell(toSave.getPowerUp());
    }

    /**
     * @author Ian Laird
     * @return player 1 record
     */
    public Snake getSnakeOneRecord() {
        return SnakeOneRecord;
    }

    /**
     * @author Ian laird
     * @return player 2 record
     */
    public Snake getSnakeTwoRecord() {
        return SnakeTwoRecord;
    }

    /**
     * @author Ian Laird
     * @return recorded power up location
     */
    public Cell getPowerUpLocation() {
        return powerUpLocation;
    }
}
