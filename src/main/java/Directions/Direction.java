package Directions;

import resources.Cell;
import resources.Snake;

public interface Direction {
    public Cell performUpdate(Snake s);
}
