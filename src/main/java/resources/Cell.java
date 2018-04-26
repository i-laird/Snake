package resources;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ian Laird
 * This class holds data for a row and column position
 */
public class Cell {
    private int row;
    private int col;
    private static int CELLSIZE = 10;

    /**
     * @author Ian Laird
     * This constructor takes an initial row and column
     * @param r the row of the resources.Cell
     * @param c the column of the resources.Cell
     */
    public Cell(int r, int c){
        row = r;
        col = c;
    }

    /**
     * @author Ian Laird
     * This is the copy constructor for a Cell
     * @param other the Cell to copy from
     */
    public Cell(Cell other){
        this.col = other.col;
        this.row = other.row;
    }

    /**
     * @author Ian Laird
     * Creates a Cell in a random location in the table
     * @param width of the table
     * @param height of the table
     * @return newly generated resources.Cell
     */
    public static Cell createRandom(int width, int height){
        return new Cell(ThreadLocalRandom.current().nextInt(7, width - 7),
                ThreadLocalRandom.current().nextInt(7, height - 7));
    }

    /**
     * @author Ian Laird
     * Returns the row of the cell
     * @return the row of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * @author Ian Laird
     * Returns the column of the cell
     * @return the column of the cell
     */
    public int getCol() {
        return col;
    }

    /**
     * @author Ian Laird
     * This function tests the equivalence of two Cell objects
     * @param o the object to test against
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

    /**
     * @author Ian Laird
     * This function returns the hashcode of the Cell
     * @return the hashcode of the Cell
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * @author Andrew Walker
     * Returns the size of cells on the grid
     * @return the size of cells on the grid
     */
    public static int getCellSize(){
        return CELLSIZE;
    }
}
