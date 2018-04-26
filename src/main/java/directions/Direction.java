package directions;

import resources.Cell;
import resources.Snake;

/**
 * {@link Direction} is an interface that allows a movement to be performed on a Snake.
 * @author Ian Laird
 */
public interface Direction {
    /**
     * moves the Snake to new position
     * @param s-the Snake to be modified
     * @return the position
     */
    public Cell performUpdate(Snake s);
}
