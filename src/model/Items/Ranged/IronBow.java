package model.Items.Ranged;

import model.Handler.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class IronBow extends Bow {
    // Item on the ground
    public IronBow(float x, float y) {
        // TODO: add the spirte for the ironbow
        image = Helper.setup("objects/wooden_bow", TILESIZE / 4, TILESIZE / 2);
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle((int) x, (int) y, TILESIZE / 4, TILESIZE / 2);
        this.damage = 2;
        this.attackCoolDown = 200;
        projSpeed = 10;
        this.bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
    }

    // In inventory
    public IronBow() {
        image = Helper.setup("objects/wooden_bow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 2;
        this.attackCoolDown = 70;
        projSpeed = 5;
    }
}
