package model;

import java.awt.*;
import java.awt.event.KeyEvent;

import static ui.GamePanel.*;

public class Player extends Entity {
    public final int screenX;
    public final int screenY;
    private static Player player = new Player();

    private Player() {
        posX = 0;
        posY = TILESIZE;
        moveSpeed = 6;
        screenX = SCREEN_WIDTH/2 - TILESIZE/2;
        screenY = SCREEN_HEIGHT/2 - TILESIZE/2;
        health = 5;

        hitBox = new Rectangle();
//        hitBox.x = 8;
//        hitBox.y = 16;
//        hitBox.width = 32;
//        hitBox.height = 32;


        hitBox.x = 0;
        hitBox.y = 0;
        hitBox.width = TILESIZE;
        hitBox.height = TILESIZE;
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

        collideLeft = false;
        collideRight = false;
        collideUp = false;
        collideDown = false;

        // Check if player hits an entity
        int indexEntityCollision = CollisionChecker.getInstance().checkEntityCollision(this, MONSTERS);
        handleMonsterCollision(indexEntityCollision);
        // Check if player runs into a wall
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

//        System.out.println(collideUp);
//        System.out.println(collideDown);
//        System.out.println(collideLeft);
//        System.out.println(collideRight);

            System.out.println(this.hashCode() + " " + velX + " " + velY + " " + collideUp + " " + collideDown + " "
             + collideLeft + " " + collideRight);

    }


    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(screenX, screenY, TILESIZE, TILESIZE);

        g2.setColor(Color.RED);
        g2.fillRect(screenX + hitBox.x, screenY + hitBox.y, hitBox.width, hitBox.height);
    }

    public void handleMonsterCollision(int index) {
        if (index != -1) {
            health -= 1;
            // System.out.println("Health: " + health);
        }
    }
}
