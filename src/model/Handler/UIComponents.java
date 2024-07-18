package model.Handler;

import java.awt.*;

public interface UIComponents {
    void draw(Graphics2D g2);
    void update(float mouseX, float mouseY, boolean mousePressed);
}
