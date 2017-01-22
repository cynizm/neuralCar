package game.obj;

import game.CarGame;
import game.Obj;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by TeTorro on 08.01.2017.
 */
public class Obstacle extends Obj {

    public double vx, vy, va;
    public int size;
    public int halfSize;

    // size = 1 small, 2 medium, 3 large
    public Obstacle(CarGame game, int x, int y, int size) {
        super(game);
        this.x = x;
        this.y = y;
        this.size = size;
        this.halfSize = size * 10;
        angle = (int)((2 * Math.PI) * Math.random());
        double v = 0.5 + 1 * Math.random();
        vx = (4 - size) * Math.cos(angle) * v;
        vy = (4 - size) * Math.sin(angle) * v;
        va = 0.01 + 0.05 * Math.random();
        setShape();
        generateRandomShape();
    }

    private void setShape() {
        Ellipse2D obstacleShape = new Ellipse2D.Double(-halfSize, -halfSize, 2 * halfSize, 2 * halfSize);
        //Rectangle.Double obstacleShape = new Rectangle.Double(-halfSize, -halfSize, 2 * halfSize, 2 * halfSize);
        shape = obstacleShape;
    }

    private void generateRandomShape() {
        Polygon randomObstacleShape = new Polygon();
        int f = 5 + (int) (5 * Math.random());
        double da = (2 * Math.PI) / f;
        double a = (2 * Math.PI) * Math.random();
        for (int i = 0; i < f; i++) {
            double ad = halfSize + halfSize * Math.random();
            double ax = ad * Math.cos(a);
            double ay = ad * Math.sin(a);
            randomObstacleShape.addPoint((int) ax, (int) ay);
            a += da;
        }
        shape = randomObstacleShape;
    }

    @Override
    public void update() {
        angle += va;

        x += vx;
        y += vy;

        x = x < -halfSize ? game.getWidth() : x;
        x = x > game.getWidth() + halfSize ? -halfSize : x;
        y = y < -halfSize ? game.getHeight() : y;
        y = y > game.getHeight() + halfSize ? -halfSize : y;
    }
}

