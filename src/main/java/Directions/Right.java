package Directions;

import resources.Cell;
import resources.Snake;

/**
 * implmentation of the {@link Direction} interface that generates a
 * Cell to the right of current location.
 *
 * @author Ian laird
 */
public class Right implements Direction {
    /**
     * moves the Snake to new position to the right
     * @param s-the Snake to be modified
     * @return the position
     * @author Ian laird
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow(), s.getHeadLocation().getCol() + 1);
    }

    /**
     * This is a factory method.
     *
     * @author Ian laird
     * @return new instance of {@link Right}
     */
    public static Direction create(){
        return new Right();
    }
}
