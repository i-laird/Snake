import directions.Direction;
import directions.DirectionFactory;
import display.Screen;
import enums.Direct;
import exceptions.SnakeBuilderError;
import game.Game;
import game.GameMaker;
import org.junit.jupiter.api.Test;
import resources.Cell;
import resources.Snake;
import resources.SnakeBuilder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * This class tests the state design pattern
 * It cannot fully test it because it is dependent on user keystrokes.
 * @author Ian Laird
 */
public class StateTests {
    private Screen screenInstance;
    private Game g;

    /**
     * This JUNIT test tests to see if the State design pattern is working properly.
     * @author Ian Laird
     */
    @Test
    void test1(){
        screenInstance = Screen.getInstance(100, 100);
        //this is the default state
        assertEquals(Direct.UP,  screenInstance.getState2());
        Direction dir = DirectionFactory.make(screenInstance.getState2());
        g = GameMaker.generateGame(true);
        try {
            Snake s = new SnakeBuilder().init().setColor(Color.BLACK).setStart(new Cell(0, 0)).collectSnakeBuilder();
            Snake t = Snake.makeSnake(s);
            assertNotSame(s, t);
            Cell c = new Cell(dir.performUpdate(s));
            assertEquals(c.getRow(), t.getHeadLocation().getRow() - 1);
            assertEquals(c.getCol(), t.getHeadLocation().getCol());
        }catch(SnakeBuilderError e){
            throw new RuntimeException();
        }

    }
}
