package model.Entities;

import model.Helper;
import model.Items.Arrow;
import model.Items.Projectiles.FlyingArrow;
import model.Items.Weapons.Ranged.*;

import java.awt.*;

import static ui.GamePanel.TILESIZE;
import static ui.GamePanel.getProjectiles;

public class Skeleton extends RangedEnemy {

    public Skeleton(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle(12, 12, 40, 40);
        maxAcceleration = 0.2f;
        mass = 1;
        maxHealth = 10;
        health = maxHealth;
        damage = 1;
        xp = 200;

        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
        attackRadius = TILESIZE * 7;
        maxAttackCooldownCount = 100;
        scaledImage = Helper.setup("entity/enemy/skeleton0", TILESIZE, TILESIZE);
    }

    public void draw(Graphics2D g2) {
        Helper.draw(g2, scaledImage, (int) posX, (int) posY);

        int posXHealth = (int) posX;
        int posYHealth = (int) posY - 10;
        int healthLeft = TILESIZE * health / maxHealth;

        Helper.draw(g2, Color.red, posXHealth, posYHealth, TILESIZE, 5);

        if (health > 0) {
            Helper.draw(g2, Color.green, posXHealth, posYHealth, healthLeft, 5);
        }
    }

    @Override
    public void dropLoot() {
        super.dropLoot();

        Helper.tryDropItem(5, new WoodenBow(), posX, posY);

        Helper.tryDropItem(1, new IronBow(), posX, posY);

        Helper.tryDropItem(0.2f, new IceBow(), posX, posY);

        Helper.tryDropItem(0.8f, new Arrow(), posX, posY);
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
            getProjectiles().add(new FlyingArrow(posX, posY, angle, 25, damage, 1, false));
        }
    }
}
