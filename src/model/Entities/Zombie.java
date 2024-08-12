package model.Entities;

import model.Helper;
import model.Items.Weapons.Melee.AnimeSword;
import model.Items.Weapons.Melee.GoldenSword;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class Zombie extends Enemy {
    // TODO: add attack cooldown to zombies so they dont insta break walls

    public Zombie(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 3;

        hitBox = new Rectangle(12, 12, 40, 40);
        maxAcceleration = (float) moveSpeed / 15;
        mass = 1;
        maxHealth = 10;
        health = maxHealth;
        damage = 2;
        xp = 100;

        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
        scaledImage = Helper.setup("entity/enemy/zombie0", TILESIZE, TILESIZE);
    }

    public void dropLoot() {
        super.dropLoot();

        Helper.tryDropItem(30, new GoldenSword(), posX, posY);

        Helper.tryDropItem(5, new AnimeSword(), posX, posY);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void attack() {
        // TODO: Implement attack for zombie, currently does damage by humping the player like collision damage
    }
}
