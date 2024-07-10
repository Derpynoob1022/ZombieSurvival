package ui;

import model.Collision.CollisionManager;
import model.Entities.Entity;
import model.Entities.Player;
import model.Entities.Zombie;
import model.Handler.*;
import model.Items.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePanel extends JPanel implements Runnable {
    public static final int TILESIZE = 64;
    public static final int SCREEN_MAXROW = 14;
    public static final int SCREEN_MAXCOL = 18;
    public static final int SCREEN_WIDTH = TILESIZE * SCREEN_MAXCOL;
    public static final int SCREEN_HEIGHT = TILESIZE * SCREEN_MAXROW;
    public static final int WORLD_MAXROW = 100;
    public static final int WORLD_MAXCOL = 100;
    public static final int WORLD_WIDTH = WORLD_MAXCOL * TILESIZE;
    public static final int WORLD_HEIGHT = WORLD_MAXROW * TILESIZE;
    public static GameState GAMESTATE = GameState.play;
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static ArrayList<Item> droppedItems = new ArrayList<>();
    public static BufferedImage GAME_SNAPSHOT;
    private Thread updateThread;
    private Thread drawThread;
    private static final int FPS = 60;
    private static final double NS_PER_UPDATE = 1000000000.0 / FPS;
    private Ui ui = new Ui();
    private volatile boolean running = true;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(KeyHandler.getInstance());
        setup();
        startThreads();
        this.addMouseListener(MouseHandler.getInstance());
        this.addMouseMotionListener(MouseHandler.getInstance());
    }

    private void startThreads() {
        updateThread = new Thread(this::updateLoop);
        drawThread = new Thread(this::drawLoop);

        updateThread.start();
        drawThread.start();
    }

    private void updateLoop() {
        long lastTime = System.nanoTime();
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / NS_PER_UPDATE;
            lastTime = now;

            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("Updates per second (UPS): " + updates);
                updates = 0;
                timer += 1000;
            }
        }
    }

    private void drawLoop() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            double delta = (now - lastTime) / NS_PER_UPDATE;
            lastTime = now;

            if (delta >= 1) {
                repaint();
                frames++;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("Frames per second (FPS): " + frames);
                frames = 0;
                timer += 1000;
            }

            try {
                long sleepTime = (long)(NS_PER_UPDATE - (System.nanoTime() - now)) / 1000000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                running = false; // Stop threads on interruption
            }
        }
    }


    @Override
    public void run() {
        startThreads();
    }

    private void setup() {
        entities = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            entities.add(new Zombie((int) (Math.random() * 10) * TILESIZE, (int) (Math.random() * 10) * TILESIZE));
        }
        entities.add(Player.getInstance());
        droppedItems = new ArrayList<>();
    }

    public synchronized void update() {
        switch (GAMESTATE) {
            case play:
                for (Entity e : entities) {
                    e.update();
                }

                CollisionManager.getInstance().update();

                // Player dies
                if (Player.getInstance().getHealth() <= 0) {
                    System.exit(0);
                }

                for (Entity e : entities) {
                    e.execute();
                }

                Iterator<Entity> iterator = entities.iterator();
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
            case pause:
                PauseHandler.getInstance().update();
                break;
        }
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        switch (GAMESTATE) {
            case play:
                renderBackground(g2);
                ui.draw(g2);
                g2.dispose();
                break;
            case pause:
                if (GAME_SNAPSHOT == null) {
                    takeSnapshot();
                }
                g2.drawImage(GAME_SNAPSHOT, 0, 0, null);
                ui.drawPause(g2);
                g2.dispose();
                break;
            case inventory:
                if (GAME_SNAPSHOT == null) {
                    takeSnapshot();
                }
                g2.drawImage(GAME_SNAPSHOT, 0, 0, null);
                ui.drawInventory(g2);
                g2.dispose();
                break;
        }
    }

    private void renderBackground(Graphics2D g2) {
        Tiles.getInstance().draw(g2);

        for (Item i : droppedItems) {
            i.draw(g2);
        }

        for (Entity e : entities) {
            e.draw(g2);
        }
    }

    private void takeSnapshot() {
        if (GAME_SNAPSHOT == null || GAME_SNAPSHOT.getWidth() != getWidth() || GAME_SNAPSHOT.getHeight() != getHeight()) {
            GAME_SNAPSHOT = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g2d = GAME_SNAPSHOT.createGraphics();
        renderBackground(g2d);
        g2d.dispose();
    }

    public static ArrayList<Entity> getEntities() {
        return entities;
    }

    public static ArrayList<Item> getDroppedItems() {
        return droppedItems;
    }
}



