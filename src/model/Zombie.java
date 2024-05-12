package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.MONSTERS;
import static ui.GamePanel.TILESIZE;

public class Zombie extends Enemy {
    public Zombie(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle();

        hitBox.x = 0;
        hitBox.y = 0;
        hitBox.width = TILESIZE;
        hitBox.height = TILESIZE;
    }

    public void draw(Graphics2D g2) {
        try {
            BufferedImage curImage = ImageIO.read(getClass().getResourceAsStream("/enemy/zombie/zombie0.png"));
            Helper.draw(g2, Helper.scaleImage(curImage, TILESIZE, TILESIZE), posX, posY);
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


            collideLeft = false;
            collideRight = false;
            collideUp = false;
            collideDown = false;

            CollisionChecker.getInstance().checkEntityCollision(this, MONSTERS);
            CollisionChecker.getInstance().checkPlayerCollision(this);
            // CollisionChecker.getInstance().CheckTile(this);

            if (!collideLeft && !collideRight) {
                // No horizontal collisions detected, move by velX
                posX += velX;
            } else if (collideLeft && velX > 0) {
                // Colliding on the left, but moving right
                posX += velX;
            } else if (collideRight && velX < 0) {
                // Colliding on the right, but moving left
                posX += velX;
            }

            if (!collideUp && !collideDown) {
                // No vertical collisions detected, move by velY
                posY += velY;
            } else if (collideUp && velY > 0) {
                // Colliding on the top, but moving down
                posY += velY;
            } else if (collideDown && velY < 0) {
                // Colliding on the bottom, but moving up
                posY += velY;
            }

//             System.out.println(this.hashCode() + " " + velX + " " + velY + " " + collideUp + " " + collideDown + " "
//             + collideLeft + " " + collideRight);
        }
    }
}
