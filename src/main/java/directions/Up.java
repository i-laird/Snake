package directions;

import old.resources.Cell;
import old.resources.Snake;

/**
 * @author Ian laird
 * Implmentation of the {@link Direction} interface that generates a
 * Cell above the current position.
 */
public class Up implements Direction {

    /**
     * @return new instance of {@link Up}
     * @author Ian laird
     * This is a factory method.
     */
    public static Direction create() {
        return new Up();
    }

    /**
     * @param s-the Snake to be modified
     * @return the position
     * @author Ian laird
     * This function moves the Snake to new position above
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow() - 1, s.getHeadLocation().getCol());
    }
}
