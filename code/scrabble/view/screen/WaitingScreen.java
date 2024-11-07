package scrabble.view.screen;

import javax.swing.*;
import java.awt.*;

/**
 * WaitingScreen represents the panel displayed while waiting for players to join the game.
 * It shows a list of player slots that can be updated with player names.
 */
public class WaitingScreen extends JPanel {

    // Array to store labels for each player
    private JLabel[] players;
    private int numPlayers = 0;

    /**
     * Constructor for WaitingScreen. Sets up the layout and adds components.
     */
    public WaitingScreen() {

        // Set layout for main panel
        this.setLayout(new BorderLayout());

        // Create a central panel with a titled border for the waiting screen
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Player Waiting Screen"));

        // Add player waiting list to the center panel
        centerPanel.add(setupPlayersWaiting());
        this.add(centerPanel, BorderLayout.CENTER);

    }

    /**
     * Initializes the panel that displays player slots and returns it.
     * This panel uses a GridLayout to list player slots vertically.
     *
     * @return JPanel containing labels for each player's slot.
     */
    private JPanel setupPlayersWaiting() {
        JPanel playersWaiting = new JPanel(new GridLayout(5,1,0,15));

        // Title label for the player list
        JLabel playerTitle = new JLabel("Players in the Game:", SwingConstants.CENTER);
        playersWaiting.add(playerTitle);

        // Initialize labels for player slots and add them to the panel
        players = new JLabel[4];
        for (int i = 0; i < players.length; i++) {
            players[i] = new JLabel("Waiting...", SwingConstants.CENTER);
            players[i].setBorder(BorderFactory.createEtchedBorder()); // Adds a border to each label
            playersWaiting.add(players[i]);
        }
        return playersWaiting;
    }

    public void addPlayerName(String name) {
        players[numPlayers].setText(name);
        numPlayers++;
    }

    /**
     * Getter for player labels.
     *
     * @return JLabel array of player slots.
     */
    public JLabel[] getPlayers() {
        return players;
    }
}
