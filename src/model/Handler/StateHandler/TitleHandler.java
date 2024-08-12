package model.Handler.StateHandler;

import model.Handler.GameState;
import model.Handler.MouseHandler;
import model.Handler.UIComponents.GameButton;
import model.Handler.UIComponents.UIComponents;

import java.awt.*;
import java.util.ArrayList;

import static ui.GamePanel.*;
import static ui.GamePanel.TILESIZE;

// Handles the TitleScreen
public class TitleHandler implements StateHandler {
    private GameButton startButton;
    private GameButton optionsButton;
    private GameButton exitButton;
    private ArrayList<UIComponents> buttons;
    private static TitleHandler titleHandler = new TitleHandler();

    private TitleHandler(){
        buttons = new ArrayList<>();
        startButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE, TILESIZE * 5, TILESIZE, "Start Game");
        optionsButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + TILESIZE + 5, TILESIZE * 5, TILESIZE, "Options");
        exitButton = new GameButton((SCREEN_WIDTH - TILESIZE * 5) / 2, SCREEN_HEIGHT / 2 - TILESIZE + 2 * TILESIZE + 10, TILESIZE * 5, TILESIZE, "Exit Game");
        buttons.add(startButton);
        buttons.add(exitButton);
        buttons.add(optionsButton);
    }

    public static TitleHandler getInstance(){
        return titleHandler;
    }

    @Override
    public void update() {
        float mouseX = (float) MouseHandler.getInstance().getX();
        float mouseY = (float) MouseHandler.getInstance().getY();
        boolean mouseClicked = MouseHandler.getInstance().isLeftClicked();

        for (UIComponents g : buttons) {
            g.update(mouseX, mouseY, mouseClicked);
        }

        if (startButton.isClicked()) {
            GAMESTATE = GameState.play;
            MouseHandler.getInstance().resetLeftClicked();
        }

        if (optionsButton.isClicked()) {
            GAMESTATE = GameState.settings;
            MouseHandler.getInstance().resetLeftClicked();
        }

        if (exitButton.isClicked()) {
            System.out.println("Jerry ate 5 meals today");
            System.exit(0);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        for (UIComponents g : buttons) {
            g.draw(g2);
        }
    }
}
