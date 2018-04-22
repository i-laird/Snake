package resources;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class Snake {
    //As the snake travels to new tiles we push on
    //As snake travels we pop tiles off b/c fixed length
    private Deque<Cell> locations = new ArrayDeque<Cell>();
    //The location of the Head should probably be in the Deque as well
    private Cell headLocation = null;
    private Cell prevTail = null;
    private Color color;
    //Holds the length of the resources.Snake
    private int length = 3;

    private Snake(){
    }

    static Snake makeSnake(){
        return new Snake();
    }
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Cell getHeadLocation() {
        return headLocation;
    }

    public Deque<Cell> getSnakeLocations(){
        return this.locations;
    }

    public Cell getPrevTail(){
        return this.prevTail;
    }
}
