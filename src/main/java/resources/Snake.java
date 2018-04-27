package resources;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * @author Ian Laird
 * This class implements the idea of the Snake
 */
public class Snake {
    //As the snake travels to new tiles we push on
    //As snake travels we pop tiles off b/c fixed length
    private Deque<Cell> locations = null;
    //The location of the Head should probably be in the Deque as well
    private Cell headLocation = null;
    private Cell prevTail = null;
    private Color color;
    //Holds the length of the resources.Snake
    private int length = 3;

    /**
     * @author Ian Laird
     * This is the default constructor for a Snake
     */
    private Snake() {
        locations = new ArrayDeque<Cell>();
    }

    /**
     * @param other the other Snake to copy from
     * @author Ian Laird
     * This is the copy constructor
     */
    private Snake(Snake other) {
        this.headLocation = new Cell(other.headLocation);
        this.color = other.color;
        this.prevTail = (other.prevTail != null ? new Cell(other.prevTail) : null);
        this.locations = new ArrayDeque<>();
        other.locations.stream().forEach(x -> this.locations.add(new Cell(x)));
    }

    /**
     * @return The newly generated Snake
     * @author Ian Laird
     * This function is a factory method for a Snake
     */
    public static Snake makeSnake() {
        return new Snake();
    }

    /**
     * @param other the Snake to copy from
     * @return The newly generated Snake from other
     * @author Ian Laird
     * Factory method that returns a copy.
     */
    public static Snake makeSnake(Snake other) {
        return new Snake(other);
    }

    /**
     * @author Andrew Walker
     * This method increments the length of the snake
     */
    public void increaseLength() {
        this.length += 1;
    }

    /**
     * @param moveTo This method is used to record a resources.Snake movement
     *               It removes the tail of teh snake and adds a new head
     * @author Ian Laird
     */
    public void moveLocation(Cell moveTo) {
        if (locations.size() == this.length) {
            prevTail = locations.getLast();
            locations.removeLast();
        }
        locations.addFirst(moveTo);
        this.headLocation = moveTo;
    }

    /**
     * @param location the location to check for
     * @return if the Snake contains the location
     * @author Ian Laird
     * This method sees if the indicated Location is present in the resources.Snake.
     * It is used to see check for resources.Snake collisions
     */
    public boolean containsMove(Cell location) {
        return this.locations.contains(location);
    }

    /**
     * @return the Snake's color
     * @author Ian Laird
     * This function returns the Snake's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the new color
     * @author Ian Laird
     * This function sets the Snake to a new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return location of the head
     * @author Ian Laird
     * This function returns the head location
     */
    public Cell getHeadLocation() {
        return headLocation;
    }

    /**
     * @param startPos the starting location
     * @author Ian Laird
     * This function sets the head location of the Snake.
     */
    void setHeadLocation(Cell startPos) {
        this.headLocation = startPos;
        this.locations.addFirst(headLocation);
    }

    /**
     * @return the collection of all the locations of the snake
     * @author Ian Laird
     * This function returns the collection of all the locations of the snake
     */
    public Deque<Cell> getSnakeLocations() {
        return this.locations;
    }

    /**
     * @return the Cell no longer occupied
     * @author Andrew Walker
     * Returns the Cell just cleared out of by the Snake
     */
    public Cell getPrevTail() {
        return this.prevTail;
    }

    /**
     * @param o the object to test against
     * @return equivalence of two Objects
     * @author Ian Laird
     * This function tests the equivalence of two Snake objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snake snake = (Snake) o;
        boolean status = false;
        if (length == snake.length &&
                Objects.equals(getHeadLocation(), snake.getHeadLocation()) &&
                (getPrevTail() != null ? Objects.equals(getPrevTail(), snake.getPrevTail()) : true) &&
                Objects.equals(getColor(), snake.getColor())) {
            status = true;
            for (Cell location : this.locations) {
                if (!snake.locations.contains(location))
                    status = false;
            }
        }
        return status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(locations, getHeadLocation(), getPrevTail(), getColor(), length);
    }
}
