import display.Screen;
import game_stuff.ServerGame;
import game_stuff.gameMaker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * {@link SingletonTests} are tests of many of the Singleton classes used. It makes
 * sure all generated referneces are to the same object
 * @author  Ian laird
 */
public class SingletonTests {
    /**
     * tests if more than one screen are generated
     * @author Ian laird
     */
    @Test
    void testScreen(){
        assertSame(Screen.getInstance(100, 100), Screen.getInstance(100, 100));
        assertSame(Screen.getInstance(250, 100), Screen.getInstance(100, 250));
    }
    /**
     * tests if more than one server are generated
     * @author Ian laird
     */
    @Test
    void testServer(){
        assertSame(gameMaker.generateGame(true), gameMaker.generateGame(true));
    }
    /**
     * tests if more than one client are generated are generated
     * @author Ian laird
     */
    @Test
    void testClient(){
        assertSame(gameMaker.generateGame(false), gameMaker.generateGame(false));
    }
    /**
     * tests if more than one Game  are generated if one is server and one is client from factory
     * @author Ian laird
     */
    @Test
    void serverAndClient(){
        assertSame(gameMaker.generateGame(true), gameMaker.generateGame(false));
    }
}
