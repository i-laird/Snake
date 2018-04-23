package Directions;

import resources.Cell;
import resources.Snake;

/**
 * implmentation of the {@link Direction} interface that generates a
 * Cell one below the current.
 *
 * @author Ian laird
 */
public class Down implements Direction {
    /**
     * moves the Snake to new position down
     * @param s-the Snake to be modified
     * @return the position
     * @author Ian laird
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow() + 1, s.getHeadLocation().getCol());
    }

    /**
     * This is a factory method.
     *
     * @author Ian laird
     * @return new instance of {@link Down}
     */
    public static Direction create(){
        return new Down();
    }
}