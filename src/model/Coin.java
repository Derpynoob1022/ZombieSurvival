package model;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class Coin extends Item {
    Helper helper = new Helper();

    public Coin(int x, int y) {
        image = helper.setup("objects/coin", TILESIZE / 4, TILESIZE / 4);
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle(x, y, TILESIZE / 4, TILESIZE / 4);
    }
}
