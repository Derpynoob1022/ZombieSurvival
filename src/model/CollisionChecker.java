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
        int index = -1; // Initialize index to indicate no collision

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

    public boolean checkStable(Entity entity1, Entity entity2) {
        int left1 = entity1.posX + entity1.hitBox.x;
        int right1 = entity1.posX + entity1.hitBox.x + entity1.hitBox.width;
        int top1 = entity1.posY + entity1.hitBox.y;
        int bottom1 = entity1.posY + entity1.hitBox.y + entity1.hitBox.height;

        int left2 = entity2.posX + entity2.hitBox.x;
        int right2 = entity2.posX + entity2.hitBox.x + entity2.hitBox.width;
        int top2 = entity2.posY + entity2.hitBox.y;
        int bottom2 = entity2.posY + entity2.hitBox.y + entity2.hitBox.height;

        // Check for non-intersecting conditions
        if (left1 > right2 || right1 < left2 || top1 > bottom2 || bottom1 < top2) {
            entity1.stable = true;
            return false; // No collision
        }

        entity1.stable = false;
//        System.out.println("Entity 1:");
//        System.out.println("Left 1: " + left1);
//        System.out.println("Right 1: " + right1);
//        System.out.println("Top 1: " + top1);
//        System.out.println("Bottom 1: " + bottom1);
//
//        System.out.println("Entity 2:");
//        System.out.println("Left 2: " + left2);
//        System.out.println("Right 2: " + right2);
//        System.out.println("Top 2: " + top2);
//        System.out.println("Bottom 2: " + bottom2);
//
//        System.out.println("Condition 1: left1 > right2");
//        System.out.println("Condition 1 Value: " + (left1 > right2));
//
//        System.out.println("Condition 2: right1 < left2");
//        System.out.println("Condition 2 Value: " + (right1 < left2));
//
//        System.out.println("Condition 3: top1 > bottom2");
//        System.out.println("Condition 3 Value: " + (top1 > bottom2));
//
//        System.out.println("Condition 4: bottom1 < top2");
//        System.out.println("Condition 4 Value: " + (bottom1 < top2));


        return true; // Collision detected
    }

    public void handleCollision(Entity entity1, Entity entity2) {
        // Calculate overlap along x-axis and y-axis
        double overlapX = Math.min(entity1.posX + entity1.hitBox.width, entity2.posX + entity2.hitBox.width) - Math.max(entity1.posX, entity2.posX);
        double overlapY = Math.min(entity1.posY + entity1.hitBox.height, entity2.posY + entity2.hitBox.height) - Math.max(entity1.posY, entity2.posY);

        // Determine the axis of least overlap
        if (overlapX < overlapY) {
            // Resolve collision along x-axis
            double sign = Math.signum(entity1.velX - entity2.velX);
            double displacement = overlapX * sign / 2.0; // Half of the overlap
            entity1.posX -= displacement;
            entity2.posX += displacement;

            // Invert velocities to simulate bounce
            entity1.velX = -entity1.velX;
            entity2.velX = -entity2.velX;
        } else {
            // Resolve collision along y-axis
            double sign = Math.signum(entity1.velY - entity2.velY);
            double displacement = overlapY * sign / 2.0; // Half of the overlap
            entity1.posY -= displacement;
            entity2.posY += displacement;

            // Invert velocities to simulate bounce
            entity1.velY = -entity1.velY;
            entity2.velY = -entity2.velY;
        }
    }


    public boolean checkClip(Entity entity) {
        int entityLeftWorldX = entity.posX + entity.hitBox.x;
        int entityRightWorldX = entity.posX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.posY + entity.hitBox.y;
        int entityBottomWorldY = entity.posY + entity.hitBox.y + entity.hitBox.height;

        int entityLeftCol = entityLeftWorldX / TILESIZE;
        int entityRightCol = entityRightWorldX / TILESIZE;
        int entityTopRow = entityTopWorldY / TILESIZE;
        int entityBottomRow = entityBottomWorldY / TILESIZE;

        int entityHitBoxX = entity.hitBox.x;
        int entityHitBoxY = entity.hitBox.y;

        entity.hitBox.x += entity.posX;
        entity.hitBox.y += entity.posY;

        try {
            int[][] mapTiles = Tiles.getInstance().getMapTile();

            for (int col = entityLeftCol; col <= entityRightCol; col++) {
                for (int row = entityTopRow; row <= entityBottomRow; row++) {
                    Tile tile = Tiles.getInstance().getListTiles()[mapTiles[col][row]];

                    if (tile.hasCollision && entity.hitBox.intersects(
                            col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE)) {
                        return true;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Handle array index out of bounds exception
            entity.collideUp = true;
            entity.collideRight = true;
            entity.collideDown = true;
            entity.collideLeft = true;
            return true;
        } finally {
            entity.hitBox.x = entityHitBoxX;
            entity.hitBox.y = entityHitBoxY;
        }
        return false;
    }

    public void handleClip(Entity entity) {

    }
}
