package model;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Item {
    protected int posX;
    protected int posY;
    protected BufferedImage image;
    protected Rectangle hitBox;
    protected String name;

    public void draw(Graphics2D g2) {
        Helper.draw(g2, image, posX, posY);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public String getName() {
        return name;
    }
}
