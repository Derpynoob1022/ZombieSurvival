package model.Handler.UIComponents;

import java.awt.*;

// Represents an intractable ui component
public interface UIComponents {
    void draw(Graphics2D g2);
    void update(float mouseX, float mouseY, boolean mousePressed);
}
