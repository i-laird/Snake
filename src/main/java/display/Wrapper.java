package display;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class Wrapper extends JPanel {

    private GameBoard board;
    private TextBox textBox;
    protected static Logger LOGGER = Logger.getLogger("Wrapper");
    private int width;
    private int height;
    private int numComponents = 2;

    public Wrapper(int width, int height){
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(this.width, this.height));
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
        this.board = new GameBoard(this.width / numComponents, this.height);
        super.add(BorderLayout.LINE_START, this.board);
        LOGGER.info("Initialized Board");
    }

    public void initText(){
        this.textBox = new TextBox(this.width / numComponents, this.height);
        this.textBox.addText("This is a test");
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
