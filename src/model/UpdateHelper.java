package model;

import static ui.GamePanel.ENTITIES;

public class UpdateHelper {

    public static void update() {

        boolean stable = false;
        int maxIterations = 10; // Set a maximum number of iterations to avoid infinite loops
        int currentIterations;

        for (Entity e : ENTITIES) {
            e.update();
        }

        for (currentIterations = 0; currentIterations < maxIterations && !stable; currentIterations++) {
            stable = true; // Assume positions are stable until proven otherwise

            for (int j = 0; j < ENTITIES.length; j++) {
                for (int k = j + 1; k < ENTITIES.length; k++) {
                    if (CollisionChecker.getInstance().checkStable(ENTITIES[j], ENTITIES[k])) {
                        CollisionChecker.getInstance().handleCollision(ENTITIES[j], ENTITIES[k]);
                    }
                    if (CollisionChecker.getInstance().checkClip(ENTITIES[j])) {
                        CollisionChecker.getInstance().handleClip(ENTITIES[j]);
                    }
                }
            }

            // Check if any entity's position changed during this iteration
            for (Entity entity : ENTITIES) {
                if (!entity.stable) {
                    stable = false;
                    break;
                }
            }
        }
    }
}
