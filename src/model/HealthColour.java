package model;

import javafx.scene.paint.Color;

public class HealthColour {
    public static Color get(State state) {
        if (state == State.HEALTHY)
            return Color.CADETBLUE;
        else if (state == State.INFECTED)
            return Color.LAWNGREEN;
        else if (state == State.DIAGNOSED)
            return Color.DARKRED;
        else if (state == State.DEAD)
            return Color.BLACK;
        else if (state == State.IMMUNE)
            return Color.LIGHTPINK;

        return Color.WHITE;
    }
}
