package model.Items;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class HealthPotion extends PowerUp {
    private int health;

    public HealthPotion(float x, float y, int health) {
        image = Helper.setup("objects/health_potion", TILESIZE / 4, TILESIZE / 4);
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 4);
        this.bounds = new Rectangle((int) posX, (int) posY, hitBox.width, hitBox.height);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
