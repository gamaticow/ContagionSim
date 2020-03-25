package controller;

import Model.Game;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    @FXML
    private Canvas mainCanvas;

    public void runGame() {
        Game game = new Game((int)mainCanvas.getWidth(), (int)mainCanvas.getHeight());
        game.initialise();

        GraphicsContext gc = mainCanvas.getGraphicsContext2D();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                game.update(gc);
            }
        }, 0, 10);
    }
}
