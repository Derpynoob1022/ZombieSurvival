package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.TILESIZE;

public class Helper {

    // Method to draw a BufferedImage
    public static void draw(Graphics2D g2, BufferedImage img, int posX, int posY) {
        Player player = Player.getInstance();

        if (isWithinPlayerView(player, posX, posY)) {
            int screenX = posX - player.getPosX() + player.getScreenX();
            int screenY = posY - player.getPosY() + player.getScreenY();
            g2.drawImage(img, screenX, screenY, null);
        }
    }

    // Method to draw a Rectangle as a filled rectangle
    public static void draw(Graphics2D g2, Color color, int posX, int posY, int width, int height) {
        Player player = Player.getInstance();

        if (isWithinPlayerView(player, posX, posY)) {
            int screenX = posX - player.getPosX() + player.getScreenX();
            int screenY = posY - player.getPosY() + player.getScreenY();
            g2.setColor(color);
            g2.fillRect(screenX, screenY, width, height);
        }
    }

    // Helper method to check if an object is within the player's view
    private static boolean isWithinPlayerView(Player player, int posX, int posY) {
        return posX + 2 * TILESIZE > player.posX - player.getScreenX() &&
                posX - 2 * TILESIZE < player.posX + player.getScreenX() &&
                posY + 2 * TILESIZE > player.posY - player.getScreenY() &&
                posY - 2 * TILESIZE < player.posY + player.getScreenY();
    }


    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width ,height, null);
        g2.dispose();

        return scaledImage;
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image = null;

        try {
            // Load the image using the class loader
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath + ".png"));
            image = scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
