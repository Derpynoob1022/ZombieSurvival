package ui;

import model.Entities.Player;
import model.Handler.*;
import model.Items.Item;
import model.Slot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ui.GamePanel.*;

public class Ui {
    private BufferedImage heart_empty;
    private BufferedImage heart_half;
    private BufferedImage heart_full;
    private BufferedImage hotbarBackground;
    private BufferedImage hotbarBackgroundSelected;
    private BufferedImage coin;
    public static Font arial_80 = new Font("Arial", Font.BOLD, 80);
    public static Font arial_30 = new Font("Arial", Font.PLAIN, 30);

    public Ui() {
        heart_empty = setup("/objects/heart_empty", TILESIZE, TILESIZE);
        heart_half = setup("/objects/heart_half", TILESIZE, TILESIZE);
        heart_full = setup("/objects/heart_full", TILESIZE, TILESIZE);
        hotbarBackground = setup("/background/hotbar", TILESIZE, TILESIZE);
        hotbarBackgroundSelected = setup("/background/highlightedHotbar", TILESIZE + 14, TILESIZE + 14);
        coin = setup("/objects/coin", TILESIZE / 4, TILESIZE / 4);
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


        g2.setColor(Color.red);
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
                BufferedImage slotImage = slot.getItem().getImage();
                // centering the image
                int centeringX = (TILESIZE - slotImage.getWidth()) / 2;
                int centeringY = (TILESIZE - slotImage.getHeight()) / 2;
                g2.drawImage(slotImage, x + centeringX, y + centeringY, null);
            }
            x += TILESIZE;
        }

        x = TILESIZE / 2;
        y = TILESIZE * 2;

        g2.drawImage(coin, x, y, null);

        g2.setColor(Color.GREEN);
        x += TILESIZE / 2;
        y += TILESIZE / 5;
        String text = "Coins: " + Player.getInstance().getCoin();
        g2.drawString(text, x, y);

    }

    public void drawInventory(Graphics2D g2) {
        g2.setColor(Color.darkGray);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));

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
            int mouseX = (int) MouseHandler.getInstance().getX();
            int mouseY = (int) MouseHandler.getInstance().getY();
            Item item = InventoryHandler.getInstance().getSelectedItem();
            int centeringX = item.getImage().getWidth() / 2;
            int centeringY = item.getImage().getHeight() / 2;
            g2.drawImage(item.getImage(), mouseX - centeringX, mouseY - centeringY, null);
        }
    }

    public void drawPause(Graphics2D g2){
        g2.setColor(Color.darkGray);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        PauseHandler.getInstance().draw(g2);
        g2.setFont(arial_80);
        g2.setColor(Color.white);
        String text = "Paused";
        g2.drawString(text, getXforCenteredText(g2, text), 200);
    }

    // TODO: implement those 2 methods
    public void drawSettings(Graphics2D g2) {
        SettingsHandler.getInstance().draw(g2);
    }

    public void drawTitle(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(arial_80);
        String text = "Zombie Game";
        g2.drawString(text, getXforCenteredText(g2, text), 200);
        TitleHandler.getInstance().draw(g2);
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

    public int getXforCenteredText(Graphics2D g2, String text){

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = SCREEN_WIDTH/2 - length/2;
        return x;
    }

    public BufferedImage getHeart_empty() {
        return heart_empty;
    }

    public BufferedImage getHeart_half() {
        return heart_half;
    }

    public BufferedImage getHeart_full() {
        return heart_full;
    }

    public BufferedImage getHotbarBackground() {
        return hotbarBackground;
    }

    public BufferedImage getHotbarBackgroundSelected() {
        return hotbarBackgroundSelected;
    }
}
