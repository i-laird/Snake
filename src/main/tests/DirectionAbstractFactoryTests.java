import Directions.*;
import Enums.Direct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link DirectionAbstractFactoryTests} tests the Abstract factory for Directions.
 * @author Ian laird
 */
public class DirectionAbstractFactoryTests {
    private Direct dirEnum;
    private Direction generated;

    /**
     * @author Ian Laird
     * tests abtract factory when passed UP enum
     */
    @Test
    void testUp(){
        dirEnum = Direct.UP;
        generated = DirectionFactory.make(dirEnum);
        assertEquals(generated.getClass(), Up.class);
    }
    /**
     * @author Ian Laird
     * tests abtract factory when passed DOWN enum
     */
    @Test
    void testDown(){
        dirEnum = Direct.DOWN;
        generated = DirectionFactory.make(dirEnum);
        assertEquals(generated.getClass(), Down.class);
    }
    /**
     * @author Ian Laird
     * tests abtract factory when passed LEFT enum
     */
    @Test
    void testLeft(){
        dirEnum = Direct.LEFT;
        generated = DirectionFactory.make(dirEnum);
        assertEquals(generated.getClass(), Left.class);
    }
    /**
     * @author Ian Laird
     * tests abtract factory when passed RIGHT enum
     */
    @Test
    void testRight(){
        dirEnum = Direct.RIGHT;
        generated = DirectionFactory.make(dirEnum);
        assertEquals(generated.getClass(), Right.class);
    }
}
