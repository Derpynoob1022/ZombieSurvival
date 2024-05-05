package ui;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        super();
        this.add(new GamePanel());
        this.setTitle("Zombie");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
