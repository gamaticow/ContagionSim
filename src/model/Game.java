package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Game {
    ArrayList<Ball> balls;
    int width, height;

    public Game(int width, int height) {
        balls = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public void initialise() {
        for (int i = 0; i < 50; i++)
            balls.add(new Ball(width, height));
    }

    private void drawBallsOnCanvas(GraphicsContext gc) {
        for (Ball ball: balls) {
            gc.setFill(ball.getColour());
            gc.fillOval(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius());
        }
    }

    public void update(GraphicsContext gc) {
        for (Ball ball: balls)
            ball.move(width, height);

        gc.clearRect(0, 0, width, height);
        drawBallsOnCanvas(gc);
    }
}
