package org.cis1200.brickbreaker;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
//import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/*
 * GameEngine extends JPanel and can listen to KeyEvent and ActionEvent
 */
public class GameEngine extends JPanel implements KeyListener, ActionListener  {
// player name
    private String playerName;
// create a Map object to store player scores
    private Map<String, Integer> playerScores = new TreeMap<>();
    private String fileName = "playerScores.txt";
    private BufferedReader br;
// set the total number of the bricks in the game
    private int bricksRows = 5;
    private int bricksCols = 7;
// keep track of total number of remaining bricks during the game
    private int totalBricks = bricksRows * bricksCols - 2;
    // set game status: true means "game got started"
    private boolean play = false;
// shoud the game start from scratch?
    private boolean restart = true;
// initialize the score a player receives so far
    private int score = 0;
// a timer object keeps issuing ActionEvent to 
// simulate the play of the game
    private Timer timer;
// set the timer delay, which controls the spead of the ball
    private int timerDelay = 11;
// a counter for the timer
    private int timerCounter = 0;
// retrieve window width and height
    private int windowWidth = RunBrickBreaker.WINDOW_WIDTH;
    private int windowHeight = RunBrickBreaker.WINDOW_HEIGHT;
// initialize the X and Y positions of top left corner of the brick wall
    private int wallX = 80;
    private int wallY = 80;
// set the border size
    private int borderSize = 5;
// set paddle width and height
    private int paddleWidth = 120;
    private int paddleHeight = 10;
// set the X position of the paddle
    private int paddleX = (windowWidth - paddleWidth) / 2;
    private int paddleY = windowHeight - 6 * paddleHeight;
// set the paddle speed
    private int paddleSpeed = 50;
// set the initial X and Y positions of the ball
    private int ballX = windowWidth / 2 + 60;
    private int ballY = windowHeight / 2 + 60;
// set the ball size
    private int ballSize = 30;
// set the ball speed
    private int ballSpeedX = 1;
    private int ballSPeedY = 2;
// set the initial movement direction of the ball
    private int ballDirX = ballSpeedX; // move to the right
    private int ballDirY = -ballSPeedY; // move up
// a Bricks object for drawing the bricks
    private Bricks iBricks;

    public GameEngine() {
        File file = new File(fileName);
// if the file exists
        if (file.exists()) {
            try {
                br = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = br.readLine()) != null) {
                    String [] fields = line.split(", ");
                    playerScores.put(fields[0], Integer.parseInt(fields[1]));
                }
                br.close();
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        } // end if 
// create a Bricks object with specified number of rows and cols
        iBricks = new Bricks(bricksRows, bricksCols);
// add the GameEngine as its own KeyListener
        addKeyListener(this);
// set the focus to the GameEnine
        setFocusable(true);
// disable the tab key to move focus away from the GameEngine
        setFocusTraversalKeysEnabled(false);
// create a timer object and add GameEngine as its ActionListener
        timer = new Timer(timerDelay, this);
// start the timer
        timer.start();
    }
// this function will be called by repaint() automatically.
    public void paintComponent(Graphics g) {
// call the paintComponent method in the super class
        super.paintComponent(g);
// set the background color to black
        g.setColor(Color.BLACK);
// fill the area in black
        g.fillRect(0, 0, windowWidth, windowHeight);
//draw the bricks
        iBricks.draw(g);
// display the instruction
// the middle of the screen vertically
        int middleY = windowHeight / 2;
        if (totalBricks == (bricksRows * bricksCols - 2) && !play) {
// set the color of the message
            g.setColor(Color.WHITE);
// set the font of the message
//g.setFont(new Font("serif", Font.BOLD, 22));
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
// display the restart message
            g.drawString("Press SpaceBar to Start/Pause/Resume", 120, middleY + 150);
            g.drawString("Use ← or → to move the paddle", 120, 
                middleY + 190);
        }
// draw the borders
// when game is in play or just gets started, draw blue borders
        if (play || totalBricks == (bricksRows * bricksCols - 2)) {
            drawBorders(g, Color.BLUE);
        } else {
// if you won, draw flashing green borders
            if (totalBricks <= 0) { 
                if (timerCounter < 50) {
                    drawBorders(g, Color.GREEN);
                }
            } else { 
// if you lost, draw flashing red borders
                if (timerCounter < 50) {
                    drawBorders(g, Color.RED);
                }
            }
        }
// set the color of the paddle
        g.setColor(Color.PINK); 
// draw the paddle in the bottom
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
// set the color of the ball
        g.setColor(Color.GREEN);
// draw the ball
        g.fillOval(ballX, ballY, ballSize, ballSize);
// display the score
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 26));
        g.drawString("Score: " + score, 560, 35);
