package model.Collision;

import model.AOE.Explosion;
import model.Background.Tile;
import model.Background.Tiles;
import model.Blocks.Block;
import model.Entities.AttackShape;
import model.Entities.Enemy;
import model.Entities.Entity;
import model.Entities.Player;
import model.Inventory.InventoryHandler;
import model.Items.Coin;
import model.Items.DroppedItem;
import model.Items.HealthPotion;
import model.Items.PowerUp;
import model.Items.Projectiles.FireBall;
import model.Items.Projectiles.FlyingArrow;
import model.Items.Projectiles.Projectiles;
import model.Items.Weapons.Melee.Sword;

import java.awt.*;
import java.util.List;
import java.util.*;

import static ui.GamePanel.*;

public class CollisionManager {
    private static CollisionManager instance = new CollisionManager();
    private Quadtree quadtree;

    private Player player;

    private CollisionManager() {
        // Initialize quadtree with game bounds
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

        // Insert all the entities into the Quadtree
        for (Entity entity : getEntities()) {
            quadtree.insert(entity);
            checkTile(entity);
        }

        // Check to see if projectile hits a wall, remove if hit wall, if not insert to Quadtree
        Iterator<Projectiles> iterator = getProjectiles().iterator();
        while(iterator.hasNext()) {
            Projectiles proj = iterator.next();
            checkTile(proj);
            if (checkTile(proj)) {
                if (proj instanceof FireBall) {
                    FireBall fireBall = (FireBall) proj;
                    getExplosions().add(new Explosion(fireBall.getPosX(), fireBall.getPosY(), fireBall.getDamage(), fireBall.getExplosionRadius(), fireBall.getDuration(), fireBall.getFiredByPlayer()));
                }
                iterator.remove();
            }
            quadtree.insert(proj);
        }

        // Insert all the explosion tiles
        for (Explosion e : getExplosions()) {
            quadtree.insert(e);
        }

        // Add blocks to Quadtree
        Iterator<Block> iteratorBlocks = getBlocks().iterator();
        while(iteratorBlocks.hasNext()) {
            Block block = iteratorBlocks.next();
            if (block.getHealth() <= 0) {
                iteratorBlocks.remove();
            }
            quadtree.insert(block);
        }

        // If there is an attackArea, insert that as well
        if (player.getAttackArea() != null) {
            quadtree.insert(player.getAttackArea());
        }

        // Handle collisions
        handleCollisions();

        // TODO!!! Maybe add items and powerUps into collidables so can include this in the Quadtree
        // Pick up any items
        checkItemPickUp();

        // Pick up any powerUps
        checkPowerUpPickUp();
    }


