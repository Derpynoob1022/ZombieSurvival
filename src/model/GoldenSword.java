package model;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class GoldenSword extends Item {
    Helper helper = new Helper();

    public GoldenSword(int x, int y) {
        image = helper.setup("objects/golden_sword", TILESIZE / 4, TILESIZE / 2);
        this.posX = x;
        this.posY = y;
        this.hitBox = new Rectangle(x, y, TILESIZE / 4, TILESIZE / 2);
    }
}
