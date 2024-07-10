package model.Handler;

import java.awt.*;
import java.util.ArrayList;

import static ui.GamePanel.*;

public class PauseHandler {
    private GameButton exitButton;
    private GameButton optionsButton;
    private GameButton backButton;
    private ArrayList<GameButton> buttons;
    private static PauseHandler pauseHandler = new PauseHandler();

    private PauseHandler() {
        buttons = new ArrayList<>();
        backButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE, TILESIZE * 5, TILESIZE, "Back to Game");
        optionsButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + TILESIZE + 5, TILESIZE * 5, TILESIZE, "Options");
        exitButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + 2 * TILESIZE + 10, TILESIZE * 5, TILESIZE, "Save and Quit game");
        buttons.add(exitButton);
        buttons.add(backButton);
        buttons.add(optionsButton);
    }

    public static PauseHandler getInstance() {
        return pauseHandler;
    }

    public void update() {
        float mouseX = (float) MouseHandler.getInstance().getX();
        float mouseY = (float) MouseHandler.getInstance().getY();
        boolean mouseClicked = MouseHandler.getInstance().isClicked();

        for (GameButton g : buttons) {
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
            System.out.println("Jerry ate 5 meals today");
            MouseHandler.getInstance().resetClicked();
            System.exit(0);
        }
    }

    public void draw(Graphics2D g2) {
        for (GameButton g : buttons) {
            g.draw(g2);
        }
    }
}