//package ui;
//
//import model.Collision.CollisionManager;
//import model.Entities.Entity;
//import model.Entities.Player;
//import model.Entities.Zombie;
//import model.Handler.*;
//import model.Items.Item;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class GamePanel extends JPanel implements Runnable {
//    public static final int TILESIZE = 64;
//    public static final int SCREEN_MAXROW = 14;
//    public static final int SCREEN_MAXCOL = 18;
//    public static final int SCREEN_WIDTH = TILESIZE * SCREEN_MAXCOL;
//    public static final int SCREEN_HEIGHT = TILESIZE * SCREEN_MAXROW;
//    public static final int WORLD_MAXROW = 100;
//    public static final int WORLD_MAXCOL = 100;
//    public static final int WORLD_WIDTH = WORLD_MAXCOL * TILESIZE;
//    public static final int WORLD_HEIGHT = WORLD_MAXROW * TILESIZE;
//    public static volatile GameState GAMESTATE = GameState.play;
//    private static ArrayList<Entity> entities = new ArrayList<>();
//    private static ArrayList<Item> droppedItems = new ArrayList<>();
//    public static BufferedImage GAME_SNAPSHOT;
//    private Thread gameThread;
//    private final int FPS = 60;
//    private Ui ui = new Ui();
//    private ExecutorService updateExecutor;
//
//    public GamePanel() {
//        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
//        this.setBackground(Color.black);
//        this.setDoubleBuffered(true);
//        this.setFocusable(true);
//        this.addKeyListener(KeyHandler.getInstance());
//        setup();
//        this.startGameThread();
//        this.addMouseListener(MouseHandler.getInstance());
//        this.addMouseMotionListener(MouseHandler.getInstance());
//
//        // Create a fixed thread pool for update tasks
//        int numThreads = Runtime.getRuntime().availableProcessors(); // Use available cores
//        updateExecutor = Executors.newFixedThreadPool(numThreads);
//    }
//
//    private void startGameThread() {
//        gameThread = new Thread(this);
//        gameThread.start();
//    }
//
//    private void setup() {
//        for (int i = 0; i < 10; i++) {
//            entities.add(new Zombie((int) (Math.random() * 10) * TILESIZE, (int) (Math.random() * 10) * TILESIZE));
//        }
//        entities.add(Player.getInstance());
//    }
//
//    @Override
//    public void run() {
//        double delay = 1000000000.0 / FPS;
//        double nextStopTime = System.nanoTime() + delay;
//
//        while (gameThread != null) {
//            update();
//            repaint();
//
//            try {
//                double remainingTime = nextStopTime - System.nanoTime();
//                if (remainingTime > 0) {
//                    Thread.sleep((long) (remainingTime / 1000000));
//                }
//
//                nextStopTime += delay;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void update() {
//        switch (GAMESTATE) {
//            case play:
//                // Submit update tasks for entities to the executor
//                for (Entity entity : entities) {
//                    updateExecutor.submit(() -> entity.update());
//                }
//                // Handle collision detection and other game logic
//                CollisionManager.getInstance().update();
//
//                // Player dies
//                if (Player.getInstance().getHealth() <= 0) {
//                    System.exit(0);
//                }
//
//                for (Entity e : entities) {
//                    e.execute();
//                }
//
//                Iterator<Entity> iterator = entities.iterator();
//                while (iterator.hasNext()) {
//                    Entity e = iterator.next();
//                    if (e.getHealth() <= 0) {
//                        e.dropLoot();
//                        iterator.remove();
//                    }
//                }
//                break;
//            case inventory:
//                InventoryHandler.getInstance().update();
//                break;
//            case pause:
//                PauseHandler.getInstance().update();
//                break;
//        }
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        Graphics2D g2 = (Graphics2D) g;
//
//        switch (GAMESTATE) {
//            case play:
//                renderBackground(g2);
//                ui.draw(g2);
//                g2.dispose();
//                break;
//            case pause:
//                if (GAME_SNAPSHOT == null) {
//                    takeSnapshot();
//                }
//                g2.drawImage(GAME_SNAPSHOT, 0, 0, null);
//                ui.drawPause(g2);
//                g2.dispose();
//                break;
//            case inventory:
//                if (GAME_SNAPSHOT == null) {
//                    takeSnapshot();
//                }
//                g2.drawImage(GAME_SNAPSHOT, 0, 0, null);
//                ui.drawInventory(g2);
//                g2.dispose();
//                break;
//        }
//    }
//
//    public static ArrayList<Entity> getEntities() {
//        return entities;
//    }
//
//    public static ArrayList<Item> getDroppedItems() {
//        return droppedItems;
//    }
//
//    private void renderBackground(Graphics2D g2) {
//        // Render background and other game elements
//        Tiles.getInstance().draw(g2);
//
//        if (droppedItems != null) { // Ensure droppedItems is not null
//            for (Item item : droppedItems) {
//                item.draw(g2);
//            }
//        }
//
//        if (entities != null) { // Ensure entities is not null
//            for (Entity entity : entities) {
//                entity.draw(g2);
//            }
//        }
//    }
//
//    private void takeSnapshot() {
//        // Create a snapshot of the current game state for pause/inventory screens
//        GAME_SNAPSHOT = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = GAME_SNAPSHOT.createGraphics();
//        renderBackground(g2);
//        g2.dispose();
//    }
//
//    @Override
//    public void addNotify() {
//        super.addNotify();
//        requestFocus();
//    }
//
//    public void dispose() {
//        if (updateExecutor != null) {
//            updateExecutor.shutdown(); // Shutdown executor service
//        }
//        if (gameThread != null) {
//            gameThread.interrupt(); // Interrupt game thread if it's running
//            gameThread = null; // Clear reference
//        }
//    }
//}

