package model.Items.Weapons.Ranged;

import model.Inventory.InventoryHandler;
import model.Items.Arrow;
import model.Items.Projectiles.FlyingArrow;
import model.Items.Weapons.Weapon;

import static ui.GamePanel.getProjectiles;

public abstract class RangedWeapon extends Weapon {
    protected int projSpeed;
    public void shoot(float posX, float posY, Double angle) {
        Arrow curArrow = InventoryHandler.getInstance().getArrow();
        if (curArrow != null) {
            getProjectiles().add(new FlyingArrow(posX, posY, angle, projSpeed, curArrow.getDamage() + damage, curArrow.getMaxNumPierce(), true));
        }
    }
}
