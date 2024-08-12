package model.Items;

import model.Helper;
import model.Inventory.Item;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class Arrow extends Item {
    private int maxNumPierce;
    private int damage;

    public Arrow() {
        this.image = Helper.setup("objects/arrow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 1;
        this.maxNumPierce = 4;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 4);
        this.isStackable = true;
    }

    public int getDamage() {
        return damage;
    }

    public int getMaxNumPierce() {
        return maxNumPierce;
    }
}
