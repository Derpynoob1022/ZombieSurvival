package model.Inventory;

import java.awt.*;
import java.awt.image.BufferedImage;

// Represents an item in the game that could be picked up
public abstract class Inventoriable {
    public Rectangle hitbox;
    public abstract BufferedImage getImage();

    public abstract Item getItem();
}
