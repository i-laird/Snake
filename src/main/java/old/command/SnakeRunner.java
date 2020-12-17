package old.command;

import old.game.SnakeGame;

/**
 * @author Andrew Walker
 * This class is a runner for the Snake old.game. Implements the old.command design pattern by extending the
 * GameRunner
 */
public class SnakeRunner extends GameRunner {

    // The SnakeGame to run
    private SnakeGame sg = new SnakeGame();

    /**
     * @author Andrew Walker
     * Implements the execute function to run the Snake old.game
     */
    @Override
    public void execute() {
        sg.startGame();
    }
}
