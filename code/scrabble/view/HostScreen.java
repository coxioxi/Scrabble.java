package scrabble.view;

import javax.swing.*;
import java.awt.*;

public class HostScreen extends JPanel {
    public HostScreen() {

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Host A Game"));
        mainPanel.setLayout(null);

        JPanel southPanel = new JPanel(new FlowLayout());
        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setBorder(BorderFactory.createTitledBorder("Host A Game"));
        JPanel eastPanel = new JPanel(new FlowLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Customizations"));
        JPanel westPanel = new JPanel(new FlowLayout());
        westPanel.setBorder(BorderFactory.createTitledBorder("Players Waiting"));

        JLabel yourIP = new JLabel("Your IP Address:", SwingConstants.RIGHT);
        JLabel hostsIP = new JLabel("**Host's IP**");
        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField nameInput = new JTextField();
        JButton hostButton = new JButton("Host");
        JLabel player1Waiting = new JLabel("**Player 1 Name**", SwingConstants.CENTER);
        JLabel player2Waiting = new JLabel("**Player 2 Name**", SwingConstants.CENTER);
        JLabel player3Waiting = new JLabel("**Player 3 Name**", SwingConstants.CENTER);
        JLabel player4Waiting = new JLabel("**Player 4 Name**", SwingConstants.CENTER);

        player1Waiting.setBorder(BorderFactory.createEtchedBorder());
        player2Waiting.setBorder(BorderFactory.createEtchedBorder());
        player3Waiting.setBorder(BorderFactory.createEtchedBorder());
        player4Waiting.setBorder(BorderFactory.createEtchedBorder());

        JPanel nameAndIP = new JPanel(new GridLayout(2,2,7,10));
        nameAndIP.add(yourIP);
        nameAndIP.add(hostsIP);
        nameAndIP.add(nameLabel);
        nameAndIP.add(nameInput);
        northPanel.add(nameAndIP);
        this.add(northPanel, BorderLayout.NORTH);

        JPanel playersWaiting = new JPanel(new GridLayout(4,1,0,10));
        playersWaiting.add(player1Waiting);
        playersWaiting.add(player2Waiting);
        playersWaiting.add(player3Waiting);
        playersWaiting.add(player4Waiting);
        westPanel.add(playersWaiting);
        this.add(westPanel, BorderLayout.WEST);

        southPanel.add(hostButton);
        this.add(southPanel, BorderLayout.SOUTH);

        JLabel challengeLabel = new JLabel("Challenges Allowed:", SwingConstants.RIGHT);
        String[] challengeChoices = {"Challenges on", "Challenges off"};
        JComboBox<String> challengeComboBox = new JComboBox<>(challengeChoices);
        JLabel dictionaryLabel = new JLabel("Dictionary Used:", SwingConstants.RIGHT);
        String[] dictionaryChoices = {"Dictionary 1", "Dictionary 2"};
        JComboBox<String> dictionaryComboBox = new JComboBox<>(dictionaryChoices);
        JLabel playerTimeLabel = new JLabel("Player Time:", SwingConstants.RIGHT);
        String[] playerTimeChoices = {"2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
        JComboBox<String> playerTimeComboBox = new JComboBox<>(playerTimeChoices);
        JLabel gameTimeLabel = new JLabel("Game Time:", SwingConstants.RIGHT);
        String[] gameTimeChoices = {"30 Minutes", "45 Minutes", "60 Minutes", "75 Minutes"};
        JComboBox<String> gameTimeComboBox = new JComboBox<>(gameTimeChoices);

        JPanel customizations = new JPanel(new GridLayout(4,2, 7, 10));
        customizations.add(challengeLabel);
        customizations.add(challengeComboBox);
        customizations.add(dictionaryLabel);
        customizations.add(dictionaryComboBox);
        customizations.add(playerTimeLabel);
        customizations.add(playerTimeComboBox);
        customizations.add(gameTimeLabel);
        customizations.add(gameTimeComboBox);
        eastPanel.add(customizations);
        this.add(eastPanel, BorderLayout.EAST);

    }
}
