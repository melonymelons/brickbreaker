=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: helenluo
48441272
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays. I implemented a 2D array to organize and store the bricks in the
     BrickBreaker game. A 2D array is well-suited for this scenario, as it mirrors
     the row-by-row arrangement of the bricks.

  2. File I/O. I utilized File I/O to save the current game status, allowing players
     to pause the game and resume later. Additionally, I used File I/O to store the
     highest scores for all players.

  3. Inheritance and Subtyping. Based on the feedback from my proposal submission,
     I created a custom 'Brick' class and developed specialized bricks through the
     following subclasses.

     * UnbreakableBrick, which cannot be broken.
     * CounterBrick, which will be broken after a certain number of hits from the ball.
     * OuchBrick, which responds by saying 'Ouch!!!' when hit by the ball.

  4. JUnit Testable Component. After reviewing the feedback from my proposal submission,
     I chose to prioritize JUnit testing over Collections and/or Maps. However, I still
     utilized a Map to store the highest score for each player.


===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  * Brick class. It defines a brick with properties for color and visibility.
    In addition, it provides a method named "hitByBall()" to be overridden by
    subclasses, UnbreakableBrick and CounterBrick.

  * UnbreakableBrick class. It inherits from the Brick class and overrides
    the method "hitByBall()" in Brick.

  * CounterBrick class. It inherits from the Brick class and overrides
   the method "hitByBall()" in Brick.

  * OuchBrick class. It inherits from the Brick class and introduces a boolean
    variable named 'ouching' to determine whether a brick instantiated from this
    class should say 'Ouch!!!' when hit by the ball.

  * Bricks class. It defines a 2D array variable of type 'Brick' to store Brick
    objects. Additionally, it includes a 'draw' function that renders each brick
    on the screen based on its visibility.

  * GameEngine class. It inherits from JPanel and implements the KeyListener and
    ActionListener interfaces. It handles the main game logic and rendering, including
    the 'paintComponent' function for drawing.

    The 'paintComponent' function also displays the game's current status, including the
    running score, win/loss status, and more.

    The 'actionPerformed' function in this class handles the actions triggered by a timed
    event, including detecting collisions between bricks and the ball.

    The 'keyTyped' function detects when the Spacebar is pressed, which is used to start,
    pause, and resume the game.

    The 'keyPressed' function detects when the left and right arrow keys are pressed,
    controlling the movement of the paddle.

    The 'saveGameState' function saves the current game state.

    The 'loadGameState' function loads the saved game state into the game.

  * The RunBrickBreaker class implements Runnable and defines a run function.
    An object named game of this class is instantiated in Game.java, allowing
    the Brick Breaker game to be launched in the same manner as the two
    example games provided in the template.

    Additionally, I used a JDialog object with a JLabel to display a window containing
    the game instructions at the start. This instruction window can be reopened from
    the 'Player' -> 'About' menu. In both cases, the instruction window is opened by
    calling the function displayInstructionWindow.

    I also added two menus to the game's GUI. The 'Game' menu provides options to 'Save' and
    'Load' the game state, as well as an 'Exit' option. The 'Player' menu allows the user to
    view the 'Top Player(s)', 'Change Player,' and access the 'About' option to display the
    instruction window. The menu bar and menus are created using the displayMenuBar function.

    The listTopPlayers function generates a string, topPlayers, containing the highest-scoring
    player(s), along with the corresponding highestScore. These are then used by the
    displayTopPlayersWindow function, which opens the top player window.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  * I didn’t encounter any major obstacles while implementing the game. However,
    I did face a learning curve in gaining a deeper understanding of the inner workings
    of the Java Swing library. Fortunately, the book Introduction to Programming Using
    Java by Dr. David J. Eck was incredibly helpful in overcoming this challenge.

    Additionally, I found that adding extra features to a relatively large project can
    sometimes be challenging. For instance, after developing the game, I decided to use
    the Spacebar to start, pause, and resume the game. Initially, I was detecting the
    'Space' key in the keyPressed function of the GameEngine class. However, during debugging,
    I discovered that holding down a key generates multiple keyPressed events until the key is
    released. As a result, I decided to use the keyTyped function to detect pressing
    the Spacebar in the game.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  * Overall, I believe my design ensures a clear separation of functionality.
    The private state is well encapsulated, with access granted only through appropriate
    methods. Where possible, I declared fields and variables as final to make them immutable.

    As I gain more expertise in Java GUI design using the Swing library, I may find better ways
    to refactor and reorganize the code. However, given the time constraints, I did my best.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

* References:

  1. For Java Swing, I primarily relied on the lecture notes and the following book:

      Introduction to Programming Using Java (Version 9.0, Swing Edition)
      by Dr. David J. Eck

      https://math.hws.edu/javanotes-swing/

  2. For HTML, I consulted the following website:

      https://www.w3schools.com/html/

      I added the game instructions to a label using HTML format.