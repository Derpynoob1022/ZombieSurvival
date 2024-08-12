package model.Items.Weapons.Ranged;

import model.Helper;
import model.Items.Projectiles.FireBall;

import java.awt.*;

import static ui.GamePanel.TILESIZE;
import static ui.GamePanel.getProjectiles;

public class MagicStaff extends RangedWeapon {
    public MagicStaff() {
        this.image = Helper.setup("objects/ice_bow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 10;
        this.maxAttackCooldownCount = 200;
        this.projSpeed = 15;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 2);
    }

    // TODO: use mana to shoot or smth
    @Override
    public void shoot(float posX, float posY, Double angle) {
        getProjectiles().add(new FireBall(posX, posY, angle, projSpeed, 2, 2 * TILESIZE, 360, true));
    }
}
