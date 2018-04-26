import command.GameRunner;
import command.SnakeRunner;

/**
 * @author Andrew Walker
 * This is the main class for running the GameRunner
 */
public class Main {
    public static void main(String[] args) {
        GameRunner sn = new SnakeRunner();
        sn.execute(); }
}
