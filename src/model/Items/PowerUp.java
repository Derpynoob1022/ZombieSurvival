package model.Items;

import model.Collision.Collidable;
import model.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp implements Collidable {
    protected float posX;
    protected float posY;
    protected BufferedImage image;
    protected Rectangle hitBox;
    protected Rectangle bounds;
    protected int groundCount;

    public void draw(Graphics2D g2) {
        Helper.draw(g2, image, posX, posY);
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getHitbox() {
        return hitBox;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void addGroundCount() {
        groundCount++;
    }

    public int getGroundCount() {
        return groundCount;
    }
}
