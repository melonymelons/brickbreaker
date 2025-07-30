package org.cis1200.brickbreaker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;

public class RunBrickBreaker implements Runnable {
    // define window width and height
    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 690;

    // define initial position of window's upper left corner
    public static final int LEFT_X = 350;
    public static final int LEFT_Y = 100;

    // for the change player menu
    private static String playMenuName;

    // pause the game?
    private static boolean pause = false;

    // top players
    private static String topPlayers;
    private static int highestScore;

    public void run() {

        // create the window object
        JFrame window = new JFrame();
        // create the game engine object, which is an JPanel object
        GameEngine gameEngine = new GameEngine();
        // set the window position and its width & height
        window.setBounds(LEFT_X, LEFT_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        // set the title of the window
        window.setTitle("Brick Breaker");
        // add the game engine into the window
        window.add(gameEngine);
        // make the window unresizable
        window.setResizable(false);
        // window will close upon exit
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // display the menu bar
        displayMenuBar(window, gameEngine);

        // show the window on the screen
        window.setVisible(true);

        // display the instruction window
        displayInstructionWindow(window);

    }

    public static void displayInstructionWindow(JFrame window) {
        // Create the popup window as an object of JDialog
        JDialog dialog = new JDialog(window, "Game Instructions", true);
        // set its size
        dialog.setSize(400, 280);
        // Center the dialog on the window
        dialog.setLocationRelativeTo(window);

        // add game instruction to the label in html format
        // Reference: https://www.w3schools.com/html/

        JLabel label = new JLabel("<html>  <div style=\"text-align: center\">" +
                "<mark><b><p style=\"font-size:22px\">Brick Breaker Game</p></b></mark>" +
                "<p style=\"text-align: left;\"> <br> Brick Breaker is a classic arcade-style" + 
                " game where the player controls " +
                "a paddle to bounce a ball and break bricks on the screen. The primary goal " +
                "is to destroy all the bricks without letting the ball fall past the paddle. " +
                "Players score points by breaking bricks.</p>" +
                "<br><br><p style=\"color:red;\"><b>Press SpaceBar to Start/Pause/Resume</b></p> " +
                "<br><p style=\"color:blue;\"><b>Use ← or → to move the paddle.</b>" +
                "</p> </div> </html>");

        // set label's size and location
        label.setPreferredSize(new Dimension(350, 260));
        label.setLocation(30, 6);

        // put the label in the center
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        // add the label to the dialog window
        dialog.add(label);

        // make the dialog window unresizable
        dialog.setResizable(false);

        // Show the dialog
        dialog.setVisible(true);

    } // end of the function

    public static void displayMenuBar(JFrame window, GameEngine gameEngine) {
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create the player menu
        JMenu playMenu = new JMenu("Player");
        JMenuItem rankingMenuItem = new JMenuItem("Top Player(s)");
        JMenuItem changeMenuItem = new JMenuItem("Change Player");
        JMenuItem aboutMenuItem = new JMenuItem("About");

        // Add action listeners to menu items
        rankingMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTopPlayersWindow(window);
            }
        });

        changeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playMenuName = JOptionPane.showInputDialog(null, "Enter your name:");
            }
        });

        aboutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayInstructionWindow(window);
            }
        });


        // Add menu items to the player menu
        playMenu.add(rankingMenuItem);
        playMenu.add(changeMenuItem);
        playMenu.addSeparator(); // Add a separator line
        playMenu.add(aboutMenuItem);

        // Create the about menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        // add ActionListener
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameEngine.saveGameState();
            }
        });

        loadMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameEngine.loadGameState();
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // add the menu item to the menu
        gameMenu.add(saveMenuItem);
        gameMenu.add(loadMenuItem);
        gameMenu.addSeparator(); // Add a separator line
        gameMenu.add(exitMenuItem);

        // Add the player and about menus to the menu bar
        menuBar.add(gameMenu);
        menuBar.add(playMenu);
        menuBar.setVisible(true);

        // Set the menu bar for the window
        window.setJMenuBar(menuBar);

    } // end of the function

    public static void listTopPlayers() {
        String fileName = "playerScores.txt";
        BufferedReader br;
        TreeMap<String, Integer> playerScores = new TreeMap<String, Integer>();

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
                topPlayers = null;
                return;
            }
        } // end if

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
                topPlayers = maxEntry.getKey();
            } else if (entry.getValue().compareTo(maxEntry.getValue()) == 0) {
                topPlayers = topPlayers + ", " + entry.getKey();
            } // end of if
        } // end of for

        if (maxEntry != null) {
            highestScore = maxEntry.getValue();
        }

    } // end of the function, topPlayers

    public static void displayTopPlayersWindow(JFrame window) {
        // Create the popup window as an object of JDialog
        JDialog dialog = new JDialog(window, "Top Player(s)", true);
        // set its size
        dialog.setSize(400, 280);
        // Center the dialog on the window
        dialog.setLocationRelativeTo(window);

        // add game instruction to the label in html format
        // Reference: https://www.w3schools.com/html/

        listTopPlayers();

        String topPlayerInfo = null;

        // if top players exist
        if (topPlayers != null && highestScore != 0) {
            topPlayerInfo = topPlayers + " with the highest score of " + highestScore;
        } else {
            topPlayerInfo = "A top player does NOT exist yet.";
        }

        // add top player information to the label in html format
        // Reference: https://www.w3schools.com/html/
        JLabel label = new JLabel("<html> <div style=\"text-align: center\">" +
                "<mark><b><p style=\"font-size:22px\">Top Player(s)</p></b></mark>" +
                "<br><br><p style=\"font-size:18px;color:red;\"><b>" +
                topPlayerInfo + "</b></p> " + "</div> </html>");

        // set label's size and location
        label.setPreferredSize(new Dimension(350, 260));
        label.setLocation(30, 6);

        // put the label in the center
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        // add the label to the dialog window
        dialog.add(label);

        // make the dialog window unresizable
        dialog.setResizable(false);

        // Show the dialog
        dialog.setVisible(true);

    } // end of the function

    public static String getPlayMenuName() {
        return playMenuName;
    }

    public boolean getPause() {
        return pause;
    }

    public String getTopPlayers() {
        return topPlayers;
    }

    public int getHighestScore() {
        return highestScore;
    }
} // end of the class