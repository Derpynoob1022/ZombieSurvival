package model.Items.Ranged;

import model.Items.Item;

public abstract class Projectiles extends Item {
    protected float velX;
    protected float velY;
    protected int moveSpeed;
    protected int damage;
    protected int maxNumPierce;
    protected int curNumPierce;

    public int getMaxNumPierce() {
        return maxNumPierce;
    }

    public int getCurNumPierce() {
        return curNumPierce;
    }

    public void addCurNumPierce() {
        this.curNumPierce++;
    }

    public abstract void execute();

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
