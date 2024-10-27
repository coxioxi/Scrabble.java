package scrabble.view;

import javax.swing.*;
import java.awt.*;

public class HostScreen extends JPanel {

    public static final String[] gameTimeChoices =
            {"30 Minutes", "45 Minutes", "60 Minutes", "75 Minutes"};
    public static final String[] playerTimeChoices =
            {"2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
    public static final String[] challengeChoices =
            {"Challenges on", "Challenges off"};
    public static final String[] dictionaryChoices =
            {"Dictionary 1", "Dictionary 2"};

    private JTextField name;
    private JLabel[] players;
    private JComboBox<String> challengeBox;
    private JComboBox<String> dictionaryBox;
    private JComboBox<String> playerTimeBox;
    private JComboBox<String> gameTimeBox;
    private JButton hostButton;

    public HostScreen() {
        this.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Host A Game"));
        mainPanel.setLayout(null);

        name = new JTextField();
        hostButton = new JButton("Host");

        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(hostButton);
        this.add(southPanel, BorderLayout.SOUTH);

        JPanel northPanel = setupBorderedPanel("Host A Game");
        northPanel.add(setupNameAndIP());
        this.add(northPanel, BorderLayout.NORTH);

        JPanel eastPanel = setupBorderedPanel("Customizations");
        eastPanel.add(setupCustomizations());
        this.add(eastPanel, BorderLayout.EAST);

        JPanel westPanel = setupBorderedPanel("Players Waiting");
        westPanel.add(setupPlayersWaiting());
        this.add(westPanel, BorderLayout.WEST);
    }

    private JPanel setupBorderedPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    private JPanel setupNameAndIP() {
        JPanel nameAndIP = new JPanel(new GridLayout(2,2,7,10));

        JLabel yourIP = new JLabel("Your IP Address:", SwingConstants.RIGHT);
        JLabel hostsIP = new JLabel("**Host's IP**");
        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);

        nameAndIP.add(yourIP);
        nameAndIP.add(hostsIP);
        nameAndIP.add(nameLabel);
        nameAndIP.add(name);
        return nameAndIP;
    }

    private JPanel setupPlayersWaiting() {
        JPanel playersWaiting = new JPanel(new GridLayout(4,1,0,10));
        players = new JLabel[4];

        for (int i = 0; i < players.length; i++) {
            players[i] = new JLabel("**Player "+(i+1)+ " Name**", SwingConstants.CENTER);
            players[i].setBorder(BorderFactory.createEtchedBorder());
            playersWaiting.add(players[i]);
        }
        return playersWaiting;
    }

    private JPanel setupCustomizations() {
        JPanel customizations = new JPanel(new GridLayout(4,2, 7, 10));

        JLabel challengeLabel = new JLabel("Challenges Allowed:", SwingConstants.RIGHT);
        challengeBox = new JComboBox<>(challengeChoices);
        JLabel dictionaryLabel = new JLabel("Dictionary Used:", SwingConstants.RIGHT);
        dictionaryBox = new JComboBox<>(dictionaryChoices);
        JLabel playerTimeLabel = new JLabel("Player Time:", SwingConstants.RIGHT);
        playerTimeBox = new JComboBox<>(playerTimeChoices);
        JLabel gameTimeLabel = new JLabel("Game Time:", SwingConstants.RIGHT);
        gameTimeBox = new JComboBox<>(gameTimeChoices);

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

    public JTextField getNameTextField() {
        return name;
    }

    public JLabel[] getPlayers() {
        return players;
    }

    public JComboBox<String> getChallengeBox() {
        return challengeBox;
    }

    public JComboBox<String> getDictionaryBox() {
        return dictionaryBox;
    }

    public JComboBox<String> getPlayerTimeBox() {
        return playerTimeBox;
    }

    public JComboBox<String> getGameTimeBox() {
        return gameTimeBox;
    }

    public JButton getHostButton() {
        return hostButton;
    }
}
