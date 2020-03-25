package model;

import javafx.scene.paint.Color;

public class HealthColour {
    public static Color get(State state) {
        if (state == State.HEALTHY)
            return Color.CADETBLUE;
        else if (state == State.INFECTED)
            return Color.GREEN;
        else if (state == State.DEAD)
            return Color.BLACK;

        return Color.WHITE;
    }
}
