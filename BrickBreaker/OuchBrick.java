package org.cis1200.brickbreaker;

import java.awt.Color;
//import java.awt.Rectangle;

/*
 * define the class "Brick" for each individual brick
 */
public class OuchBrick extends Brick {

    // add an extra field, ouching, to the inherited class, OuchBrick
    private boolean ouching = false;

    OuchBrick(Color color, boolean isVisible, boolean ouching) {
        // call the constructor of the superclass, Brick
        super(color, isVisible);

        this.ouching = ouching;
    }

    OuchBrick() {
        super();
        throw new IllegalArgumentException("color, isVisible, or ouching need to be set.");
    }

    // add two extra methods to the inherited class, OuchBrick
    public boolean isOuching() {
        return ouching;
    }

    public void setOuching(boolean ouching) {
        this.ouching = ouching;
    }

}