    // TODO: Refactor this mess it works rn but its definitely just a bunch of mumbo jumbo
    private void handleCollisions() {
        // Set to store pairs of checked entities
        Set<String> checkedPairs = new HashSet<>();

        // TODO: Change this so it iterates through the entire QuadTree instead of this or wtv this is!!!
        // TODO: Change this so it iterates through the entire QuadTree instead of this or wtv this is!!!
        // TODO: Change this so it iterates through the entire QuadTree instead of this or wtv this is!!!
        for (Entity entity : getEntities()) {
            // Initialize a new list to pass retrieve the collidables that has a chance for collision
            List<Collidable> potentialCollisions = new ArrayList<>();
            quadtree.retrieve(potentialCollisions, entity);

            // Then iterates through the entire list
            for (Collidable collidable : potentialCollisions) {
                if (collidable instanceof Entity) {
                    Entity other = (Entity) collidable;

                    // Generate a unique key for the pair
                    String pairKey = generatePairKey(entity, other);

                    // Check if this pair has already been checked
                    if (!checkedPairs.contains(pairKey)) {
                        // Mark this pair as checked
                        checkedPairs.add(pairKey);

                        if (entity != other && entity.getBounds().intersects(other.getBounds())) {
                            applyImpulse(entity, other);
                            checkTile(entity);
                            checkTile(other);
                            if (entity instanceof Player && collidable instanceof Enemy) {
                                ((Enemy) collidable).handlePlayerCollision();
                            }
                        }
                    }
                } else if (collidable instanceof AttackShape) {
                    // If attackArea hits enemy, then damage them
                    if (player.getAttackArea() != null && player.getAttackArea().intersects(entity.getBounds())) {
                        if (entity instanceof Enemy) {
                            if (!entity.isInvincible()) {
                                entity.hit(((Sword) player.getSelectedItem()).getDamage());
                                entity.setInvincible(true);
                            }
                        }
                    }
                    // TODO: Could definitly refactor this code there are so many repetitive code
                } else if (collidable instanceof FlyingArrow) {
                    // If arrow fired by player hits enemy, then damage them and increase the pierce
                    if (collidable.getBounds().intersects(entity.getBounds())) {
                        FlyingArrow proj = (FlyingArrow) collidable;
                        if (entity instanceof Enemy) {
                            if (proj.getFiredByPlayer()) {
                                if (!entity.isInvincible()) {
                                    proj.addCurNumPierce();
                                    if (proj.getCurNumPierce() >= proj.getMaxNumPierce()) {
                                        getProjectiles().remove(collidable);
                                    }
                                    entity.hit(proj.getDamage());
                                    entity.setInvincible(true);
                                }
                            }
                        } else if (entity instanceof Player) {
                            // If arrow fired by Enemy hits player, then damage player
                            if (!proj.getFiredByPlayer()) {
                                if (!entity.isInvincible()) {
                                    // If for some reasons, that the enemy arrow can pierce
                                    proj.addCurNumPierce();
                                    if (proj.getCurNumPierce() >= proj.getMaxNumPierce()) {
                                        getProjectiles().remove(collidable);
                                    }
                                    // Currently looking at only bow damage
                                    entity.hit(proj.getDamage());
                                    entity.setInvincible(true);
                                }
                            }
                        }
                    }
                } else if (collidable instanceof FireBall) {
                    // If fireball hits anything except friendlies, it explodes
                    if (collidable.getBounds().intersects(entity.getBounds())) {
                        FireBall proj = (FireBall) collidable;
                        if (entity instanceof Enemy) {
                            if (proj.getFiredByPlayer()) {
                                if (!entity.isInvincible()) {
                                    // Creates an explosion with radius from the fireball
                                    getExplosions().add(new Explosion(proj.getPosX(), proj.getPosY(), proj.getDamage(), proj.getExplosionRadius(), proj.getDuration(), proj.getFiredByPlayer()));
                                    proj.addCurNumPierce();
                                    if (proj.getCurNumPierce() >= proj.getMaxNumPierce()) {
                                        getProjectiles().remove(collidable);
                                    }
                                    entity.hit(proj.getDamage());
                                }
                            }
                        } else if (entity instanceof Player) {
                            if (!proj.getFiredByPlayer()) {
                                if (!entity.isInvincible()) {
                                    // Creates an explosion with radius from the fireball
                                    getExplosions().add(new Explosion(proj.getPosX(), proj.getPosY(), proj.getDamage(), proj.getExplosionRadius(), proj.getDuration(), proj.getFiredByPlayer()));
                                    proj.addCurNumPierce();
                                    if (proj.getCurNumPierce() >= proj.getMaxNumPierce()) {
                                        getProjectiles().remove(collidable);
                                    }
                                    entity.hit(proj.getDamage());
                                }
                            }
                        }
                    }
                } else if (collidable instanceof Explosion) {
                    // If player walks through the explosion fired by enemy, take damage, vice versa
                    if (collidable.getBounds().intersects(entity.getBounds())) {
                        Explosion explosion = (Explosion) collidable;
                        if (entity instanceof Enemy) {
                            if (explosion.getFiredByPlayer()) {
                                if (!entity.isInvincible()) {
                                    entity.hit(explosion.getDamage());
                                    entity.setInvincible(true);
                                }
                            }
                        } else if (entity instanceof Player) {
                            if (!explosion.getFiredByPlayer()) {
                                if (!entity.isInvincible()) {
                                    entity.hit(explosion.getDamage());
                                    entity.setInvincible(true);
                                }
                            }
                        }
                    }
                } else if (collidable instanceof Block) {
                    // If hits a block, then bounce off it (the ones player places)
                    // TODO: make it so that arrow can't pierce it and will delete itself too
                    Block block = (Block) collidable;
                    if (block.getBounds().intersects(entity.getBounds())) {
                        applyImpulseBlock(entity, block);
                        if (entity instanceof Enemy) {
                            block.reduceHealth(((Enemy) entity).getDamage());
                        }
                    }
                }
            }
        }
    }

    // To make sure that no 2 entities are checked again but should be removed if change handleCollision method entirely
    private String generatePairKey(Entity entity1, Entity entity2) {
        int id1 = System.identityHashCode(entity1);
        int id2 = System.identityHashCode(entity2);

        // Ensure the key is always in the same order
        return id1 < id2 ? id1 + "-" + id2 : id2 + "-" + id1;
    }

    private void applyImpulseBlock(Entity entity, Block block) {
        // TODO: Player can walk through walls
        float overlapX = entity.getBounds().x - block.getBounds().x;
        float overlapY = entity.getBounds().y - block.getBounds().y;

        if (Math.abs(overlapX) > Math.abs(overlapY)) {
            float impulseX = (overlapX > 0 ? 1 : -1) * entity.getMass();
            entity.setVelX(entity.getVelX() + impulseX / entity.getMass());
        } else {
            float impulseY = (overlapY > 0 ? 1 : -1) * entity.getMass();
            entity.setVelY(entity.getVelY() + impulseY / entity.getMass());
        }

//        updateEntityBounds(entity);
    }


