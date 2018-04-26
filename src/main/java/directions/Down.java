package directions;

import resources.Cell;
import resources.Snake;

/**
 * @author Ian laird
 * implmentation of the {@link Direction} interface that generates a
 * Cell one below the current.
 */
public class Down implements Direction {
    /**
     * @author Ian Laird
     * This function moves the Snake to new position down
     * @param s-the Snake to be modified
     * @return the position
     */
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow() + 1, s.getHeadLocation().getCol());
    }

    /**
     * @author Ian laird
     * This is a factory method.
     * @return new instance of {@link Down}
     */
    public static Direction create(){
        return new Down();
    }
}