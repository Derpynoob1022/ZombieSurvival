package model.Items;

import model.Collision.Collidable;
import model.Helper;
import model.Inventory.Inventoriable;
import model.Inventory.Item;
import model.Inventory.Stack;

import java.awt.*;

// Represents a dropped Item and its count
public class DroppedItem implements Collidable {
    private Rectangle bounds;
    private Item item;
    private int count;
    private int groundCount = 0;
    private int pickUpCount = 0;
    private int maxPickUpCount = 180;

    public DroppedItem(Inventoriable inventoriable, float posX, float posY) {
        this.item = inventoriable.getItem();
        bounds = new Rectangle((int) posX + inventoriable.getItem().getHitbox().x, (int) posY + inventoriable.getItem().getHitbox().y, inventoriable.getItem().getHitbox().width, inventoriable.getItem().getHitbox().height);
        if (inventoriable instanceof Stack) {
            this.count = ((Stack) inventoriable).getCount();
        } else {
            count = 1;
        }
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Graphics2D g2) {
        Helper.draw(g2, item.getImage(), bounds.x, bounds.y);
    }

    public int getPickUpCount() {
        return pickUpCount;
    }

    public int getMaxPickUpCount() {
        return maxPickUpCount;
    }

    public void addGroundCount() {
        groundCount++;
    }

    public int getGroundCount() {
        return groundCount;
    }

    public void  addPickUpCount() {
        this.pickUpCount++;
    }
}
