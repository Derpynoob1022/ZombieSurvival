package model;

import model.Handler.Helper;

import java.awt.image.BufferedImage;

import static ui.GamePanel.TILESIZE;

public class Heart {
    private Helper helper = new Helper();
    public BufferedImage heart_empty;
    public BufferedImage heart_half;
    public BufferedImage heart_full;

    public Heart() {
        heart_empty = helper.setup("/objects/heart_empty", TILESIZE, TILESIZE);
        heart_half = helper.setup("/objects/heart_half", TILESIZE, TILESIZE);
        heart_full = helper.setup("/objects/heart_full", TILESIZE, TILESIZE);
    }
}
