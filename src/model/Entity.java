package model;

import java.awt.*;

public abstract class Entity {
    protected int posX;
    protected int posY;
    protected int velX;
    protected int velY;
    protected int moveSpeed;
    protected boolean collision;
    protected Rectangle hitBox;
    protected Rectangle nextHitBox;
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

    public void hit(int i) {
        health -= i;
    }

    public boolean isCollision() {
        return collision;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Rectangle getNextHitBox() {
        return nextHitBox;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public int getiFrames() {
        return iFrames;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public void setNextHitBox(Rectangle nextHitBox) {
        this.nextHitBox = nextHitBox;
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

    public abstract void dropLoot();
}
