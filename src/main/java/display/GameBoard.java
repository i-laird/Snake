package display;

import old.resources.Cell;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andrew Walker
 * {@link GameBoard} is used in Screen for graphics
 */
public class GameBoard extends JPanel {
    // The width and height of the panel
    private int width;
    private int height;

    // The master matrix for the grid
    private Color[][] plot;

    /**
     * @param width  the width of the panel
     * @param height the height of the panel
     * @author Andrew Walker
     * This method is the constructor for the GameBoard
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        super.setPreferredSize(new Dimension(width, height));
        plot = new Color[this.height / Cell.getCellSize()][this.width / Cell.getCellSize()];
        super.setVisible(true);
    }

    /**
     * @param g Graphics object
     * @author Andrew Walker
     * This method is the overriden function to paint the panel
     */
    public void paintComponent(Graphics g) {
        for (int x = 0; x < height / Cell.getCellSize(); x++) {
            for (int y = 0; y < width / Cell.getCellSize(); y++) {
                g.setColor(plot[x][y]);
                g.fillRect(y * Cell.getCellSize(), x * Cell.getCellSize(), height / Cell.getCellSize(), width / Cell.getCellSize());
            }
        }
    }

    /**
     * @param row   the row of the plot to change
     * @param col   the col of the plot to change
     * @param color the Color of the plot to change
     * @author Andrew Walker
     * This method plots a new color in the master matrix
     */
    public void colorLocation(int row, int col, Color color) {
        this.plot[row][col] = color;
    }

    /**
     * @param row the row of the plot to change
     * @param col the col of the plot to change
     * @author Andrew Walker
     * This method plots the default color in the master matrix
     */
    public void unColorLocation(int row, int col) {
        this.plot[row][col] = Color.BLACK;
    }


    /**
     * @author Andrew Walker
     * This method plots the default color in the entire master matrix
     */
    public void plotBackground() {
        for (int x = 0; x < height / Cell.getCellSize(); x++) {
            for (int y = 0; y < width / Cell.getCellSize(); y++) {
                plot[x][y] = Color.BLACK;
            }
        }
    }
}
