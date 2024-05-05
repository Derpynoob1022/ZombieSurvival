package model;

import ui.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingHelper {

    public static void draw(GamePanel gp, Graphics2D g2, BufferedImage img, float posX, float posY) {
        Player player = Player.getInstance();

        if (posX + gp.TILESIZE > player.posX - player.screenX &&
                posX - gp.TILESIZE < player.posX + player.screenX &&
                posY + gp.TILESIZE > player.posY - player.screenY &&
                posY - gp.TILESIZE < player.posY + player.screenY) {

            float screenX = posX - player.posX + player.screenX;
            float screenY = posY - player.posY + player.screenY;

            g2.drawImage(img, (int) screenX, (int) screenY, null);
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
