package model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
    private static MouseHandler mH = new MouseHandler();
    public double x;
    public double y;
    public boolean pressed;
    private boolean clicked;

    private MouseHandler() {
    }

    public static MouseHandler getInstance() {
        return mH;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        clicked = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        pressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pressed) {
            x = e.getX();
            y = e.getY();
//            System.out.println(x);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public boolean isClicked() {
        return clicked;
    }

    public void resetClicked() {
        clicked = false;
    }
}
