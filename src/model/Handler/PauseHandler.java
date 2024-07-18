package model.Handler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ui.GamePanel.*;

public class PauseHandler {
    private GameButton exitButton;
    private GameButton optionsButton;
    private GameButton backButton;
    private List<UIComponents> uiComponents;
    private static PauseHandler pauseHandler = new PauseHandler();

    private PauseHandler() {
        uiComponents = new ArrayList<>();
        backButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE, TILESIZE * 5, TILESIZE, "Back to Game");
        optionsButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + TILESIZE + 5, TILESIZE * 5, TILESIZE, "Options");
        exitButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + 2 * TILESIZE + 10, TILESIZE * 5, TILESIZE, "Save and Quit game");
        uiComponents.add(exitButton);
        uiComponents.add(backButton);
        uiComponents.add(optionsButton);
    }

    public static PauseHandler getInstance() {
        return pauseHandler;
    }

    public void update() {
        float mouseX = (float) MouseHandler.getInstance().getX();
        float mouseY = (float) MouseHandler.getInstance().getY();
        boolean mouseClicked = MouseHandler.getInstance().isClicked();

        for (UIComponents g : uiComponents) {
            g.update(mouseX, mouseY, mouseClicked);
        }

        if (backButton.isClicked()) {
            GAMESTATE = GameState.play;
            MouseHandler.getInstance().resetClicked();
        }

        if (optionsButton.isClicked()) {
            GAMESTATE = GameState.settings;
            MouseHandler.getInstance().resetClicked();
        }

        if (exitButton.isClicked()) {
            GAMESTATE = GameState.title;
            MouseHandler.getInstance().resetClicked();
        }
    }

    public void draw(Graphics2D g2) {
        for (UIComponents g : uiComponents) {
            g.draw(g2);
        }
    }
}
