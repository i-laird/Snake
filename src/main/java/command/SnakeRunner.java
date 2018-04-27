package command;

import game.SnakeGame;

/**
 * @author Andrew Walker
 * This class is a runner for the Snake game. Implements the command design pattern by extending the
 * GameRunner
 */
public class SnakeRunner extends GameRunner {

    // The SnakeGame to run
    private SnakeGame sg = new SnakeGame();

    /**
     * @author Andrew Walker
     * Implements the execute function to run the Snake game
     */
    @Override
    public void execute() {
        sg.startGame();
    }
}
