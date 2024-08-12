package model.Items.Weapons;

import model.Inventory.Item;

// Represents a weapon
public abstract class Weapon extends Item {
    protected int damage;
    protected int maxAttackCooldownCount;
    protected int currentAttackCount;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMaxAttackCooldownCount() {
        return maxAttackCooldownCount;
    }

    public void addCurrentAttackCount() {
        currentAttackCount++;
    }

    public int getCurrentAttackCount() {
        return currentAttackCount;
    }

    public void resetCurrentAttackCount() {
        currentAttackCount = 0;
    }
}
