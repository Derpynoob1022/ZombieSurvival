package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.*;

public class Zombie extends Enemy {
    private Helper helper = new Helper();

    public Zombie(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle(12, 24, 48, 48);

        health = 5;
        maxHealth = 5;
    }

    public void draw (Graphics2D g2) {
        try {
            BufferedImage scaledImage = Helper.scaleImage(ImageIO.read(getClass().getResourceAsStream("/enemy/zombie/zombie0.png")), TILESIZE, TILESIZE);

            Helper.draw(g2, scaledImage, posX, posY);

            int posXHealth = posX;
            int posYHealth = posY - 10;
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
            System.out.println("goldgoldgold");
        }
        // System.out.println("Dropped loot");
    }

    @Override
    public void update() {

        int distX = Player.getInstance().posX - posX;
        int distY = Player.getInstance().posY - posY;

        double distance = Math.sqrt(distX * distX + distY * distY);

        velX = (int) Math.round(distX / distance * moveSpeed);
        velY = (int) Math.round(distY / distance * moveSpeed);


        collision = false;
        CollisionChecker.getInstance().CheckTile(this);
        int collisionIndex = CollisionChecker.getInstance().checkEntityCollision(this, ENTITIES);
        handlePlayerCollision(collisionIndex);

        posX += velX;
        posY += velY;

        if (invincible == true) {
            iFrames++;
            if (iFrames > 60) {
                invincible = false;
                iFrames = 0;
            }
        }
//             System.out.println(this.hashCode() + " " + velX + " " + velY + " " + collideUp + " " + collideDown + " "
//             + collideLeft + " " + collideRight);
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
