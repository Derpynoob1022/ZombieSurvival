package model.Handler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ui.GamePanel.*;

public class SettingsHandler{
    private static SettingsHandler settingsHandler = new SettingsHandler();
    private List<UIComponents> uiComponents;
    private GameBar musicBar;
    private GameBar soundEffectBar;
    private GameButton editControlsButton;
    private GameButton returnButton;

    private SettingsHandler(){
        uiComponents = new ArrayList<>();
        musicBar = new GameBar((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE, TILESIZE * 5, TILESIZE, "Music");
        soundEffectBar = new GameBar((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + TILESIZE + 5, TILESIZE * 5, TILESIZE, "Sound Effects");
        editControlsButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + 2 * TILESIZE + 10, TILESIZE * 5, TILESIZE, "Edit Controls");
        returnButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + 3 * TILESIZE + 15, TILESIZE * 5, TILESIZE, "Back");
        uiComponents.add(musicBar);
        uiComponents.add(soundEffectBar);
        uiComponents.add(editControlsButton);
        uiComponents.add(returnButton);
    }

    public static SettingsHandler getInstance(){
        return settingsHandler;
    }

    public void update() {
        float mouseX = (float) MouseHandler.getInstance().getX();
        float mouseY = (float) MouseHandler.getInstance().getY();
        boolean mousePressed = MouseHandler.getInstance().isPressed(); // pressed has problem where it registers it as clicked as soon as u enter that screen, click doesn't let u drag

        for (UIComponents g : uiComponents) {
            g.update(mouseX, mouseY, mousePressed);
        }

        if (editControlsButton.isClicked()) {
            MouseHandler.getInstance().resetClicked();
        }

        if (returnButton.isClicked()) {
            GAMESTATE = GameState.pause;
            MouseHandler.getInstance().resetClicked();
        }
    }

    public void draw(Graphics2D g2) {
        for (UIComponents g : uiComponents) {
            g.draw(g2);
        }
    }
}
