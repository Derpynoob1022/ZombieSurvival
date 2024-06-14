package model;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class GoldenSword extends Sword {
    Helper helper = new Helper();

    // TODO: Add constructor overloading so that the item could be a dropped item or a picked up item
    // When dropped on the ground
    public GoldenSword(int x, int y) {
        image = helper.setup("objects/golden_sword", TILESIZE / 4, TILESIZE / 2);
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle(x, y, TILESIZE / 4, TILESIZE / 2);
        this.damage = 3;
        this.attackWidth = TILESIZE;
        this.attackHeight = 2 * TILESIZE;
    }

    // In inventory
    public GoldenSword() {
        image = helper.setup("objects/golden_sword", TILESIZE / 4, TILESIZE / 2);
        this.damage = 3;
        this.attackWidth = TILESIZE;
        this.attackHeight = 2 * TILESIZE;
    }
}
