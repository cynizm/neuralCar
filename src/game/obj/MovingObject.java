package game.obj;

import game.CarGame;
import game.Keyboard;
import game.Obj;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        accelerating = Keyboard.keyDown[KeyEvent.VK_UP];
        if (accelerating) {
            vx = round(4 * Math.cos(angle), 2);
            vy = round(4 * Math.sin(angle), 2);
        } else {
            vx = 0;
            vy = 0;
        }
        if (Keyboard.keyDown[KeyEvent.VK_LEFT] && accelerating) {
            angle -= 0.1;
            angle = angle % 360;
            angle = round(angle, 10);
        } else if (Keyboard.keyDown[KeyEvent.VK_RIGHT] && accelerating) {
            angle += 0.1;
            angle = angle % 360;
            angle = round(angle, 10);
        }

        vx = vx >= 2 ? 2 : vx;
        vy = vy >= 2 ? 2 : vy;
        x += vx;
        y += vy;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }
}
