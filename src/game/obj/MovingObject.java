package game.obj;

import game.CarGame;
import game.Keyboard;
import game.Obj;

import java.awt.event.KeyEvent;

/**
 * Created by TeTorro on 19.01.2017.
 */
public abstract class MovingObject extends Obj {

    public double vx, vy;
    public boolean accelerating;


    public MovingObject(CarGame game) {
        super(game);
    }

    public void moveObject(){
        accelerating = true;
        if (accelerating) {
            vx = 4*Math.cos(Math.toRadians(angle));
            vy = 4*Math.sin(Math.toRadians(angle));
            //System.out.println("Angle R: " +Math.toRadians(angle)  + " Angle: " + angle + " x: " + x + " y: " + y + " vx: " + vx + " vy: " + vy);
        } else {
            vx = 0;
            vy = 0;
        }
        if ((Keyboard.keyDown[KeyEvent.VK_LEFT] || game.getDecision().get(0)) && accelerating) {
            angle -=3;
        } else if ((Keyboard.keyDown[KeyEvent.VK_RIGHT] || game.getDecision().get(0)) && accelerating) {
            angle +=3;
        }
        System.out.println(game.getDecision().get(0));

        //vx = vx >= 2 ? 2 : vx;
       // vy = vy >= 2 ? 2 : vy;
        x+=vx;
        y+=vy;
    }
}
