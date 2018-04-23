package display;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class TextBox extends JLabel {

    private Deque<String> components;
    private int capacity;
    private int width, height;

    public TextBox(int width, int height){
        this.components = new ArrayDeque<>();
        this.capacity = 5;
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.setVerticalTextPosition(SwingConstants.TOP);
        super.setVisible(true);
    }

    public void addText (String text){
        if(this.components.size() >= this.capacity){
            this.components.removeFirst();
        }
        this.components.addLast(text);
        String display = "";
        for(String s : this.components){
            display += s + "\n";
        }
        this.setText(display);
    }
}
