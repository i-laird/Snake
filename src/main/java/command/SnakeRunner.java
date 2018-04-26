import game.*;

public class SnakeRunner extends GameRunner {

    private SnakeGame sg = new SnakeGame();

    @Override
    public void execute() {
        sg.startGame();
    }
}
