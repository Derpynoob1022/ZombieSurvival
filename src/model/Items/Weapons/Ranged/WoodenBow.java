package model.Items.Weapons.Ranged;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class WoodenBow extends RangedWeapon {
    public WoodenBow() {
        image = Helper.setup("objects/wooden_bow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 2;
        this.maxAttackCooldownCount = 70;
        projSpeed = 5;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 2);
    }
}
