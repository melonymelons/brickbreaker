package org.cis1200.brickbreaker;

import java.awt.Color;

public class UnbreakableBrick extends Brick {

    public UnbreakableBrick(Color color, boolean isVisible) {
        // call the constructor of the superclass, Brick
        super(color, isVisible);
    }

    public UnbreakableBrick() {
        super();
    }

    @Override
    public void hitByBall() {
        isVisible = true;
    }

}
