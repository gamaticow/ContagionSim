package sample;

public class Ball {
    int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDirection() {
        return direction;
    }

    float speed;
    float direction;

    public Ball() {
        x = 50;
        y = 50;
        speed = 1;
        direction = 0;
    }

    public void move() {
        x = (int) (x + speed * Math.cos(direction * Math.PI / 180));
        y = (int) (y + speed * Math.sin(direction * Math.PI / 180));
    }
}
