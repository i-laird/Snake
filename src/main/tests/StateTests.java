import display.Screen;
import game.Game;
import game.GameMaker;
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
        assertEquals(0,  screenInstance.getState());
        g = GameMaker.generateGame(true);
    }
}
