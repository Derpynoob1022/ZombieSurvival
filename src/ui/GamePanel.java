package ui;

import model.Background.Tiles;
import model.Blocks.Block;
import model.Collision.CollisionManager;
import model.Inventory.InventoryHandler;
import model.Items.DroppedItem;
import model.Entities.Enemy;
import model.Entities.Entity;
import model.Entities.Player;
import model.Handler.*;
import model.Handler.StateHandler.*;
import model.AOE.Explosion;
import model.Items.Projectiles.Projectiles;
import model.Items.PowerUp;
import model.Blocks.WoodWall;

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
    public static final int WORLD_MAXROW = 102;
    public static final int WORLD_MAXCOL = 102;
    public static final int WORLD_WIDTH = WORLD_MAXCOL * TILESIZE;
    public static final int WORLD_HEIGHT = WORLD_MAXROW * TILESIZE;
    public static GameState GAMESTATE;
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static ArrayList<DroppedItem> droppedItems = new ArrayList<>();
    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static ArrayList<Projectiles> projectiles = new ArrayList<>();
    private static ArrayList<Block> blocks = new ArrayList<>();
    private static ArrayList<Explosion> explosions = new ArrayList<>();
    public static BufferedImage GAME_SNAPSHOT;
    private Thread updateThread;
    private Thread drawThread;
    private static final int FPS = 60;
    private Ui ui = new Ui();


    public GamePanel() {
        // Set the JFrame to the correct configuration
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        GAMESTATE = GameState.title;
        this.addKeyListener(KeyHandler.getInstance());
        this.addMouseListener(MouseHandler.getInstance());
        this.addMouseMotionListener(MouseHandler.getInstance());
        this.addMouseWheelListener(MouseHandler.getInstance());

        setup();        // Any additional setup
        startThreads();
    }

    @Override
    public void run() {
        startThreads();
    }

    private void setup() {
        blocks.add(new WoodWall(2, 2));
        blocks.add(new WoodWall(2, 1));
        blocks.add(new WoodWall(2, 3));
        blocks.add(new WoodWall(2, 4));
        blocks.add(new WoodWall(2, 5));
        blocks.add(new WoodWall(2, 6));
        entities.add(Player.getInstance());
    }

    private void startThreads() {
        // Start the threads (one for drawing and another for updating)
        updateThread = new Thread(this::updateLoop);
        drawThread = new Thread(this::drawLoop);

        updateThread.start();
        drawThread.start();
    }

    private void updateLoop() {
        double delay = 1000000000 / FPS;
        double nextStopTime = System.nanoTime() + delay;

        while (updateThread != null) {
            update();

            try {

                double remainingTime = nextStopTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime); // Stops updating for the remaining time
                nextStopTime += delay;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawLoop() {
        double delay = 1000000000 / (FPS);
        double nextStopTime = System.nanoTime() + delay;

        while (drawThread != null) {
            repaint();

            try {

                double remainingTime = nextStopTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime); // Stops updating for the remaining time
                nextStopTime += delay;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void update() {
        // TODO: Maybe refactor this playstate into another class called PlayHandler
        switch (GAMESTATE) {
            case play:
                for (Entity e : entities) {
                    e.update();
                }

                for (Explosion e: explosions) {
                    e.update();
                }

                // Explosion update returns true if it's timer ran out, removes from the list currently updating.
                Iterator<Explosion> iteratorExplosion = explosions.iterator();
                while (iteratorExplosion.hasNext()) {
                    Explosion e = iteratorExplosion.next();
                    if (e.update()) {
                        iteratorExplosion.remove();
                    }
                }

                // Manages all the collision detection
                CollisionManager.getInstance().update();

                // If the player dies, switches GAMESTATE to death screen.
                // TODO: add a death screen
                if (Player.getInstance().getHealth() <= 0) {
                    GAMESTATE = GameState.death;
                }

                // Executes the movement and updates after resolving all the collisions
                for (Entity e : entities) {
                    e.execute();
                }

                for (Projectiles p : projectiles) {
                    p.execute();
                }

                // Remove enemies after their health falls to 0
                Iterator<Entity> iteratorEntity = entities.iterator();
                while (iteratorEntity.hasNext()) {
                    Entity e = iteratorEntity.next();
                    if (e.getHealth() <= 0 && e instanceof Enemy) {
                        ((Enemy) e).dropLoot();
                        Player.getInstance().addXp(e.getXpDrop());
                        iteratorEntity.remove();
                    }
                }

                // Despawn items on the ground after a while, so it doesn't lag
                Iterator<DroppedItem> iteratorItem = droppedItems.iterator();
                while (iteratorItem.hasNext()) {
                    DroppedItem e = iteratorItem.next();
                    e.addGroundCount();
                    e.addPickUpCount();
                    // Dropped item disappear after a while
                    if (e.getGroundCount() > 1200) {
                        iteratorItem.remove();
                    }
                }

                // Despawn items on the ground after a while, so it doesn't lag
                Iterator<PowerUp> iteratorPowerUps = powerUps.iterator();
                while (iteratorItem.hasNext()) {
                    DroppedItem e = iteratorItem.next();
                    e.addGroundCount();
                    // Dropped item disappear after a while
                    if (e.getGroundCount() > 3600) {
                        iteratorItem.remove();
                    }
                }

                // If only player is left standing, adds another level and spawns the respective mobs
                if (entities.size() == 1) {
                    LevelHandler.getInstance().addLevel();
                    LevelHandler.getInstance().spawn();
                }

                break;
            case inventory:
                InventoryHandler.getInstance().update();
                break;
            case pause:
                PauseHandler.getInstance().update();
                break;
            case title:
                TitleHandler.getInstance().update();
                break;
            case settings:
                SettingsHandler.getInstance().update();
                break;
            case control:
                ControlHandler.getInstance().update();
                break;
            case death:
                DeathHandler.getInstance().update();
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
                if (GAME_SNAPSHOT != null) {
                    GAME_SNAPSHOT = null; // Reset the snapshot
                }
                break;
            case pause:
                if (GAME_SNAPSHOT == null) {
                    takeSnapshot(); // Generates a screenshot of the play-screen, so it gets displayed in the background without keep regenerating the image.
                }
                g2.drawImage(GAME_SNAPSHOT, 0, 0, null);
                ui.drawPause(g2);
                break;
            case inventory:
                if (GAME_SNAPSHOT == null) {
                    takeSnapshot();
                }
                g2.drawImage(GAME_SNAPSHOT, 0, 0, null);
                ui.drawInventory(g2);
                break;
            case title:
                ui.drawTitle(g2);
                break;
            case settings:
                ui.drawSettings(g2);
                break;
            case control:
                ui.drawControl(g2);
                break;
            case death:
                ui.drawDeath(g2);
                break;
        }

        g2.dispose(); // Disposes all the graphical objects
    }

    private void renderBackground(Graphics2D g2) {
        // Renders all the objects in the game
        Tiles.getInstance().draw(g2);

        for (Explosion c : explosions) {
            c.draw(g2);
        }

        for (Block b : blocks) {
            b.draw(g2);
        }

        for (DroppedItem i : droppedItems) {
            i.draw(g2);
        }

        for (PowerUp p : powerUps) {
            p.draw(g2);
        }

        for (Entity e : entities) {
            e.draw(g2);
        }

        for (Projectiles p : projectiles) {
            p.draw(g2);
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

    public static ArrayList<DroppedItem> getDroppedItems() {
        return droppedItems;
    }

    public static ArrayList<Projectiles> getProjectiles() {
        return projectiles;
    }

    public static ArrayList<Block> getBlocks() {
        return blocks;
    }

    public static ArrayList<Explosion> getExplosions() {
        return explosions;
    }

    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
}
