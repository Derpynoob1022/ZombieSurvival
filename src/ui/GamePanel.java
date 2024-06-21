package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePanel extends JPanel implements Runnable {
    public static final int TILESIZE = 72;
    public static final int SCREEN_MAXROW = 12;
    public static final int SCREEN_MAXCOL = 16;
    public static final int SCREEN_WIDTH = TILESIZE * SCREEN_MAXCOL;
    public static final int SCREEN_HEIGHT = TILESIZE * SCREEN_MAXROW;
    public static final int WORLD_MAXROW = 100;
    public static final int WORLD_MAXCOL = 100;
    public static GameState GAMESTATE = GameState.play;
    public static ArrayList<Entity> ENTITIES;
    public static ArrayList<Item> DROPPED_ITEMS;
    private Thread GT;
    private final int FPS = 60;
    private Ui ui = new Ui();

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(KeyHandler.getInstance());
        setup();
        this.GameThread();
        this.addMouseListener(MouseHandler.getInstance());
        this.addMouseMotionListener(MouseHandler.getInstance());
    }

    private void GameThread() {
        GT = new Thread(this);
        GT.start();
    }

    @Override
    public void run() {

        double delay = 1000000000 / FPS;
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

    private void setup() {
        ENTITIES = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ENTITIES.add(new Zombie((i + 6) *TILESIZE, 2 * TILESIZE));
        }
        ENTITIES.add(Player.getInstance());
        DROPPED_ITEMS = new ArrayList<>();
    }

    public synchronized void update() {
        switch(GAMESTATE) {
            case play:
                for (Entity e : ENTITIES) {
                    e.update();
                }

                // Player dies
                if (Player.getInstance().getHealth() <= 0) {
                    System.exit(0);
                }

                Iterator<Entity> iterator = ENTITIES.iterator();
                while (iterator.hasNext()) {
                    Entity e = iterator.next();
                    if (e.getHealth() <= 0) {
                        e.dropLoot();
                        iterator.remove();
                    }
                }
                break;

            case inventory:
                InventoryHandler.getInstance().update();
                break;
        }
    }

    // used synchronized to prevent concurrent modification error
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        switch (GAMESTATE) {
            case play:
                renderBackground(g2);
                ui.draw(g2);
                g2.dispose();
                break;
            case pause:
                break;
            case inventory:
                renderBackground(g2);
                ui.drawInventory(g2);
                g2.dispose();
                break;
        }
    }

    private void renderBackground(Graphics2D g2) {
        // drawing the components when playing
        Tiles.getInstance().draw(g2);

        for (Item i : DROPPED_ITEMS) {
            i.draw(g2);
        }

        for (Entity e : ENTITIES) {
            e.draw(g2);
        }
    }
}
