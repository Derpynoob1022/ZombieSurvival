package model.Background;

import model.Entities.Player;
import model.Helper;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static ui.GamePanel.*;

public class Tiles {
    private Tile[] listTiles;
    private int mapTile[][];
    private static Tiles tiles = new Tiles();

    private Tiles() {
        listTiles = new Tile[20]; // 20 is the number of different tiles we can have
        mapTile = new int[WORLD_MAXCOL][WORLD_MAXROW];
        setup(0, "void", true);
        setup(1, "floor_1", false);
        setup(2, "floor_2", false);
        setup(3, "floor_3", false);
        setup(4, "floor_4", false);
        setup(5, "floor_5", false);
        setup(6, "floor_6", false);
        setup(7, "floor_7", false);
        setup(8, "floor_8", false);
        setup(9, "wall", true);
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
        listTiles[index] = new Tile();
        listTiles[index].hasCollision = collision;
        listTiles[index].tileImage = Helper.setup("background/" + imageName, TILESIZE, TILESIZE);
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
                int posX = col * TILESIZE - TILESIZE;
                int posY = row * TILESIZE - TILESIZE;

                float screenX = posX - player.getPosX() + player.getScreenX();
                float screenY = posY - player.getPosY() + player.getScreenY();
                g2.drawImage(listTiles[tileNum].tileImage, (int) screenX, (int) screenY, null);
            }
        }
    }
}
