package model.game;

import javafx.scene.paint.Color;

/**
 * This enumeration is used to describe the current state of an individual.
 * @author Quentin Cld
 */
public enum State {
    HEALTHY     (Color.CADETBLUE),
    INFECTED    (Color.LAWNGREEN),
    DIAGNOSED   (Color.DARKRED),
    DEAD        (Color.BLACK),
    IMMUNE      (Color.LIGHTPINK);

    private final Color color;

    State(Color color){
        this.color = color;
    }

    /**
     * Returns the colour corresponding to the state of an individual.
     * @return The colour of the state.
     */
    public Color getColor(){
        return color;
    }
}
