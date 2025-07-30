package org.cis1200.brickbreaker;

import org.junit.jupiter.api.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class BrickBreakerTest {
    @Test
    public void createBrick() {
        Brick brick = new Brick(Color.RED, true);

        assertEquals(Color.RED, brick.getColor());
        assertTrue(brick.isVisible());
        brick.setColor(Color.BLACK);
        assertEquals(Color.BLACK, brick.getColor());
        brick.setVisible(false);
        assertFalse(brick.isVisible());
    }

    @Test
    public void createBrick2() {

        // test with a different set of color and visibility
        Brick brick = new Brick(Color.GREEN, false);

        assertEquals(Color.GREEN, brick.getColor());
        assertFalse(brick.isVisible());
        brick.setColor(Color.WHITE);
        brick.setVisible(true);
        assertEquals(Color.WHITE, brick.getColor());
        brick.setVisible(true);
    }

    @Test
    public void createBrick3() {

        // test the edge case with exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> { 
                    Brick brick = new Brick(); });

        assertEquals("color or isVisible need to be set.", exception.getMessage());

    } // end of the function

    @Test
    public void createOuchBrick() {
        OuchBrick ouchBrick = new OuchBrick(Color.BLUE, false, true);

        assertEquals(Color.BLUE, ouchBrick.getColor());
        assertFalse(ouchBrick.isVisible());
        assertTrue(ouchBrick.isOuching());
    }

    @Test
    public void createOuchBrick2() {

        // test with a different set of arguments
        OuchBrick ouchBrick = new OuchBrick(Color.WHITE, true, false);

        assertEquals(Color.WHITE, ouchBrick.getColor());
        assertTrue(ouchBrick.isVisible());
        assertFalse(ouchBrick.isOuching());
    }

    @Test
    public void createOuchBrick3() {

        // test the edge case with exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> { 
                    OuchBrick brick = new OuchBrick(); });

        assertEquals("color or isVisible need to be set.", exception.getMessage());

    } // end of the function

    @Test
    public void createUnbreakableBrick() {
        UnbreakableBrick unbreakableBrick = new UnbreakableBrick(Color.LIGHT_GRAY, false);

        assertEquals(Color.LIGHT_GRAY, unbreakableBrick .getColor());
        assertFalse(unbreakableBrick .isVisible());
    }

    @Test
    public void createCounterBrick() {
        CounterBrick counterBrick = new CounterBrick(Color.WHITE, false, 3);

        assertEquals(Color.WHITE, counterBrick.getColor());
        assertFalse(counterBrick.isVisible());
        assertEquals(3, counterBrick.getCounter());
    }

    @Test
    public void createBricks() {

        // test a different set of rows and cols for the bricks
        Bricks iBricks = new Bricks(5, 7);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                Brick brick = iBricks.getBricks()[row][col];
                assertTrue(brick.isVisible());
                if (col == 2 || col == 3) {
                    if (brick instanceof OuchBrick) {
                        assertFalse(((OuchBrick) brick).isOuching());
                    }
                }
            } // end of inner for
        } // end of outer for

    } // end of function "createBricks"

    @Test
    public void createBricks2() {

        // test the edge case with exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> { 
                    Bricks iBricks = new Bricks(0, 0); });

        assertEquals("neither row nor col can be zero", exception.getMessage());

    } // end of the function

    @Test
    public void createGmeEngine() {

        // test the initial state of the GameEngine
        GameEngine gameEngine = new GameEngine();

        assertFalse(gameEngine.isPlay());
        assertTrue(gameEngine.isRestart());
        assertEquals(0, gameEngine.getScore());
        assertEquals(33, gameEngine.getTotalBricks());

    } // end of the function

    @Test
    public void createGmeEngine2() {

        // test the timer counter change
        GameEngine gameEngine = new GameEngine();
        gameEngine.actionPerformed(null);
        assertEquals(1, gameEngine.getTimerCounter());
        gameEngine.actionPerformed(null);
        assertEquals(2, gameEngine.getTimerCounter());
        gameEngine.actionPerformed(null);
        assertEquals(3, gameEngine.getTimerCounter());

    } // end of the function

    @Test
    public void createGmeEngine3() {
        // test the ball movement and locations
        GameEngine gameEngine = new GameEngine();
        gameEngine.setPlay(true);

        int ballX = gameEngine.getBallX();
        int ballY = gameEngine.getBallY();
        int ballDirX = gameEngine.getBallDirX();
        int ballDirY = gameEngine.getBallDirY();

        gameEngine.actionPerformed(null);
        assertEquals(ballX + ballDirX, gameEngine.getBallX());
        gameEngine.actionPerformed(null);
        assertEquals(ballX + 2 * ballDirX, gameEngine.getBallX());
        gameEngine.actionPerformed(null);
        assertEquals(ballX + 3 * ballDirX, gameEngine.getBallX());

    } // end of the function

 /* @Test
    public void createBricks() {
        Bricks iBricks = new Bricks(3, 3);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Brick brick = iBricks.getBricks()[row][col];
                assertTrue(brick.isVisible());
                if (col % 2 == 1) {
                    assertFalse(((OuchBrick) brick).isOuching());
                }
            } // end of inner for
        } // end of outer for

    } // end of function "createBricks"*/
} // end of the class