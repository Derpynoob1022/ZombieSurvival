package model;

import java.awt.*;
import java.util.*;

import static ui.GamePanel.DROPPED_ITEMS;
import static ui.GamePanel.TILESIZE;

public class CollisionChecker {
    private static CollisionChecker cc = new CollisionChecker();
    // public Map<Integer, Set<Entity>> collisionSys = new HashMap<>();

    private CollisionChecker() {
    }

    public static CollisionChecker getInstance() {
        return cc;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = (int) entity.posX + entity.hitBox.x;
        int entityRightWorldX = (int) entity.posX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = (int) entity.posY + entity.hitBox.y;
        int entityBottomWorldY = (int) entity.posY + entity.hitBox.y + entity.hitBox.height;

        int entityLeftCol = entityLeftWorldX / TILESIZE;
        int entityRightCol = entityRightWorldX / TILESIZE;
        int entityTopRow = entityTopWorldY / TILESIZE;
        int entityBottomRow = entityBottomWorldY / TILESIZE;

        int roundedVelX;
        if (entity.getVelX() >= 0) {
            roundedVelX = (int) Math.ceil(entity.getVelX());
        } else {
            roundedVelX = (int) Math.floor(entity.getVelX());
        }

        int roundedVelY;
        if (entity.getVelY() >= 0) {
            roundedVelY = (int) Math.ceil(entity.getVelY());
        } else {
            roundedVelY = (int) Math.floor(entity.getVelY());
        }

        try {
            if (entity.getVelX() > 0) {
                int entityRightColNext = (entityRightWorldX + roundedVelX) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    if (entity.velX > 0) {
                        entity.velX = 0;
                    }
                }
            }

            if (entity.getVelX() < 0) {
                int entityLeftColNext = (entityLeftWorldX + roundedVelX) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    if (entity.velX < 0) {
                        entity.velX = 0;
                    }
                }
            }

