package display;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * @author Andrew Walker
 * This class extends JLabel to add functionality to the textbox
 */
public class TextBox extends JLabel {

    private Deque<String> components;
    private int capacity;

    /**
     * @author Andrew Walker
     * This is the constructor for a text box component
     * @param width the width of the textbox
     * @param height the height of the textbox
     */
    public TextBox(int width, int height){
        this.components = new ArrayDeque<>();
        this.capacity = 10;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.setVerticalTextPosition(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setBorder(new EmptyBorder(0,20,0,0));
        super.setVisible(true);
    }

    /**
     * @author Andrew Walker
     * This function adds a new line of text to the textbox
     * @param text the text to add to the screen
     */
    public void addText (String text){
        if(this.components.size() >= this.capacity){
            this.components.removeFirst();
        }
        this.components.addLast(text);
        StringBuilder stringBuilt = new StringBuilder();
        stringBuilt.append("<html>");
        for(String s : this.components){
            stringBuilt.append("\t\t\t").append(s).append("<br>");
        }
        stringBuilt.append("</html>");
        String display = stringBuilt.toString();
        this.setText(display);
    }
}
