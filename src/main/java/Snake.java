import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.Deque;

public class Snake {
    //As the snake travels to new tiles we push on
    //As snake travels we pop tiles off b/c fixed length
    private Deque<Pair<Integer,Integer>> locations = new ArrayDeque<Pair<Integer, Integer>>();

    //Holds the length of the Snake
    private int length = 0;

    public void drawSnake(Screen screen){
        //Should draw all elements in the Snake onto Screen
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
     * This method is used to record a Snake movement
     * It removes the tail of teh snake and adds a new head
     */
    public void moveLocation(Pair<Integer, Integer> moveTo){
        if(locations.size() == this.length){
            locations.pop();
        }
        locations.push(moveTo);
    }
}
