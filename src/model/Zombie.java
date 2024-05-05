package model;

import ui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Zombie extends Enemy {
    private GamePanel gp;

    public Zombie(GamePanel gp) {
        this.gp = gp;
        posX = 200;
        posY = 200;
        moveSpeed = 0.5F;
    }

    public void draw(Graphics2D g2) {
        try {
            BufferedImage curImage = ImageIO.read(getClass().getResourceAsStream("/enemy/zombie/zombie0.png"));
            DrawingHelper.draw(gp, g2, DrawingHelper.scaleImage(curImage, gp.TILESIZE, gp.TILESIZE), (int) posX, (int) posY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        float distX = Player.getInstance().posX - posX;
        float distY = Player.getInstance().posY - posY;

        double distance = Math.sqrt(distX * distX + distY * distY);

        if (Math.round(distance) != 0) {
            //find the velocity of x and y-axis
            double velX = distX / distance * moveSpeed;
            double velY = distY / distance * moveSpeed;

            posX += velX;
            posY += velY;
        }
    }
}
