package model;

import ui.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public float posX;
    public float posY;
    private float moveSpeed;
    public final int screenX;
    public final int screenY;
    private static Player player;
    private GamePanel gp;

    private Player(GamePanel gp) {
        this.gp = gp;
        posX = 0;
        posY = 0;
        moveSpeed = 2.5F;
        screenX = gp.SCREEN_WIDTH/2;
        screenY = gp.SCREEN_HEIGHT/2 - gp.TILESIZE/2;
    }

    public static Player getInstance() {
        return player;
    }

    public static void initialize(GamePanel gp) {
        if (player == null) {
            player = new Player(gp);
        }
    }

    public void update() {
        boolean keyPressedW = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_W);
        boolean keyPressedA = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_A);
        boolean keyPressedS = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_S);
        boolean keyPressedD = KeyHandler.getInstance().isKeyPressed(KeyEvent.VK_D);

        int moveX = 0;
        int moveY = 0;

        if (keyPressedW) {
            moveY -= moveSpeed;
        }
        if (keyPressedA) {
            moveX -= moveSpeed;
        }
        if (keyPressedS) {
            moveY += moveSpeed;
        }
        if (keyPressedD) {
            moveX += moveSpeed;
        }

        double magnitude = Math.sqrt(moveX * moveX + moveY * moveY);
        if (magnitude != 0) {
            moveX = (int) Math.round(moveX / magnitude * moveSpeed);
            moveY = (int) Math.round(moveY / magnitude * moveSpeed);
        }

        posX += moveX;
        posY += moveY;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(screenX, screenY, 20, 20);
    }
}
