package model.Items.Ranged;

import model.Items.Weapon;

import static ui.GamePanel.getProjectiles;

public abstract class Bow extends Weapon {
    protected int projSpeed;
    public void shoot(float posX, float posY, Double angle) {
        getProjectiles().add(new Arrow(posX, posY, angle, projSpeed, damage));
    }
}
