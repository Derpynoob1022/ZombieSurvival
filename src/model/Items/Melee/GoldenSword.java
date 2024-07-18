package model.Items.Melee;

import model.Handler.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class GoldenSword extends Sword {
    // TODO: Add constructor overloading so that the item could be a dropped item or a picked up item
    // When dropped on the ground
    public GoldenSword(float x, float y) {
        image = Helper.setup("objects/golden_sword", TILESIZE / 4, TILESIZE / 2);
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle((int) x, (int) y, TILESIZE / 4, TILESIZE / 2);
        this.damage = 1;
        this.attackWidth = TILESIZE;
        this.attackHeight = 2 * TILESIZE;
        this.attackCoolDown = 20;
        this.bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
    }

    // In inventory
    public GoldenSword() {
        image = Helper.setup("objects/golden_sword", TILESIZE / 4, TILESIZE / 2);
        this.damage = 1;
        this.attackWidth = TILESIZE;
        this.attackHeight = 2 * TILESIZE;
        this.attackCoolDown = 20;
    }
}
