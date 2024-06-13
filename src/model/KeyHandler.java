package model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler implements KeyListener {
    private static final KeyHandler kH = new KeyHandler();
    private Set<Integer> pressedKeys = new HashSet<>();
    private int lastNumberKeyPressed = 1;

    private KeyHandler() {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.add(keyCode);
        if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8) {
            lastNumberKeyPressed = keyCode - KeyEvent.VK_1 + 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public int getLastNumberKeyPressed() {
        return lastNumberKeyPressed;
    }

    public static KeyHandler getInstance() {
        return kH;
    }
}
