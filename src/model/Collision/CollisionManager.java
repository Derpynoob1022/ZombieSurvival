package model.Collision;

import model.Entities.*;
import model.Items.Coin;
import model.Items.Item;
import model.Tile;
import model.Handler.Tiles;

import java.awt.*;
import java.util.List;
import java.util.*;

import static ui.GamePanel.*;

public class CollisionManager {
    private static CollisionManager instance = new CollisionManager();
    private Quadtree quadtree;

    private Player player;

    private CollisionManager() {
        // Initialize quadtree with game bounds or specific area
        Rectangle worldBounds = new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        quadtree = new Quadtree(0, worldBounds);
        player = Player.getInstance();
    }

    public static CollisionManager getInstance() {
        return instance;
    }

    public void update() {
        // Clear the quadtree and insert all entities
        quadtree.clear();

        for (Entity entity : getEntities()) {
            quadtree.insert(entity);
            checkTile(entity);
        }

        if (player.getAttackArea() != null) {
            quadtree.insert(player.getAttackArea());
        }

        quadtree.insert(player);

        // Handle collisions
        handleCollisions();

        // Pick up any items
        checkItemPickUp();
    }


    private void handleCollisions() {
        for (Entity entity : getEntities()) {
            List<Collidable> potentialCollisions = new ArrayList<>();
            quadtree.retrieve(potentialCollisions, entity);

            for (Collidable collidable : potentialCollisions) {
                if (collidable instanceof Entity) {
                    Entity other = (Entity) collidable;

                    if (entity != other && entity.getBounds().intersects(other.getBounds())) {
                        applyImpulse(entity, other);
                        checkTile(entity);
                        checkTile(other);
                        if (collidable instanceof Player && entity instanceof Enemy) {
                            ((Enemy) entity).handlePlayerCollision();
                        }
                    }
                }
                if (collidable instanceof AttackShape) {
                    if (player.getAttackArea() != null && player.getAttackArea().intersects(entity.getBounds())) {
                        if (entity instanceof Enemy) {
                            if (!entity.isInvincible()) {
                                entity.hit(((Sword) player.getSelectedItem()).getDamage());
                                entity.setInvincible(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyImpulse(Entity entity, Entity other) {
        float overlapX = entity.getBounds().x - other.getBounds().x;
        float overlapY = entity.getBounds().y - other.getBounds().y;

        float totalMass = entity.getMass() + other.getMass();

        if (Math.abs(overlapX) > Math.abs(overlapY)) {
            float impulseX = (overlapX > 0 ? 1 : -1) * (2 * entity.getMass() * other.getMass() / totalMass);
            entity.setVelX(entity.getVelX() + impulseX / entity.getMass());
            other.setVelX(other.getVelX() - impulseX / other.getMass());
        } else {
            float impulseY = (overlapY > 0 ? 1 : -1) * (2 * entity.getMass() * other.getMass() / totalMass);
            entity.setVelY(entity.getVelY() + impulseY / entity.getMass());
            other.setVelY(other.getVelY() - impulseY / other.getMass());
        }

        updateEntityBounds(entity);
        updateEntityBounds(other);
    }

    private void updateEntityBounds(Entity entity) {
        int newBoundX = (int) (entity.getPosX() + entity.getVelX() + entity.getHitBox().x);
        int newBoundY = (int) (entity.getPosY() + entity.getVelY() + entity.getHitBox().y);
        entity.getBounds().x = newBoundX;
        entity.getBounds().y = newBoundY;
    }

    private void checkTile(Entity entity) {
        int entityLeftWorldX = entity.getBounds().x;
        int entityRightWorldX = entity.getBounds().x + entity.getBounds().width;
        int entityTopWorldY = entity.getBounds().y;
        int entityBottomWorldY = entity.getBounds().y + entity.getBounds().height;

        int entityLeftCol = entityLeftWorldX / TILESIZE;
        int entityRightCol = entityRightWorldX / TILESIZE;
        int entityTopRow = entityTopWorldY / TILESIZE;
        int entityBottomRow = entityBottomWorldY / TILESIZE;

        int roundedVelX = entity.getVelX() >= 0 ? (int) Math.ceil(entity.getVelX()) : (int) Math.floor(entity.getVelX());
        int roundedVelY = entity.getVelY() >= 0 ? (int) Math.ceil(entity.getVelY()) : (int) Math.floor(entity.getVelY());

        Tile[] currentTileList = Tiles.getInstance().getListTiles();
        int[][] currentMapTile = Tiles.getInstance().getMapTile();

        try {
            if (entity.getVelX() > 0) {
                int entityRightColNext = (entityRightWorldX + roundedVelX) / TILESIZE;
                Tile nextTile1 = currentTileList[currentMapTile[entityRightColNext][entityTopRow]];
                Tile nextTile2 = currentTileList[currentMapTile[entityRightColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.setVelX(0);
                }
            }

            if (entity.getVelX() < 0) {
                int entityLeftColNext = (entityLeftWorldX + roundedVelX) / TILESIZE;
                Tile nextTile1 = currentTileList[currentMapTile[entityLeftColNext][entityTopRow]];
                Tile nextTile2 = currentTileList[currentMapTile[entityLeftColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.setVelX(0);
                }
            }

            if (entity.getVelY() > 0) {
                int entityBottomRowNext = (entityBottomWorldY + roundedVelY) / TILESIZE;
                Tile nextTile1 = currentTileList[currentMapTile[entityLeftCol][entityBottomRowNext]];
                Tile nextTile2 = currentTileList[currentMapTile[entityRightCol][entityBottomRowNext]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.setVelY(0);
                }
            }

            if (entity.getVelY() < 0) {
                int entityTopRowNext = (entityTopWorldY + roundedVelY) / TILESIZE;
                Tile nextTile1 = currentTileList[currentMapTile[entityLeftCol][entityTopRowNext]];
                Tile nextTile2 = currentTileList[currentMapTile[entityRightCol][entityTopRowNext]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.setVelY(0);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Handle the exception if the entity is out of bounds
        }
    }

    //    private void handleEntityCollision(Entity entity, Entity other) {
//        // Calculate the new velocities based on a simple elastic collision model
//        float massSum = entity.getMass() + other.getMass();
//        float massDiff = entity.getMass() - other.getMass();
//
//        float newVelX1 = (entity.getVelX() * massDiff + 2 * other.mass * other.getVelX()) / massSum;
//        float newVelY1 = (entity.velY * massDiff + 2 * other.mass * other.velY) / massSum;
//        float newVelX2 = (other.getVelX() * -massDiff + 2 * entity.mass * entity.getVelX()) / massSum;
//        float newVelY2 = (other.velY * -massDiff + 2 * entity.mass * entity.velY) / massSum;
//
//        // Apply a small friction effect
//        float friction = 0.9f;
//        entity.setVelX(newVelX1 * friction);
//        entity.velY = newVelY1 * friction;
//        other.setVelX(newVelX1 * friction);
//        other.velY = newVelY2 * friction;
//
//        // Update bounds considering the hitBox offsets
//        entity.bounds.x = (int) (entity.posX + entity.velX + entity.hitBox.x);
//        entity.bounds.y = (int) (entity.posY + entity.velY + entity.hitBox.y);
//        other.bounds.x = (int) (other.posX + other.velX + other.hitBox.x);
//        other.bounds.y = (int) (other.posY + other.velY + other.hitBox.y);
//
//        // Resolve tile collisions for entity and other
//        checkTile(entity);
//        checkTile(other);
//
//        // Adjust velocities if collision with tile occurred
//        if (entity.collisionUp || entity.collisionDown || entity.collisionLeft || entity.collisionRight) {
//            // If entity collides, adjust other's velocity
//            if (entity.collisionRight && other.velX > 0) {
//                other.velX = 0;
//            } else if (entity.collisionLeft && other.velX < 0) {
//                other.velX = 0;
//            }
//            if (entity.collisionDown && other.velY > 0) {
//                other.velY = 0;
//            } else if (entity.collisionUp && other.velY < 0) {
//                other.velY = 0;
//            }
//        }
//
//        if (other.collisionUp || other.collisionDown || other.collisionLeft || other.collisionRight) {
//            // If other collides, adjust entity's velocity
//            if (other.collisionRight && entity.velX > 0) {
//                entity.velX = 0;
//            } else if (other.collisionLeft && entity.velX < 0) {
//                entity.velX = 0;
//            }
//            if (other.collisionDown && entity.velY > 0) {
//                entity.velY = 0;
//            } else if (other.collisionUp && entity.velY < 0) {
//                entity.velY = 0;
//            }
//        }
//
//        // Update bounds after collision handling
//        entity.bounds.x = (int) (entity.posX + entity.velX + entity.hitBox.x);
//        entity.bounds.y = (int) (entity.posY + entity.velY + entity.hitBox.y);
//        other.bounds.x = (int) (other.posX + other.velX + other.hitBox.x);
//        other.bounds.y = (int) (other.posY + other.velY + other.hitBox.y);
//    }



//    private void handleEntityCollision(Entity entity, Entity other) {
//        // Calculate new velocities
//        float v1x = ((entity.mass - other.mass) * entity.velX + 2 * other.mass * other.velX) / (entity.mass + other.mass);
//        float v1y = ((entity.mass - other.mass) * entity.velY + 2 * other.mass * other.velY) / (entity.mass + other.mass);
//        float v2x = ((other.mass - entity.mass) * other.velX + 2 * entity.mass * entity.velX) / (entity.mass + other.mass);
//        float v2y = ((other.mass - entity.mass) * other.velY + 2 * entity.mass * entity.velY) / (entity.mass + other.mass);
//
//        // Update velocities
//        entity.velX = v1x;
//        entity.velY = v1y;
//
//        entity.bounds.x = (int) (entity.posX + entity.hitBox.x + entity.velX);
//        entity.bounds.y = (int) (entity.posY + entity.hitBox.y + entity.velY);
//        checkTile(entity);
//
//        other.velX = v2x;
//        other.velY = v2y;
//        other.bounds.x = (int) (other.posX + other.hitBox.x + other.velX);
//        other.bounds.y = (int) (other.posY + other.hitBox.y + other.velY);
//        checkTile(other);
//
//        entity.collision = true;
//    }



//    private void handleCollisions() {
//        // Retrieve potential collisions from the quadtree
//        AttackShape attackArea = player.getAttackArea();
//
//        // Use a set to store pairs of entities that have already been checked
//        Set<Pair<Entity, Entity>> checkedPairs = new HashSet<>();
//
//        for (Entity entity : ENTITIES) {
//            List<Collidable> potentialCollisions = new ArrayList<>();
//            quadtree.retrieve(potentialCollisions, entity);
//
//            for (Collidable collidable : potentialCollisions) {
//                if (collidable instanceof Entity) {
//                    Entity other = (Entity) collidable;
//
//                    // Create a pair (entity, other) and (other, entity) for checking
//                    Pair<Entity, Entity> pair1 = new Pair<>(entity, other);
//                    Pair<Entity, Entity> pair2 = new Pair<>(other, entity);
//
//                    // Handle collision if the pair hasn't been checked yet
//                    if (entity != other && entity.getBounds().intersects(other.getBounds())
//                            && !checkedPairs.contains(pair1) && !checkedPairs.contains(pair2)) {
//                        handleEntityCollision(entity, other);
//
//                        // Add the pair to the set of checked pairs
//                        checkedPairs.add(pair1);
//                        checkedPairs.add(pair2);
//                    }
//                }
//            }
//
//            // Check for attack collision
//            if (attackArea != null && attackArea.intersects(entity.getBounds())) {
//                if (entity instanceof Enemy) {
//                    if (!entity.invincible) {
//                        entity.hit(((Sword) player.getSelectedItem()).getDamage());
//                        entity.invincible = true;
//                    }
//                }
//            }
//        }
//    }


//    public void checkTile(Entity entity) {
//        int entityLeftWorldX = entity.getBounds().x;
//        int entityRightWorldX = entity.getBounds().x + entity.getBounds().width;
//        int entityTopWorldY = entity.getBounds().y;
//        int entityBottomWorldY = entity.getBounds().y + entity.getBounds().height;
//
//        int entityLeftCol = entityLeftWorldX / TILESIZE;
//        int entityRightCol = entityRightWorldX / TILESIZE;
//        int entityTopRow = entityTopWorldY / TILESIZE;
//        int entityBottomRow = entityBottomWorldY / TILESIZE;
//
//        int roundedVelX;
//        if (entity.getVelX() >= 0) {
//            roundedVelX = (int) Math.ceil(entity.getVelX());
//        } else {
//            roundedVelX = (int) Math.floor(entity.getVelX());
//        }
//
//        int roundedVelY;
//        if (entity.getVelY() >= 0) {
//            roundedVelY = (int) Math.ceil(entity.getVelY());
//        } else {
//            roundedVelY = (int) Math.floor(entity.getVelY());
//        }
//
//        try {
//            if (entity.getVelX() > 0) {
//                int entityRightColNext = (entityRightWorldX + roundedVelX) / TILESIZE;
//                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityTopRow]];
//                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityBottomRow]];
//
//                if (nextTile1.hasCollision || nextTile2.hasCollision) {
//                    if (entity.velX > 0) {
//                        entity.velX = 0;
//                    }
//                }
//            }
//
//            if (entity.getVelX() < 0) {
//                int entityLeftColNext = (entityLeftWorldX + roundedVelX) / TILESIZE;
//                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityTopRow]];
//                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityBottomRow]];
//
//                if (nextTile1.hasCollision || nextTile2.hasCollision) {
//                    if (entity.velX < 0) {
//                        entity.velX = 0;
//                    }
//                }
//            }
//
//            if (entity.getVelY() > 0) {
//                int entityBottomRowNext = (entityBottomWorldY + roundedVelY) / TILESIZE;
//                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityBottomRowNext]];
//                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityBottomRowNext]];
//
//                if (nextTile1.hasCollision || nextTile2.hasCollision) {
//                    if (entity.velY > 0) {
//                        entity.velY = 0;
//                    }
//                }
//            }
//
//            if (entity.getVelY() < 0) {
//                int entityTopRowNext = (entityTopWorldY + roundedVelY) / TILESIZE;
//                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityTopRowNext]];
//                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityTopRowNext]];
//
//                if (nextTile1.hasCollision || nextTile2.hasCollision) {
//                    if (entity.velY < 0) {
//                        entity.velY = 0;
//                    }
//                }
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//        }
//    }

//    private void handleEntityCollision(Entity entity, Entity other) {
//         // Calculate new velocities
//        float v1x = ((entity.mass - other.mass) * entity.velX + 2 * other.mass * other.velX) / (entity.mass + other.mass);
//        float v1y = ((entity.mass - other.mass) * entity.velY + 2 * other.mass * other.velY) / (entity.mass + other.mass);
//        float v2x = ((other.mass - entity.mass) * other.velX + 2 * entity.mass * entity.velX) / (entity.mass + other.mass);
//        float v2y = ((other.mass - entity.mass) * other.velY + 2 * entity.mass * entity.velY) / (entity.mass + other.mass);
//
//        // Update velocities
//        entity.velX = v1x;
//        entity.velY = v1y;
//        checkTile(entity);
//
//        other.velX = v2x;
//        other.velY = v2y;
//        checkTile(other);
//
//        entity.collision = true;
//    }

    public void checkItemPickUp() {
        Iterator<Item> iterator = getDroppedItems().iterator();
        while (iterator.hasNext()) {
            Item i = iterator.next();
            if (player.getBounds().intersects(i.getHitbox())) {
                if (i instanceof Coin) {
                    // is a coin
                    player.addCoin();
                    iterator.remove();
                } else {
                    // other items (sword)
                    if (player.addItem(i)) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}