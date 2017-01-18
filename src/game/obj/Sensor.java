package game.obj;

import game.CarGame;
import game.Keyboard;
import game.Obj;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by TeTorro on 15.01.2017.
 */
public class Sensor extends Obj {

    public double vx, vy;
    public boolean accelerating;
    public Color color;
    private int id;

    public Sensor(CarGame game, int x, int y, int id) {
        super(game);
        setShape(x, y);
        visible = false;
        this.id = id;
    }

    public void setShape(int x, int y) {
        Polygon carShape = new Polygon();

        carShape.addPoint(2+x, 2+y);
        carShape.addPoint(-2+x, 2+y);
        carShape.addPoint(-2+x, -2+y);
        carShape.addPoint(2+x, -2+y);

        shape = carShape;
    }

    @Override
    public void updatePlaying() {
        if (accelerating = Keyboard.keyDown[KeyEvent.VK_UP]) {
            vx = 4 * Math.cos(angle);
            vy = 4 * Math.sin(angle);
        } else {
            vx =0;
            vy =0;
        }
        if (Keyboard.keyDown[KeyEvent.VK_LEFT]&& accelerating) {
            angle -= 0.1;
            angle = angle % 360;
        }
        else if (Keyboard.keyDown[KeyEvent.VK_RIGHT] && accelerating) {
            angle += 0.1;
            angle = angle % 360;
        }

        vx = vx > 2 ? 2 : vx;
        vy = vy > 2 ? 2 : vy;
        x += vx;
        y += vy;

        if(x<10 || x>game.getWidth()-30 || y<10 || y>game.getHeight()-30){
            game.hit();
        }
        visible = true;
        Obstacle hittedObstacle = (Obstacle) game.checkCollision(this, Obstacle.class);
        if (hittedObstacle != null) {
            //game.hit();
            System.out.println("colision");
            visible=false;
            return;
        }
    }

    @Override
    public void StateChanged(CarGame.State newState) {
        if (newState == CarGame.State.PLAYING) {
            x = game.getWidth() / 2;
            y = game.getHeight() / 2;
            vx = vy = 0;
            angle = 0;
        }
        else if (newState == CarGame.State.HITTED) {
            visible = false;
        }
        else if (newState == CarGame.State.GAME_OVER) {
            visible = false;
        }
    }
}
