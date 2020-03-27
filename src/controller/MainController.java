package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import model.Game;
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

        Timeline updateGame = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                game.update(gc);
            }
        }));
        updateGame.setCycleCount(Timeline.INDEFINITE);
        updateGame.play();

        // ---------------------------------------------------------------------------------------------------
        // This way of updating seems to cause Java to think that he can dispose of some data,
        // see https://stackoverflow.com/questions/52918195/java-game-with-javafx-canvas-canvas-stops-updating
        // and https://stackoverflow.com/questions/9966136/javafx-periodic-background-task/9966213#9966213
        // ---------------------------------------------------------------------------------------------------

        /*Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                game.update(gc);
            }
        }, 0, 10);*/
    }
}
