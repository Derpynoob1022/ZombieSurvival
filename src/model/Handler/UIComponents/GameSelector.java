package model.Handler.UIComponents;

import java.awt.*;

import static ui.GamePanel.TILESIZE;
import static ui.Ui.arial_30;

public class GameSelector implements UIComponents {
    // !!! TODO: put check last key pressed in this class so the logic associates with each button rather than the global last key pressed.
    private Rectangle hitbox;
    private String key;
    private String text;
    private boolean isHovered;
    private boolean isSelected;
    private int x;

    public GameSelector(int x, int y, int width, int height, String text, String key) {
        this.hitbox = new Rectangle(x + TILESIZE * 4, y, width - 4 * TILESIZE, height);
        this.text = text;
        this.key = key;
        this.x = x;
    }

    public void update(float x, float y, boolean mousedClicked) {
        isHovered = hitbox.contains(x, y);
        if (!isSelected) {
            isSelected = isHovered && mousedClicked;
        }
    }

    public void draw(Graphics2D g2) {
        if (isHovered) {
            g2.setColor(Color.GRAY);
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }
        g2.fill(hitbox);

        if (key != null) {
            String drawnString;
            if (isSelected) {
                drawnString = ">" + key + "<";
                g2.setColor(Color.RED);
            } else {
                drawnString = key;
                g2.setColor(Color.BLACK);
            }
            g2.setFont(arial_30);
            FontMetrics fm = g2.getFontMetrics();
            int length = (int) fm.getStringBounds(drawnString, g2).getWidth();
            int height = (int) fm.getStringBounds(drawnString, g2).getHeight();
            int ascent = fm.getAscent();

            int stringX = hitbox.x + (hitbox.width - length) / 2;
            int stringY = hitbox.y + (hitbox.height - height) / 2 + ascent;

            g2.drawString(drawnString, stringX, stringY);
            g2.setColor(Color.BLACK);
        }

        g2.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

        g2.setColor(Color.WHITE);

        g2.setFont(arial_30);
        FontMetrics fm = g2.getFontMetrics();
        int height = (int) fm.getStringBounds(text, g2).getHeight();
        int ascent = fm.getAscent();
        int stringY = hitbox.y + (hitbox.height - height) / 2 + ascent;

        g2.drawString(text, x, stringY);
        g2.setColor(Color.BLACK);
    }

    public void changeKey(String newKey) {
        if (!key.equals(newKey)) {
            key = newKey;
            System.out.println(key);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void deselect() {
        isSelected = false;
    }

    public String getKey() {
        return key;
    }
}