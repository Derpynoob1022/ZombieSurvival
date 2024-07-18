package model.Handler;

import java.awt.*;

import static ui.Ui.arial_30;

public class GameBar implements UIComponents {
    private Rectangle hitbox;
    private String text;
    private boolean isHovered;
    private boolean isPressed;
    private float sliderX;
    private float sliderY;
    private float percentage;
    private int sliderWidth = 10;

    public GameBar(int x, int y, int width, int height, String text) {
        this.hitbox = new Rectangle(x, y , width, height);
        this.text = text;
        sliderY = y;
        sliderX = x + width - sliderWidth;
        percentage = 1f;
    }

    public void update(float mouseX, float mouseY, boolean pressed) {
        isHovered = hitbox.contains(mouseX, mouseY);
        isPressed = isHovered && pressed;

        if (isPressed) {
            if (mouseX - sliderWidth / 2 < hitbox.x) {
                sliderX = hitbox.x;
            } else if (mouseX > hitbox.x + hitbox.width - sliderWidth) {
                sliderX = hitbox.x + hitbox.width - sliderWidth;
            } else {
                sliderX = mouseX - sliderWidth / 2;
            }
            percentage = (sliderX - hitbox.x) / (hitbox.width - sliderWidth);
        }
    }


    public void draw(Graphics2D g2) {
        // Drawing the background of the gamebar depending on if we are hovering it or not
        if (isHovered) {
            g2.setColor(Color.GRAY);
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }
        g2.fill(hitbox);

        // Drawing edges of the gamebar
        g2.setColor(Color.white);
        g2.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

        // Drawing slider
        g2.fillRect((int) sliderX, (int) sliderY, sliderWidth, hitbox.height);

        // TODO: refactor this as a helper method
        // Drawing text
        String drawnString = text + ": " + String.format("%.2f", percentage * 100) + "%";
        g2.setColor(Color.BLACK);
        g2.setFont(arial_30);
        FontMetrics fm = g2.getFontMetrics();
        int length = (int) fm.getStringBounds(drawnString, g2).getWidth();
        int height = (int) fm.getStringBounds(drawnString, g2).getHeight();
        int ascent = fm.getAscent();

        int stringX = hitbox.x + (hitbox.width - length) / 2;
        int stringY = hitbox.y + (hitbox.height - height) / 2 + ascent;

        g2.drawString(drawnString, stringX, stringY);
    }

    public float getPercentage() {
        return percentage;
    }
}
