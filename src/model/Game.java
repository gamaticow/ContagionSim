package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Game {
    ArrayList<Ball> balls, deadBalls;
    int width, height;
    int iter = 0;

    public Game(int width, int height) {
        balls = new ArrayList<>();
        deadBalls = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    private boolean canPlace(Ball b) {
        for (int j = 0; j < balls.size(); j++) {
            if (b.collideWith(balls.get(j))) {
                return false;
            }
        }

        return true;
    }

    public void initialise() {
        Ball ball;
        boolean canPlace = true;
        for (int i = 0; i < 99; i++) {
            ball = new Ball(width, height, State.HEALTHY);
            while (!canPlace(ball))
                ball = new Ball(width, height, State.HEALTHY);

            balls.add(ball);
        }

        ball = new Ball(width, height, State.INFECTED);
        while (!canPlace(ball)) {
            ball = new Ball(width, height, State.INFECTED);
        }
        balls.add(ball);
    }

    private void drawBallsOnCanvas(GraphicsContext gc) {
        for (Ball ball: deadBalls) {
            gc.setFill(ball.getColour());
            gc.fillOval(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius());
        }

        for (Ball ball: balls) {
            gc.setFill(ball.getColour());
            gc.fillOval(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius());
        }
    }

    private void makeCollision(Ball b1, Ball b2) {
        int tmp_dx, tmp_dy;
        double tmp_direction;

        tmp_dx = b1.getDx();
        tmp_dy = b1.getDy();
        tmp_direction = b1.getDirection();

        b1.setDx(b2.getDx());
        b1.setDy(b2.getDy());
        b1.setDirection(b2.getDirection());

        b2.setDx(tmp_dx);
        b2.setDy(tmp_dy);
        b2.setDirection(tmp_direction);
    }

    private void checkHealth(Ball b1, Ball b2) {
        if (b1.getState() == State.INFECTED && b2.getState() == State.INFECTED)
            return;
        if (b1.getState() == State.IMMUNE || b2.getState() == State.IMMUNE)
            return;

        if ((b1.getState() == State.INFECTED || b1.getState() == State.DIAGNOSED) && b2.getState() == State.HEALTHY) {
            b2.setState(State.INFECTED);
        }

        if ((b2.getState() == State.INFECTED || b2.getState() == State.DIAGNOSED) && b1.getState() == State.HEALTHY) {
            b1.setState(State.INFECTED);
        }

    }

    private void checkForCollision() {
        ArrayList<Integer> AlreadyDoneIndex = new ArrayList<>();

        for (int i = 0; i < balls.size(); i++) {
            if (!AlreadyDoneIndex.contains(i)) {
                for (int j = 0; j < balls.size(); j++) {
                    if (i != j) {
                        if (balls.get(i).collideWith(balls.get(j))) {
                            makeCollision(balls.get(i), balls.get(j));
                            checkHealth(balls.get(i), balls.get(j));
                            AlreadyDoneIndex.add(j);
                        }
                    }
                }
            }
        }
    }

    public void update(GraphicsContext gc) {
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).update(width, height);

            if (balls.get(i).getState() == State.DEAD) {
                toRemove.add(i);
            }
        }

        for (int idx: toRemove) {
            deadBalls.add(balls.get(idx));
            balls.remove(idx);
        }


        checkForCollision();

        gc.clearRect(0, 0, width, height);
        drawBallsOnCanvas(gc);
        iter++;
    }
}
