package model.Handler;

import model.Entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.TILESIZE;

public class Helper {
    //  MADE PLAYER A STATIC VARIABLE HERE, CAUSE LAG??
    private static Player player = Player.getInstance();

    // Method to draw a BufferedImage
    public static void draw(Graphics2D g2, BufferedImage img, float posX, float posY) {

        if (isWithinPlayerView(posX, posY)) {
            float screenX = posX - player.getPosX() + player.getScreenX();
            float screenY = posY - player.getPosY() + player.getScreenY();
            g2.drawImage(img, (int) screenX, (int) screenY, null);
        }
    }

    // Method to draw a Rectangle as a filled rectangle
    public static void draw(Graphics2D g2, Color color, float posX, float posY, int width, int height) {

        if (isWithinPlayerView(posX, posY)) {
            float screenX = posX - player.getPosX() + player.getScreenX();
            float screenY = posY - player.getPosY() + player.getScreenY();
            g2.setColor(color);
            g2.fillRect((int) screenX, (int) screenY, width, height);
        }
    }

    public static void draw(Graphics2D g2, BufferedImage image, float posX, float posY, double angle) {
        if (isWithinPlayerView(posX, posY)) {
            float screenX = posX - player.getPosX() + player.getScreenX();
            float screenY = posY - player.getPosY() + player.getScreenY();

            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // Create a new transform for drawing
            AffineTransform drawTransform = new AffineTransform();

            // Translate to the screen coordinates first
            drawTransform.translate(screenX, screenY);

            // Rotate around the origin of the image (center of the image)
            drawTransform.rotate(angle + Math.PI / 2, imageWidth / 2.0, imageHeight / 2.0);

            // Draw the image using the transformed graphics context
            g2.drawImage(image, drawTransform, null);
        }
    }


    // Helper method to check if an object is within the player's view
    public static boolean isWithinPlayerView(float posX, float posY) {
        return posX + 2 * TILESIZE > player.getPosX() - player.getScreenX() &&
                posX - 2 * TILESIZE < player.getPosX() + player.getScreenX() &&
                posY + 2 * TILESIZE > player.getPosY() - player.getScreenY() &&
                posY - 2 * TILESIZE < player.getPosY() + player.getScreenY();
    }


    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width ,height, null);
        g2.dispose();

        return scaledImage;
    }

    public static BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image = null;

        try {
            // Load the image using the class loader
            image = ImageIO.read(Helper.class.getClassLoader().getResourceAsStream(imagePath + ".png"));
            image = scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
