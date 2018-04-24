import Enums.Direct;
import display.Screen;
import game_stuff.Game;
import game_stuff.gameMaker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the state design pattern
 * It cannot fully test it because it is dependent on user keystrokes.
 * @author Ian Laird
 */
public class StateTests {
    private Screen screenInstance;
    private Game g;
    @Test
    void test1(){
        screenInstance = Screen.getInstance(100, 100);
        //this is the default state
        assertEquals(Direct.UP, screenInstance.getState2());
        g = gameMaker.generateGame(true);
    }
}
