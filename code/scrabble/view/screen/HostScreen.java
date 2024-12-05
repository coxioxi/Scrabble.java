package scrabble.view.screen;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * HostScreen is a panel that allows a player to host a game by setting up their name,
 * selecting game options, and viewing players who are waiting to join.
 */
public class HostScreen extends JPanel {
    public static final Color[] WAITING_PLAYER_LABEL_COLORS =
            new Color[] { Color.red.darker(), Color.cyan.darker(), Color.orange.darker(), Color.green.darker()};

    public static final String DICTIONARY_PATH = "code/dictionary.txt";
    public static final String DEFAULT_WAITING_TEXT = "  Waiting...  ";

    // Options for game time, player time, challenges, and dictionary selection
    public static final String[] gameTimeChoices =
            {"30 Minutes", "45 Minutes", "60 Minutes", "75 Minutes"};
    public static final String[] playerTimeChoices =
            {"2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
    public static final String[] challengeChoices =
            {"Disabled", "Enabled"};
    public static final String[] dictionaryChoices =
            {"Dictionary 1"};

    // Components for user input and display
    private JLabel[] players; // Labels for showing player slots
    private int numPlayers; // Counter for the number of players added
    private JComboBox<String> challengeBox; // Dropdown for challenge mode selection
    private JComboBox<String> dictionaryBox; // Dropdown for dictionary selection
    private JComboBox<String> playerTimeBox; // Dropdown for player time selection
    private JComboBox<String> gameTimeBox; // Dropdown for game time selection
    private JButton hostButton; // Button to start hosting the game
    private JLabel hostsIP; // Label to display the host's IP address
    private JLabel hostPort; // Label to display the port number


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
        hostButton.setFont(getFont().deriveFont(Font.PLAIN, 16f));

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
        JPanel centerContainer = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel centerPanel = setupBorderedPanel("Players Waiting");
        centerPanel.add(setupPlayersWaiting());
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 25;
        c.ipady = 25;
        centerContainer.add(centerPanel, c);
        this.add(centerContainer, BorderLayout.CENTER);
    }

    /**
     * Adds a player's name to the waiting list on the screen.
     *
     * @param name The name of the player to add.
     */
    public void addPlayerName(String name) {
        players[numPlayers].setText(name);
        players[numPlayers].revalidate();
        players[numPlayers].repaint();
        numPlayers++;
    }

    /**
     * Resets all player name slots to the default waiting text.
     */
    public void resetPlayerNames() {
        for (JLabel label : players) {
            label.setText(DEFAULT_WAITING_TEXT);
        }
        numPlayers = 0;
    }

    /**
     * Helper method to create a panel with a titled border and FlowLayout.
     *
     * @param title The title of the border.
     * @return JPanel with a titled border.
     */
    private JPanel setupBorderedPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title, TitledBorder.LEFT,
                TitledBorder.TOP, getFont().deriveFont(Font.PLAIN, 18f), Color.darkGray)
        );
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
        yourIP.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        hostsIP = new JLabel("**Host's IP**");
        hostsIP.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        JLabel port = new JLabel("Port:", SwingConstants.RIGHT);
        port.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        hostPort = new JLabel("**Port**");
        hostPort.setFont(getFont().deriveFont(Font.PLAIN, 18f));

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

        // Initialize each player slot with the default text and a border
        for (int i = 0; i < players.length; i++) {
            players[i] = new JLabel(DEFAULT_WAITING_TEXT, SwingConstants.CENTER);
            players[i].setFont(getFont().deriveFont(Font.PLAIN, 24f));
            players[i].setBorder(BorderFactory.createEtchedBorder());
            players[i].setBackground(WAITING_PLAYER_LABEL_COLORS[i]);
            players[i].setSize(50, 20);
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
        challengeLabel.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        challengeBox = new JComboBox<>(challengeChoices);
        challengeBox.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        challengeLabel.setEnabled(false);
        challengeBox.setEnabled(false);
        JLabel dictionaryLabel = new JLabel("Dictionary Used:", SwingConstants.RIGHT);
        dictionaryLabel.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        dictionaryBox = new JComboBox<>(dictionaryChoices);
        dictionaryBox.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        dictionaryBox.setEditable(false);
        JLabel playerTimeLabel = new JLabel("Player Time:", SwingConstants.RIGHT);
        playerTimeLabel.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        playerTimeBox = new JComboBox<>(playerTimeChoices);
        playerTimeBox.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        JLabel gameTimeLabel = new JLabel("Game Time:", SwingConstants.RIGHT);
        gameTimeLabel.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        gameTimeBox = new JComboBox<>(gameTimeChoices);
        gameTimeBox.setFont(getFont().deriveFont(Font.PLAIN, 18f));

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

    // Getters for UI components to allow external access
    public JLabel[] getPlayers() {
        return players;
    }

    public boolean getChallengeBox() {
        return challengeBox.getSelectedIndex() != 0;
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
