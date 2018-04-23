import exceptions.BuilderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.Cell;
import resources.Snake;
import resources.SnakeBuilder;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link SnakeBuilderTests} tests the Snake Builder to make sure that it functions properly.
 * @author Ian Laird
 */
public class SnakeBuilderTests {

    private Snake testSnake;
    private SnakeBuilder sb;

    /**
     * Initializes a new SnakeBuilder each time
     * @author Ian Laird
     */
    @BeforeEach
    public void setup(){
        sb = new SnakeBuilder();
    }

    /**
     * tests Builder setting color
     * @author Ian laird
     * @throws BuilderException if Builder is not initialized
     */
    @Test
    public void colorTest() throws BuilderException{
        testSnake = sb.init().setColor(Color.BLUE).collectSnakeBuilder();
        assertEquals(testSnake.getColor(), Color.BLUE);
    }

    /**
     * tests Builder setting start Location
     * @author Ian laird
     * @throws BuilderException if Builder is not initialized
     */
    @Test
    public void locationTest() throws BuilderException{
        testSnake = sb.init().setStart(new Cell(0, 0)).collectSnakeBuilder();
        assertEquals(testSnake.getHeadLocation(), new Cell(0, 0));
    }

    /**
     * tests Builder setting color and start location
     * @author Ian laird
     * @throws BuilderException if Builder is not initialized
     */
    @Test
    public void bothTest() throws BuilderException{
        testSnake = sb.init().setColor(Color.BLACK).setStart(new Cell(5, 5)).collectSnakeBuilder();
        assertEquals(Color.BLACK, testSnake.getColor());
        assertEquals(testSnake.getHeadLocation(), new Cell(5, 5));
    }

    /**
     * tests Builder throwing error if color is set without initializing.
     * @author Ian laird
     */
    @Test
    public void testExceptionThrown(){
        assertThrows(BuilderException.class, ()->sb.setColor(Color.BLACK));
    }
}
