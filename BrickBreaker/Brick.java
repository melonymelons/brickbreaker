package org.cis1200.brickbreaker;

import java.awt.Color;
//import java.awt.Rectangle;

/*
 * define the class "Brick" for each individual brick
 */
public class Brick {

    // color of the Brick
    private Color color;

    // is the Brick visible?
    // this field is "protected" so that subclasses can access it
    protected boolean isVisible  = true;

    Brick(Color color, boolean isVisible) {
        this.color = color;
        this.isVisible = isVisible;
    }

    Brick() {
        throw new IllegalArgumentException("color or isVisible need to be set.");
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void hitByBall() {
        isVisible = false;
    }

}
