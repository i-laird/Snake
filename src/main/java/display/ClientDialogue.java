package display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ClientDialogue extends JPanel implements ActionListener {
    protected JLabel typeLabel;
    protected JButton submitButton;
    protected JTextField username, host;

    public ClientDialogue() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 100));

        typeLabel = new JLabel("Please wait until the server has initialized.");

        username = new JTextField("Please enter your name");
        username.setPreferredSize(new Dimension(250, 10));
        username.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if(username.getText().isEmpty()) {
                    username.setText("Please enter your name");
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(username.getText().equals("Please enter your name")) {
                    username.setText("");
                }
            }
        });

        host = new JTextField("Please enter the hostname");
        host.setPreferredSize(new Dimension(250, 10));
        host.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if(host.getText().isEmpty()) {
                    host.setText("Please enter the hostname");
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(host.getText().equals("Please enter the hostname")) {
                    host.setText("");
                }
            }
        });

        submitButton = new JButton("Submit");
        submitButton.setVerticalTextPosition(AbstractButton.CENTER);
        submitButton.setHorizontalTextPosition(AbstractButton.LEADING);
        submitButton.setActionCommand("submit");

        submitButton.addActionListener(this);

        add(typeLabel);
        add(username);
        add(host);
        add(submitButton);
    }

    public void actionPerformed(ActionEvent e) {
        if ("yes".equals(e.getActionCommand())) {

        } else {

        }
    }
}

