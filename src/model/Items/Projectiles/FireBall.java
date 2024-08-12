package model.Items.Projectiles;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class FireBall extends Projectiles {
    private int explosionRadius;
    private int duration;

    public FireBall(float x, float y, double angle, int projSpeed, int damage, int explosionRadius, int duration, boolean firedByPlayer) {
        image = Helper.setup("objects/fireball", TILESIZE / 2, TILESIZE / 2);
        this.damage = 2 + damage;
        this.posX = x;
        this.posY = y;
        this.angle = angle;
        this.velX = (float) (projSpeed * Math.cos(angle));
        this.velY = (float) (projSpeed * Math.sin(angle));
        this.explosionRadius = explosionRadius;
        this.duration = duration;
        this.firedByPlayer = firedByPlayer;

        this.hitBox = new Rectangle(0, 0, TILESIZE / 2, TILESIZE / 2);
        this.bounds = new Rectangle((int) posX, (int) posY, hitBox.width, hitBox.height);
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public int getDuration() {
        return duration;
    }
}
