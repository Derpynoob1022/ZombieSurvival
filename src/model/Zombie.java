package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.*;

public class Zombie extends Enemy {
    public Zombie(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle();

        hitBox.x = 12;
        hitBox.y = 24;
        hitBox.width = 48;
        hitBox.height = 48;

        health = 5;
        maxHealth = 5;
    }

    public void draw(Graphics2D g2) {
        try {
            BufferedImage curImage = ImageIO.read(getClass().getResourceAsStream("/enemy/zombie/zombie0.png"));
            Player player = Player.getInstance();

            if (posX + 2 * TILESIZE > player.posX - player.screenX &&
                    posX - 2 * TILESIZE < player.posX + player.screenX &&
                    posY + 2 * TILESIZE > player.posY - player.screenY &&
                    posY - 2 * TILESIZE < player.posY + player.screenY) {

                int screenX = posX - player.posX + player.screenX;
                int screenY = posY - player.posY + player.screenY;

                int screenXHealth = screenX;
                int screenYHealth = screenY - 10;
                int healthLeft = TILESIZE * health / maxHealth;

                g2.setColor(Color.red);
                g2.fillRect(screenXHealth, screenYHealth, TILESIZE, 5);

                if (health > 0) {

                    g2.setColor(Color.GREEN);
                    g2.fillRect(screenXHealth, screenYHealth, healthLeft, 5);
                }

                g2.drawImage(Helper.scaleImage(curImage, TILESIZE, TILESIZE), screenX, screenY, null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

        int distX = Player.getInstance().posX - posX;
        int distY = Player.getInstance().posY - posY;

        double distance = Math.sqrt(distX * distX + distY * distY);

        if (Math.round(distance) != 0) {
            // Calculate velocities as integers
            velX = (int) Math.round(distX / distance * moveSpeed);
            velY = (int) Math.round(distY / distance * moveSpeed);


            collision = false;

            int collisionIndex = CollisionChecker.getInstance().checkEntityCollision(this, ENTITIES);
            handlePlayerCollision(collisionIndex);
//            CollisionChecker.getInstance().CheckTile(this);

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
