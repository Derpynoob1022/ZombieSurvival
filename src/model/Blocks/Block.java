package model.Blocks;

import model.Collision.Collidable;
import model.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

// Represents a placeable block in the game
// TODO: Add a way for the player to place blocks
public abstract class Block implements Collidable {
    protected int health;
    protected int posX;
    protected int posY;
    protected BufferedImage image;
    protected boolean collision;
    protected Rectangle bounds;

    public void draw(Graphics2D g2) {
        Helper.draw(g2, image, posX, posY);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public void reduceHealth(int i) {
        health -= i;
    }

    public int getHealth() {
        return health;
    }
}
