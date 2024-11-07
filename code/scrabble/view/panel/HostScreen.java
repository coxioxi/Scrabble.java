package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

/**
 * HostScreen is a panel that allows a player to host a game by setting up their name,
 * selecting game options, and viewing players who are waiting to join.
 */
public class HostScreen extends JPanel {

    public static final String DICTIONARY_PATH = "../../../dictionary.txt";

    // Options for game time, player time, challenges, and dictionary selection
    public static final String[] gameTimeChoices =
            {"30 Minutes", "45 Minutes", "60 Minutes", "75 Minutes"};
    public static final String[] playerTimeChoices =
            {"2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
    public static final String[] challengeChoices =
            {"Challenges on", "Challenges off"};
    public static final String[] dictionaryChoices =
            {"Dictionary 1"};

    // Components for user input and display
    private JLabel[] players;
    private int numPlayers;
    private JComboBox<String> challengeBox;
    private JComboBox<String> dictionaryBox;
    private JComboBox<String> playerTimeBox;
    private JComboBox<String> gameTimeBox;
    private JButton hostButton;
    private JLabel hostsIP;
    private JLabel hostPort;


    /**
     * Constructor for HostScreen. Sets up the layout with sections for player info,
     * game customizations, and a list of players who are waiting to join.
     */
    public HostScreen() {
        this.setLayout(new BorderLayout());

        // Main panel with a titled border
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Host A Game"));
        mainPanel.setLayout(null);

        // Input field for player's name and button to host the game
        hostButton = new JButton("Host");

        // Panel at the bottom for the host button
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(hostButton);
        this.add(southPanel, BorderLayout.SOUTH);

        // Panel at the top for entering the host name and IP information
        JPanel northPanel = setupBorderedPanel("Host A Game");
        northPanel.add(setupNameAndIP());
        this.add(northPanel, BorderLayout.NORTH);

        // Panel on the right for game customization options
        JPanel eastPanel = setupBorderedPanel("Customizations");
        eastPanel.add(setupCustomizations());
        this.add(eastPanel, BorderLayout.EAST);

        // Panel on the left to display players waiting to join
        JPanel westPanel = setupBorderedPanel("Players Waiting");
        westPanel.add(setupPlayersWaiting());
        this.add(westPanel, BorderLayout.WEST);
    }

    public void addPlayerName(String name) {
        players[numPlayers].setText(name);
        numPlayers++;
    }

    /**
     * Helper method to create a panel with a titled border and FlowLayout.
     *
     * @param title The title of the border.
     * @return JPanel with a titled border.
     */
    private JPanel setupBorderedPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    /**
     * Sets up a panel to display the player's name and the host's IP address.
     *
     * @return JPanel containing labels and text fields for name and IP.
     */
    private JPanel setupNameAndIP() {
        JPanel nameAndIP = new JPanel(new GridLayout(2,2,7,10));

        JLabel yourIP = new JLabel("Your IP Address:", SwingConstants.RIGHT);
        hostsIP = new JLabel("**Host's IP**");
        JLabel port = new JLabel("Port:", SwingConstants.RIGHT);
        hostPort = new JLabel("**Port**");

        nameAndIP.add(yourIP);
        nameAndIP.add(hostsIP);
        nameAndIP.add(port);
        nameAndIP.add(hostPort);
        return nameAndIP;
    }

    /**
     * Sets up a panel to display the list of players who are waiting to join the game.
     *
     * @return JPanel containing player name labels.
     */
    private JPanel setupPlayersWaiting() {
        JPanel playersWaiting = new JPanel(new GridLayout(4,1,0,10));
        players = new JLabel[4];

        for (int i = 0; i < players.length; i++) {
            players[i] = new JLabel("  Waiting...  ", SwingConstants.CENTER);
            players[i].setBorder(BorderFactory.createEtchedBorder());
            playersWaiting.add(players[i]);
        }
        return playersWaiting;
    }

    /**
     * Sets up a panel to allow the host to customize game settings, including challenges,
     * dictionary selection, player time, and game time.
     *
     * @return JPanel containing customization options as combo boxes.
     */
    private JPanel setupCustomizations() {
        JPanel customizations = new JPanel(new GridLayout(4,2, 7, 10));

        // Labels and combo boxes for each customization option
        JLabel challengeLabel = new JLabel("Challenges Allowed:", SwingConstants.RIGHT);
        challengeBox = new JComboBox<>(challengeChoices);
        JLabel dictionaryLabel = new JLabel("Dictionary Used:", SwingConstants.RIGHT);
        dictionaryBox = new JComboBox<>(dictionaryChoices);
        dictionaryBox.setEditable(false);
        JLabel playerTimeLabel = new JLabel("Player Time:", SwingConstants.RIGHT);
        playerTimeBox = new JComboBox<>(playerTimeChoices);
        JLabel gameTimeLabel = new JLabel("Game Time:", SwingConstants.RIGHT);
        gameTimeBox = new JComboBox<>(gameTimeChoices);

        // Adding labels and combo boxes to the panel
        customizations.add(challengeLabel);
        customizations.add(challengeBox);
        customizations.add(dictionaryLabel);
        customizations.add(dictionaryBox);
        customizations.add(playerTimeLabel);
        customizations.add(playerTimeBox);
        customizations.add(gameTimeLabel);
        customizations.add(gameTimeBox);
        return customizations;
    }

    // Getters for the components
    public JLabel[] getPlayers() {
        return players;
    }

    public boolean getChallengeBox() {
        return challengeBox.getSelectedIndex() == 0;
    }

    public String getDictionaryPath() {
        return DICTIONARY_PATH;
    }

    public String getPlayerTimeBox() { return playerTimeChoices[playerTimeBox.getSelectedIndex()]; }

    public String getGameTimeBox() { return gameTimeChoices[gameTimeBox.getSelectedIndex()]; }

    public JButton getHostButton() {
        return hostButton;
    }

    public JLabel getHostsIP() {
        return hostsIP;
    }

    public JLabel getHostPort() {
        return hostPort;
    }
}
