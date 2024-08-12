package model;

import model.Items.DroppedItem;
import model.Entities.Player;
import model.Items.Coin;
import model.Items.HealthPotion;
import model.Inventory.Item;
import model.Inventory.Stack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.*;

// All the helper functions are here
public class Helper {

    // Method to draw a BufferedImage
    public static void draw(Graphics2D g2, BufferedImage img, float posX, float posY) {
        Player player = Player.getInstance();
        if (isWithinPlayerView(posX, posY)) {
            float screenX = posX - player.getPosX() + player.getScreenX();
            float screenY = posY - player.getPosY() + player.getScreenY();
            g2.drawImage(img, (int) screenX, (int) screenY, null);
        }
    }

    // Method to draw a Rectangle as a filled rectangle
    public static void draw(Graphics2D g2, Color color, float posX, float posY, int width, int height) {
        Player player = Player.getInstance();
        if (isWithinPlayerView(posX, posY)) {
            float screenX = posX - player.getPosX() + player.getScreenX();
            float screenY = posY - player.getPosY() + player.getScreenY();
            g2.setColor(color);
            g2.fillRect((int) screenX, (int) screenY, width, height);
        }
    }

    // Draws a BufferedImage at an angle
    public static void draw(Graphics2D g2, BufferedImage image, float posX, float posY, double angle) {
        Player player = Player.getInstance();
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
        Player player = Player.getInstance();
        return posX + 2 * TILESIZE > player.getPosX() - player.getScreenX() &&
                posX - 2 * TILESIZE < player.getPosX() + player.getScreenX() &&
                posY + 2 * TILESIZE > player.getPosY() - player.getScreenY() &&
                posY - 2 * TILESIZE < player.getPosY() + player.getScreenY();
    }


    // Sets up the Image by giving the image relative path and the width and height
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

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width ,height, null);
        g2.dispose();

        return scaledImage;
    }


    // REQUIRES: chancePercentage can't be >= 1
    public static void tryDropItem(float chancePercentage, Item item, float posX, float posY) {
        if (!item.getIsStackable()) {
            float chance = (float) (Math.random() * 100);

            // Check if the random chance is less than or equal to the chancePercentage
            if (chance <= chancePercentage) {
                int variantX = (int) (Math.random() * TILESIZE);
                int variantY = (int) (Math.random() * TILESIZE);
                getDroppedItems().add(new DroppedItem(item, posX + variantX, posY + variantY));
            }
        } else {
            int amount = 0;
            while (true) {
                float chance = (float) (Math.random() * 100);
                // If it doesn't hit, then exist the loop
                if (chance > chancePercentage) {
                    break;
                }
                amount++;  // Increment the amount
            }
            if (amount >= 1) {
                int variantX = (int) (Math.random() * TILESIZE);
                int variantY = (int) (Math.random() * TILESIZE);

                getDroppedItems().add(new DroppedItem(new Stack(item, amount), posX + variantX, posY + variantY));
            }
        }
    }

    public static void tryDropHealth(float chancePercentage, float posX, float posY) {
        float chance = (float) (Math.random() * 100);

        // Check if the random chance is less than or equal to the chancePercentage
        if (chance <= chancePercentage) {
            int variantX = (int) (Math.random() * TILESIZE);
            int variantY = (int) (Math.random() * TILESIZE);
            getPowerUps().add(new HealthPotion(posX + variantX, posY + variantY, 5));
        }
    }

    public static void tryDropCoin(float chancePercentage, float posX, float posY) {
        int amount = 0;

        while (true) {
            float chance = (float) (Math.random() * 100);
            if (chance > chancePercentage) {
                break;
            }
            amount++;  // Increment the amount of coins to drop
        }

        int variantX = (int) (Math.random() * TILESIZE);
        int variantY = (int) (Math.random() * TILESIZE);

        getPowerUps().add(new Coin(posX + variantX, posY + variantY, amount));
    }
}
