package Directions;

import resources.Cell;
import resources.Snake;

public class Up implements Direction {
    @Override
    public Cell performUpdate(Snake s) {
        return new Cell(s.getHeadLocation().getRow() - 1, s.getHeadLocation().getCol());
    }

    public static Direction create(){
        return new Up();
    }
}
