package resources;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Cell {
    private int row;
    private int col;
    private static int CELLSIZE = 10;


    /**
     * @author: Ian Laird
     * @param r the row of the resources.Cell
     * @param c the column of the resources.Cell
     */
    public Cell(int r, int c){
        row = r;
        col = c;
    }

    /**
     * creates a resources.Cell in a random location in the table
     * @author: Ian Laird
     * @param width of the table
     * @param height of the table
     * @return newly generated resources.Cell
     */
    public static Cell createRandom(int width, int height){
        return new Cell(ThreadLocalRandom.current().nextInt(0, width),
                ThreadLocalRandom.current().nextInt(0, height));
    }

    /**
     * @authot: Ian Laird
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * @authot: Ian Laird
     * @return column
     */
    public int getCol() {
        return col;
    }

    /**
     * @authot: Ian Laird
     * @return equivalence of two Objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public static int getCellSize(){
        return CELLSIZE;
    }
}
