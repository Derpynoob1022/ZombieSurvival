package model.Items.Projectiles;

import model.Collision.Collidable;
import model.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Projectiles implements Collidable {
    protected BufferedImage image;
    protected float velX;
    protected float velY;
    protected float posX;
    protected float posY;
    protected Rectangle hitBox;
    protected Rectangle bounds;
    protected int damage;
    protected int maxNumPierce;
    protected int curNumPierce;
    protected double angle;
    protected boolean firedByPlayer;

    public void draw(Graphics2D g2) {
        Helper.draw(g2, image, posX, posY, angle);
    }

    public void execute() {
        posX += velX;
        posY += velY;

        bounds.x = (int) posX + hitBox.x;
        bounds.y = (int) posY + hitBox.y;
    }

    public int getMaxNumPierce() {
        return maxNumPierce;
    }

    public int getCurNumPierce() {
        return curNumPierce;
    }

    public void addCurNumPierce() {
        this.curNumPierce++;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean getFiredByPlayer() {
        return firedByPlayer;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }
}
