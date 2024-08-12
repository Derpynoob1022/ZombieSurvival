package model.Items.Projectiles;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class FlyingArrow extends Projectiles {

    // Arrow after leaving the bow
    public FlyingArrow(float x, float y, double angle, int projSpeed, int damage, int maxNumPierce, boolean firedByPlayer) {
        image = Helper.setup("objects/arrow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 2 + damage;
        this.posX = x;
        this.posY = y;
        this.angle = angle;
        this.firedByPlayer = firedByPlayer;
        this.curNumPierce = 0;
        this.maxNumPierce = maxNumPierce;
        this.velX = (float) (projSpeed * Math.cos(angle));
        this.velY = (float) (projSpeed * Math.sin(angle));

        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 4);
        this.bounds = new Rectangle((int) posX, (int) posY, hitBox.width, hitBox.height);
    }
}
