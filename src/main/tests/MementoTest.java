import exceptions.BuilderException;
import game_stuff.Game;
import game_stuff.GameRecord;
import game_stuff.gameMaker;
import org.junit.jupiter.api.Test;
import resources.Cell;
import resources.SnakeBuilder;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link MementoTest} tests the Memento design pattern.
 * @author Ian laird
 */
public class MementoTest {
    private GameRecord gr, gr2;
    private Game g;

    /**
     * This stores a Game as a memento and then retrieves it.
     * @author Ian laird
     * @throws BuilderException-this will not be thrown
     */
    @Test
    void testRestoration() throws BuilderException {
        Cell player1Cell = Cell.createRandom(100, 100);
        Cell player2Cell = Cell.createRandom(100, 100);
        Cell puCell = Cell.createRandom(100, 100);
        g = gameMaker.generateGame(true);
        g.setPlayerOne(new SnakeBuilder().init().setColor(Color.BLACK).setStart(player1Cell).collectSnakeBuilder());
        g.setPlayerTwo(new SnakeBuilder().init().setColor(Color.GREEN).setStart(player2Cell).collectSnakeBuilder());
        g.setPowerUp(puCell);
        gr = g.createRecord();
        //Now see if everything is equal
        assertEquals(gr.getSnakeOneRecord(), g.getPlayerOne());
        assertEquals(gr.getSnakeTwoRecord(), g.getPlayerTwo());
        assertEquals(gr.getPowerUpLocation(), g.getPowerUp());
        g.restoreFromOldState(gr);
        gr2 = g.createRecord();
        assertEquals(gr, gr2);
    }
}
