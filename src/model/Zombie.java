package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.*;

public class Zombie extends Enemy {
    // private Helper helper = new Helper();

    public Zombie(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle(12, 24, 48, 48);
        acceleration = 0.2f;
        mass = 1;
        health = 5;
        maxHealth = 5;
    }

    public void draw (Graphics2D g2) {
        try {
            BufferedImage scaledImage = Helper.scaleImage(ImageIO.read(getClass().getResourceAsStream("/enemy/zombie/zombie0.png")), TILESIZE, TILESIZE);

            Helper.draw(g2, scaledImage, (int) posX, (int) posY);

            int posXHealth = (int) posX;
            int posYHealth = (int) posY - 10;
            int healthLeft = TILESIZE * health / maxHealth;

            Helper.draw(g2, Color.red, posXHealth, posYHealth, TILESIZE, 5);

            if (health > 0) {
                Helper.draw(g2, Color.green, posXHealth, posYHealth, healthLeft, 5);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropLoot() {
        int variantX = (int) (Math.random() * TILESIZE);
        int variantY = (int) (Math.random() * TILESIZE);
        DROPPED_ITEMS.add(new Coin(posX + variantX, posY + variantY));

        // between 1 and 60
        int chance = (int) Math.floor(Math.random() * 60) + 1;

        // 50% chance of dropping a sword
        if (chance > 30) {
            variantX = (int) (Math.random() * TILESIZE);
            variantY = (int) (Math.random() * TILESIZE);
            DROPPED_ITEMS.add(new GoldenSword(posX + variantX, posY + variantY));
        }
        // System.out.println("Dropped loot");
    }

    @Override
    public void update() {

        float distX = Player.getInstance().posX - posX;
        float distY = Player.getInstance().posY - posY;

        double distance = Math.sqrt(distX * distX + distY * distY);

        double accX = 0;
        double accY = 0;

        if (distance != 0) {
            // Calculate normalized acceleration components
            double normalizedAccX = distX / distance;
            double normalizedAccY = distY / distance;

            // Apply acceleration magnitude
            accX = normalizedAccX * acceleration;
            accY = normalizedAccY * acceleration;
        }

        // Calculate desired velocity after acceleration
        double newVelX = velX + accX;
        double newVelY = velY + accY;

        // Calculate current speed
        double currentSpeed = Math.sqrt(newVelX * newVelX + newVelY * newVelY);

        // Limit velocity to moveSpeed if it exceeds the maximum speed
        if (currentSpeed > moveSpeed) {
            double ratio = moveSpeed / currentSpeed;
            newVelX *= ratio;
            newVelY *= ratio;
        }

        // Apply the new velocity
        velX = (float) newVelX;
        velY = (float) newVelY;

        // Checking for collision in the next frame
        collision = false;
        CollisionChecker.getInstance().checkTile(this);
        int collisionIndex = CollisionChecker.getInstance().checkEntityCollision(this, ENTITIES);
        handlePlayerCollision(collisionIndex);

        // Executing the movement
        posX += velX;
        posY += velY;


        if (invincible) {
            iFrames++;
            if (iFrames > 60) {
                invincible = false;
                iFrames = 0;
            }
        }
    }

    public void handlePlayerCollision(int index) {
        if (index != -1) {
            if (ENTITIES.get(index) instanceof Player) {
                if (!Player.getInstance().invincible) {
                    Player.getInstance().health -= 1;
                    Player.getInstance().invincible = true;
                }
            }
        }
    }
}
