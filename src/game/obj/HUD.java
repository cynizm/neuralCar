package game.obj;

import game.CarGame;
import game.Keyboard;
import game.Obj;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

/**
 * Created by TeTorro on 08.01.2017.
 */
public class HUD extends Obj {

    private Font font = new Font("Arial", Font.PLAIN, 30);

    private String creditText = "Created by Krystian Jaworek";
    private String startText = "PUSH SPACE TO START";
    private String gameOverText = "GAME OVER";

    private boolean titleState;
    private boolean gameOverState;


    public HUD(CarGame game) {
        super(game);
        createShipShape();
    }

    private void createShipShape() {
//        Polygon shipShape = new Polygon();
//        shipShape.addPoint(-5, -10);
//        shipShape.addPoint(5, -10);
//        shipShape.addPoint(5, 10);
//        shipShape.addPoint(-5, 10);
//        shape = shipShape;
    }

    @Override
    public void updateTitle() {
        if (Keyboard.keyDown[KeyEvent.VK_SPACE]) {
            game.start();
        }
    }

    @Override
    public void updateGameOver() {
            game.backToTitle();
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.setColor(Color.WHITE);
        g.setFont(font);

        // draw game over
        if (gameOverState) {
            int gameOverWidth = g.getFontMetrics().stringWidth(gameOverText);
            int gameOverX = (game.getWidth() - gameOverWidth) / 2;
            g.drawString(gameOverText, gameOverX, game.getHeight() / 2);
        }

        // draw credit
        if (titleState) {
            int creditWidth = g.getFontMetrics().stringWidth(creditText);
            int creditX = (game.getWidth() - creditWidth) / 2;
            g.drawString(creditText, creditX, game.getHeight() - 50);
        }

        // push space to start
        if (titleState) {
            int startWidth = g.getFontMetrics().stringWidth(startText);
            int startX = (game.getWidth() - startWidth) / 2;
            g.drawString(startText, startX, game.getHeight() / 2);
        }

        g.setTransform(at);
    }

    @Override
    public void StateChanged(CarGame.State newState) {
        titleState = newState == CarGame.State.TITLE;
        gameOverState = newState == CarGame.State.GAME_OVER;
    }

}
