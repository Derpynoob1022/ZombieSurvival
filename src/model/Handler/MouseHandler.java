package model.Handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class MouseHandler implements MouseListener, MouseMotionListener {
    private static MouseHandler instance = new MouseHandler();
    private double x;
    private double y;
    private boolean pressed;
    private boolean clicked;
    private boolean isDragging;

    private MouseHandler() {
    }

    public static MouseHandler getInstance() {
        return instance;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        pressed = true;
        clicked = true; // Set clicked to true when the mouse is pressed
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
        clicked = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        clicked = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void resetClicked() {
        clicked = false;
    }
}
