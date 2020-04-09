package model;

import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents an individual member of the population.
 * @author Quentin Cld
 */
public class Ball {
    private static final double CHANCE_OF_DEATH = 0.06;

    int x, y, dx, dy, radius;
    double true_x, true_y, speed, direction;
    Color colour;
    State state;

    int timeInfected = 0;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }

    public Color getColour() {
        return colour;
    }

    public int getRadius() {
        return radius;
    }

    public State getState() {
        return state;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setState(State state) {
        this.state = state;
        this.colour = HealthColour.get(this.state);
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public Ball() {
        radius = 10;

        x = 50;
        y = 50;
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;
        speed = 1;
        direction = (int) (Math.random() * 360);

        state = State.HEALTHY;
        colour = HealthColour.get(state);
    }

    public Ball(int max_x, int max_y) {
        radius = 10;

        x = ThreadLocalRandom.current().nextInt(radius + 1, max_x - radius);
        y = ThreadLocalRandom.current().nextInt(radius + 1, max_y - radius);
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;
        speed = 1;
        direction = (int) (Math.random() * 360);

        state = State.HEALTHY;
        colour = HealthColour.get(state);
    }

    public Ball(int max_x, int max_y, State state) {
        radius = 10;

        x = ThreadLocalRandom.current().nextInt(radius + 1, max_x - radius);
        y = ThreadLocalRandom.current().nextInt(radius + 1, max_y - radius);
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;
        speed = 1;
        direction = (int) (Math.random() * 360);

        this.state = state;
        colour = HealthColour.get(state);
    }

    public Ball(int max_x, int max_y, float speed) {
        radius = 10;

        x = ThreadLocalRandom.current().nextInt(radius + 1, max_x - radius);
        y = ThreadLocalRandom.current().nextInt(radius + 1, max_y - radius);
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;
        this.speed = speed;
        direction = (int) (Math.random() * 360);;

        state = State.HEALTHY;
        colour = HealthColour.get(state);
    }

    /**
     * This function is called each step to compute the direction and the distance traveled
     * by an individual. It also checks for direction change.
     * @see Ball#changeDirection(int, int) 
     * @param x_limit The width of the canvas (representing the space available
     *                to the individuals).
     * @param y_limit The height of the canvas (representing the space available
     *                to the individuals).
     */
    public void move(int x_limit, int y_limit) {
        true_x = true_x + speed * Math.cos(direction * Math.PI / 180);
        true_y = true_y + speed * Math.sin(direction * Math.PI / 180);

        x = (int) true_x;
        y = (int) true_y;

        changeDirection(x_limit, y_limit);
    }

    /**
     * Checks if the individual reached a bound of the canvas, and changes the direction
     * accordingly.
     * @param x_limit The width of the canvas (representing the space available
     *                to the individuals).
     * @param y_limit The height of the canvas (representing the space available
     *                to the individuals).
     */
    private void changeDirection(int x_limit, int y_limit) {
        if (y < radius + 1) {
            if (dx == 1)
                direction -= direction * 2;
            else
                direction += (180 - direction) * 2;

            dx -= dx;
        }

        if (y > y_limit - radius - 1) {
            if (dx == 1)
                direction += (360 - direction) * 2;
            else
                direction -= (direction - 180) * 2;

            dx -= dx;
        }

        if (x < radius + 1) {
            if (dy == 1)
                direction += (270 - direction) * 2;
            else
                direction -= (direction - 90) * 2;

            dy -= dy;
        }

        if (x > x_limit - radius - 1) {
            if (dy == 1)
                direction -= (direction - 270) * 2;
            else
                direction += (90 - direction) * 2;

            dy -= dy;
        }
    }

    /**
     * Checks for a collision with another individual. The +1 is here to solve an edge case.
     * @param b2 The other individual.
     * @return True if there is a collision, false otherwise.
     */
    public boolean collideWith(Ball b2) {
        return (Math.sqrt(Math.pow(x - b2.getX(), 2) + Math.pow(y - b2.getY(), 2)) <= (radius / 2.0f) + (b2.getRadius() / 2.0f) + 1);
    }

    /**
     * This function is called each step, for each individual. It takes care of checking the state of the individual,
     * moving it and changing its state.
     * @see Ball#move(int, int)
     * @param x_limit The width of the canvas (representing the space available
     *                to the individuals).
     * @param y_limit The height of the canvas (representing the space available
     *                to the individuals).
     */
    public void update(int x_limit, int y_limit) {
        if (state == State.DEAD)
            return;

        move(x_limit, y_limit);

        if (state == State.INFECTED || state == State.DIAGNOSED) {
            timeInfected++;
            if (timeInfected == 250) {
                setState(State.DIAGNOSED);
            }

            if (timeInfected == 400) {
                if (Math.random() < CHANCE_OF_DEATH)
                    setState(State.DEAD);
            }

            if (timeInfected > 500) {
                setState(State.IMMUNE);
            }
        }
    }
}
