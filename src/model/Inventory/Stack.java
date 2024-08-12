package model.Inventory;

import java.awt.image.BufferedImage;

// Represents a stack of an item which includes an item and the number of that item
public class Stack extends Inventoriable {
    private Item item;
    private int count;
    private static final int MAX_COUNT = 99;

    public Stack(Item item, int count) {
        this.item = item;
        this.count = Math.min(count, MAX_COUNT);
    }

    @Override
    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    // Adds items to the count, if it exceeds the max stack, then it would return the remainder of the amount trying to add
    public int addItems(int count) {
        int newCount = this.count + count;
        if (newCount > MAX_COUNT) {
            int leftovers = newCount - MAX_COUNT;
            this.count = MAX_COUNT;
            return leftovers;
        } else {
            this.count = newCount;
            return 0;
        }
    }

    // Return true if able to remove the items and then removes it, return false if don't have enough to remove
    public boolean removeItems(int count) {
        if (this.count > count) {
            this.count -= count;
            return true;
        } else {
            return false;
        }
    }

    public BufferedImage getImage() {
        return item.getImage();
    }
}

