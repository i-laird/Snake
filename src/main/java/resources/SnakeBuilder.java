package resources;

import exceptions.SnakeBuilderError;

import java.awt.*;

public class SnakeBuilder {
    private Snake mySnake = null;

    /**
     * @return this for chaining
     * @author Ian Laird
     * Initializes the Snake that is being constructed
     */
    public SnakeBuilder init() {
        mySnake = Snake.makeSnake();
        return this;
    }

    /**
     * @param wantedColor the color that is desired
     * @return this for chaining
     * @throws SnakeBuilderError thrown if the snake is null
     * @author Ian Laird
     * Sets the color of the Snake being constructed
     */
    public SnakeBuilder setColor(Color wantedColor) throws SnakeBuilderError {
        if (mySnake == null)
            throw new SnakeBuilderError("need to init before setting color");
        mySnake.setColor(wantedColor);
        return this;
    }

    /**
     * @param startPos the initial position of the Snake
     * @return this for chaining
     * @throws SnakeBuilderError thrown if the snake is null
     * @author Ian Laird
     * Sets the color of the Snake being constructed
     */
    public SnakeBuilder setStart(Cell startPos) throws SnakeBuilderError {
        if (mySnake == null)
            throw new SnakeBuilderError("need to init before can set Cell");
        mySnake.setHeadLocation(startPos);
        return this;
    }

    /**
     * @return created Snake
     * @throws SnakeBuilderError thrown if the snake is null
     * @author Ian Laird
     * returns the Snake that has been created.
     */
    public Snake collectSnakeBuilder() throws SnakeBuilderError {
        if (mySnake == null)
            throw new SnakeBuilderError("need to init before collection");
        Snake returnSnake = mySnake;
        mySnake = null;
        return returnSnake;
    }
}
