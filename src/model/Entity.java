package model;

import java.awt.*;

public abstract class Entity {
    protected float posX;
    protected float posY;
    protected float velX;
    protected int mass;
    protected float velY;
    protected int moveSpeed;
    protected float acceleration;
    protected boolean collision;
    protected Rectangle hitBox;
    protected int health;
    protected boolean invincible;
    protected int iFrames;
    protected int maxHealth;
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

    public float getAcceleration() {
        return acceleration;
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

    public Rectangle getHitbox() {
        return hitBox;
    }

    public abstract void dropLoot();
}
