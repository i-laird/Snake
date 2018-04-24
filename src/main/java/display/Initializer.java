package display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Initializer extends JPanel implements ActionListener {
    protected JLabel typeLabel;
    protected JButton serverButton, clientButton;
    protected ClientDialogue cd;

    public Initializer() {

        setPreferredSize(new Dimension(200, 200));

        typeLabel = new JLabel("Will you be a server?");

        serverButton = new JButton("Yes");
        serverButton.setVerticalTextPosition(AbstractButton.CENTER);
        serverButton.setHorizontalTextPosition(AbstractButton.LEADING);
        serverButton.setActionCommand("yes");
        serverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        serverButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        clientButton = new JButton("No");
        clientButton.setVerticalTextPosition(AbstractButton.CENTER);
        clientButton.setHorizontalTextPosition(AbstractButton.LEADING);
        clientButton.setActionCommand("no");
        clientButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        serverButton.addActionListener(this);
        clientButton.addActionListener(this);

        serverButton.setToolTipText("Click this button to disable the middle button.");
        clientButton.setToolTipText("This middle button does nothing when click it.");

        cd = new ClientDialogue();
        cd.setVisible(false);

        add(typeLabel);
        add(serverButton);
        add(clientButton);
        add(cd);
    }

    public void actionPerformed(ActionEvent e) {
        if ("yes".equals(e.getActionCommand())) {
            cd.setVisible(true);
        } else {

        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Initializer newContentPane = new Initializer();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void showScren() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {createAndShowGUI();}
        });
    }
}