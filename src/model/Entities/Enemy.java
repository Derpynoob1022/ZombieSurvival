package model.Entities;

import model.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public abstract class Enemy extends Entity {
    protected int damage;
    protected int attackCooldownCount;
    protected int maxAttackCooldownCount;
    protected boolean ableToAttack;

    // TODO: maybe refactor to player class
    public void handlePlayerCollision() {
        if (!Player.getInstance().invincible) {
            Player.getInstance().health -= damage;
            Player.getInstance().invincible = true;
        }
    }

    public void dropLoot() {
        Helper.tryDropCoin(80, posX, posY);
        Helper.tryDropHealth(50, posX, posY);
    }

    public int getDamage() {
        return damage;
    }


    public boolean getAbleToAttack() {
        return ableToAttack;
    }

    public void setAbleToAttack(boolean set) {
        ableToAttack = set;
    }

    // Updates the enemy velocity and attackCounter
    public void update() {
        // Currently only targeting the player. TODO: will add targeting defences and the base later
        float distX = Player.getInstance().posX - posX;
        float distY = Player.getInstance().posY - posY;

        double distance = Math.sqrt(distX * distX + distY * distY);

        double accX = 0;
        double accY = 0;

        if (distance != 0) {
            // Calculate normalized acceleration components
            double normalizedAccX = distX / distance;
            double normalizedAccY = distY / distance;

            // Apply acceleration magnitude
            accX = normalizedAccX * maxAcceleration;
            accY = normalizedAccY * maxAcceleration;
        }

        // Calculate desired velocity after acceleration
        double newVelX = velX + accX;
        double newVelY = velY + accY;

        // Calculate current speed
        double currentSpeed = Math.sqrt(newVelX * newVelX + newVelY * newVelY);

        // Limit velocity to moveSpeed if it exceeds the maximum speed
        if (currentSpeed > moveSpeed) {
            double ratio = moveSpeed / currentSpeed;
            newVelX *= ratio;
            newVelY *= ratio;
        }

        // Apply the new velocity
        velX = (float) newVelX;
        velY = (float) newVelY;

        bounds.x = (int) (posX + hitBox.x + velX);
        bounds.y = (int) (posY + hitBox.y + velY);

        if (invincible) {
            iFrames++;
            if (iFrames > 10) {
                invincible = false;
                iFrames = 0;
            }
        }

        if (ableToAttack) {
            attack();
            ableToAttack = false;
        } else {
            attackCooldownCount++;
            if (attackCooldownCount > maxAttackCooldownCount) {
                ableToAttack = true;
                attackCooldownCount = 0;
            }
        }
    }

    @Override
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

    public void scaleSpeed(float scaleFactor) {
        this.moveSpeed *= scaleFactor;
        this.maxAcceleration *= scaleFactor;
//        System.out.println(moveSpeed);
//        System.out.println(maxAcceleration);
    }

    public abstract void attack();

    public void scaleDamage(float scaleFactor) {
        this.damage *= scaleFactor;
    }

    public void scaleHealth(float scaleFactor) {
        this.health *= scaleFactor;
    }
}
