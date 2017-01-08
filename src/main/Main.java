package main;

import game.CarGame;
import game.Display;

/**
 * Created by TeTorro on 08.01.2017.
 */
public class Main {

    public static void main(String[] args) {
        CarGame carGame = new CarGame();
        Display display = new Display(carGame);
        display.start();
    }

}
