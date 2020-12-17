package directions;

import old.resources.Cell;
import old.resources.Snake;

/**
 * @author Ian laird
 * implmentation of the {@link Direction} interface that generates a
 * Cell one below the current.
 */
public class Down implements Direction {
    /**
     * @return new instance of {@link Down}
     * @author Ian laird
     * This is a factory method.
     */
    public static Direction create() {
        return new Down();
    }

    /**
     * @param s-the Snake to be modified
     * @return the position
     * @author Ian Laird
     * This function moves the Snake to new position down
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow() + 1, s.getHeadLocation().getCol());
    }
}