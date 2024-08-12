package model.Items;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class Coin extends PowerUp {
    private int amount;

    public Coin(float x, float y, int amount) {
        // Different image for different amount of coins received
        if (amount < 3) {
            image = Helper.setup("objects/copper_coin", TILESIZE / 4, TILESIZE / 4);
        } else if (amount < 5) {
            image = Helper.setup("objects/silver_coin", TILESIZE / 4, TILESIZE / 4);
        } else {
            image = Helper.setup("objects/gold_coin", TILESIZE / 4, TILESIZE / 4);
        }
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 4);
        this.bounds = new Rectangle((int) posX, (int) posY, hitBox.width, hitBox.height);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
