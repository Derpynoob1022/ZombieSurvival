package model.Handler.StateHandler;

import model.Handler.GameState;
import model.Handler.KeyHandler;
import model.Handler.MouseHandler;
import model.Handler.UIComponents.GameButton;
import model.Handler.UIComponents.GameSelector;
import model.Handler.UIComponents.UIComponents;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import static ui.GamePanel.*;

public class ControlHandler implements StateHandler {
    private static ControlHandler controlHandler = new ControlHandler();
    private List<UIComponents> uiComponents;
    private GameSelector moveUp;
    private GameSelector moveLeft;
    private GameSelector moveRight;
    private GameSelector moveDown;
    private GameSelector use;
    private GameSelector inventory;
    private GameSelector pause;
    private GameSelector heal;
    private GameButton exitButton;
    private int scrollOffset;
    private static final int SCROLL_STEP = 20;
    private GameSelector currentlySelected;


    private ControlHandler() {
        uiComponents = new ArrayList<>();
        exitButton = new GameButton(0, 0, TILESIZE * 3, TILESIZE,"Back");
        moveUp = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2 - TILESIZE, TILESIZE * 7, TILESIZE, "Move Up", "W");
        moveDown = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2 + 2 * TILESIZE, TILESIZE * 7, TILESIZE, "Move Down", "S");
        moveLeft = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2, TILESIZE * 7, TILESIZE, "Move Left", "A");
        moveRight = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2 + TILESIZE, TILESIZE * 7, TILESIZE, "Move Right", "D");
        use = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2 + 3 * TILESIZE, TILESIZE * 7, TILESIZE, "Use", "F");
        inventory = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2 + 4 * TILESIZE, TILESIZE * 7, TILESIZE, "Inventory", "E");
        pause = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2 + 5 * TILESIZE, TILESIZE * 7, TILESIZE, "Pause", "Escape");
        heal = new GameSelector((SCREEN_WIDTH - TILESIZE * 7) / 2, SCREEN_HEIGHT / 2 + 6 * TILESIZE, TILESIZE * 7, TILESIZE, "Heal", "H");
        uiComponents.add(exitButton);
        uiComponents.add(moveUp);
        uiComponents.add(moveLeft);
        uiComponents.add(moveRight);
        uiComponents.add(moveDown);
        uiComponents.add(use);
        uiComponents.add(inventory);
        uiComponents.add(pause);
        uiComponents.add(heal);
        scrollOffset = 0;
        currentlySelected = null;
    }

    @Override
    public void update() {
        float mouseX = (float) MouseHandler.getInstance().getX();
        float mouseY = (float) MouseHandler.getInstance().getY();
        boolean mouseClicked = MouseHandler.getInstance().isLeftClicked();

        // Iterate through UI components and update them
        for (UIComponents u : uiComponents) {

            if (u instanceof GameSelector) {
                u.update(mouseX, mouseY + scrollOffset, mouseClicked);
                GameSelector gs = (GameSelector) u;
                // Check if the current GameSelector is selected
                if (gs.isSelected() && gs != currentlySelected) {
                    if (currentlySelected != null) {
                        currentlySelected.deselect(); // Deselect the previous selection
                    }
                    currentlySelected = gs; // Update the currently selected GameSelector
                    // System.out.println("Selected: " + currentlySelected);
                    // Clear the last key pressed to wait for a new key
                    KeyHandler.getInstance().resetLastKeyPressed();
                    break;
                }
            } else {
                u.update(mouseX, mouseY, mouseClicked);
            }
        }

        MouseHandler.getInstance().resetLeftClicked();

        // Handle key change for the currently selected GameSelector
        if (currentlySelected != null) {
            String lastKeyPressed = KeyHandler.getInstance().getLastKeyPressed();
            if (lastKeyPressed != null && !lastKeyPressed.isEmpty()) {
                currentlySelected.changeKey(lastKeyPressed);
                // System.out.println("Key changed to " + lastKeyPressed);
                // Reset the last key pressed after the change
                KeyHandler.getInstance().resetLastKeyPressed();
                currentlySelected.deselect();
                currentlySelected = null;
            }
        }

        if (exitButton.isClicked()) {
            GAMESTATE = GameState.settings;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        for (UIComponents u : uiComponents) {
            if (u instanceof GameSelector) {
                g2.translate(0, -scrollOffset);
                u.draw(g2);
                g2.translate(0, scrollOffset);
            } else {
                u.draw(g2);
            }
        }
    }

    public static ControlHandler getInstance() {
        return controlHandler;
    }

    public void handleMouseWheelEvent(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        scrollOffset += notches * SCROLL_STEP;
        // Limit scrolling to prevent excessive scrolling beyond UI components
        scrollOffset = Math.max(0, Math.min(scrollOffset, getMaxScrollOffset()));
    }

    private int getMaxScrollOffset() {
        // Calculate the maximum scroll offset based on the number of UI components
        return Math.max(0,  uiComponents.size() * TILESIZE + SCREEN_HEIGHT / 2 - SCREEN_HEIGHT);
    }

    // Helper methods to translate a String into a keyCode
    private int getKeyCode(String keyString) {
        if (keyString.length() == 1) {
            char character = keyString.charAt(0);
            return KeyEvent.getExtendedKeyCodeForChar(character);
        } else {
            return getSpecialKeyCode(keyString);
        }
    }

    private int getSpecialKeyCode(String keyString) {
        switch (keyString.toUpperCase()) {
            case "ESCAPE":
                return KeyEvent.VK_ESCAPE;
            case "ENTER":
                return KeyEvent.VK_ENTER;
            case "SPACE":
                return KeyEvent.VK_SPACE;
            case "TAB":
                return KeyEvent.VK_TAB;
            case "SHIFT":
                return KeyEvent.VK_SHIFT;
            case "CONTROL":
                return KeyEvent.VK_CONTROL;
            case "ALT":
                return KeyEvent.VK_ALT;
            case "UP":
                return KeyEvent.VK_UP;
            case "DOWN":
                return KeyEvent.VK_DOWN;
            case "LEFT":
                return KeyEvent.VK_LEFT;
            case "RIGHT":
                return KeyEvent.VK_RIGHT;
            // Add more special keys as needed
            default:
                throw new IllegalArgumentException("Key not recognized: " + keyString);
        }
    }

    public int getMoveUpKeycode() {
        return getKeyCode(moveUp.getKey());
    }

    public int getMoveLeftKeycode() {
        return getKeyCode(moveLeft.getKey());
    }

    public int getMoveRightKeycode() {
        return getKeyCode(moveRight.getKey());
    }

    public int getMoveDownKeycode() {
        return getKeyCode(moveDown.getKey());
    }


    public int getInventoryKeycode() {
        return getKeyCode(inventory.getKey());
    }

    public int getPauseKeycode() {
        return getKeyCode(pause.getKey());
    }
}