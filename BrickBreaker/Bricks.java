package org.cis1200.brickbreaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Font;

public class Bricks {
// a 2D array to hold bricks
    private Brick [][] bricks;
// initialize the X and Y positions of
// top left corner of the brick wall
    private int wallX = 80;
    private int wallY = 80;
// define the color (RGB values) for a brick
    private Color brickColor = new Color(102, 51, 0);
// define variables for brick width and height
    private final int brickWidth;
    private final int brickHeight;
  
    public Bricks(int row, int col) {
// detect illegal argument
        if (row == 0 || col == 0) {
            throw new IllegalArgumentException("neither row nor col can be zero");
        }
// calculate the brick width, leaving some space on both sides
        brickWidth = (int)(RunBrickBreaker.WINDOW_WIDTH - 160) / col;
// calculate the brick height, using one third of   
// the main window for the bricks
        brickHeight = (int)(RunBrickBreaker.WINDOW_HEIGHT / 3) / row;
        bricks = new Brick [row][col];
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
// initialize each brick in the 2D array: bricks
                if (j % 2 == 0) {
                    bricks[i][j] = new Brick(brickColor, true);
                } else {
                    bricks[i][j] = new OuchBrick(brickColor, true, false);
                }
            }
        }
// set two of the bricks at the bottom row to unbreakable bricks
// define the color (RGB values) for an unbreakable brick
        Color unbreakableBrickColor = new Color(213, 207, 207);
        bricks[4][0] = new UnbreakableBrick(Color.DARK_GRAY, true);
        bricks[4][6] = new UnbreakableBrick(Color.DARK_GRAY, true);
// set two of the bricks at the top row to counter bricks
// set the counter to 3
        Color counterBrickColor = new Color(226, 114, 91);
        bricks[0][1] = new CounterBrick(counterBrickColor, true, 3);
        bricks[0][5] = new CounterBrick(counterBrickColor, true, 3);
    } // end of the constructor

    public void draw(Graphics g) {
// draw the brick wall
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                Brick brick = bricks[i][j];
                if (brick.isVisible()) {
// set the brick color
                    g.setColor(brick.getColor());
// draw the brick
                    g.fillRect(wallX + j * brickWidth, 
                        wallY + i * brickHeight, brickWidth, brickHeight);
                    Color grayMortar = new Color(102, 102, 102); 
// set the color of a brick edge
                    g.setColor(grayMortar);
// set the line (edge) width of a brick
// "setStroke" is only defined for type "Graphics2D"
                    ((Graphics2D)g).setStroke(new BasicStroke(3));
// draw the edges of a brick
                    g.drawRect(wallX + j * brickWidth, 
                        wallY + i * brickHeight, brickWidth, brickHeight);
                } else if (brick instanceof OuchBrick && ((OuchBrick) brick).isOuching()) {
// display "ouch" message
                    g.setColor(Color.RED);
                    g.setFont(new Font("serif", Font.BOLD, 26));
                    g.drawString("Ouch!!!", 300, 35);
// only ouch once
                    ((OuchBrick)brick).setOuching(false);
                } 
            } // end inner for
        } // end outer for
    } // end draw

    public Brick [][] getBricks() {
        return bricks;
    }

    public int getbrickWidth() {
        return brickWidth;
    }

    public int getbrickHeight() {
        return brickHeight;
    }
} // end of class
