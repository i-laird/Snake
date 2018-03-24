package resources;

import Enums.Color;
import resources.Cell;
import resources.Screen;

import java.util.ArrayDeque;
import java.util.Deque;

public class Snake {
    //As the snake travels to new tiles we push on
    //As snake travels we pop tiles off b/c fixed length
    private Deque<Cell> locations = new ArrayDeque<Cell>();
    //The location of the Head should probably be in the Deque as well
    private Cell headLocation = null;

    private Color color;

    //Holds the length of the resources.Snake
    private int length = 0;

    public Snake(Cell startPos){
        this.headLocation = headLocation;
        this.locations.addLast(headLocation);
    }

    public void drawSnake(Screen screen, int cubeDimension){
        //Should draw all elements in the resources.Snake onto resources.Screen
    }

    /**
     * When token is picked up the length increases by one
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
            locations.removeFirst();
        }
        locations.addLast(moveTo);
        this.headLocation = moveTo;
    }

    /**
     * This method sees if the indicated Location is present in the resources.Snake.
     * It is used to see check for resources.Snake collisions
     * @param location
     * @return
     */
    public boolean snakeCoverMove(Cell location){
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
}
