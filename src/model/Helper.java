package model;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ui.GamePanel.TILESIZE;

public class Helper {

    public static void draw(Graphics2D g2, BufferedImage img, int posX, int posY) {
        Player player = Player.getInstance();

        if (posX + 2 * TILESIZE > player.posX - player.screenX &&
                posX - 2 * TILESIZE < player.posX + player.screenX &&
                posY + 2 * TILESIZE > player.posY - player.screenY &&
                posY - 2 * TILESIZE < player.posY + player.screenY) {

            int screenX = posX - player.posX + player.screenX;
            int screenY = posY - player.posY + player.screenY;

            g2.drawImage(img, screenX, screenY, null);
        }
    }

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width ,height, null);
        g2.dispose();

        return scaledImage;
    }
}
