package directions;

import resources.Cell;
import resources.Snake;

/**
 * @author Ian laird
 * implmentation of the {@link Direction} interface that generates a
 * Cell to the left of current location.
 */
public class Left implements Direction {

    /**
     * @author Ian laird
     * This function moves the Snake to new position to the left
     * @param s-the Snake to be modified
     * @return the position
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow(), s.getHeadLocation().getCol() - 1);
    }

    /**
     * @author Ian laird
     * This is a factory method.
     * @return new instance of {@link Left}
     */
    public static Direction create(){
        return new Left();
    }
}