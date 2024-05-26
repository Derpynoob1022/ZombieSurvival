package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Entity {
    protected int posX;
    protected int posY;
    protected int velX;
    protected int velY;
    protected int moveSpeed;
    protected boolean collision;
    protected Rectangle hitBox;
    protected int health;
    protected boolean invincible;
    protected int iFrames;
    protected int maxHealth;
    public abstract void draw(Graphics2D g2);

    public abstract void update();

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = Helper.scaleImage(image, width, height);

        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void hit() {
        health -= 1;
    }
}
