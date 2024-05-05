package model;

import ui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tiles {
    private Tile[] tiles;
    private int mapTile[][];
    private GamePanel gp;

    public Tiles(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[20]; // 20 is the number of different tiles we can have
        mapTile = new int[gp.WORLD_MAXCOL][gp.WORLD_MAXROW];
        setup(0, "yes");
        setup(1, "no");
        loadMap("/map/world01.txt");
    }

    public void setup(int index, String imageName) {

        try {
            tiles[index] = new Tile();
            BufferedImage curImage = ImageIO.read(getClass().getResourceAsStream("/background/"
                    + imageName + ".png"));
            tiles[index].tileImage = DrawingHelper.scaleImage(curImage, gp.TILESIZE, gp.TILESIZE);

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

            while(col < gp.WORLD_MAXCOL && row < gp.WORLD_MAXROW) {

                String line = br.readLine();

                while(col < gp.WORLD_MAXCOL) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTile[col][row] = num;
                    col++;

                }
                if(col == gp.WORLD_MAXCOL) {
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
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.WORLD_MAXCOL && worldRow < gp.WORLD_MAXROW) {

            int tileNum = mapTile[worldCol][worldRow];

            int posX = worldCol * gp.TILESIZE;
            int posY = worldRow * gp.TILESIZE;

            DrawingHelper.draw(gp, g2, tiles[tileNum].tileImage, posX, posY);

            worldCol++;


            if (worldCol == gp.WORLD_MAXCOL) {
                worldCol = 0;
                worldRow++;

            }
        }
    }
}
