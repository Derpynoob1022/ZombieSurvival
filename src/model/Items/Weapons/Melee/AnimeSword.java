package model.Items.Weapons.Melee;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class AnimeSword extends Sword {
    public AnimeSword() {
        image = Helper.setup("objects/anime_sword", TILESIZE / 4, TILESIZE / 2);
        this.damage = 5;
        this.attackWidth = TILESIZE;
        this.attackHeight = 2 * TILESIZE;
        this.maxAttackCooldownCount = 50;
        this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 2);
    }
}
