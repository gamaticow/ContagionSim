package model.game;

import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents an individual member of the population.
 * @author Quentin Cld
 */
public class Individual {
    /**
     * The chance of death for an individual that is infected with disease
     */
    public static float chanceToDie = 0.06f;

    /**
     * The default speed of an individual
     */
    public static int defaultSpeed = 1;

    /**
     * The default size of an individual
     */
    public static int defaultRadius = 10;

    /**
     * The time to diagnostic a individual
     */
    public static int timeToDiagnostic = 250;

    /**
     * The x position of the individual
     */
    int x;

    /**
     * The y position of the individual
     */
    int y;

    /**
     * The direction in x of the individual
     */
    int dx;

    /**
     * The direction in y of the individual
     */
    int dy;

    /**
     * The size of the individual
     */
    int radius;

    /**
     * The x position of the individual as a float
     */
    double true_x;

    /**
     * The y position of the individual as a float
     */
    double true_y;

    /**
     * The speed of the individual
     */
    double speed;

    /**
     * The direction of the individual, as an angle (0 - 360)
     */
    double direction;

    /**
     * The colour of the individual
     * @see State#getColor()
     */
    Color colour;

    /**
     * The state of the individual
     * @see State
     */
    State state;

    /**
     * The number of iteration the individual has been infected
     */
    int timeInfected = 0;

    /**
     * The type of the individual.
     */
    IndividualType type;

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

    public void setState(State state) {
        this.state = state;
        this.colour = this.state.getColor();
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public Individual(IndividualType type) {
        radius = defaultRadius;

        x = 50;
        y = 50;
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;

        this.type = type;
        speed = getSpeed(defaultSpeed);
        direction = (int) (Math.random() * 360);

        state = State.HEALTHY;
        colour = state.getColor();
    }

    public Individual(IndividualType type, int max_x, int max_y) {
        radius = defaultRadius;

        x = ThreadLocalRandom.current().nextInt(radius + 1, max_x - radius);
        y = ThreadLocalRandom.current().nextInt(radius + 1, max_y - radius);
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;

        this.type = type;
        speed = getSpeed(defaultSpeed);
        direction = (int) (Math.random() * 360);

        state = State.HEALTHY;
        colour = state.getColor();
    }

    public Individual(IndividualType type, int max_x, int max_y, State state) {
        radius = defaultRadius;

        x = ThreadLocalRandom.current().nextInt(radius + 1, max_x - radius);
        y = ThreadLocalRandom.current().nextInt(radius + 1, max_y - radius);
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;
        this.type = type;
        speed = getSpeed(defaultSpeed);
        direction = (int) (Math.random() * 360);

        this.state = state;
        colour = state.getColor();
    }

    public Individual(IndividualType type, int max_x, int max_y, float speed) {
        radius = defaultRadius;

        x = ThreadLocalRandom.current().nextInt(radius + 1, max_x - radius);
        y = ThreadLocalRandom.current().nextInt(radius + 1, max_y - radius);
        true_x = x;
        true_y = y;

        dx = Math.random() > 0.5? 1: -1;
        dy = Math.random() > 0.5? 1: -1;
        this.type = type;
        this.speed = getSpeed(speed);
        direction = (int) (Math.random() * 360);;

        state = State.HEALTHY;
        colour = state.getColor();
    }

    /**
     * This function is called each step to compute the direction and the distance traveled
     * by an individual. It also checks for direction change.
     * @see #changeDirection(int, int)
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
     * @param i2 The other individual.
     * @return True if there is a collision, false otherwise.
     */
    public boolean collideWith(Individual i2) {
        return (Math.sqrt(Math.pow(x - i2.getX(), 2) + Math.pow(y - i2.getY(), 2)) <= (radius / 2.0f) + (i2.getRadius() / 2.0f) + 1);
    }

    /**
     * This function is called each step, for each individual. It takes care of checking the state of the individual,
     * moving it and changing its state.
     * @see #move(int, int)
     * @param x_limit The width of the canvas (representing the space available
     *                to the individuals).
     * @param y_limit The height of the canvas (representing the space available
     *                to the individuals).
     */
    public void update(int x_limit, int y_limit, boolean containment) {
        if (state == State.DEAD)
            return;

        move(x_limit, y_limit);

        if (state == State.INFECTED || state == State.DIAGNOSED) {
            timeInfected++;
            if (timeInfected == timeToDiagnostic) {
                setState(State.DIAGNOSED);
            }

            if (timeInfected == timeToDiagnostic + 150) {
                if (Math.random() < chanceToDie*type.getMortality())
                    setState(State.DEAD);
            }

            if (timeInfected > timeToDiagnostic + 250) {
                setState(State.IMMUNE);
            }
        }

        if (containment && speed != 0.1 && (state == State.DIAGNOSED))
            speed = getSpeed(0.1);

        else if (containment && speed != 0.5 && state == State.IMMUNE)
            speed = getSpeed(0.5);

        else if (containment && speed > 0.5 && !(state == State.DEAD))
            speed = getSpeed(0.5);


    }


    /**
     * Randomize the speed and add the IndividualType speed modifier.
     * 70% to 130% of the speed.
     * @param of The speed to randomize.
     * @return The speed to use.
     */
    private double getSpeed(double of){
        return (of*(Math.random()*0.6+0.7))*type.getSpeed();
    }
}
