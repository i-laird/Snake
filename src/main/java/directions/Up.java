package directions;

import resources.Cell;
import resources.Snake;

/**
 * @author Ian laird
 * Implmentation of the {@link Direction} interface that generates a
 * Cell above the current position.
 */
public class Up implements Direction {

    /**
     * @author Ian laird
     * This function moves the Snake to new position above
     * @param s-the Snake to be modified
     * @return the position
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow() - 1, s.getHeadLocation().getCol());
    }

    /**
     * @author Ian laird
     * This is a factory method.
     * @return new instance of {@link Up}
     */
    public static Direction create(){
        return new Up();
    }
}
