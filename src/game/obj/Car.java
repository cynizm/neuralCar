package game.obj;

import game.CarGame;
import game.Keyboard;
import game.Obj;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by TeTorro on 08.01.2017.
 */
public class Car extends Obj {

    public double vx, vy;
    public boolean accelerating;

    public Car(CarGame game) {
        super(game);
        setShape();
        visible = false;
    }

    private void setShape() {
        Polygon shipShape = new Polygon();
        shipShape.addPoint(-10, -5);
        shipShape.addPoint(10, -5);
        shipShape.addPoint(10, 5);
        shipShape.addPoint(-10, 5);
        shape = shipShape;
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
        if (Keyboard.keyDown[KeyEvent.VK_LEFT]) {
            angle -= 0.1;
        }
        else if (Keyboard.keyDown[KeyEvent.VK_RIGHT]) {
            angle += 0.1;
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
                game.hit();
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
        }
        else if (newState == CarGame.State.HITTED) {
            visible = false;
        }
        else if (newState == CarGame.State.GAME_OVER) {
            visible = false;
        }
    }

}
