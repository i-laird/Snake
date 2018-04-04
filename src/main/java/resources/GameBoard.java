package resources;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private int width;
    private int height;
    private Color[][] plot;
    private Color BACKGROUND = Color.BLACK;

    public GameBoard(int width, int height){
        this.width = width;
        this.height = height;
        plot = new Color[this.height][this.width];
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int x = 0; x < height / Cell.getCellSize(); x++){
            for(int y = 0; y < width / Cell.getCellSize(); y++){
                g.setColor(plot[x][y]);
                g.drawRect(x * Cell.getCellSize(), y * Cell.getCellSize(), height / Cell.getCellSize(), width / Cell.getCellSize());
            }
        }
    }

    /**
     * this function should color the rectangle at a certain location in the game the desired color.
     * This indicates that the player currently has visited there
     */
    public void colorLocation(int row, int col, Color color){
        this.plot[row][col] = color;
    }

    /**
     * This function should restore a block to default color indicating that the player no longer covers
     * that space.
     */
    public void unColorLocation(int row, int col){
        this.plot[row][col] = BACKGROUND;
    }

    public void refresh(){
        this.repaint();
    }
}
