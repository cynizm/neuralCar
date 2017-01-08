package game.obj;

import game.CarGame;
import game.Obj;

/**
 * Created by TeTorro on 08.01.2017.
 */
public class Initializer extends Obj {

    private long startTime;

    public Initializer(CarGame game) {
        super(game);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void updateInitializing() {
        if (System.currentTimeMillis() - startTime > 100) {
            game.setState(CarGame.State.TITLE);
        }
    }

}