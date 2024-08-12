package model.Entities;

import model.Handler.KeyHandler;
import model.Handler.MouseHandler;
import model.Handler.StateHandler.ControlHandler;
import model.Helper;
import model.Inventory.Inventoriable;
import model.Inventory.InventoryHandler;
import model.Inventory.Slot;
import model.Items.Weapons.Melee.Sword;
import model.Items.Weapons.Ranged.RangedWeapon;
import model.Items.Weapons.Weapon;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static ui.GamePanel.*;

public class Player extends Entity {
    private final int screenX;
    private final int screenY;
    private static Player player = new Player();
    private AttackShape attackArea;
    private double angle;
    private int maxAttackDuration = 3;
    private int curAttackDuration = 0;
    private int coin;
    private Inventoriable selectedItem;
    private BufferedImage scaledImage;
    // TODO: add a bigger range to pickup
    private Rectangle pickupBox;
    private int xp;

    private Player() {
        scaledImage = Helper.setup("entity/player", TILESIZE, TILESIZE);
        posX = TILESIZE;
        posY = TILESIZE;
        moveSpeed = 4;
        maxAcceleration = 1;
        hitBox = new Rectangle(12, 12, 40, 40);
        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
        screenX = SCREEN_WIDTH / 2 - scaledImage.getWidth() / 2;
        screenY = SCREEN_HEIGHT / 2 - scaledImage.getHeight() / 2;
        maxHealth = 100;
        health = 100;
        mass = 10;
        coin = 0;
        selectedItem = InventoryHandler.getInstance().getItem(KeyHandler.getInstance().getLastNumberKeyPressed());
    }

    public static Player getInstance() {
        return player;
    }

    @Override
    public void update() {
        boolean keyPressedW = KeyHandler.getInstance().isKeyPressed(ControlHandler.getInstance().getMoveUpKeycode());
        boolean keyPressedA = KeyHandler.getInstance().isKeyPressed(ControlHandler.getInstance().getMoveLeftKeycode());
        boolean keyPressedS = KeyHandler.getInstance().isKeyPressed(ControlHandler.getInstance().getMoveDownKeycode());
        boolean keyPressedD = KeyHandler.getInstance().isKeyPressed(ControlHandler.getInstance().getMoveRightKeycode());
        boolean leftPressed = MouseHandler.getInstance().isLeftPressed();

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

        // If attack animation exceeds the max, it's going to set the attackArea to null
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

        // attacking if character is holding a weapon
        if (selectedItem instanceof Weapon) {
            Weapon curWeapon = ((Weapon) selectedItem);
            if (curWeapon.getCurrentAttackCount() > curWeapon.getMaxAttackCooldownCount()) {
                if (leftPressed) {
                    attack();
                    curWeapon.resetCurrentAttackCount();
                }
            }
        }
        addWeaponCoolDownCount();
    }

    public void addWeaponCoolDownCount() {
        for (Slot slot : getInventory()) {
            if (slot != null && slot.getItem() instanceof Weapon) {
                ((Weapon) slot.getItem()).addCurrentAttackCount();
            }
        }
    }

    public void addXp(int i) {
        xp += i;
    }

    public int getXp() {
        return xp;
    }

    public void draw(Graphics2D g2) {
        Helper.draw(g2, scaledImage, (int) posX, (int) posY);

        // TODO: maybe change this so that its not null but it just doesn't draw or deal damage when not attacking
        if (attackArea != null) {
            drawAttack(g2);
        }
    }

    public void attack() {
        float centerX = posX + hitBox.x + hitBox.width / 2;
        float centerY = posY + hitBox.y + hitBox.height / 2;
        angle = Math.atan2(MouseHandler.getInstance().getY() - SCREEN_HEIGHT / 2, MouseHandler.getInstance().getX() - SCREEN_WIDTH / 2);
        if (selectedItem instanceof Sword) {
            // TODO: add custom attack area depending on the weapon
            attackArea = new AttackShape(posX + TILESIZE / 2, posY + TILESIZE / 2, TILESIZE * 2, TILESIZE, angle);
        } else if (selectedItem instanceof RangedWeapon) {
            ((RangedWeapon) selectedItem).shoot(centerX, centerY, angle);
        }
    }


    public void drawAttack(Graphics2D g2) {
        if (selectedItem instanceof Sword) {
            Sword currentWeapon = (Sword) selectedItem;
            BufferedImage attack = Helper.scaleImage(currentWeapon.getImage(), currentWeapon.getAttackWidth(), currentWeapon.getAttackHeight());

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
    }

    public int getLevel() {
        return (int) Math.floor(Math.sqrt(xp/25 + 12.25) - 3.5);
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

    public void addCoin(int amount) {
        this.coin += amount;
    }

    public Inventoriable getSelectedItem() {
        return selectedItem;
    }

    public AttackShape getAttackArea() {
        return attackArea;
    }

    public int getCoin() {
        return coin;
    }

    public void addHealth(int amount) {
        health += amount;
    }

    public void reset() {
        InventoryHandler.getInstance().reset();
        posX = 0;
        posY = 0;
        moveSpeed = 4;
        maxAcceleration = 1;
        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
        maxHealth = 10;
        health = 10;
        mass = 10;
        coin = 0;
    }
}