            if (entity.getVelY() > 0) {
                int entityBottomRowNext = (entityBottomWorldY + roundedVelY) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityBottomRowNext]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityBottomRowNext]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    if (entity.velY > 0) {
                        entity.velY = 0;
                    }
                }
            }

            if (entity.getVelY() < 0) {
                int entityTopRowNext = (entityTopWorldY + roundedVelY) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityTopRowNext]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityTopRowNext]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    if (entity.velY < 0) {
                        entity.velY = 0;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public int checkEntityCollision(Entity entity, ArrayList<Entity> entities) {
        int index = -1; // Initialize index to indicate no collision

        int entityHitBoxX = entity.hitBox.x;
        int entityHitBoxY = entity.hitBox.y;

        // this.CheckTile(entity);

        entity.hitBox.x += entity.posX + entity.velX;
        entity.hitBox.y += entity.posY + entity.velY;


        for (int i = 0; i < entities.size(); i++) {
            if (!entities.get(i).equals(entity)) { // Check if not checking against itself

                int entitiesHitBoxX = entities.get(i).hitBox.x;
                int entitiesHitBoxY = entities.get(i).hitBox.y;

                // this.CheckTile(entities.get(i));

                entities.get(i).hitBox.x += entities.get(i).posX + entities.get(i).velX;
                entities.get(i).hitBox.y += entities.get(i).posY + entities.get(i).velY;

                if (entity.hitBox.intersects(entities.get(i).hitBox)) {

                    // Calculate new velocities
                    float v1x = ((entity.mass - entities.get(i).mass) * entity.velX + 2 * entities.get(i).mass * entities.get(i).velX) / (entity.mass + entities.get(i).mass);
                    float v1y = ((entity.mass - entities.get(i).mass) * entity.velY + 2 * entities.get(i).mass * entities.get(i).velY) / (entity.mass + entities.get(i).mass);
                    float v2x = ((entities.get(i).mass - entity.mass) * entities.get(i).velX + 2 * entity.mass * entity.velX) / (entity.mass + entities.get(i).mass);
                    float v2y = ((entities.get(i).mass - entity.mass) * entities.get(i).velY + 2 * entity.mass * entity.velY) / (entity.mass + entities.get(i).mass);

                    // Update velocities
                    entity.velX = v1x;
                    entity.velY = v1y;
                    checkTile(entity);

                    entities.get(i).velX = v2x;
                    entities.get(i).velY = v2y;
                    checkTile(entities.get(i));

                    index = i;
                    entity.collision = true;

//                    float totalMass = entity.mass + entities.get(i).mass;
//                    float vX = (entity.velX * entity.mass + entities.get(i).velX * entities.get(i).mass) / totalMass;
//                    float vY = (entity.velY * entity.mass + entities.get(i).velY * entities.get(i).mass) / totalMass;
//
//                    entity.velX = vX;
//                    entity.velY = vY;
//                    checkTile(entity);
//
//                    entities.get(i).velX = vX;
//                    entities.get(i).velY = vY;
//                    checkTile(entities.get(i));
//
//                    index = i;
//                    entity.collision = true;
                }


                // Reset the hit box of the current entity
                entities.get(i).hitBox.x = entitiesHitBoxX;
                entities.get(i).hitBox.y = entitiesHitBoxY;
            }
        }

        // Reset the hit box of the main entity
        entity.hitBox.x = entityHitBoxX;
        entity.hitBox.y = entityHitBoxY;

        return index;
    }

//    public boolean sysContains(Entity e, Map<Integer, Set<Entity>> map) {
//        for (Set<Entity> entity : map.values()) {
//            if (entity.contains(e)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void handleCollision(Map<Integer, Set<Entity>> sys) {
//        // Iterate over the collision systems stored in the map
//        for (Set<Entity> collisionSystem : sys.values()) {
//            int totalVelX = 0;
//            int totalVelY = 0;
//
//            // Iterate over the entities in the collision system
//            for (Entity collidedEntity : collisionSystem) {
//                totalVelX += collidedEntity.velX;
//                totalVelY += collidedEntity.velY;
//            }
//
//            // Update velocities of entities in the collision system with the resultant velocity
//            for (Entity collidedEntity : collisionSystem) {
//                collidedEntity.velX = totalVelX;
//                collidedEntity.velY = totalVelY;
//            }
//        }
//
//    }
    public ArrayList<Integer> checkAttackCollision(Shape shape, ArrayList<Entity> entities) {
        ArrayList<Integer> IndexList = new ArrayList<>(); // Initialize index to indicate no collision

        for (int i = 0; i < entities.size(); i++) {
            if (!entities.get(i).equals(Player.getInstance())) { // Check if not checking against itself

                int entitiesHitBoxX = entities.get(i).hitBox.x;
                int entitiesHitBoxY = entities.get(i).hitBox.y;

                // this.CheckTile(entities.get(i));

                entities.get(i).hitBox.x += entities.get(i).posX + entities.get(i).velX;
                entities.get(i).hitBox.y += entities.get(i).posY + entities.get(i).velY;

                if (shape.intersects(entities.get(i).hitBox)) {
                    IndexList.add(i);
                }

                // Reset the hit box of the current entity
                entities.get(i).hitBox.x = entitiesHitBoxX;
                entities.get(i).hitBox.y = entitiesHitBoxY;
            }
        }
        return IndexList;
    }

    // Checks collision with items on the ground
    // maybe when full inv can't pick up?
    public void checkItemPickUp() {
        ArrayList<Integer> IndexList = new ArrayList<>(); // Initialize index to indicate no collision

        int PLayerHitBoxX = Player.getInstance().hitBox.x;
        int PlayerHitBoxY = Player.getInstance().hitBox.y;

        Player.getInstance().hitBox.x += Player.getInstance().posX + Player.getInstance().velX;
        Player.getInstance().hitBox.y += Player.getInstance().posY + Player.getInstance().velY;
        // TODO: Make the hitbox rectangle bigger

        Iterator<Item> iterator = DROPPED_ITEMS.iterator();
        while (iterator.hasNext()) {
            Item i = iterator.next();
            if (Player.getInstance().hitBox.intersects(i.hitBox)) {
                if (i instanceof Coin) {
                    // is a coin
                    Player.getInstance().addCoin();
                    iterator.remove();
                } else {
                    // other items (sword)
                    if (Player.getInstance().addItem(i)) {
                        iterator.remove();
                    }
                }
            }
        }

        // Reset the hit box of the current entity
        Player.getInstance().hitBox.x = PLayerHitBoxX;
        Player.getInstance().hitBox.y = PlayerHitBoxY;
    }
}
