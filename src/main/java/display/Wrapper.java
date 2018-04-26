package display;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * @author Andrew Walker
 * This is a wrapper to contain the components of the game on the screen
 */
public class Wrapper extends JPanel {

    private static Logger LOGGER = Logger.getLogger("Wrapper");
    private static final int WRAPPER_WIDTH = 800;
    private static final int WRAPPER_HEIGHT = 400;
    private static final int COMPONENT_WIDTH = 400;
    private static final int COMPONENT_HEIGHT = 400;

    private GameBoard board;
    private TextBox textBox;

    /**
     * @author Andrew Walker
     * Constructs a default wrapper
     */
    public Wrapper(){
        this.setPreferredSize(new Dimension(WRAPPER_WIDTH, WRAPPER_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.requestFocus();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    /**
     * @author Andrew Walker
     * This method initializes the gameBoard and adds it to the panel
     */
    public void initBoard(){
        this.board = new GameBoard(COMPONENT_WIDTH, COMPONENT_HEIGHT);
        super.add(BorderLayout.LINE_START, this.board);
        LOGGER.info("Initialized Board");
    }

    /**
     * @author Andrew Walker
     * This method initializes the textBox and adds it to the panel
     */
    public void initText(){
        this.textBox = new TextBox(COMPONENT_WIDTH, COMPONENT_HEIGHT);
        super.add(BorderLayout.LINE_END, this.textBox);
        LOGGER.info("Initialized TextBox");
    }

    /**
     * @author Andrew Walker
     * This function updates the components in the wrapper
     */
    public void update() {
        board.repaint();
        textBox.repaint();
        super.repaint();
    }

    /**
     * @author Andrew Walker
     * This function returns the {@link GameBoard}
     * @return the {@link GameBoard}
     */
    public GameBoard getBoard(){
        return this.board;
    }

    /**
     * @author Andrew Walker
     * This function returns the {@link TextBox}
     * @return the {@link TextBox}
     */
    public TextBox getTextBox(){
        return this.textBox;
    }

}
