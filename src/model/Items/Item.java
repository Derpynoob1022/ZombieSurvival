package model.Items;

import model.Collision.Collidable;
import model.Handler.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Item implements Collidable {
    protected float posX;
    protected float posY;
    protected BufferedImage image;
    protected String name;
    protected Rectangle hitBox;
    protected Rectangle bounds;

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

    public String getName() {
        return name;
    }
}
