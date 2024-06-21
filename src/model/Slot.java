package model;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class Slot {
    private Rectangle hitbox;
    private Item item;

    public Slot(int x, int y) {
        hitbox = new Rectangle(x, y, TILESIZE, TILESIZE);
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
