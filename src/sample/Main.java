package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    @FXML
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Ball ball = new Ball();

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ball.move();
                drawBallsOnCanvas(gc, ball);
            }
        }, 0, 500);
    }

    private void drawBallsOnCanvas(GraphicsContext gc, Ball ball) {
        gc.fillOval(ball.getX(), ball.getY(), 10, 10);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
