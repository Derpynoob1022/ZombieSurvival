package model.Blocks;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class WoodWall extends Block {
    public WoodWall(int tileX, int tileY) {
        this.posX = tileX * TILESIZE;
        this.posY = tileY * TILESIZE;
        this.health = 100;
        collision = true;
        this.bounds = new Rectangle(posX, posY, TILESIZE, TILESIZE);
        this.image = Helper.setup("block/woodWall", TILESIZE, TILESIZE);
    }
}
