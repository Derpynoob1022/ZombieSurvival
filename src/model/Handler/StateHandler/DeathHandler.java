package model.Handler.StateHandler;

import model.Handler.GameState;
import model.Handler.LevelHandler;
import model.Handler.MouseHandler;
import model.Handler.UIComponents.GameButton;
import model.Handler.UIComponents.UIComponents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ui.GamePanel.*;

public class DeathHandler implements StateHandler {
    private GameButton retryButton;
    private GameButton exitButton;
    private List<UIComponents> uiComponents;
    private static DeathHandler deathHandler = new DeathHandler();

    private DeathHandler() {
        uiComponents = new ArrayList<>();
        retryButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE, TILESIZE * 5, TILESIZE, "Retry");
        exitButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + TILESIZE + 5, TILESIZE * 5, TILESIZE, "Save and Quit game");
        uiComponents.add(retryButton);
        uiComponents.add(exitButton);
    }

    public static DeathHandler getInstance() {
        return deathHandler;
    }

    public void update() {
        float mouseX = (float) MouseHandler.getInstance().getX();
        float mouseY = (float) MouseHandler.getInstance().getY();
        boolean mouseClicked = MouseHandler.getInstance().isLeftClicked();

        for (UIComponents g : uiComponents) {
            g.update(mouseX, mouseY, mouseClicked);
        }

        if (retryButton.isClicked()) {
            GAMESTATE = GameState.play;
            LevelHandler.getInstance().reset();
            MouseHandler.getInstance().resetLeftClicked();
        }

        if (exitButton.isClicked()) {
            GAMESTATE = GameState.title;
            MouseHandler.getInstance().resetLeftClicked();
        }
    }

    public void draw(Graphics2D g2) {
        for (UIComponents g : uiComponents) {
            g.draw(g2);
        }
    }
}
