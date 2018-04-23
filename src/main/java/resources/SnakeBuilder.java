package resources;

import exceptions.BuilderException;

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
    public SnakeBuilder setColor(Color wantedColor) throws BuilderException{
        if(mySnake == null)
            throw new BuilderException("need to init before setting color");
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
    public SnakeBuilder setStart(Cell startPos) throws BuilderException{
        if(mySnake == null)
            throw new BuilderException("need to init before can set Cell");
        mySnake.setHeadLocation(startPos);
        return this;
    }
    /**
     * returns the Snake that has been created.
     *
     * @author Ian Laird
     * @return created Snake
     */
    public Snake collectSnakeBuilder() throws BuilderException{
        if(mySnake == null)
            throw new BuilderException("need to init before collection");
        Snake returnSnake = mySnake;
        mySnake = null;
        return returnSnake;
    }
}
