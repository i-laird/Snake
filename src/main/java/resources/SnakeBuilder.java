package resources;

import java.awt.*;

public class SnakeBuilder {
    private Snake mySnake = null;
    public SnakeBuilder init(){
        mySnake = Snake.makeSnake();
        return this;
    }
    public SnakeBuilder setColor(Color wantedColor){
        mySnake.setColor(wantedColor);
        return this;
    }
    public SnakeBuilder setStart(Cell startPos){
        mySnake.setHeadLocation(startPos);
        return this;
    }
    public Snake collectSnakeBuilder(){
        Snake returnSnake = mySnake;
        mySnake = null;
        return returnSnake;
    }
}
