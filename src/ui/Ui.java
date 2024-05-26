package ui;

import model.Heart;
import model.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ui.GamePanel.TILESIZE;

public class Ui {
    public BufferedImage heart_empty;
    public BufferedImage heart_half;
    public BufferedImage heart_full;

    public Ui() {

        Heart heart = new Heart();
        heart_empty = heart.heart_empty;
        heart_half = heart.heart_half;
        heart_full = heart.heart_full;
    }

    public void draw(Graphics2D g2) {
        int x = TILESIZE / 2;
        int y = TILESIZE / 2;
        int i = 0;

        //draw blank hearts
        while (i < Player.getInstance().getMaxHealth() / 2) {
            g2.drawImage(heart_empty, x, y, null);
            i++;
            x += TILESIZE;
        }

        //reset
        x = TILESIZE / 2;
        y = TILESIZE / 2;
        i = 0;

        //draw current health
        while (i < Player.getInstance().getHealth()) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < Player.getInstance().getHealth()) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += TILESIZE;
        }
    }
}
