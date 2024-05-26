package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ui.GamePanel.TILESIZE;

public class CollisionChecker {
    private static CollisionChecker cc = new CollisionChecker();
    public Map<Integer, Set<Entity>> collisionSys = new HashMap<>();

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

        int entityLeftCol = entityLeftWorldX / TILESIZE;
        int entityRightCol = entityRightWorldX / TILESIZE;
        int entityTopRow = entityTopWorldY / TILESIZE;
        int entityBottomRow = entityBottomWorldY / TILESIZE;


        try {
            if (entity.getVelX() > 0) {
                int entityRightColNext = (entityRightWorldX + entity.getVelX()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    if (entity.velX > 0) {
                        entity.velX = 0;
                    }
                }
            }

            if (entity.getVelX() < 0) {
                int entityLeftColNext = (entityLeftWorldX + entity.getVelX()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    if (entity.velX < 0) {
                        entity.velX = 0;
                    }
                }
            }

            if (entity.getVelY() > 0) {
                int entityBottomRowNext = (entityBottomWorldY + entity.getVelY()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityBottomRowNext]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityBottomRowNext]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    if (entity.velY > 0) {
                        entity.velY = 0;
                    }
                }
            }

            if (entity.getVelY() < 0) {
                int entityTopRowNext = (entityTopWorldY + entity.getVelY()) / TILESIZE;
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

        this.CheckTile(entity);

        entity.hitBox.x += entity.posX + entity.velX;
        entity.hitBox.y += entity.posY + entity.velY;


        for (int i = 0; i < entities.size(); i++) {
            if (!entities.get(i).equals(entity)) { // Check if not checking against itself

                int entitiesHitBoxX = entities.get(i).hitBox.x;
                int entitiesHitBoxY = entities.get(i).hitBox.y;

                this.CheckTile(entities.get(i));


                entities.get(i).hitBox.x += entities.get(i).posX + entities.get(i).velX;
                entities.get(i).hitBox.y += entities.get(i).posY + entities.get(i).velY;

                if (entity.hitBox.intersects(entities.get(i).hitBox)) {
//                    if (!sysContains(entity, collisionSys)) {
//                        if (!sysContains(entities[i], collisionSys)) {
//                            Set tempSet = new HashSet<>();
//                            tempSet.add(entity);
//                            tempSet.add(entities[i]);
//                            collisionSys.put(entity.hashCode(), tempSet);
//                        } else {
//                            for (int key : collisionSys.keySet()) {
//                                if (collisionSys.get(key).contains(entities[i])) {
//                                    collisionSys.get(key).add(entity);
//                                    break;
//                                }
//                            }
//                        }
//                    } else if (sysContains(entity, collisionSys) && sysContains(entities[i], collisionSys)) {
//                        Set tempSet = new HashSet<>();
//                        Iterator<Map.Entry<Integer, Set<Entity>>> iterator = collisionSys.entrySet().iterator();
//                        while (iterator.hasNext()) {
//                            Map.Entry<Integer, Set<Entity>> entry = iterator.next();
//                            Set<Entity> entitySet = entry.getValue();
//                            if (entitySet.contains(entities[i])) {
//                                tempSet = entitySet;
//                                iterator.remove(); // Remove the entry from the map using the iterator
//                            }
//                        }
//
//
//                        for (int key : collisionSys.keySet()) {
//                            if (collisionSys.get(key).contains(entity)) {
//                                collisionSys.get(key).addAll(tempSet);
//                            }
//                        }
//                    } else {
//                        for (int key : collisionSys.keySet()) {
//                            if (collisionSys.get(key).contains(entity)) {
//                                collisionSys.get(key).add(entities[i]);
//                                break;
//                            }
//                        }
//                    }
                    if (entity.hitBox.x > entities.get(i).hitBox.x) {
                        if (entity.velX < 0) {
                            entity.velX = 0;
                        }
                    }

                    if (entity.hitBox.x < entities.get(i).hitBox.x) {
                        if (entity.velX > 0) {
                            entity.velX = 0;
                        }
                    }

                    if (entity.hitBox.y > entities.get(i).hitBox.y) {
                        if (entity.velY < 0) {
                            entity.velY = 0;
                        }
                    }

                    if (entity.hitBox.y < entities.get(i).hitBox.y) {
                        if (entity.velY > 0) {
                            entity.velY = 0;
                        }
                    }
                    index = i;
                    entity.collision = true;
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

                this.CheckTile(entities.get(i));

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
}
