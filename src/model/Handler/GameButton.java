package model.Handler;

import java.awt.*;

import static ui.Ui.*;

public class GameButton implements UIComponents {
    private Rectangle hitbox;
    private String text;
    private boolean isHovered;
    private boolean isClicked;

    public GameButton(int x, int y, int width, int height, String text) {
        this.hitbox = new Rectangle(x, y, width, height);
        this.text = text;
    }

    public void update(float x, float y, boolean mousedClicked) {
        isHovered = hitbox.contains(x, y);
        isClicked = isHovered && mousedClicked;
    }

    public void draw(Graphics2D g2) {
        if (isHovered) {
            g2.setColor(Color.GRAY);
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }
        g2.fill(hitbox);

        g2.setColor(Color.BLACK);
        g2.setFont(arial_30);
        FontMetrics fm = g2.getFontMetrics();
        int length = (int) fm.getStringBounds(text, g2).getWidth();
        int height = (int) fm.getStringBounds(text, g2).getHeight();
        int ascent = fm.getAscent();

        int stringX = hitbox.x + (hitbox.width - length) / 2;
        int stringY = hitbox.y + (hitbox.height - height) / 2 + ascent;

        g2.drawString(text, stringX, stringY);

        g2.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public boolean isClicked() {
        return isClicked;
    }
}

