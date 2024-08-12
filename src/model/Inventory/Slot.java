package model.Inventory;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

// Represents a slot in the inventory with a hitbox and either 1 or no inventoriable item inside.
public class Slot {
    private Rectangle hitbox;
    private Inventoriable item;
    private boolean isTrash;

    public Slot(int x, int y, boolean isTrash) {
        hitbox = new Rectangle(x, y, TILESIZE, TILESIZE);
        this.isTrash = isTrash;
    }

    public void setItem(Inventoriable newItem) {
        item = newItem;
    }

    public void removeItem() {
        item = null;
    }


    public Inventoriable getItem() {
        return item;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isTrash() {
        return isTrash;
    }

    public boolean isEmpty() {
        return item == null;
    }
}
