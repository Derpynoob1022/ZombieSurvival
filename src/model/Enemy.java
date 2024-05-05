package model;

import java.awt.*;

public abstract class Enemy {
    protected float posX;
    protected float posY;
    protected float moveSpeed;

    public abstract void draw(Graphics2D g2);
    public abstract void update();
}
