package model;

import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class Ball {
    int x, y, dx, dy, radius;
    double true_x, true_y, speed, direction;
    Color colour;
    State state;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public void move(int x_limit, int y_limit) {
        true_x = true_x + speed * Math.cos(direction * Math.PI / 180);
        true_y = true_y + speed * Math.sin(direction * Math.PI / 180);

        x = (int) true_x;
        y = (int) true_y;

        changeDirection(x_limit, y_limit);
    }

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
}
