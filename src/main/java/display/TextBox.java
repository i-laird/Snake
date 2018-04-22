package display;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class TextBox extends JTextArea {

    private Deque<String> components;
    private int capacity;

    public TextBox(){
        this.components = new ArrayDeque<>();
        this.setEditable(false);
    }

    public TextBox(int capacity){
        this.components = new ArrayDeque<>();
        this.capacity = capacity;
        this.setEditable(false);
    }

    public void addText (String text){
        if(this.components.size() >= this.capacity){
            this.components.removeFirst();
        }
        this.components.addLast(text);
    }



}
