package model.Handler;

import model.Handler.StateHandler.ControlHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import static ui.GamePanel.*;

public class KeyHandler implements KeyListener {
    private static final KeyHandler kH = new KeyHandler();
    private Set<Integer> pressedKeys = new HashSet<>(); // List of currently pressed keycodes
    private int lastNumberKeyPressed = 1;
    private boolean canPressInventory = true;
    private boolean canPressPause = true;
    private String lastKeyPressed;

    private KeyHandler() {
        // nothing here
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.add(keyCode); // Adds it to the list

        // Last number key pressed is used switch inventory slots
        // TODO: maybe remove this so that it just uses last keyPressed and if its not a numbered key then ignore
        if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8) {
            lastNumberKeyPressed = keyCode - KeyEvent.VK_1 + 1;
        }

        lastKeyPressed = KeyEvent.getKeyText(keyCode);

        // Logic for switching to inventory state and back is here
        // TODO: Remove this and refactor this to the playHandler that i should add !!!
        if (e.getKeyCode() == ControlHandler.getInstance().getInventoryKeycode() && canPressInventory) {
            switch (GAMESTATE) {
                case play:
                    GAMESTATE = GameState.inventory;
                    canPressInventory = false;
                    break;
                case inventory:
                    GAMESTATE = GameState.play;
                    canPressInventory = false;
                    GAME_SNAPSHOT = null;
                    break;
            }
        }

        if (e.getKeyCode() == ControlHandler.getInstance().getPauseKeycode() && canPressPause) {
            switch (GAMESTATE) {
                case play:
                case inventory:
                    GAMESTATE = GameState.pause;
                    canPressPause = false;
                    break;
                case pause:
                    GAMESTATE = GameState.play;
                    canPressPause = false;
                    GAME_SNAPSHOT = null;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == ControlHandler.getInstance().getInventoryKeycode()) {
            canPressInventory = true;
        }

        if (e.getKeyCode() == ControlHandler.getInstance().getPauseKeycode()) {
            canPressPause = true;
        }

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

    public String getLastKeyPressed() {
        return lastKeyPressed;
    }

    public void resetLastKeyPressed() {
        lastKeyPressed = null;
    }

    public static KeyHandler getInstance() {
        return kH;
    }
}
