package model.Items.Ranged;

import model.Handler.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class WoodenBow extends Bow {
    // Item on the ground
    public WoodenBow(float x, float y) {
        image = Helper.setup("objects/wooden_bow", TILESIZE / 4, TILESIZE / 2);
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle((int) x, (int) y, TILESIZE / 4, TILESIZE / 2);
        this.damage = 2;
        this.attackCoolDown = 70;
        projSpeed = 5;
        this.bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
    }

    // In inventory
    public WoodenBow() {
        image = Helper.setup("objects/wooden_bow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 2;
        this.attackCoolDown = 70;
        projSpeed = 5;
    }
}