// display the player's name
        if (playerName != null && !playerName.equals("")) {
            g.drawString("Player: " + playerName, 30, 35);
        }
/* Condition #1: totalBricks <= 0
* no more bricks: game is over and you won
* 
* Condition #2:  ballY + ballSize > paddleY + paddleHeight
* the ball falls below the bottom of the paddle
* game is over and you lose
*/
        if (totalBricks <= 0 || ballY + ballSize > paddleY + paddleHeight) {
            restart = true;
            displayMessage(g);
            boolean updateFile = false;
// When the game is over, save the player scores.
            if (playerName != null && playerScores.containsKey(playerName)) {
// if the playerName scored higher, update the score
                if (playerScores.get(playerName) < score) {
                    playerScores.put(playerName, score); 
                    updateFile = true;
                }
            } else { // save the first score for the playerName
// if the playerName is not null
                if (playerName != null) { 
                    playerScores.put(playerName, score);
                    updateFile = true;
                }
            }
            if (updateFile) { // if we need to update the file
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
// Iterate over the TreeMap using a for-each loop
                    for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
// only save the scores with a nonempty player name
                        if ((!entry.getKey().equals("")) && 
                                entry.getKey() != null) {
                            bw.write(entry.getKey() + ", " + entry.getValue() + '\n');
                        }
                    }
                    bw.close();
                } catch (IOException e) {
                    System.err.println("Error writing file: " + e.getMessage());
                }
            }
        } // end if for game over
// dispose the graphics context to save memory resources
        g.dispose();
    }

    public void drawBorders(Graphics g, Color color) {
// set the color of borders
        g.setColor(color);
// draw the top border
        g.fillRect(0, 0, windowWidth, 5);
// draw the left border
        g.fillRect(0, 0, 5, windowHeight);
// draw the right border
        g.fillRect(windowWidth - 5, 0, 5, windowHeight);
    }

    public void displayMessage(Graphics g) {
// game is over
        play = false;
// the ball cannot move anymore
        ballDirX = 0;
        ballDirY = 0;
// the middle of the screen vertically
        int middleY = (int)(windowHeight / 2);
// set the color of the instruction
        g.setColor(Color.WHITE);
// set the font of the instruction
//g.setFont(new Font("serif", Font.BOLD, 22));
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
// display the restart message
        g.drawString("Press SpaceBar to Start/Pause/Resume", 120, middleY + 100);
        g.drawString("Use ← or → to move the paddle", 120, 
            middleY + 140);
        if (totalBricks <= 0) { // you won
            g.setColor(Color.BLUE);
            g.drawString("Congratulations! You Won.", 120, middleY + 50);
        } else { // you lose
// set the color of losing message to red
            g.setColor(Color.RED);
            g.drawString("Sorry, you lost. Please try again.", 120, middleY + 50);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
// update the timerCounter for flashing the borders
        timerCounter++;
        if (timerCounter == 100) {
            timerCounter = 1;
        }
        if (play) {
// bounding box of the ball 
            Rectangle ballBox = new Rectangle(ballX, ballY, ballSize, ballSize);
// bounding box of the paddle
            Rectangle paddleBox = new Rectangle(paddleX, paddleY, paddleWidth, paddleHeight);
// if the ball hits the paddle
            if (ballBox.intersects(paddleBox)) {
// change the Y direction of the ball
                ballDirY = -ballDirY;
            }
// retrieve the bricks encapsulated in iBricks
            Brick [][] bricks = iBricks.getBricks();
            boolean ballHitaBreak = false;
            for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[0].length; j++) {
                    Brick brick = bricks[i][j];
// if the brick is still there
                    if (brick.isVisible()) {
                        int brickX = j * iBricks.getbrickWidth() + wallX;
                        int brickY = i * iBricks.getbrickHeight() + wallY;
                        int brickWidth = iBricks.getbrickWidth();
                        int brickHeight = iBricks.getbrickHeight();
// bounding box of the brick
                        Rectangle brickBox = new Rectangle(brickX, brickY, brickWidth, brickHeight);
// if the ball hits a brick
                        if (ballBox.intersects(brickBox)) {
                            ballHitaBreak = true;
// make the brick invisible
//bricks[i][j].setVisible(false);
                            brick.hitByBall();
// if the brick is NOT visible
                            if (!brick.isVisible()) {
                                score = score + 10;
// decrease the total number of bricks
                                totalBricks--;
                            }
//if the brick is of type OuchBrick
                            if (brick instanceof OuchBrick) {
                                ((OuchBrick)brick).setOuching(true);
                            }
//if the ball is outside the brick horizontally
                            if (ballX >= brickBox.x + brickBox.width - 1 ||
                                    ballX + ballSize - 1 <= brickBox.x) {
// the ball hits the bricks from horizontally
                                ballDirX = -ballDirX;
                            } else {
// otherwise, the ball hits the brick from
// either the top or bottom
                                ballDirY = -ballDirY;
                            } // end of else
                        } // end of 2nd if - intersects
                    } // end of 1st if - isVisible
// break out of the inner for loop
                    if (ballHitaBreak) {
                        break;
                    }
                } // end of inner for
// break out of the outer for loop
                if (ballHitaBreak) {
                    break;
                }
            } // end of outer for
// adjust the ball's X position
            ballX = ballX + ballDirX;
// adjust the ball's Y position
            ballY = ballY + ballDirY;
// if the ball is out of the left or right border
            if (ballX < borderSize ||
                    ballX + ballSize > RunBrickBreaker.WINDOW_WIDTH - borderSize) {
                ballDirX = -ballDirX;
            }
// if the ball is out of the top border
            if (ballY < borderSize) {
                ballDirY = -ballDirY;
            }
        }
        repaint();
    } // end of actionPerformed()

    @Override
    public void keyTyped(KeyEvent evt) {
// detect the press of SpaceBar
        if (evt.getKeyChar() == ' ') {
// if it is NOT in the play mode
            if (!play) {
                if (!restart) {
                    play = true;
                    return;
                }
// ask for player's name
                if (playerName == null) {
                    playerName = JOptionPane.showInputDialog(null, 
                        "Enter your name:");
                }
// if the user changed the name via the menu bar
                String playerMenuName = RunBrickBreaker.getPlayMenuName();
                if (playerMenuName != null && playerMenuName != playerName) {
                    playerName = playerMenuName;
                }
// set the play mode to true
                play = true;
// reset the timerCounter
                timerCounter = 0;
                ballX = windowWidth / 2 + 60;
                ballY = windowHeight / 2 + 60;
// move to the right or left randomly
                ballDirX = (Math.random() > 0.5) ? ballSpeedX : -ballSpeedX; 
                ballDirY = -ballSPeedY; // move up
                score = 0;
                totalBricks = bricksRows * bricksCols - 2;
                iBricks = new Bricks(bricksRows, bricksCols);
                repaint();
            } else {
                play = false;
                restart = false;
            }
        } // end if
    } // end of function

    @Override
    public void keyPressed(KeyEvent evt) {
// detect the press of right arrow key
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT && play) {
//if the right side of the paddle hits the right border
            if (paddleX + paddleWidth > windowWidth - borderSize) {
                paddleX = windowWidth - borderSize - paddleWidth;
            } else {
//play = true;
                paddleX = paddleX + paddleSpeed;
            }
        }
