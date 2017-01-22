package game.obj;

import game.CarGame;

import java.awt.*;

/**
 * Created by TeTorro on 15.01.2017.
 */
public class Sensor extends MovingObject {

    public double vx, vy;
    public boolean accelerating;
    public Color color;
    private int id;
    double lengh;
    private CarGame carGame;



    private boolean colision;

    public Sensor(CarGame game, int x, int y, int x2, int y2, int id) {
        super(game);
        setShape(x, y, x2, y2);
        visible = false;
        this.id = id;
        this.carGame = game;
    }

    public void setShape(int x, int y, int x2, int y2) {
        Polygon sensor = new Polygon();

        sensor.addPoint(x,y);
        sensor.addPoint(x2, y2);
        sensor.addPoint(x-1,y-1);

        shape = sensor;
    }

    @Override
    public void updatePlaying() {
        moveObject();

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
            angle = carGame.getCar().angle;
        }
        else if (newState == CarGame.State.HITTED) {
            visible = false;
        }
        else if (newState == CarGame.State.GAME_OVER) {
            visible = false;
        }
    }

    public boolean isColision() {
        return colision;
    }

    public void setColision(boolean colision) {
        this.colision = colision;
    }
}
