package directions;

import old.resources.Cell;
import old.resources.Snake;

/**
 * @author Ian laird
 * implmentation of the {@link Direction} interface that generates a
 * Cell to the right of current location.
 */
public class Right implements Direction {

    /**
     * This is a factory method.
     *
     * @return new instance of {@link Right}
     * @author Ian laird
     */
    public static Direction create() {
        return new Right();
    }

    /**
     * @param s-the Snake to be modified
     * @return the position
     * @author Ian laird
     * This function moves the Snake to new position to the right
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow(), s.getHeadLocation().getCol() + 1);
    }
}
