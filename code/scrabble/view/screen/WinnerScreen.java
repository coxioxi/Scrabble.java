package scrabble.view.screen;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.model.Player;

import javax.swing.*;
import java.awt.*;

/**
 * WinnerScreen displays the final standings of players at the end of the game,
 * showing player names and scores in order of rank.
 */
public class WinnerScreen extends JPanel {

    /**
     * Constructor for WinnerScreen. Sets up the layout with player rankings and a button to return to the main menu.
     *
     * @param players Array of Player objects, ordered by their final ranking.
     */
    public WinnerScreen(Player[] players) {
        // Set layout for the main panel
        this.setLayout(new BorderLayout());

        // Create a central panel with a titled border for the podium display
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Podium"));

        // Create a south panel for the "Return to Main Menu" button
        JPanel southPanel = new JPanel(new FlowLayout());

        // Create a panel to represent the podium with a vertical layout for player rankings
        JPanel podium = new JPanel(new GridLayout(4,1, 7, 10));

        // Labels to display each player's rank, name, and score
        JLabel firstPlaceName = new JLabel("1st: " + players[0].getName() + " | " + players[0].getScore());
        JLabel secondPlaceName = new JLabel("2nd: " + players[1].getName() + " | " + players[1].getScore());
        JLabel thirdPlaceName = new JLabel("3rd: " + players[2].getName() + " | " + players[2].getScore());
        JLabel fourthPlaceName = new JLabel("4th: " + players[3].getName() + " | " + players[3].getScore());

        // Button to return to the main menu
        JButton returnToMain = new JButton("Return to Main Menu");

        // Set borders for each ranking label to visually separate them
        firstPlaceName.setBorder(BorderFactory.createEtchedBorder());
        secondPlaceName.setBorder(BorderFactory.createEtchedBorder());
        thirdPlaceName.setBorder(BorderFactory.createEtchedBorder());
        fourthPlaceName.setBorder(BorderFactory.createEtchedBorder());

        // Add each ranking label to the podium panel in order
        podium.add(firstPlaceName);
        podium.add(secondPlaceName);
        podium.add(thirdPlaceName);
        podium.add(fourthPlaceName);

        // Add the podium to the center panel and set it to the center of the main layout
        centerPanel.add(podium);
        this.add(centerPanel, BorderLayout.CENTER);

        // Add the "Return to Main Menu" button to the south panel and position it at the bottom of the layout
        southPanel.add(returnToMain);
        this.add(southPanel, BorderLayout.SOUTH);
    }
}
