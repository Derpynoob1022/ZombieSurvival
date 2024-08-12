package model.Items.Weapons.Ranged;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class IceBow extends RangedWeapon {
    public IceBow() {
        this.image = Helper.setup("objects/ice_bow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 10;
        this.maxAttackCooldownCount = 20;
        this.projSpeed = 15;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 2);
    }

    // Shoots 3 arrows at the same time
    @Override
    public void shoot(float posX, float posY, Double angle) {
        super.shoot(posX, posY, angle);
        super.shoot(posX, posY, angle + Math.PI / 8);
        super.shoot(posX, posY, angle - Math.PI / 8);
    }
}
