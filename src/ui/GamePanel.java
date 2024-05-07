package ui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int TILESIZE = 48;
    public static final int SCREEN_MAXROW = 12;
    public static final int SCREEN_MAXCOL = 16;
    public static final int SCREEN_WIDTH = TILESIZE * SCREEN_MAXCOL;
    public static final int SCREEN_HEIGHT = TILESIZE * SCREEN_MAXROW;
    public static final int WORLD_MAXROW = 100;
    public static final int WORLD_MAXCOL = 100;
    private Zombie z = new Zombie();
    private Thread GT;
    private int FPS = 60;

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

        Tiles.getInstance().draw(g2);

        Player.getInstance().draw(g2);

        z.draw(g2);

        g2.dispose();
    }
}
