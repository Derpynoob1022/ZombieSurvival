package model.Items.Weapons.Melee;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class GoldenSword extends Sword {
    public GoldenSword() {
        image = Helper.setup("objects/golden_sword", TILESIZE / 4, TILESIZE / 2);
        this.damage = 1;
        this.attackWidth = TILESIZE;
        this.attackHeight = 2 * TILESIZE;
        this.maxAttackCooldownCount = 20;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 2);
    }
}
