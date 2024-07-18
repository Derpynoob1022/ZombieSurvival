package model.Handler;

import model.Entities.Player;
import model.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static ui.GamePanel.*;

public class Tiles {
    private Tile[] listTiles;
    private int mapTile[][];
    private static Tiles tiles = new Tiles();
    Helper helper = new Helper();

    private Tiles() {
        listTiles = new Tile[20]; // 20 is the number of different tiles we can have
        mapTile = new int[WORLD_MAXCOL][WORLD_MAXROW];
        setup(0, "yes", false);
        setup(1, "no", false);
        setup(2, "wall", true);
        loadMap("/map/world01.txt");
    }

    public static Tiles getInstance() {
        return tiles;
    }

    public int[][] getMapTile() {
        return mapTile;
    }

    public Tile[] getListTiles() {
        return listTiles;
    }

    public void setup(int index, String imageName, boolean collision) {

        try {
            listTiles[index] = new Tile();
            listTiles[index].hasCollision = collision;
            BufferedImage curImage = ImageIO.read(getClass().getResourceAsStream("/background/"
                    + imageName + ".png"));
            listTiles[index].tileImage = helper.scaleImage(curImage, TILESIZE, TILESIZE);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < WORLD_MAXCOL && row < WORLD_MAXROW) {

                String line = br.readLine();

                while (col < WORLD_MAXCOL) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTile[col][row] = num;
                    col++;

                }
                if (col == WORLD_MAXCOL) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch(Exception e){
        }
    }

    public void draw(Graphics2D g2) {
        Player player = Player.getInstance();

        // Calculate the bounds of the render screen based on the player's position
        int startCol = Math.max(0, (int) (player.getPosX() - player.getScreenX()) / TILESIZE - 2);
        int endCol = Math.min(WORLD_MAXCOL - 1, (int) (player.getPosX() + player.getScreenX()) / TILESIZE + 2);
        int startRow = Math.max(0, (int) (player.getPosY() - player.getScreenY()) / TILESIZE - 2);
        int endRow = Math.min(WORLD_MAXROW - 1, (int) (player.getPosY() + player.getScreenY()) / TILESIZE + 2);

        // Iterate only through the tiles within the calculated bounds
        for (int col = startCol; col <= endCol; col++) {
            for (int row = startRow; row <= endRow; row++) {
                int tileNum = mapTile[col][row];
                int posX = col * TILESIZE;
                int posY = row * TILESIZE;

                float screenX = posX - player.getPosX() + player.getScreenX();
                float screenY = posY - player.getPosY() + player.getScreenY();
                g2.drawImage(listTiles[tileNum].tileImage, (int) screenX, (int) screenY, null);
            }
        }
    }
}
