package directions;

import resources.Cell;
import resources.Snake;

/**
 * @author Ian Laird
 * {@link Direction} is an interface that allows a movement to be performed on a Snake.
 */
public interface Direction {
    /**
     * @param s the Snake to be modified
     * @return the new position of the snake
     * @author Ian Laird
     * This function moves the Snake to new position
     */
    Cell performUpdate(Snake s);
}
