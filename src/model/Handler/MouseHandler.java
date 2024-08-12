package model.Handler;

import model.Handler.StateHandler.ControlHandler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import static ui.GamePanel.GAMESTATE;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
    private static MouseHandler instance = new MouseHandler();
    private double x;
    private double y;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean leftClicked;
    private boolean rightClicked;
    private boolean isDragging;

    private MouseHandler() {
    }

    public static MouseHandler getInstance() {
        return instance;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not used because registers click after you left go of the button. Which makes clicking UIs very weird
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = true;
            leftClicked = true; // Set leftClicked to true when the left mouse button is pressed
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightPressed = true;
            rightClicked = true; // Set rightClicked to true when the right mouse button is pressed
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = false;
            leftClicked = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightPressed = false;
            rightClicked = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        // If it drags, it resets clicked for both buttons
        leftClicked = false;
        rightClicked = false;
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Passing scroll wheel movement to control handler (maybe rework the logic)
        if (GAMESTATE == GameState.control) {
            ControlHandler.getInstance().handleMouseWheelEvent(e);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftClicked() {
        return leftClicked;
    }

    public boolean isRightClicked() {
        return rightClicked;
    }

    public void resetLeftClicked() {
        leftClicked = false;
    }

    public void resetRightClicked() {
        rightClicked = false;
    }
}
