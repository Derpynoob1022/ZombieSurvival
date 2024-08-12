package ui;

import model.Entities.Player;
import model.Handler.*;
import model.Handler.StateHandler.*;
import model.Helper;
import model.Inventory.InventoryHandler;
import model.Inventory.Stack;
import model.Inventory.Slot;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ui.GamePanel.*;

public class Ui {
    private BufferedImage heart_empty;
    private BufferedImage heart_half;
    private BufferedImage heart_full;
    private BufferedImage hotbarBackground;
    private BufferedImage hotbarBackgroundSelected;
    private BufferedImage coin;
    public static Font arial_80 = new Font("Arial", Font.BOLD, 80); // Instantiating new fonts
    public static Font arial_30 = new Font("Arial", Font.PLAIN, 30);

    public Ui() {
        // Setting up all the sprites
        heart_empty = Helper.setup("objects/heart_empty", TILESIZE, TILESIZE);
        heart_half = Helper.setup("objects/heart_half", TILESIZE, TILESIZE);
        heart_full = Helper.setup("objects/heart_full", TILESIZE, TILESIZE);
        hotbarBackground = Helper.setup("background/hotbar", TILESIZE, TILESIZE);
        hotbarBackgroundSelected = Helper.setup("background/highlightedHotbar", TILESIZE + 14, TILESIZE + 14);
        coin = Helper.setup("objects/gold_coin", TILESIZE / 4, TILESIZE / 4);
    }

    public void draw(Graphics2D g2) {
        // Initialize the x and y
        int x = SCREEN_WIDTH - TILESIZE * 3 / 2;
        int y = TILESIZE / 2;
        int i = 0;

        // Draw blank hearts
        while (i < Player.getInstance().getMaxHealth() / 2) {
            g2.drawImage(heart_empty, x, y, null);
            i++;
            x -= TILESIZE;
        }

        // Reset the x value
        x += TILESIZE;
        i = 0;

        // Draw current health of the player from left to right
        while (i < Player.getInstance().getHealth()) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < Player.getInstance().getHealth()) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += TILESIZE;
        }

        // Reset x
        x = TILESIZE / 2;

        // Drawing hotbar background
        for (i = 0; i < 8; i++) {
            g2.drawImage(hotbarBackground, x, y, null);
            x += TILESIZE;
        }

        // Draw selected hotbar slot background
        x = TILESIZE / 2 + (KeyHandler.getInstance().getLastNumberKeyPressed() - 1) * TILESIZE - 7;
        y = TILESIZE / 2 - 7;
        g2.drawImage(hotbarBackgroundSelected, x, y, null);


        // Reset x and y and change colour
        g2.setColor(Color.red);
        x = TILESIZE / 2 + 5;
        y = TILESIZE / 2 + 15;

        // Drawing number for each slot of the hotbar
        for (i = 0; i < 8; i++) {
            g2.drawString(Integer.toString(i + 1), x, y);
            x += TILESIZE;
        }

        // Reset x and y
        x = TILESIZE / 2;
        y = TILESIZE / 2;

        // Drawing the items in the hotbar
        for (i = 0; i < 8; i++) {
            Slot slot = Player.getInstance().getInventory()[i];
            if (slot != null && slot.getItem() != null) {
                BufferedImage slotImage = slot.getItem().getImage();
                // centering the image
                int centeringX = (TILESIZE - slotImage.getWidth()) / 2;
                int centeringY = (TILESIZE - slotImage.getHeight()) / 2;
                g2.drawImage(slotImage, x + centeringX, y + centeringY, null);
                // If the item is a stack of something, it will display the count of the stack
                if (slot.getItem() instanceof Stack) {
                    if (((Stack) slot.getItem()).getCount() > 1) {
                        g2.drawString(Integer.toString(((Stack)slot.getItem()).getCount()), x + TILESIZE - 20, y + TILESIZE - 20);
                    }
                }
            }
            x += TILESIZE;
        }

        // Drawing the player's coin count
        x = TILESIZE / 2;
        y = TILESIZE * 2;
        g2.drawImage(coin, x, y, null);
        g2.setColor(Color.GREEN);
        x += TILESIZE / 2;
        y += TILESIZE / 5;
        String text = "Coins: " + Player.getInstance().getCoin();
        g2.drawString(text, x, y);

        // Drawing the current level number
        x = TILESIZE;
        y = TILESIZE * 3;
        text = "Wave: " + LevelHandler.getInstance().getWave();
        g2.drawString(text, x, y);

        // Drawing the current player level
        y += TILESIZE;
        text = "Level: " + Player.getInstance().getLevel();
        g2.drawString(text, x, y);

        // Drawing the current player xp count
        x += TILESIZE;
        text = "Xp: " + Player.getInstance().getXp();
        g2.drawString(text, x, y);
    }

    public void drawInventory(Graphics2D g2) {
        InventoryHandler.getInstance().draw(g2);
    }

    public void drawPause(Graphics2D g2){
        // Draws a semi transparent layer
        g2.setColor(Color.darkGray);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Draws the pause screen
        g2.setFont(arial_80);
        g2.setColor(Color.white);
        String text = "Paused";
        g2.drawString(text, getXforCenteredText(g2, text), 200);

        // Draws the intractable ui components
        PauseHandler.getInstance().draw(g2);
    }

    public void drawSettings(Graphics2D g2) {
        // Draws the intractable ui components
        SettingsHandler.getInstance().draw(g2);
    }

    public void drawTitle(Graphics2D g2) {
        // Draws the title
        g2.setColor(Color.white);
        g2.setFont(arial_80);
        String text = "Zombie Game";
        g2.drawString(text, getXforCenteredText(g2, text), 200);

        // Draws the intractable ui components
        TitleHandler.getInstance().draw(g2);
    }

    public void drawControl(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(arial_80);
        String text = "Key binds";
        g2.drawString(text, getXforCenteredText(g2, text), 200);

        // Draws the intractable ui components
        ControlHandler.getInstance().draw(g2);
    }

    public void drawDeath(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.setFont(arial_80);
        String text = "You Died";
        g2.drawString(text, getXforCenteredText(g2, text), 200);

        // Draws the intractable ui components
        DeathHandler.getInstance().draw(g2);
    }

    public int getXforCenteredText(Graphics2D g2, String text){
        // Helper method to get the center x for the text
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = SCREEN_WIDTH/2 - length/2;
        return x;
    }
}
