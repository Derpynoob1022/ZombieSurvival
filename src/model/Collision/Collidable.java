package model.Collision;

import java.awt.*;

// Anything that can collide or has a hitbox
public interface Collidable {
    // TODO: make this an abstract class? So could put the bounds rectangle here for more refactoring
    Rectangle getBounds();
}
