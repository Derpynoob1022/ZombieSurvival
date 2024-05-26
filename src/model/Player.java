package model;

import java.awt.*;
import java.awt.event.KeyEvent;

import static ui.GamePanel.*;

public class Player extends Entity {
    public final int screenX;
    public final int screenY;
    private static Player player = new Player();
    public Shape attackArea = new Rectangle(0, 0, 0, 0);
    public boolean attackCD;
    public int attackCDCount;

    private Player() {
        posX = 0;
        posY = TILESIZE;
        moveSpeed = 6;
        screenX = SCREEN_WIDTH/2 - TILESIZE/2;
        screenY = SCREEN_HEIGHT/2 - TILESIZE/2;
        maxHealth = 100;
        health = 100;

        hitBox = new Rectangle();
        hitBox.x = 12;
        hitBox.y = 12;
        hitBox.width = 48;
        hitBox.height = 48;
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
        boolean clicked = MouseHandler.getInstance().pressed;

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

        collision = false;
        CollisionChecker.getInstance().checkEntityCollision(this, ENTITIES);

        posX += velX;
        posY += velY;

        if (invincible == true) {
            iFrames++;
            if (iFrames > 60) {
                invincible = false;
                iFrames = 0;
            }
        }

        if (attackCD) {
            attackArea = null;
        } else if (clicked) {
            attack();
        }


        if (attackCD == true) {
            attackCDCount++;
            if (attackCDCount > 60) {
                attackCD = false;
                attackCDCount = 0;
            }
        }
    }


    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(screenX, screenY, TILESIZE, TILESIZE);

        g2.setColor(Color.RED);
        g2.fillRect(screenX + hitBox.x, screenY + hitBox.y, hitBox.width, hitBox.height);

        g2.setColor(Color.BLUE);
        g2.draw(attackArea);
    }

    public void attack() {
        double angle = Math.atan2(MouseHandler.getInstance().y - SCREEN_HEIGHT / 2, MouseHandler.getInstance().x - SCREEN_WIDTH / 2);
        attackArea = new AttackShape(posX + TILESIZE / 2, posY + TILESIZE / 2, TILESIZE * 2, TILESIZE, angle);

        for (int i : CollisionChecker.getInstance().checkAttackCollision(attackArea, ENTITIES)) {
            Entity curEntity = ENTITIES.get(i);
            if (!curEntity.invincible) {
                curEntity.hit();
                curEntity.invincible = true;
            }
        }
    }
}
