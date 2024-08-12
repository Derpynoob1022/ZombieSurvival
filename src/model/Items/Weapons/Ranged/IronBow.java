package model.Items.Weapons.Ranged;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class IronBow extends RangedWeapon {
    public IronBow() {
        this.image = Helper.setup("objects/wooden_bow", TILESIZE / 4, TILESIZE / 2);
        this.damage = 4;
        this.maxAttackCooldownCount = 200;
        this.projSpeed = 10;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 2);
    }
}
