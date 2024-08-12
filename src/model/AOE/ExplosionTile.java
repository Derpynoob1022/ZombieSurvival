package model.AOE;

import model.Collision.Collidable;
import model.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ui.GamePanel.TILESIZE;

// Represents a one single unit square of explosion
public class ExplosionTile implements Collidable {
    private Rectangle bounds;
    private BufferedImage image;

    public ExplosionTile(int x, int y, int size) {
        this.bounds = new Rectangle(x, y, size, size);
        this.image = Helper.setup("objects/explosion", TILESIZE, TILESIZE);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Graphics2D g2) {
        Helper.draw(g2, image, bounds.x, bounds.y);
    }
}
