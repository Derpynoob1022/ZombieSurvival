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
        posY = 0;
        moveSpeed = 4;
        screenX = SCREEN_WIDTH/2 - TILESIZE/2;
        screenY = SCREEN_HEIGHT/2 - TILESIZE/2;

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

        collide = false;
        CollisionChecker.getInstance().CheckTile(this);
        if (!collide) {
            posX += velX;
            posY += velY;
        }
    }



    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(screenX, screenY, TILESIZE, TILESIZE);

        g2.setColor(Color.RED);
        g2.fillRect(screenX + hitBox.x, screenY + hitBox.y, hitBox.width, hitBox.height);
    }
}
