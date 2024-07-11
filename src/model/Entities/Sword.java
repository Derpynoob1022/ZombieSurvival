package model.Entities;

import model.Items.Item;

public class Sword extends Item {
    protected int damage;
    protected int attackWidth;
    protected int attackHeight;

    public int getDamage() {
        return damage;
    }

    public int getAttackWidth() {
        return attackWidth;
    }

    public int getAttackHeight() {
        return attackHeight;
    }
}
