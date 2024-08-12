package model.Entities;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class Goblin extends RangedEnemy {
    private int dashCount;
    private int dashDuration;
    private double dashAngle;
    private boolean isDashing;
    private int dashChargeCount;
    private int dashChargeDuration;

    public Goblin(int x, int y) {
        posX = x;
        posY = y;
        moveSpeed = 5;

        hitBox = new Rectangle(12, 12, 40, 40);
        maxAcceleration = 0.2f;
        mass = 1;
        maxHealth = 10;
        health = maxHealth;
        damage = 2;
        xp = 500;

        bounds = new Rectangle((int) posX + hitBox.x, (int) posY + hitBox.y, hitBox.width, hitBox.height);
        attackRadius = TILESIZE * 6;
        maxAttackCooldownCount = 250;
        dashCount = 0;
        dashChargeCount = 0;
        dashChargeDuration = 120;
        dashDuration = 10;
        isDashing = false;

        scaledImage = Helper.setup("entity/enemy/goblin0", TILESIZE, TILESIZE);
    }

    public void draw(Graphics2D g2) {
        Helper.draw(g2, scaledImage, (int) posX, (int) posY);

        int posXHealth = (int) posX;
        int posYHealth = (int) posY - 10;
        int healthLeft = TILESIZE * health / maxHealth;

        Helper.draw(g2, Color.red, posXHealth, posYHealth, TILESIZE, 5);

        if (health > 0) {
            Helper.draw(g2, Color.green, posXHealth, posYHealth, healthLeft, 5);
        }
    }

    @Override
    public void dropLoot() {
        super.dropLoot();
        // doesn't drop anything yet
    }

    @Override
    public void update() {
        if (isDashing) {
            if (dashChargeCount > dashChargeDuration) {
                dash();
            } else {
                dashChargeCount++;
                velX = 0;
                velY = 0;
                float distX = Player.getInstance().getPosX() - posX;
                float distY = Player.getInstance().getPosY() - posY;
                dashAngle = Math.atan2(distY, distX);
            }
        } else {
            super.update();
        }
    }

    public void attack() {
        float distX = Player.getInstance().getPosX() - posX;
        float distY = Player.getInstance().getPosY() - posY;

        double distance = Math.sqrt(distX * distX + distY * distY);

        if (distance < attackRadius) {
            isDashing = true;
            dashCount = 0;
            damage = damage * 2;
        }
    }

    public void dash() {
        if (dashCount < dashDuration) {
            double dashSpeed = moveSpeed * 5; // Adjust this multiplier as needed for dash speed
            // TODO: problem with executing without checking collision
            velX = (float) (dashSpeed * Math.cos(dashAngle));
            velY = (float) (dashSpeed * Math.sin(dashAngle));
            dashCount++;
        } else {
            isDashing = false;
            velX = (float) (moveSpeed * Math.cos(dashAngle));
            velY = (float) (moveSpeed * Math.sin(dashAngle));
            dashCount = 0;
            ableToAttack = false;
            damage = damage / 2;
            dashChargeCount = 0;
        }
    }
}
