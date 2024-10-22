package scrabble.model;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame {
    private static final Font labelFont = new Font("Arial", Font.PLAIN, 28);
    private static final Font titleFont = new Font("Times New Roman", Font.BOLD, 40);

    public static void mainMenu() {
        JFrame menuFrame = new JFrame();
        JLabel menuTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 16));
        menuFrame.add(menuTitle, BorderLayout.NORTH);

        JButton hostButton = new JButton("Host");
        JButton joinButton = new JButton("Join");
        JButton audioButton = new JButton("Game Audio");
        JButton fxButton = new JButton("Audio FX");
        JButton quitButton = new JButton("Quit");

        hostButton.setBounds(250, 150, 100, 40);
        joinButton.setBounds(250, 220, 100, 40);
        audioButton.setBounds(250, 290, 100, 40);
        fxButton.setBounds(250, 360, 100, 40);
        quitButton.setBounds(250, 430, 100, 40);

        menuFrame.add(hostButton, BorderLayout.CENTER);
        menuFrame.add(joinButton, BorderLayout.CENTER);
        menuFrame.add(audioButton, BorderLayout.CENTER);
        menuFrame.add(fxButton, BorderLayout.CENTER);
        menuFrame.add(quitButton, BorderLayout.CENTER);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuFrame.setSize(500, 700);
        menuFrame.setVisible(true);
    }


    public static void hostScreen() {
        JFrame hostFrame = new JFrame("Scrabble");
        hostFrame.setSize(800,600);
        hostFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        hostFrame.setLayout(null);
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0,0,800,600);
        mainPanel.setLayout(null);


        JLabel hostTitle = new JLabel("Host Game", SwingConstants.CENTER);
        hostTitle.setFont(titleFont);
        hostTitle.setBounds(275,5,250,45);
        mainPanel.add(hostTitle);

        // Main Frame details
        JLabel yourIP = new JLabel("Your IP Address:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel hostsIP = new JLabel("**Host's IP**");
        JTextField nameInput = new JTextField();
        JButton hostButton = new JButton("Host");
        JLabel waitingLabel = new JLabel("Players Waiting");
        JLabel player1Waiting = new JLabel("**Player 1 Name**");
        JLabel player2Waiting = new JLabel("**Player 2 Name**");
        JLabel player3Waiting = new JLabel("**Player 3 Name**");
        JLabel player4Waiting = new JLabel("**Player 4 Name**");

        Font waitingFont = new Font("Arial", Font.PLAIN, 20);

        yourIP.setFont(labelFont);
        nameLabel.setFont(labelFont);
        hostsIP.setFont(labelFont);
        nameInput.setFont(labelFont);
        hostButton.setFont(labelFont);
        waitingLabel.setFont(labelFont);
        player1Waiting.setFont(waitingFont);
        player2Waiting.setFont(waitingFont);
        player3Waiting.setFont(waitingFont);
        player4Waiting.setFont(waitingFont);

        player1Waiting.setHorizontalAlignment(SwingConstants.CENTER);
        player1Waiting.setVerticalAlignment(SwingConstants.CENTER);
        player2Waiting.setHorizontalAlignment(SwingConstants.CENTER);
        player2Waiting.setVerticalAlignment(SwingConstants.CENTER);
        player3Waiting.setHorizontalAlignment(SwingConstants.CENTER);
        player3Waiting.setVerticalAlignment(SwingConstants.CENTER);
        player4Waiting.setHorizontalAlignment(SwingConstants.CENTER);
        player4Waiting.setVerticalAlignment(SwingConstants.CENTER);

        player1Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player2Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player3Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player4Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        yourIP.setBounds(25, 80, 240, 40);
        nameLabel.setBounds(25, 150, 100, 40);
        hostsIP.setBounds(245, 80, 200, 40);
        nameInput.setBounds(115, 150, 225, 40);
        hostButton.setBounds(325, 490, 150, 50);
        waitingLabel.setBounds(50, 215, 200, 40);
        player1Waiting.setBounds(25, 270, 250, 40);
        player2Waiting.setBounds(25, 330, 250, 40);
        player3Waiting.setBounds(25, 390, 250, 40);
        player4Waiting.setBounds(25, 450, 250, 40);

        // Customization Panel details
        JPanel customizations = new JPanel();
        customizations.setBounds(450, 150, 300, 250);
        customizations.setBorder(new LineBorder(Color.BLACK, 2));
        customizations.setLayout(null);

        JLabel customizationLabel = new JLabel("Customizations");
        JLabel challengeLabel = new JLabel("Challenges Allowed:");
        JLabel dictionaryLabel = new JLabel("Dictionary Used:");
        JLabel playerTimeLabel = new JLabel("Player Time:");
        JLabel gameTimeLabel = new JLabel("Game Time:");

        String[] challengeChoices = {"Challenges on", "Challenges off"};
        JComboBox<String> challengeComboBox = new JComboBox<>(challengeChoices);
        String[] dictionaryChoices = {"Dictionary 1", "Dictionary 2"};
        JComboBox<String> dictionaryComboBox = new JComboBox<>(dictionaryChoices);
        String[] playerTimeChoices = {"2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
        JComboBox<String> playerTimeComboBox = new JComboBox<>(playerTimeChoices);
        String[] gameTimeChoices = {"30 Minutes", "45 Minutes", "60 Minutes", "75 Minutes"};
        JComboBox<String> gameTimeComboBox = new JComboBox<>(gameTimeChoices);

        Font customizationFont = new Font("Arial", Font.PLAIN, 16);

        customizationLabel.setFont(labelFont);
        challengeLabel.setFont(customizationFont);
        dictionaryLabel.setFont(customizationFont);
        playerTimeLabel.setFont(customizationFont);
        gameTimeLabel.setFont(customizationFont);
        challengeComboBox.setFont(customizationFont);
        dictionaryComboBox.setFont(customizationFont);
        playerTimeComboBox.setFont(customizationFont);
        gameTimeComboBox.setFont(customizationFont);

        customizationLabel.setBounds(50, 10, 200, 30);
        challengeLabel.setBounds(10, 80, 150, 20);
        dictionaryLabel.setBounds(10, 120, 150, 20);
        playerTimeLabel.setBounds(10, 160, 150, 20);
        gameTimeLabel.setBounds(10, 200, 150, 20);
        challengeComboBox.setBounds(160, 78, 130, 25);
        dictionaryComboBox.setBounds(133, 118, 120, 25);
        playerTimeComboBox.setBounds(105, 158, 105, 25);
        gameTimeComboBox.setBounds(105, 198, 105, 25);

        // adds items to the main frame
        mainPanel.add(yourIP);
        mainPanel.add(nameLabel);
        mainPanel.add(hostsIP);
        mainPanel.add(nameInput);
        mainPanel.add(hostButton);
        mainPanel.add(waitingLabel);
        mainPanel.add(player1Waiting);
        mainPanel.add(player2Waiting);
        mainPanel.add(player3Waiting);
        mainPanel.add(player4Waiting);
        hostFrame.add(mainPanel);

        // adds items to the customization panel
        customizations.add(customizationLabel);
        customizations.add(challengeLabel);
        customizations.add(dictionaryLabel);
        customizations.add(playerTimeLabel);
        customizations.add(gameTimeLabel);
        customizations.add(challengeComboBox);
        customizations.add(dictionaryComboBox);
        customizations.add(playerTimeComboBox);
        customizations.add(gameTimeComboBox);
        mainPanel.add(customizations);

        hostFrame.setVisible(true);
    }

    public void joinScreen() {
        JPanel joinPanel = new JPanel();

    }

    public void waitingScreen() {
        JPanel waitPanel = new JPanel();

    }

    public void gameScreen() {
        JPanel gamePanel = new JPanel();

    }

    public void winnerScreen() {
        JPanel winnerPanel = new JPanel();

    }

    public static void main(String[] args) {

    }


}
