package resources;

import java.awt.*;

public class SnakeBuilder {
    private Snake mySnake = null;

    /**
     * Initializes the Snake that is being constructed
     *
     * @author Ian Laird
     * @return this for chaining
     */
    public SnakeBuilder init(){
        mySnake = Snake.makeSnake();
        return this;
    }
    /**
     * Sets the color of the Snake being constructed
     *
     * @author Ian Laird
     * @param wantedColor-the color that is desired
     * @return this for chaining
     */
    public SnakeBuilder setColor(Color wantedColor){
        mySnake.setColor(wantedColor);
        return this;
    }
    /**
     * Sets the color of the Snake being constructed
     *
     * @author Ian Laird
     * @param startPos-the initial position of the Snake
     * @return this for chaining
     */
    public SnakeBuilder setStart(Cell startPos){
        mySnake.setHeadLocation(startPos);
        return this;
    }
    /**
     * returns the Snake that has been created.
     *
     * @author Ian Laird
     * @return created Snake
     */
    public Snake collectSnakeBuilder(){
        Snake returnSnake = mySnake;
        mySnake = null;
        return returnSnake;
    }
}
