package ui;

import javax.swing.*;

public class Frame extends JFrame implements Constants {
 
    public Frame() {
        
        setVisible(true);
        setTitle("New life");
        setBounds(10, 10, 800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Panel panel = new Panel();
        setContentPane(panel);
    }
}
