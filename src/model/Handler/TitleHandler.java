package model.Handler;

import java.awt.*;
import java.util.ArrayList;

import static ui.GamePanel.*;
import static ui.GamePanel.TILESIZE;

public class TitleHandler {
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

    public void update() {
        float mouseX = (float) MouseHandler.getInstance().getX();
        float mouseY = (float) MouseHandler.getInstance().getY();
        boolean mouseClicked = MouseHandler.getInstance().isClicked();

//        System.out.println("Clicked: " + MouseHandler.getInstance().isClicked());
//        System.out.println("Pressed: " + MouseHandler.getInstance().isPressed());
        for (UIComponents g : buttons) {
            g.update(mouseX, mouseY, mouseClicked);
        }

        if (startButton.isClicked()) {
            GAMESTATE = GameState.play;
            MouseHandler.getInstance().resetClicked();
        }

        if (optionsButton.isClicked()) {
            GAMESTATE = GameState.settings;
            MouseHandler.getInstance().resetClicked();
        }

        if (exitButton.isClicked()) {
            System.out.println("Jerry ate 5 meals today");
            System.exit(0);
        }
    }

    public void draw(Graphics2D g2) {
        for (UIComponents g : buttons) {
            g.draw(g2);
        }
    }
}
