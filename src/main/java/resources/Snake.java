package resources;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

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
    private Snake(){
        locations = new ArrayDeque<Cell>();
    }

    /**
     * @author Ian Laird
     * @param other
     * Copy constructor
     */
    private Snake(Snake other){
        this.headLocation = other.headLocation;
        this.color = other.color;
        this.prevTail = other.prevTail;
        this.locations = new ArrayDeque<>(other.locations);
    }

    /**
     * @author Ian Laird
     * factory method for a Snake
     */
    public static Snake makeSnake(){
        return new Snake();
    }
    /**
     * @author Ian Laird
     * @param other
     * Factory method that returns a copy.
     */
    public static Snake makeSnake(Snake other){
        return new Snake(other);
    }
    /**
     * @author Ian Laird
     * @param startPos
     * sets the head location of the Snake.
     */
    void setHeadLocation(Cell startPos){
        this.headLocation = startPos;
        this.locations.addFirst(headLocation);
    }

    /**
     * @author: Andrew Walker
     * This method increments the length of the snake
     */
    public void increaseLength(){
        this.length+=1;
    }

    /**
     * @author: Ian Laird
     * @param moveTo
     * This method is used to record a resources.Snake movement
     * It removes the tail of teh snake and adds a new head
     */
    public void moveLocation(Cell moveTo){
        if(locations.size() == this.length){
            prevTail = locations.getLast();
            locations.removeLast();
        }
        locations.addFirst(moveTo);
        this.headLocation = moveTo;
    }

    /**
     * This method sees if the indicated Location is present in the resources.Snake.
     * It is used to see check for resources.Snake collisions
     * @param location
     * @return
     */
    public boolean containsMove(Cell location){
        return this.locations.contains(location);
    }

    /**
     * @author Ian Laird
     * @return  Snake color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @author Ian Laird
     * @param   color- the new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @author Ian Laird
     * @return location of the head
     */
    public Cell getHeadLocation() {
        return headLocation;
    }

    /**
     * @author Ian Laird
     * @return  All Snake locations
     */
    public Deque<Cell> getSnakeLocations(){
        return this.locations;
    }

    /**
     * @author Ian Laird
     * @return  the Cell no longer occupied
     */
    public Cell getPrevTail(){
        return this.prevTail;
    }
}
