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
                int entityRightColNext = (entityRightWorldX + entity.getVelX()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collideRight = true;
                }
            }

            if (entity.getVelX() < 0) {
                int entityLeftColNext = (entityLeftWorldX + entity.getVelX()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityTopRow]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftColNext][entityBottomRow]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collideLeft = true;
                }
            }

            if (entity.getVelY() > 0) {
                int entityBottomRowNext = (entityBottomWorldY + entity.getVelY()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityBottomRowNext]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityBottomRowNext]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collideDown = true;
                }
            }

            if (entity.getVelY() < 0) {
                int entityTopRowNext = (entityTopWorldY + entity.getVelY()) / TILESIZE;
                Tile nextTile1 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityLeftCol][entityTopRowNext]];
                Tile nextTile2 = Tiles.getInstance().getListTiles()[Tiles.getInstance().getMapTile()[entityRightCol][entityTopRowNext]];

                if (nextTile1.hasCollision || nextTile2.hasCollision) {
                    entity.collideUp = true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            entity.collideRight = true;
            entity.collideLeft = true;
            entity.collideUp = true;
            entity.collideDown = true;
        }
    }

    public int checkEntityCollision(Entity entity, Entity[] entities) {
        int index = -1 ; // Initialize index to indicate no collision

        int entityHitBoxX = entity.hitBox.x;
        int entityHitBoxY = entity.hitBox.y;

        this.CheckTile(entity);

        if (!entity.collideLeft && !entity.collideRight) {
            // No horizontal collisions detected, move by velX
            entity.hitBox.x += entity.velX;
        } else if (entity.collideLeft && entity.velX > 0) {
            // Colliding on the left, but moving right
            entity.hitBox.x += entity.velX;
        } else if (entity.collideRight && entity.velX < 0) {
            // Colliding on the right, but moving left
            entity.hitBox.x += entity.velX;
        }

        if (!entity.collideUp && !entity.collideDown) {
            // No vertical collisions detected, move by velY
            entity.hitBox.y += entity.velY;
        } else if (entity.collideUp && entity.velY > 0) {
            // Colliding on the top, but moving down
            entity.hitBox.y += entity.velY;
        } else if (entity.collideDown && entity.velY < 0) {
            // Colliding on the bottom, but moving up
            entity.hitBox.y += entity.velY;
        }

        entity.hitBox.x += entity.posX;
        entity.hitBox.y += entity.posY;


        for (int i = 0; i < entities.length; i++) {
                if (!entities[i].equals(entity)) { // Check if not checking against itself

                    int entitiesHitBoxX = entities[i].hitBox.x;
                    int entitiesHitBoxY = entities[i].hitBox.y;

                    this.CheckTile(entities[i]);

                    if (!entities[i].collideLeft && !entities[i].collideRight) {
                        // No horizontal collisions detected, move by velX
                        entities[i].hitBox.x += entities[i].velX;
                    } else if (entities[i].collideLeft && entities[i].velX > 0) {
                        // Colliding on the left, but moving right
                        entities[i].hitBox.x += entities[i].velX;
                    } else if (entities[i].collideRight && entities[i].velX < 0) {
                        // Colliding on the right, but moving left
                        entities[i].hitBox.x += entities[i].velX;
                    }

                    if (!entities[i].collideUp && !entities[i].collideDown) {
                        // No vertical collisions detected, move by velY
                        entities[i].hitBox.y += entities[i].velY;
                    } else if (entities[i].collideUp && entities[i].velY > 0) {
                        // Colliding on the top, but moving down
                        entities[i].hitBox.y += entities[i].velY;
                    } else if (entities[i].collideDown && entities[i].velY < 0) {
                        // Colliding on the bottom, but moving up
                        entities[i].hitBox.y += entities[i].velY;
                    }

                    entities[i].hitBox.x += entities[i].posX;
                    entities[i].hitBox.y += entities[i].posY;

                    if (entity.hitBox.intersects(entities[i].hitBox)) {

                        if (entity.hitBox.x > entities[i].hitBox.x) {
                            entity.collideLeft = true;
                        }

                        if (entity.hitBox.x < entities[i].hitBox.x) {
                            entity.collideRight = true;
                        }

                        if (entity.hitBox.y > entities[i].hitBox.y) {
                            entity.collideUp = true;
                        }

                        if (entity.hitBox.y < entities[i].hitBox.y) {
                            entity.collideDown = true;
                        }
                        index = i;
                    }

                    // Reset the hit box of the current entity
                    entities[i].hitBox.x = entitiesHitBoxX;
                    entities[i].hitBox.y = entitiesHitBoxY;
                }
        }

        // Reset the hit box of the main entity
        entity.hitBox.x = entityHitBoxX;
        entity.hitBox.y = entityHitBoxY;

        return index;
    }


    public boolean checkPlayerCollision(Entity entity) {
        boolean playerCollision = false;

        int entityHitBoxX = entity.hitBox.x;
        int entityHitBoxY = entity.hitBox.y;

        this.CheckTile(entity);

        if (!entity.collideLeft && !entity.collideRight) {
            // No horizontal collisions detected, move by velX
            entity.hitBox.x += entity.velX;
        } else if (entity.collideLeft && entity.velX > 0) {
            // Colliding on the left, but moving right
            entity.hitBox.x += entity.velX;
        } else if (entity.collideRight && entity.velX < 0) {
            // Colliding on the right, but moving left
            entity.hitBox.x += entity.velX;
        }

        if (!entity.collideUp && !entity.collideDown) {
            // No vertical collisions detected, move by velY
            entity.hitBox.y += entity.velY;
        } else if (entity.collideUp && entity.velY > 0) {
            // Colliding on the top, but moving down
            entity.hitBox.y += entity.velY;
        } else if (entity.collideDown && entity.velY < 0) {
            // Colliding on the bottom, but moving up
            entity.hitBox.y += entity.velY;
        }

        entity.hitBox.x += entity.posX;
        entity.hitBox.y += entity.posY;

        int playerHitBoxX = Player.getInstance().hitBox.x;
        int playerHitBoxY = Player.getInstance().hitBox.y;

        this.CheckTile(Player.getInstance());

        if (!Player.getInstance().collideLeft && !Player.getInstance().collideRight) {
            // No horizontal collisions detected, move by velX
            Player.getInstance().hitBox.x += Player.getInstance().velX;
        } else if (Player.getInstance().collideLeft && Player.getInstance().velX > 0) {
            // Colliding on the left, but moving right
            Player.getInstance().hitBox.x += Player.getInstance().velX;
        } else if (Player.getInstance().collideRight && Player.getInstance().velX < 0) {
            // Colliding on the right, but moving left
            Player.getInstance().hitBox.x += Player.getInstance().velX;
        }

        if (!Player.getInstance().collideUp && !Player.getInstance().collideDown) {
            // No vertical collisions detected, move by velY
            Player.getInstance().hitBox.y += Player.getInstance().velY;
        } else if (Player.getInstance().collideUp && Player.getInstance().velY > 0) {
            // Colliding on the top, but moving down
            Player.getInstance().hitBox.y += Player.getInstance().velY;
        } else if (Player.getInstance().collideDown && Player.getInstance().velY < 0) {
            // Colliding on the bottom, but moving up
            Player.getInstance().hitBox.y += Player.getInstance().velY;
        }

        Player.getInstance().hitBox.x += Player.getInstance().posX;
        Player.getInstance().hitBox.y += Player.getInstance().posY;


        if (entity.hitBox.intersects(Player.getInstance().hitBox)) {

            if (entity.hitBox.x > Player.getInstance().hitBox.x) {
                entity.collideLeft = true;
            }

            if (entity.hitBox.x < Player.getInstance().hitBox.x) {
                entity.collideRight = true;
            }

            if (entity.hitBox.y > Player.getInstance().hitBox.y) {
                entity.collideUp = true;
            }

            if (entity.hitBox.y < Player.getInstance().hitBox.y) {
                entity.collideDown = true;
            }

            playerCollision = true;
        }

        entity.hitBox.x = entityHitBoxX;
        entity.hitBox.y = entityHitBoxY;

        Player.getInstance().hitBox.x = playerHitBoxX;
        Player.getInstance().hitBox.y = playerHitBoxY;

        return playerCollision;
    }
}
