package model.Entities;

import model.Handler.Helper;
import model.Items.Coin;
import model.Items.Melee.AnimeSword;
import model.Items.Melee.GoldenSword;
import model.Items.Ranged.WoodenBow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

import static ui.GamePanel.TILESIZE;
import static ui.GamePanel.getDroppedItems;

public class Zombie extends Enemy {
    // private Helper helper = new Helper();

    public Zombie(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle(12, 12, 40, 40);
        maxAcceleration = 0.2f;
        mass = 1;
        maxHealth = 10;
        health = maxHealth;

        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
        try {
            scaledImage = Helper.scaleImage(ImageIO.read(getClass().getResourceAsStream("/enemy/zombie/zombie0.png")), TILESIZE, TILESIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        Helper.draw(g2, scaledImage, (int) posX, (int) posY);

        int posXHealth = (int) posX;
        int posYHealth = (int) posY - 10;
        int healthLeft = TILESIZE * health / maxHealth;

        Helper.draw(g2, Color.red, posXHealth, posYHealth, TILESIZE, 5);

        if (health > 0) {
            Helper.draw(g2, Color.green, posXHealth, posYHealth, healthLeft, 5);
        }
    }

    @Override
    public void dropLoot() {
        for (int i = 0; i < 3; i++) {
            int variantX = (int) (Math.random() * TILESIZE);
            int variantY = (int) (Math.random() * TILESIZE);
            getDroppedItems().add(new Coin(posX + variantX, posY + variantY));
        }

        // between 1 and 60
        int chance = (int) Math.floor(Math.random() * 100) + 1;

        // 30% chance of dropping a sword
        if (chance < 30) {
            int variantX = (int) (Math.random() * TILESIZE);
            int variantY = (int) (Math.random() * TILESIZE);
            getDroppedItems().add(new GoldenSword(posX + variantX, posY + variantY));
        }

        chance = (int) Math.floor(Math.random() * 100) + 1;

        if (chance < 5) {
            int variantX = (int) (Math.random() * TILESIZE);
            int variantY = (int) (Math.random() * TILESIZE);
            getDroppedItems().add(new AnimeSword(posX + variantX, posY + variantY));
        }

        chance = (int) Math.floor(Math.random() * 100) + 1;

        if (chance < 10) {
            int variantX = (int) (Math.random() * TILESIZE);
            int variantY = (int) (Math.random() * TILESIZE);
            getDroppedItems().add(new WoodenBow(posX + variantX, posY + variantY));
        }

        chance = (int) Math.floor(Math.random() * 100) + 1;

        if (chance < 5) {
            int variantX = (int) (Math.random() * TILESIZE);
            int variantY = (int) (Math.random() * TILESIZE);
            getDroppedItems().add(new WoodenBow(posX + variantX, posY + variantY));
        }
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
            accX = normalizedAccX * maxAcceleration;
            accY = normalizedAccY * maxAcceleration;
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

        bounds.x = (int) (posX + hitBox.x + velX);
        bounds.y = (int) (posY + hitBox.y + velY);


        if (invincible) {
            iFrames++;
            if (iFrames > 10) {
                invincible = false;
                iFrames = 0;
            }
        }
    }

    @Override
    public void execute() {
        // Executing the movement
        posX += velX;
        posY += velY;
    }
}
