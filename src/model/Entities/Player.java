package model.Entities;

import model.Handler.*;
import model.Items.Item;
import model.Slot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
        maxAcceleration = 1;
        screenX = SCREEN_WIDTH / 2 - TILESIZE / 2;
        screenY = SCREEN_HEIGHT / 2 - TILESIZE / 2;
        maxHealth = 10;
        health = 10;
        mass = 10;

        hitBox = new Rectangle(12, 12, 40, 40);
        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
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
        boolean clicked = MouseHandler.getInstance().isPressed();

        selectedItem = InventoryHandler.getInstance().getItem(KeyHandler.getInstance().getLastNumberKeyPressed() - 1);

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
            accX = normalizedAccX * maxAcceleration;
            accY = normalizedAccY * maxAcceleration;

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

        bounds.x = (int) (posX + hitBox.x + velX);
        bounds.y = (int) (posY + hitBox.y + velY);

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
    public void execute() {
        // Executing the movement
        posX += velX;
        posY += velY;
    }

    @Override
    public void dropLoot() {
        // Player doesn't drop any loot
    }

    public void draw(Graphics2D g2) {
        try {
            BufferedImage scaledImage = Helper.scaleImage(ImageIO.read(getClass().getResourceAsStream("/enemy/player.png")), TILESIZE, TILESIZE);

            Helper.draw(g2, scaledImage, (int) posX, (int) posY);

            // TODO: maybe change this so that its not null but it just doesn't draw or deal damage when not attacking
            if (attackArea != null) {
                drawAttack(g2);
            }
        } catch (IOException e) {

        }
    }

    public void attack() {
        if (selectedItem instanceof Sword) {
            // TODO: add custom attack area depending on the weapon
            angle = Math.atan2(MouseHandler.getInstance().getY() - SCREEN_HEIGHT / 2, MouseHandler.getInstance().getX() - SCREEN_WIDTH / 2);
            attackArea = new AttackShape(posX + TILESIZE / 2, posY + TILESIZE / 2, TILESIZE * 2, TILESIZE, angle);
            attackCD = true;
        } else {
            // Set attackArea to null when not using a Sword
            attackArea = null;
            attackCD = true; // Depending on your logic
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
        BufferedImage attack = helper.scaleImage(currentWeapon.getImage(), currentWeapon.getAttackWidth(), currentWeapon.getAttackHeight());

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

    public Slot[] getInventory() {
        return InventoryHandler.getInstance().getInventory();
    }

    public void addCoin() {
        this.coin++;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public AttackShape getAttackArea() {
        return attackArea;
    }
}
