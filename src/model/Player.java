package model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

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
    private int coin;
    private Item selectedItem;
    private Helper helper;

    private Player() {
        helper = new Helper();
        posX = 0;
        posY = TILESIZE;
        moveSpeed = 6;
        acceleration = 1;
        screenX = SCREEN_WIDTH/2 - TILESIZE/2;
        screenY = SCREEN_HEIGHT/2 - TILESIZE/2;
        maxHealth = 10;
        health = 100;
        mass = 10;

        hitBox = new Rectangle(12, 12, 48, 48);
        coin = 0;
        selectedItem = InventoryHandler.getInstance().getItem(KeyHandler.getInstance().getLastNumberKeyPressed());
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

        selectedItem = InventoryHandler.getInstance().getItem(KeyHandler.getInstance().getLastNumberKeyPressed() - 1);

//        if (selectedItem != null) {
//            System.out.println(selectedItem.getClass());
//        }
//            for (int i = 0; i < 4; i++) {
//                if (inventory[i] != null) {
//                    System.out.print(inventory[i].getClass());
//                }
//
//        }
//        System.out.println(KeyHandler.getInstance().getLastNumberKeyPressed());

        double accX = 0;
        double accY = 0;

        // Handle key inputs for acceleration
        if (keyPressedW) {
            accY -= 1;
        }
        if (keyPressedA) {
            accX -= 1;
        }
        if (keyPressedS) {
            accY += 1;
        }
        if (keyPressedD) {
            accX += 1;
        }

        // Calculate the magnitude of the acceleration vector
        double magnitude = Math.sqrt(accX * accX + accY * accY);

        if (magnitude != 0) {
            // Normalize the acceleration vector
            double normalizedAccX = accX / magnitude;
            double normalizedAccY = accY / magnitude;

            // Apply the acceleration magnitude
            accX = normalizedAccX * acceleration;
            accY = normalizedAccY * acceleration;

            // Calculate the proposed new velocity
            double newVelX = velX + accX;
            double newVelY = velY + accY;

            // Calculate the speed of the proposed new velocity
            double proposedSpeed = Math.sqrt(newVelX * newVelX + newVelY * newVelY);

            if (proposedSpeed > moveSpeed) {
                // Normalize the velocity to not exceed the maximum speed
                double ratio = moveSpeed / proposedSpeed;
                newVelX *= ratio;
                newVelY *= ratio;
            }

            // Update the velocity
            velX = (float) newVelX;
            velY = (float) newVelY;
        } else {
            // Apply friction when there is no acceleration
            velX *= 0.9;
            velY *= 0.9;
        }

        // System.out.println(posX + " " + posY);

//        System.out.println(String.format("accX: %.2f, accY: %.2f, newVelX: %.2f, newVelY: %.2f, velX: %.2f, velY: %.2f",
//                accX, accY, velX, velY, velX, velY));

        // Checking for collision in the next frame
        collision = false;
        CollisionChecker.getInstance().checkTile(this);
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

        // attacking if character is holding a sword
        if (selectedItem instanceof Sword) {
            if (!attackCD) {
                if (clicked) {
                    attack();
                }
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

        // TODO: maybe change this so that its not null but it just doesn't draw or deal damage when not attacking
        if (attackArea != null) {
            drawAttack(g2);
        }
    }

    public void attack() {
        if (selectedItem instanceof Sword) {
            // TODO: add custom attack area depending on the weapon
            angle = Math.atan2(MouseHandler.getInstance().y - SCREEN_HEIGHT / 2, MouseHandler.getInstance().x - SCREEN_WIDTH / 2);
            attackArea = new AttackShape(posX + TILESIZE / 2, posY + TILESIZE / 2, TILESIZE * 2, TILESIZE, angle);

            for (int i : CollisionChecker.getInstance().checkAttackCollision(attackArea, ENTITIES)) {
                Entity curEntity = ENTITIES.get(i);
                if (!curEntity.invincible) {
                    curEntity.hit(((Sword) selectedItem).getDamage());
                    curEntity.invincible = true;
                }
            }
            attackCD = true;
        }  else {
            // TODO: add different weapon attacks
        }
    }

    public boolean addItem(Item item) {
        for (int i = 0; i < InventoryHandler.getInstance().getInventory().length; i++) {
            if (InventoryHandler.getInstance().getItem(i) == null) { // Find the first empty slot
                InventoryHandler.getInstance().addItem(i, item); // Add the new item
                return true;
            }
        }
        return false;
    }

    public void drawAttack(Graphics2D g2) {
        Sword currentWeapon = (Sword) selectedItem;
        BufferedImage attack = helper.scaleImage(currentWeapon.image, currentWeapon.getAttackWidth(), currentWeapon.getAttackHeight());

        // Calculate center of the screen
        int centerX = SCREEN_WIDTH / 2;
        int centerY = SCREEN_HEIGHT / 2;

        // Calculate the bottom middle point of the image
        int imageWidth = attack.getWidth();
        int imageHeight = attack.getHeight();
        int bottomMiddleX = imageWidth / 2;
        int bottomMiddleY = imageHeight;

        // Create a new transform for drawing centered on the screen
        AffineTransform drawTransform = new AffineTransform();

        // Translate to the center of the screen first
        drawTransform.translate(centerX - bottomMiddleX, centerY - bottomMiddleY);

        // Rotate around the bottom middle point of the image
        drawTransform.rotate(angle + Math.PI / 2, bottomMiddleX, bottomMiddleY);

        // Translate the image so that the bottom middle point is at the origin
        // drawTransform.translate(-bottomMiddleX, -bottomMiddleY);

        // Draw the image using the transformed graphics context
        g2.drawImage(attack, drawTransform, null);
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

    public Slot[] getInventory() {
        return InventoryHandler.getInstance().getInventory();
    }

    public void setCurAttackDuration(int curAttackDuration) {
        this.curAttackDuration = curAttackDuration;
    }

    public void addCoin() {
        this.coin++;
    }
}
