package resources;

import exceptions.SnakeBuilderError;

import java.awt.*;

public class SnakeBuilder {
    private Snake mySnake = null;

    /**
     * @author Ian Laird
     * Initializes the Snake that is being constructed
     * @return this for chaining
     */
    public SnakeBuilder init(){
        mySnake = Snake.makeSnake();
        return this;
    }
    /**

     * Sets the color of the Snake being constructed
     * @param wantedColor the color that is desired
     * @return this for chaining
     */
    public SnakeBuilder setColor(Color wantedColor) throws SnakeBuilderError {
        if(mySnake == null)
            throw new SnakeBuilderError("need to init before setting color");
        mySnake.setColor(wantedColor);
        return this;
    }
    /**
     * @author Ian Laird
     * Sets the color of the Snake being constructed
     * @param startPos the initial position of the Snake
     * @return this for chaining
     */
    public SnakeBuilder setStart(Cell startPos) throws SnakeBuilderError {
        if(mySnake == null)
            throw new SnakeBuilderError("need to init before can set Cell");
        mySnake.setHeadLocation(startPos);
        return this;
    }
    /**
     * @author Ian Laird
     * returns the Snake that has been created.
     * @return created Snake
     */
    public Snake collectSnakeBuilder() throws SnakeBuilderError {
        if(mySnake == null)
            throw new SnakeBuilderError("need to init before collection");
        Snake returnSnake = mySnake;
        mySnake = null;
        return returnSnake;
    }
}
