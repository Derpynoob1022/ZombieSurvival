package model;

import static ui.GamePanel.*;

public class CollisionChecker {
    private static CollisionChecker cc = new CollisionChecker();

    private CollisionChecker() {
    }

    public static CollisionChecker getInstance() {
        return cc;
    }

    public void CheckTile(Entity entity) {

        int entityLeftWorldX = entity.posX + entity.hitBox.x;
        int entityRightWorldX = entity.posX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.posY + entity.hitBox.y;
        int entityBottomWorldY = entity.posY + entity.hitBox.y + entity.hitBox.height;

        int entityLeftCol = entityLeftWorldX/TILESIZE;
        int entityRightCol = entityRightWorldX/TILESIZE;
        int entityTopRow = entityTopWorldY/TILESIZE;
        int entityBottomRow = entityBottomWorldY/TILESIZE;

        try {
            if (entity.getVelX() > 0) {
                entityRightCol = (entityRightWorldX + entity.getVelX()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collide = true;
                }
            }

            if (entity.getVelX() < 0) {
                entityLeftCol = (entityLeftWorldX + entity.getVelX()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collide = true;
                }
            }

            if (entity.getVelY() > 0) {
                entityBottomRow = (entityBottomWorldY + entity.getVelY()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityBottomRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collide = true;
                }
            }

            if (entity.getVelY() < 0) {
                entityTopRow = (entityTopWorldY + entity.getVelY()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityTopRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collide = true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            entity.collide = true;
            System.out.println(entityLeftCol);
        }
    }
}
