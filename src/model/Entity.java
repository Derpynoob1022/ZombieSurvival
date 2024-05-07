package model;

import java.awt.*;

public abstract class Entity {
    protected int posX;
    protected int posY;
    protected int velX;
    protected int velY;
    protected int moveSpeed;
    public boolean collide;
    protected Rectangle hitBox;

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
}
