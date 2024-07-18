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
    public abstract void draw(Graphics2D g2);

    public abstract void update();

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

    public float getMaxAcceleration() {
        return maxAcceleration;
    }

    public int getMoveSpeed() {
        return moveSpeed;
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

    public void setMass(int mass) {
        this.mass = mass;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setMaxAcceleration(float maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
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

    public void setiFrames(int iFrames) {
        this.iFrames = iFrames;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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

    public int getiFrames() {
        return iFrames;
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
    public abstract void execute();

    public abstract void dropLoot();

    public int getMass() {
        return mass;
    }
}
