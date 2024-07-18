package model.Items.Ranged;

import model.Handler.Helper;

import java.awt.*;

import static ui.GamePanel.TILESIZE;

public class Arrow extends Projectiles {
    private Double angle;
    // Item in the inventory
     public Arrow() {
         image = Helper.setup("objects/arrow", TILESIZE / 4, TILESIZE / 4);
         damage = 4;
     }

     // Item flying
     public Arrow(float x, float y, double angle, int projSpeed, int damage) {
         image = Helper.setup("objects/arrow", TILESIZE / 4, TILESIZE / 4);
         this.damage = 2 + damage;
         this.posX = x;
         this.posY = y;
         this.angle = angle;
         this.maxNumPierce = 4;
         this.velX = (float) (projSpeed * Math.cos(angle));
         this.velY = (float) (projSpeed * Math.sin(angle));

         this.hitBox = new Rectangle(0, 0, TILESIZE / 4, TILESIZE / 4);
         this.bounds = new Rectangle((int) posX, (int) posY, hitBox.width, hitBox.height);
     }

     public void execute() {
         posX += velX;
         posY += velY;

         bounds.x = (int) posX + hitBox.x;
         bounds.y = (int) posY + hitBox.y;
     }

     public void draw(Graphics2D g2) {
         Helper.draw(g2, image, posX, posY, angle);
     }
}
