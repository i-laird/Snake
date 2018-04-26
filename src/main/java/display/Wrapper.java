package display;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class Wrapper extends JPanel {
    protected static Logger LOGGER = Logger.getLogger("Wrapper");
    protected static final int WRAPPER_WIDTH = 800;
    protected static final int WRAPPER_HEIGHT = 400;
    protected static final int COMPONENT_WIDTH = 400;
    protected static final int COMPONENT_HEIGHT = 400;

    private GameBoard board;
    private TextBox textBox;

    public Wrapper(){
        this.setPreferredSize(new Dimension(WRAPPER_WIDTH, WRAPPER_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.requestFocus();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    /**
     * @author: Andrew Walker
     * This method initializes the gameBoard and adds it to the panel
     */
    public void initBoard(){
        this.board = new GameBoard(COMPONENT_WIDTH, COMPONENT_HEIGHT);
        super.add(BorderLayout.LINE_START, this.board);
        LOGGER.info("Initialized Board");
    }

    public void initText(){
        this.textBox = new TextBox(COMPONENT_WIDTH, COMPONENT_HEIGHT);
        super.add(BorderLayout.LINE_END, this.textBox);
        LOGGER.info("Initialized TextBox");
    }

    public void update() {
        board.repaint();
        textBox.repaint();
        super.repaint();
    }

    public GameBoard getBoard(){
        return this.board;
    }

    public TextBox getTextBox(){
        return this.textBox;
    }

}
