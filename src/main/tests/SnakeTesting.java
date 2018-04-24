import exceptions.BuilderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.Cell;
import resources.Snake;
import resources.SnakeBuilder;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * {@link SnakeTesting} tests various functionality necessary to having a proper {@link Snake}
 * @author ian laird
 */
public class SnakeTesting {
    private Snake ms, ms2;
    private int DEFAULT_SIZE = 3;

    /**
     * This initialzes the two {@link Snake}
     * @author Ian laird
     * @throws BuilderException-should never be thrown
     */
    @BeforeEach
    void init() throws BuilderException {
        ms = new SnakeBuilder().init().setColor(Color.BLACK).setStart(new Cell(0, 0)).collectSnakeBuilder();
        ms2 = new SnakeBuilder().init().setColor(Color.GREEN).setStart(new Cell(5, 5)).collectSnakeBuilder();

    }

    /**
     * @author Ian laird
     * tests that the two {@link Snake} generated are not the same
     */
    @Test
    void factoryDistinct(){
        assertNotSame(ms, ms2);
    }
    /**
     * @author Ian laird
     * tests that the {@link Snake} properly sizes
     */
    @Test
    void testLength(){
        ms.moveLocation(new Cell(0 ,1));
        ms.moveLocation(new Cell(0 ,2));
        ms.moveLocation(new Cell(0 ,3));
        ms.moveLocation(new Cell(0 ,4));
        ms.moveLocation(new Cell(0 ,5));
        assertEquals(ms.getSnakeLocations().size(), DEFAULT_SIZE);
    }
    /**
     * @author Ian laird
     * tests that the{@link Snake} properly saves Tail location
     */
    @Test
    void testMovement(){
        ms.moveLocation(new Cell(0 ,1));
        ms.moveLocation(new Cell(0 ,2));
        ms.moveLocation(new Cell(0 ,3));
        assertEquals(ms.getPrevTail(), new Cell(0, 0));
        ms.moveLocation(new Cell(0 ,4));
        assertEquals(ms.getPrevTail(), new Cell(0, 1));
    }
}
