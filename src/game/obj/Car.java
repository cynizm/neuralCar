package game.obj;

import game.CarGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TeTorro on 08.01.2017.
 */
public class Car extends MovingObject {

    public Car(CarGame game) {
        super(game);
        setShape();
        this.game = game;
        //setSensors()
        visible = false;
        //sensors = new ArrayList<>();
    }

    private void setSensors() {
        List<Sensor>carSensors = new ArrayList<>();
        //carSensors.add();
    }

    private void setShape() {
        Polygon carShape = new Polygon();
        carShape.addPoint(-20, -10);
        carShape.addPoint(20, -10);
        carShape.addPoint(20, 10);
        carShape.addPoint(-20, 10);
        shape = carShape;
    }


    @Override
    public void updatePlaying() {
        moveObject();
        angle=game.getCar().angle;
        if(x<10 || x>game.getWidth()-30 || y<10 || y>game.getHeight()-30){
            game.hit();
        }
            visible = true;
            Obstacle hittedObstacle = (Obstacle) game.checkCollision(this, Obstacle.class);
            if (hittedObstacle != null) {
                //game.hit();
                //visible=false;
                return;
            }
    }

    @Override
    public void StateChanged(CarGame.State newState) {
        if (newState == CarGame.State.PLAYING) {
            x = game.getWidth() / 2;
            y = game.getHeight() / 2;
            vx = vy = 0;
            angle=0;
        }
        else if (newState == CarGame.State.HITTED) {
            visible = false;
        }
        else if (newState == CarGame.State.GAME_OVER) {
            visible = false;
        }
    }

}
