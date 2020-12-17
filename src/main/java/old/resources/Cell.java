package old.resources;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Andrew Walker
 * This class holds data for a row and column position
 */
public class Cell {
    private static int CELLSIZE = 10;
    private int row;
    private int col;

    /**
     * @param r the row of the old.resources.Cell
     * @param c the column of the old.resources.Cell
     * @author Andrew Walker
     * This constructor takes an initial row and column
     */
    public Cell(int r, int c) {
        row = r;
        col = c;
    }

    /**
     * @param other the Cell to copy from
     * @author Andrew Walker
     * This is the copy constructor for a Cell
     */
    public Cell(Cell other) {
        this.col = other.col;
        this.row = other.row;
    }

    /**
     * @param width  of the table
     * @param height of the table
     * @return newly generated old.resources.Cell
     * @author Andrew Walker
     * Creates a Cell in a random location in the table
     */
    public static Cell createRandom(int width, int height) {
        return new Cell(ThreadLocalRandom.current().nextInt(7, width - 7),
                ThreadLocalRandom.current().nextInt(7, height - 7));
    }

    /**
     * @return the size of cells on the grid
     * @author Andrew Walker
     * Returns the size of cells on the grid
     */
    public static int getCellSize() {
        return CELLSIZE;
    }

    /**
     * @return the row of the cell
     * @author Andrew Walker
     * Returns the row of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column of the cell
     * @author Andrew Walker
     * Returns the column of the cell
     */
    public int getCol() {
        return col;
    }

    /**
     * @param o the object to test against
     * @return equivalence of two Objects
     * @author Andrew Walker
     * This function tests the equivalence of two Cell objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                col == cell.col;
    }

    /**
     * @return the hashcode of the Cell
     * @author Andrew Walker
     * This function returns the hashcode of the Cell
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
