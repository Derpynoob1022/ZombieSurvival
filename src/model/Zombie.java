package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.TILESIZE;

public class Zombie extends Enemy {
    public Zombie() {
        posX = 200;
        posY = 200;
        moveSpeed = 2;

        hitBox = new Rectangle();

        hitBox.x = 0;
        hitBox.y = 0;
        hitBox.width = TILESIZE;
        hitBox.height = TILESIZE;
    }

    public void draw(Graphics2D g2) {
        try {
            BufferedImage curImage = ImageIO.read(getClass().getResourceAsStream("/enemy/zombie/zombie0.png"));
            Helper.draw(g2, Helper.scaleImage(curImage, TILESIZE, TILESIZE), (int) posX, (int) posY);
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

            collide = false;
            CollisionChecker.getInstance().CheckTile(this);
            if (!collide) {
                // Update positions
                posX += velX;
                posY += velY;
            }
        }
    }

}