    private void applyImpulse(Entity entity, Entity other) {
        // Change the velocity of both entities, so they bounce off each other, not updating the bounds rn cuz they will be checked next frame
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

//        updateEntityBounds(entity);
//        updateEntityBounds(other);
    }

//    private void updateEntityBounds(Entity entity) {
//        int newBoundX = (int) (entity.getPosX() + entity.getVelX() + entity.getHitBox().x);
//        int newBoundY = (int) (entity.getPosY() + entity.getVelY() + entity.getHitBox().y);
//        entity.getBounds().x = newBoundX;
//        entity.getBounds().y = newBoundY;
//    }

    private void checkTile(Entity entity) {
        // Checks for background walls that can't walk through, might be stupid because there isn't that many background walls.
        // But maybe there should be more background objects that entities can't walk through like a mineral deposit or smth.
        // TODO: Maybe change this and make everything a block instead of having Tiles that you can't walk through and Blocks that player place and you also can't walk through
        // in other words, remove the collision functionality of tiles
        int entityLeftWorldX = entity.getBounds().x + TILESIZE;
        int entityRightWorldX = entity.getBounds().x + entity.getBounds().width + TILESIZE;
        int entityTopWorldY = entity.getBounds().y + TILESIZE;
        int entityBottomWorldY = entity.getBounds().y + entity.getBounds().height + TILESIZE;

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

    private boolean checkTile(Projectiles proj) {
        // Checks for tile collisions for projectiles
        int entityLeftWorldX = proj.getBounds().x + TILESIZE;
        int entityRightWorldX = proj.getBounds().x + proj.getBounds().width + TILESIZE;
        int entityTopWorldY = proj.getBounds().y + TILESIZE;
        int entityBottomWorldY = proj.getBounds().y + proj.getBounds().height + TILESIZE;

        int entityLeftCol = entityLeftWorldX / TILESIZE;
        int entityRightCol = entityRightWorldX / TILESIZE;
        int entityTopRow = entityTopWorldY / TILESIZE;
        int entityBottomRow = entityBottomWorldY / TILESIZE;

        int roundedVelX = proj.getVelX() >= 0 ? (int) Math.ceil(proj.getVelX()) : (int) Math.floor(proj.getVelX());
        int roundedVelY = proj.getVelY() >= 0 ? (int) Math.ceil(proj.getVelY()) : (int) Math.floor(proj.getVelY());

        Tile[] currentTileList = Tiles.getInstance().getListTiles();
        int[][] currentMapTile = Tiles.getInstance().getMapTile();


        try {
            if (proj.getVelX() != 0) {
                int nextCol = proj.getVelX() > 0 ? (entityRightWorldX + roundedVelX) / TILESIZE : (entityLeftWorldX + roundedVelX) / TILESIZE;
                Tile nextTile1 = currentTileList[currentMapTile[nextCol][entityTopRow]];
                Tile nextTile2 = currentTileList[currentMapTile[nextCol][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    return true;
                }
            }

            if (proj.getVelY() != 0) {
                int nextRow = proj.getVelY() > 0 ? (entityBottomWorldY + roundedVelY) / TILESIZE : (entityTopWorldY + roundedVelY) / TILESIZE;
                Tile nextTile1 = currentTileList[currentMapTile[entityLeftCol][nextRow]];
                Tile nextTile2 = currentTileList[currentMapTile[entityRightCol][nextRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    return true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        return false;
    }

    public void checkItemPickUp() {
        // Currently iterates through all the items and see if they collide with the player
        Iterator<DroppedItem> iterator = getDroppedItems().iterator();
        while (iterator.hasNext()) {
            DroppedItem i = iterator.next();
            if (player.getBounds().intersects(i.getBounds())) {
                if (i.getPickUpCount() > i.getMaxPickUpCount()) {
                    if (InventoryHandler.getInstance().addItem(i)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public void checkPowerUpPickUp() {
        // Same thing with the item pickup
        // Feels like can refactor so that instead of checking for the type of powerUp, just make one method that handles pickup
        Iterator<PowerUp> iterator = getPowerUps().iterator();
        while (iterator.hasNext()) {
            PowerUp i = iterator.next();
            if (player.getBounds().intersects(i.getBounds())) {
                if (i instanceof Coin) {
                    Player.getInstance().addCoin(((Coin) i).getAmount());
                    iterator.remove();
                } else if (i instanceof HealthPotion) {
                    Player.getInstance().addHealth(((HealthPotion) i).getHealth());
                    iterator.remove();
                }
            }
        }
    }
}
