package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.TILESIZE;

public class Heart {
    public BufferedImage heart_empty;
    public BufferedImage heart_half;
    public BufferedImage heart_full;

    public Heart() {
        heart_empty = setup("/objects/heart_empty", TILESIZE, TILESIZE);
        heart_half = setup("/objects/heart_half", TILESIZE, TILESIZE);
        heart_full = setup("/objects/heart_full", TILESIZE, TILESIZE);
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = Helper.scaleImage(image, width, height);

        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
