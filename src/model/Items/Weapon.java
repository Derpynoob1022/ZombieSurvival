package model.Items;

public abstract class Weapon extends Item {
    protected int damage;
    protected int attackCoolDown;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAttackCoolDown() {
        return attackCoolDown;
    }

    public void setAttackCoolDown(int attackCoolDown) {
        this.attackCoolDown = attackCoolDown;
    }
}
