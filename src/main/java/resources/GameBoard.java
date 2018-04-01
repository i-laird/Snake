package resources;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private int width;
    private int height;
    private int tiles;

    public GameBoard(int width, int height, int tiles){
        this.width = width;
        this.height = height;
        this.tiles = tiles;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for(int x = 0; x < height / tiles; x++){
            for(int y = 0; y < width / tiles; y++){
                g.drawRect(x * tiles, y * tiles, height / tiles, width / tiles);
            }
        }
    }
}
