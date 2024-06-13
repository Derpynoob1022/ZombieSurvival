package model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static ui.GamePanel.*;

public class AttackShape implements Shape {
    private Rectangle2D rectangle;
    private double angle; // Rotation angle in radians
    private AffineTransform transform;
    Helper helper = new Helper();

    public AttackShape(double x, double y, double width, double height, double angle) {
        this.rectangle = new Rectangle2D.Double(x, y, width, height);
        this.angle = angle;
        this.transform = new AffineTransform();
        updateTransform();
    }

    private void updateTransform() {
        // Desired reference point (e.g., left mid-point of the rectangle)
        double refX = rectangle.getMinX();
        double refY = rectangle.getCenterY();

        // Center of the top side of the rectangle
        double rectLeftX = rectangle.getMinX();
        double rectCenterY = rectangle.getCenterY();

        // Translate the rectangle so that the left mid-point is at (refX, refY)
        double translateX = refX - rectLeftX;
        double translateY = refY - rectCenterY;

        transform.setToIdentity();
        transform.translate(translateX, translateY);
        transform.rotate(angle, refX, refY);
    }

    public void draw(Graphics2D g2) {
        BufferedImage attack = helper.setup("objects/staff", (int) rectangle.getHeight(), (int) rectangle.getWidth());

        // Calculate center of the screen
        int centerX = SCREEN_WIDTH / 2;
        int centerY = SCREEN_HEIGHT / 2;

        // Calculate the bottom middle point of the image
        int imageWidth = attack.getWidth();
        int imageHeight = attack.getHeight();
        int bottomMiddleX = imageWidth / 2;
        int bottomMiddleY = imageHeight;

        // Create a new transform for drawing centered on the screen
        AffineTransform drawTransform = new AffineTransform();

        // Translate to the center of the screen first
        drawTransform.translate(centerX - bottomMiddleX, centerY - bottomMiddleY);

        // Rotate around the bottom middle point of the image
        drawTransform.rotate(angle + Math.PI / 2, bottomMiddleX, bottomMiddleY);

        // Translate the image so that the bottom middle point is at the origin
        // drawTransform.translate(-bottomMiddleX, -bottomMiddleY);

        // Draw the image using the transformed graphics context
        g2.drawImage(attack, drawTransform, null);
    }

    public void setAngle(double angle) {
        this.angle = angle;
        updateTransform();
    }

    @Override
    public Rectangle getBounds() {
        return transform.createTransformedShape(rectangle).getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return transform.createTransformedShape(rectangle).getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return transform.createTransformedShape(rectangle).contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return transform.createTransformedShape(rectangle).contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return transform.createTransformedShape(rectangle).intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return transform.createTransformedShape(rectangle).intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return transform.createTransformedShape(rectangle).contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return transform.createTransformedShape(rectangle).contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return transform.createTransformedShape(rectangle).getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return transform.createTransformedShape(rectangle).getPathIterator(at, flatness);
    }
}

