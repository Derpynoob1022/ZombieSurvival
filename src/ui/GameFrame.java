package ui;

import javax.swing.*;

public class GameFrame extends JFrame {

    private GamePanel gamePanel;

    public GameFrame() {
        super();
        this.gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setTitle("Zombie");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
