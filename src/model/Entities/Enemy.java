package model.Entities;

public abstract class Enemy extends Entity {
    public void handlePlayerCollision() {
        if (!Player.getInstance().invincible) {
            Player.getInstance().health -= 1;
            Player.getInstance().invincible = true;
        }
    }
}
