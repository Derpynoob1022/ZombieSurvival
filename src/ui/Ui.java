package ui;

import model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.*;

public class Ui {
    public BufferedImage heart_empty;
    public BufferedImage heart_half;
    public BufferedImage heart_full;
    public BufferedImage hotbarBackground;
    public BufferedImage hotbarBackgroundSelected;

    public Ui() {
        heart_empty = setup("/objects/heart_empty", TILESIZE, TILESIZE);
        heart_half = setup("/objects/heart_half", TILESIZE, TILESIZE);
        heart_full = setup("/objects/heart_full", TILESIZE, TILESIZE);
        hotbarBackground = setup("/background/hotbar", TILESIZE, TILESIZE);
        hotbarBackgroundSelected = setup("/background/highlightedHotbar", TILESIZE + 14, TILESIZE + 14);
    }

    public void draw(Graphics2D g2) {
        int x = SCREEN_WIDTH - TILESIZE * 3 / 2;
        int y = TILESIZE / 2;
        int i = 0;

        //draw blank hearts
        while (i < Player.getInstance().getMaxHealth() / 2) {
            g2.drawImage(heart_empty, x, y, null);
            i++;
            x -= TILESIZE;
        }

        //reset
        x += TILESIZE;
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

        x = TILESIZE / 2;

        // drawing hotbar background
        for (i = 0; i < 8; i++) {
            g2.drawImage(hotbarBackground, x, y, null);
            x += TILESIZE;
        }

        // draw highlighted background
        x = TILESIZE / 2 + (KeyHandler.getInstance().getLastNumberKeyPressed() - 1) * TILESIZE - 7;
        y = TILESIZE / 2 - 7;
        g2.drawImage(hotbarBackgroundSelected, x, y, null);


        x = TILESIZE / 2 + 5;
        y = TILESIZE / 2 + 15;
        // drawing number on the hotbar
        for (i = 0; i < 8; i++) {
            g2.drawString(Integer.toString(i + 1), x, y);
            x += TILESIZE;
        }

        // reset x
        x = TILESIZE / 2;
        y = TILESIZE / 2;
        // drawing the items in the hotbar
        for (i = 0; i < 8; i++) {
            Slot slot = Player.getInstance().getInventory()[i];
            if (slot != null && slot.getItem() != null) {
                // centering the image
                BufferedImage slotImage = slot.getItem().getImage();
                int centeringX = (TILESIZE - slotImage.getWidth()) / 2;
                int centeringY = (TILESIZE - slotImage.getHeight()) / 2;
                g2.drawImage(slotImage, x + centeringX, y + centeringY, null);
            }
            x += TILESIZE;
        }
    }

    public void drawInventory(Graphics2D g2) {
        g2.setColor(Color.darkGray);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        int x;
        int y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        // drawing inventory background
        for (int g = 0; g < 4; g++) {
            x = SCREEN_WIDTH / 2 - TILESIZE * 4;
            for (int i = 0; i < 8; i++) {
                g2.drawImage(hotbarBackground, x, y, null);
                x += TILESIZE;
            }
            y -= TILESIZE;
        }

        y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        for (int g = 0; g < 4; g++) {  // 4 rows
            x = SCREEN_WIDTH / 2 - TILESIZE * 4;  // Reset x for each row
            for (int i = 0; i < 8; i++) {  // 8 columns
                Slot slot = Player.getInstance().getInventory()[g * 8 + i];
                if (slot != null && slot.getItem() != null) {
                    BufferedImage slotImage = slot.getItem().getImage();
                    // Centering the image
                    int centeringX = (TILESIZE - slotImage.getWidth()) / 2;
                    int centeringY = (TILESIZE - slotImage.getHeight()) / 2;
                    g2.drawImage(slotImage, x + centeringX, y + centeringY, null);
                }
                x += TILESIZE;
            }
            y -= TILESIZE;
        }

        if (InventoryHandler.getInstance().getGrabbedItem()) {
            int mouseX = (int) MouseHandler.getInstance().x;
            int mouseY = (int) MouseHandler.getInstance().y;
            Item item = InventoryHandler.getInstance().getSelectedItem();
            int centeringX = item.getImage().getWidth() / 2;
            int centeringY = item.getImage().getHeight() / 2;
            g2.drawImage(item.getImage(), mouseX - centeringX, mouseY - centeringY, null);
        }
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = Helper.scaleImage(image, width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