// detect the press of left arrow key
        if (evt.getKeyCode() == KeyEvent.VK_LEFT && play) {
//if the right side of the paddle hits the left border
            if (paddleX < borderSize) {
                paddleX = borderSize;
            } else {
//moveLeft();
                paddleX = paddleX - paddleSpeed;
            }
        } // end of if
    } // end of the function

// a KeyListener is required to override the function below
// even though it is not being used.
    @Override
    public void keyReleased(KeyEvent evt) { }

// save game state
    public void saveGameState() {
        String fileName = "gameState.txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
// save playerName and score
            bw.write("" + playerName + ", " + score + '\n');
// save X and Y coordinates of the ball
            bw.write("" + ballX + ", " + ballY + '\n');
// save the X and Y direction speed of the ball
            bw.write("" + ballDirX + ", " + ballDirY + '\n');
// save X and Y coordinates of the paddle
            bw.write("" + paddleX + ", " + paddleY + '\n');
            Brick[][] bricks = iBricks.getBricks();
            int row = bricks.length;
            int col = bricks[0].length;
            bw.write("" + row + ", " + col + '\n');
            for (int i = 0; i < row; i++) {
                String aRowOfBricks = null;
                for (int j = 0; j < col; j++) {
                    if (bricks[i][j].isVisible()) {
                        if (j == 0) {
                            aRowOfBricks = "1";
                        } else {
                            aRowOfBricks = aRowOfBricks + ", 1";
                        }
                    } else {
                        if (j == 0) {
                            aRowOfBricks = "0";
                        } else {
                            aRowOfBricks = aRowOfBricks + ", 0";
                        }
                    }
                } // end of inner for
                bw.write(aRowOfBricks + '\n');
            } // end of outer forBrikcs
// save the game state for two counter bricks
            String cbStr = "";
            if (bricks[0][1].isVisible()) {
                cbStr = "1";
            } else {
                cbStr = "0";
            }
            cbStr = cbStr + ", " + ((CounterBrick)bricks[0][1]).getCounter();
            bw.write(cbStr + '\n');
            if (bricks[0][5].isVisible()) {
                cbStr = "1";
            } else {
                cbStr = "0";
            }
            cbStr = cbStr + ", " + ((CounterBrick)bricks[0][5]).getCounter();
            bw.write(cbStr + '\n');
// close the file
            bw.close();
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    } // end of the function

