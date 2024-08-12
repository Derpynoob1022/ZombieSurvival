package model.Inventory;

import java.awt.*;
import java.awt.image.BufferedImage;

// Represents an item
public abstract class Item extends Inventoriable {
    protected BufferedImage image;
    protected String name; // Will add the name later
    protected Rectangle hitBox;
    protected boolean isStackable;

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getHitbox() {
        return hitBox;
    }

    public boolean getIsStackable() {
        return isStackable;
    }

    @Override
    public Item getItem() {
        return this;
    }

    // Override the equals, so we are comparing the type of each object rather than the objects themselves
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
