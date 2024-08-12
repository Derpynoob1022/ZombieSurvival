package model.AOE;

import model.Collision.Collidable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ui.GamePanel.TILESIZE;

// Represents a single explosion
public class Explosion implements Collidable {
    private List<ExplosionTile> explosionTiles;
    private int damage;
    private boolean firedByPlayer;
    private int count;
    private int duration;

    public Explosion(float centerX, float centerY, int damage, int explosionRadius, int duration, boolean firedByPlayer) {
        this.damage = damage;
        explosionTiles = new ArrayList<>();
        createExplosionTiles(centerX, centerY, explosionRadius);
        this.firedByPlayer = firedByPlayer;
        this.duration = duration;
    }

    private void createExplosionTiles(float centerX, float centerY, int radius) {

        int startX = (int) (centerX - radius);
        int startY = (int) (centerY - radius);
        int endX = (int) (centerX + radius);
        int endY = (int) (centerY + radius);

        // makes a bunch of explosionTiles and orient them into the shape of a circle with explosionRadius as the radius
        for (int x = startX; x <= endX; x += TILESIZE) {
            for (int y = startY; y <= endY; y += TILESIZE) {
                if (Math.hypot(centerX - x + TILESIZE / 2, centerY - y + TILESIZE / 2) <= radius) {
                    explosionTiles.add(new ExplosionTile(x, y, TILESIZE));
                }
            }
        }
    }

    // Returns the entire explosion area
    @Override
    public Rectangle getBounds() {
        // Return the bounding rectangle of the entire explosion area
        if (explosionTiles.isEmpty()) return new Rectangle();
        Rectangle bounds = explosionTiles.get(0).getBounds();
        for (ExplosionTile tile : explosionTiles) {
            bounds = bounds.union(tile.getBounds());
        }
        return bounds;
    }

    public void draw(Graphics2D g2) {
        for (ExplosionTile e : explosionTiles) {
            e.draw(g2);
        }
    }

    // If returns true, then the explosion gets deleted
    public boolean update() {
        count++;
        if (count > duration) {
            return true;
        }
        return false;
    }

    public int getDamage() {
        return damage;
    }

    public boolean getFiredByPlayer() {
        return firedByPlayer;
    }
}
