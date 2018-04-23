import Directions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link DirectionFactoryTests} will tests the various Direction factory methods present.
 * @author Ian laird
 */
public class DirectionFactoryTests {
    private Direction dir;

    /**
     * This tests the Up factory method.
     * @author Ian laird
     */
    @Test
    void testUp(){
        dir = Up.create();
        assertEquals(dir.getClass(), Up.class);
    }
    /**
     * This tests the left factory method.
     * @author Ian laird
     */
    @Test
    void testDown(){
        dir = Down.create();
        assertEquals(dir.getClass(), Down.class);
    }
    /**
     * This tests the right factory method.
     * @author Ian laird
     */
    @Test
    void testLeft(){
        dir = Left.create();
        assertEquals(dir.getClass(), Left.class);
    }
    /**
     * This tests the Right factory method.
     * @author Ian laird
     */
    @Test
    void testRight(){
        dir = Right.create();
        assertEquals(dir.getClass(), Right.class);
    }
}
