package model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static ui.GamePanel.*;

public class Player extends Entity {
    private final int screenX;
    private final int screenY;
    private static Player player = new Player();
    private AttackShape attackArea;
    private boolean attackCD;
    private int attackCDCount;
    private double angle;
    private int maxAttackDuration = 5;
    private int curAttackDuration = 0;
    private ArrayList<Item> inventory;
    private int coin;
    private int selectedItem;

    private Player() {
        posX = 0;
        posY = TILESIZE;
        moveSpeed = 6;
        screenX = SCREEN_WIDTH/2 - TILESIZE/2;
        screenY = SCREEN_HEIGHT/2 - TILESIZE/2;
        maxHealth = 10;
        health = 100;

        hitBox = new Rectangle(12, 12, 48, 48);

        inventory = new ArrayList<>();
        coin = 0;
        selectedItem = 1;
    }

    public static Player getInstance() {
        return player;
    }

    @Override
    public void update() {
        boolean keyPressedW = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_W);
        boolean keyPressedA = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_A);
        boolean keyPressedS = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_S);
        boolean keyPressedD = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_D);
        boolean clicked = MouseHandler.getInstance().pressed;

        selectedItem = KeyHandler.getInstance().getLastNumberKeyPressed();

        velX = 0;
        velY = 0;

        if (keyPressedW) {
            velY -= 1;
        }
        if (keyPressedA) {
            velX -= 1;
        }
        if (keyPressedS) {
            velY += 1;
        }
        if (keyPressedD) {
            velX += 1;
        }

        double magnitude = Math.sqrt(velX * velX + velY * velY);

        if (magnitude != 0) {
            double normalizedVelX = velX / magnitude;
            double normalizedVelY = velY / magnitude;
            velX = (int) Math.round(normalizedVelX * moveSpeed);
            velY = (int) Math.round(normalizedVelY * moveSpeed);
        }

        // Checking for collision in the next frame
        // TODO: remove collision boolean variable?
        collision = false;
        CollisionChecker.getInstance().CheckTile(this);
        CollisionChecker.getInstance().checkEntityCollision(this, ENTITIES);
        CollisionChecker.getInstance().checkItemPickUp();

        // Executing the movement
        posX += velX;
        posY += velY;

        // attack animation timer
        if (attackArea != null) {
            curAttackDuration++;
        }

        // If attack animation exceeds the max, its going to set the attackArea to null
        if (curAttackDuration > maxAttackDuration) {
            attackArea = null;
            curAttackDuration = 0;
        }

        // timer to be vulnerable again
        if (invincible == true) {
            iFrames++;
            if (iFrames > 60) {
                invincible = false;
                iFrames = 0;
            }
        }

        // attacking
        if (!attackCD) {
           if (clicked) {
               attack();
           }
        }

        // timer to attack again
        if (attackCD) {
            attackCDCount++;
            if (attackCDCount > 20) {
                attackCD = false;
                attackCDCount = 0;
            }
        }
        // System.out.println(inventory.size());
    }

    @Override
    public void dropLoot() {
        // Player doesn't drop any loot
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(screenX, screenY, TILESIZE, TILESIZE);

        g2.setColor(Color.RED);
        g2.fillRect(screenX + hitBox.x, screenY + hitBox.y, hitBox.width, hitBox.height);

        if (attackArea != null) {
            attackArea.draw(g2);
        }
    }

    public void attack() {
        angle = Math.atan2(MouseHandler.getInstance().y - SCREEN_HEIGHT / 2, MouseHandler.getInstance().x - SCREEN_WIDTH / 2);
        attackArea = new AttackShape(posX + TILESIZE / 2, posY + TILESIZE / 2, TILESIZE * 2, TILESIZE, angle);

        for (int i : CollisionChecker.getInstance().checkAttackCollision(attackArea, ENTITIES)) {
            Entity curEntity = ENTITIES.get(i);
            if (!curEntity.invincible) {
                curEntity.hit();
                curEntity.invincible = true;
            }
        }
        attackCD = true;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public static Player getPlayer() {
        return player;
    }

    public AttackShape getAttackArea() {
        return attackArea;
    }

    public boolean isAttackCD() {
        return attackCD;
    }

    public int getAttackCDCount() {
        return attackCDCount;
    }

    public double getAngle() {
        return angle;
    }

    public int getMaxAttackDuration() {
        return maxAttackDuration;
    }

    public int getCurAttackDuration() {
        return curAttackDuration;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public int getCoin() {
        return coin;
    }

    public static void setPlayer(Player player) {
        Player.player = player;
    }

    public void setAttackArea(AttackShape attackArea) {
        this.attackArea = attackArea;
    }

    public void setAttackCD(boolean attackCD) {
        this.attackCD = attackCD;
    }

    public void setAttackCDCount(int attackCDCount) {
        this.attackCDCount = attackCDCount;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setMaxAttackDuration(int maxAttackDuration) {
        this.maxAttackDuration = maxAttackDuration;
    }

    public void setCurAttackDuration(int curAttackDuration) {
        this.curAttackDuration = curAttackDuration;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public void addCoin() {
        this.coin++;
    }
}
