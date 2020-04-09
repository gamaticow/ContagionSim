package model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * This class manages the game (initialising, drawing, balls collision, state update, game update).
 * @author Quentin Cld
 */
public class Game {
    private final static float CONTAINMENT_MIN_PEOPLE_PERCENTAGE = 0.25f;
    private ArrayList<Ball> balls, deadBalls;
    private int width, height;
    private int iter = 0;
    private boolean containment = false;

    public Game(int width, int height) {
        balls = new ArrayList<>();
        deadBalls = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if there is no collision between an existing individual and the one being placed.
     * @param b The existing individual.
     * @return True if there's no collision, false otherwise.
     */
    private boolean canPlace(Ball b) {
        for (int j = 0; j < balls.size(); j++) {
            if (b.collideWith(balls.get(j))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Initialise the game (place individuals).
     */
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

    /**
     * Draws the individuals on the canvas, according to their colour and coordinates.
     * @see Ball
     * @param gc The graphics context used to draw the individuals.
     */
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

    /**
     * Makes a collision happen between two individuals. Each individual will bounce
     * towards the initial direction of the other one.
     * @param b1 The first individual.
     * @param b2 The second individual.
     */
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

    /**
     * Updates the state of the individuals when a collision happens.
     * @param b1 The first individual.
     * @param b2 The second individual.
     */
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

    /**
     * Checks if a collision occurred between some individuals. An ArrayList is used in order
     * to reduce the number of collision checking.
     */
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

    /**
     * Updates the game at each step.
     * @see Ball#update(int, int)
     * @param gc The graphics context used to draw the individuals.
     */
    public void update(GraphicsContext gc) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        int diagnosedCases = 0;

        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).update(width, height);

            if (balls.get(i).getState() == State.DEAD) {
                toRemove.add(i);
            }

            if (balls.get(i).getState() == State.DIAGNOSED ||
                balls.get(i).getState() == State.IMMUNE ||
                balls.get(i).getState() == State.DEAD) {

                diagnosedCases++;
            }
        }

        for (int idx: toRemove) {
            deadBalls.add(balls.get(idx));
            balls.remove(idx);
        }

        if (!containment && (float) diagnosedCases / (float)balls.size() > CONTAINMENT_MIN_PEOPLE_PERCENTAGE) {
            containment = true;
            for (Ball ball: balls) {
                ball.setSpeed(ball.getSpeed() / 2);
            }
        }


        checkForCollision();

        gc.clearRect(0, 0, width, height);
        drawBallsOnCanvas(gc);
        iter++;
    }
}
