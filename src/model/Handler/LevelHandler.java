package model.Handler;

import model.Entities.*;

import static ui.GamePanel.TILESIZE;
import static ui.GamePanel.getEntities;

// Responsible for spawning new enemies
public class LevelHandler {
    private static LevelHandler levelHandler = new LevelHandler();
    private int wave;
    private int delay = 5; // Delay between each spawn to prevent lag

    private LevelHandler() {
        wave = 3;
    }

    public static LevelHandler getInstance() {
        return levelHandler;
    }

    public void spawn() {
        if (wave < 5) {
            spawnZombies(wave * 2, 1f, 1f, 1f);
        } else if (wave < 10) {
            spawnZombies(wave * 2, 1f, 1f, 1f);
        } else {

        }
    }

    public void addLevel() {
        wave++;
    }

    public int getWave() {
        return wave;
    }

    public void spawnZombies(int count, float scaleSpeed, float scaleDamage, float scaleHealth) {
        for (int i = 0; i < wave * 5; i++) {
            if (count > delay) {
                Zombie curZombie = new Zombie(((int) (Math.random() * 10)) * TILESIZE, ((int) (Math.random() * 10)) * TILESIZE);
                curZombie.scaleSpeed(scaleSpeed);
                curZombie.scaleDamage(scaleDamage);
                curZombie.scaleHealth(scaleHealth);
                getEntities().add(curZombie);
                count = 0;
            }
            count++;
        }
    }

    public void spawnSkeletons(int count, float scaleSpeed, float scaleDamage, float scaleHealth) {
        for (int i = 0; i < wave * 5; i++) {
            if (count > delay) {
                Skeleton curSkeleton = new Skeleton(((int) (Math.random() * 10)) * TILESIZE, ((int) (Math.random() * 10)) * TILESIZE);
                curSkeleton.scaleSpeed(scaleSpeed);
                curSkeleton.scaleDamage(scaleDamage);
                curSkeleton.scaleHealth(scaleHealth);
                getEntities().add(curSkeleton);
                count = 0;
            }
            count++;
        }
    }

    public void spawnDemons(int count, float scaleSpeed, float scaleDamage, float scaleHealth) {
        for (int i = 0; i < wave * 5; i++) {
            if (count > delay) {
                Demon curDemon = new Demon(((int) (Math.random() * 10)) * TILESIZE, ((int) (Math.random() * 10)) * TILESIZE);
                curDemon.scaleSpeed(scaleSpeed);
                curDemon.scaleDamage(scaleDamage);
                curDemon.scaleHealth(scaleHealth);
                getEntities().add(curDemon);
                count = 0;
            }
            count++;
        }
    }

    public void spawnGoblins(int count, float scaleSpeed, float scaleDamage, float scaleHealth) {
        for (int i = 0; i < wave * 5; i++) {
            if (count > delay) {
                Goblin curGoblin = new Goblin(((int) (Math.random() * 10)) * TILESIZE, ((int) (Math.random() * 10)) * TILESIZE);
                curGoblin.scaleSpeed(scaleSpeed);
                curGoblin.scaleDamage(scaleDamage);
                curGoblin.scaleHealth(scaleHealth);
                getEntities().add(curGoblin);
                count = 0;
            }
            count++;
        }
    }

    public void reset() {
        // TODO: Currently bugged, player move too fast and stuff
        wave = 0;
        Player.getInstance().reset();
        getEntities().add(Player.getInstance());
    }
}
