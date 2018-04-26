package directions;

import resources.Cell;
import resources.Snake;

/**
 * implmentation of the {@link Direction} interface that generates a
 * Cell above the current position.
 *
 * @author Ian laird
 */
public class Up implements Direction {
    /**
     * moves the Snake to new position above
     * @param s-the Snake to be modified
     * @return the position
     * @author Ian laird
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow() - 1, s.getHeadLocation().getCol());
    }

    /**
     * This is a factory method.
     *
     * @author Ian laird
     * @return new instance of {@link Up}
     */
    public static Direction create(){
        return new Up();
    }
}