// load game state
    public void loadGameState() {
        play = false;
        restart = false;
        String fileName = "gameState.txt";
        File file = new File(fileName);
// if the file exists
        if (file.exists()) {
            try {
                br = new BufferedReader(new FileReader(fileName));
                String line;
                String [] fields = null;
                if ((line = br.readLine()) != null) {
                    fields = line.split(", ");
                    playerName = fields[0];
                    score = Integer.parseInt(fields[1]);
                }
                if ((line = br.readLine()) != null) {
                    fields = line.split(", ");
                    ballX = Integer.parseInt(fields[0]);
                    ballY = Integer.parseInt(fields[1]);
                }
                if ((line = br.readLine()) != null) {
                    fields = line.split(", ");
                    ballDirX = Integer.parseInt(fields[0]);
                    ballDirY = Integer.parseInt(fields[1]);
                }
                if ((line = br.readLine()) != null) {
                    fields = line.split(", ");
                    paddleX = Integer.parseInt(fields[0]);
                    paddleY = Integer.parseInt(fields[1]);
                }
                int row = 0;
                int col = 0;
                if ((line = br.readLine()) != null) {
                    fields = line.split(", ");
                    row = Integer.parseInt(fields[0]);
                    col = Integer.parseInt(fields[1]);
                }
// restore the total number of bricks
// each brick carries 10 points
                totalBricks = row * col - score / 10 - 2;
                Brick[][] bricks = iBricks.getBricks();
// brick color
                Color brickColor = new Color(102, 51, 0);
// restore the visibility status of each brick
                for (int i = 0; i < row; i++) {
                    if  ((line = br.readLine()) != null) {
                        fields = line.split(", ");
                    }
                    for (int j = 0; j < col; j++) {
// initialze each brick in the 2D array: bricks
                        if (fields[j].equals("1")) {
                            if (j % 2 == 0) {
                                bricks[i][j] = new Brick(brickColor, true);
                            } else {
                                bricks[i][j] = new OuchBrick(brickColor, true, false);
                            }
                        } else {
                            if (j % 2 == 0) {
                                bricks[i][j] = new Brick(brickColor, false);
                            } else {
                                bricks[i][j] = new OuchBrick(brickColor, false, false);
                            }
                        }
                    } // end of inner for
                } // end of outer for
// set two of the bricks at the bottom row to unbreakable bricks
// define the color (RGB values) for an unbreakable brick
                Color unbreakableBrickColor = new Color(213, 207, 207);
                bricks[4][0] = new UnbreakableBrick(Color.DARK_GRAY, true);
                bricks[4][6] = new UnbreakableBrick(Color.DARK_GRAY, true);
// restore two counter bricks
                if ((line = br.readLine()) != null) {
                    fields = line.split(", ");
                }
                boolean isVisible;
                if (fields[0].equals("1")) {
                    isVisible = true;
                } else {
                    isVisible = false;
                }
                int counter;
                counter = Integer.parseInt(fields[1]);
// set two of the bricks at the top row to counter bricks
// set the counter to 3
                Color counterBrickColor = new Color(226, 114, 91);
                bricks[0][1] = new CounterBrick(counterBrickColor, isVisible, counter);
                if ((line = br.readLine()) != null) {
                    fields = line.split(", ");
                }
                if (fields[0].equals("1")) {
                    isVisible = true;
                } else {
                    isVisible = false;
                }
                counter = Integer.parseInt(fields[1]);
                bricks[0][5] = new CounterBrick(counterBrickColor, isVisible, counter);
                br.close();
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }
    } // end of the function
/**********************************************************
* The functions below are mainly for the JUnit testing of
* the game state
***********************************************************/
    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public boolean isRestart() {
        return restart;
    }

    public int getScore() {
        return score;
    }

    public int getTotalBricks() {
        return totalBricks;
    }

    public int getTimerCounter() {
        return timerCounter;
    }

    public int getBallX() { 
        return ballX; 
    }

    public int getBallY() { 
        return ballY; 
    }

    public int getBallDirX() { 
        return ballDirX; 
    }

    public int getBallDirY() { 
        return ballDirY; 
    }
} // end of the class
