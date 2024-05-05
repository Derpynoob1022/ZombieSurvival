package ui;

import model.KeyHandler;
import model.Player;
import model.Tiles;
import model.Zombie;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public final int TILESIZE = 48;
    public final int SCREEN_MAXROW = 12;
    public final int SCREEN_MAXCOL = 16;
    public final int SCREEN_WIDTH = TILESIZE * SCREEN_MAXCOL;
    public final int SCREEN_HEIGHT = TILESIZE * SCREEN_MAXROW;
    public final int WORLD_MAXROW = 100;
    public final int WORLD_MAXCOL = 100;
    private Zombie z = new Zombie(this);
    private Thread GT;
    private int FPS = 120;
    private Tiles tiles = new Tiles(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(KeyHandler.getInstance());
        this.GameThread();
    }

    private void GameThread() {
        GT = new Thread(this);
        GT.start();
    }

    @Override
    public void run() {

        Player.initialize(this);
        double delay = 1000000000/FPS;
        double nextStopTime = System.nanoTime() + delay;

        while (GT != null) {
            update();
            repaint();

            try {

                double remainingTime = nextStopTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextStopTime += delay;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        Player.getInstance().update();
        z.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tiles.draw(g2);

        Player.getInstance().draw(g2);

        z.draw(g2);


        g2.dispose();
    }
}
