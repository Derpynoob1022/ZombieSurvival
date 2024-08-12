package model.Entities;

import model.Collision.Collidable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity implements Collidable {
    protected float posX;
    protected float posY;
    protected float velX;
    protected int mass;
    protected float velY;
    protected int moveSpeed;
    protected float maxAcceleration;
    protected Rectangle hitBox;
    protected Rectangle bounds;
    protected int health;
    protected boolean invincible;
    protected int iFrames;
    protected int maxHealth;
    protected BufferedImage scaledImage;
    protected int xp;
    public abstract void draw(Graphics2D g2);
    public abstract void update();

    public void execute() {
        // Executing the movement
        posX += velX;
        posY += velY;

        bounds.x = (int) posX + hitBox.x;
        bounds.y = (int) posY + hitBox.y;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void hit(int i) {
        health -= i;
    }

    public Rectangle getHitbox() {
        return hitBox;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getMass() {
        return mass;
    }

    public int getXpDrop() {
        return xp;
    }
}
