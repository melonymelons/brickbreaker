package org.cis1200.brickbreaker;

import java.awt.Color;

public class CounterBrick extends Brick {

    private int counter;

    public CounterBrick(Color color, boolean isVisible, int counter) {
        // call the constructor of the superclass, Brick
        super(color, isVisible);
        this.counter = counter;
    }

    public CounterBrick() {
        super();
        throw new IllegalArgumentException("color, isVisible, or counter need to be set.");
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public void hitByBall() {

        // decrease the counter by 1
        counter--;
        // if "counter != 0", set isVisible to true
        isVisible = (counter != 0);

    } // end of the function

} // end of the class
