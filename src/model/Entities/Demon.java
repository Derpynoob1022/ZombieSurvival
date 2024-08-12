package model.Entities;

import model.Helper;
import model.Items.Projectiles.FireBall;
import model.Items.Weapons.Ranged.MagicStaff;

import java.awt.*;

import static ui.GamePanel.TILESIZE;
import static ui.GamePanel.getProjectiles;

public class Demon extends RangedEnemy {

    public Demon(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle(12, 12, 40, 40);
        maxAcceleration = 0.2f;
        mass = 1;
        maxHealth = 10;
        health = maxHealth;
        damage = 2;
        xp = 500;

        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
        attackRadius = TILESIZE * 10;
        maxAttackCooldownCount = 250;
        scaledImage = Helper.setup("entity/enemy/demon0", TILESIZE, TILESIZE);
    }

    @Override
    public void dropLoot() {
        super.dropLoot();
        Helper.tryDropItem(0.1f, new MagicStaff(), posX, posY);
    }

    @Override
    public void update() {
        super.update();
    }

    public void attack() {
        float distX = Player.getInstance().getPosX() - posX;
        float distY = Player.getInstance().getPosY() - posY;

        double distance = Math.sqrt(distX * distX + distY * distY);

        if (distance < attackRadius) {
            double angle = Math.atan2(distY, distX);
            getProjectiles().add(new FireBall(posX, posY, angle, 20, damage, 3* TILESIZE, 180, false));
        }
    }
}
