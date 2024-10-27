package scrabble.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GameFrame extends JFrame {
    private static final Font labelFont = new Font("Arial", Font.PLAIN, 28);
    private static final Font titleFont = new Font("Times New Roman", Font.BOLD, 40);

    public static void mainMenu() {
        JFrame menuFrame = new JFrame("Scrabble");
        JLabel menuTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        JPanel background = new JPanel();
        menuFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        menuTitle.setFont(titleFont);
        menuFrame.add(menuTitle, BorderLayout.NORTH);

        JButton hostButton = new JButton("Host");
        JButton joinButton = new JButton("Join");
        JButton audioButton = new JButton("Game Audio");
        JButton fxButton = new JButton("Audio FX");
        JButton quitButton = new JButton("Quit");

        hostButton.setBounds(95, 90, 200, 60);
        joinButton.setBounds(95, 160, 200, 60);
        audioButton.setBounds(95, 230, 200, 60);
        fxButton.setBounds(95, 300, 200, 60);
        quitButton.setBounds(95, 370, 200, 60);
        background.setBounds(100, 100, 200, 100);

        hostButton.setFont(labelFont);
        joinButton.setFont(labelFont);
        audioButton.setFont(labelFont);
        fxButton.setFont(labelFont);
        quitButton.setFont(labelFont);

        menuFrame.add(hostButton);
        menuFrame.add(joinButton);
        menuFrame.add(audioButton);
        menuFrame.add(fxButton);
        menuFrame.add(quitButton);
        menuFrame.add(background);


        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();  // Close the main menu
                hostScreen();  // Open host screen
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();  // Close the main menu
                joinScreen();  // Open join screen
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuFrame.setSize(400, 500);
        menuFrame.setVisible(true);
    }


    public static void hostScreen() {
        JFrame hostFrame = new JFrame("Scrabble");
        hostFrame.setSize(800,600);
        //hostFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        hostFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hostFrame.dispose();
                mainMenu();
            }
        });
    }

    public static void joinScreen() {
        // Highest level frame and panel
        JFrame joinFrame = new JFrame("Scrabble");
        joinFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        joinFrame.setSize(800, 800);
        joinFrame.setLayout(null);

        // Add title at the top of the frame
        JLabel joinTitle = new JLabel("Player Joining Screen", SwingConstants.CENTER);
        joinTitle.setFont(titleFont);
        joinTitle.setBounds(150, 30, 500, 50);
        joinFrame.add(joinTitle);

        // Name label and text field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(150, 100, 100, 40);
        nameLabel.setFont(labelFont);
        joinFrame.add(nameLabel);

        JTextField enterName = new JTextField();
        enterName.setBounds(250, 100, 200, 40);
        joinFrame.add(enterName);

        // Panel for border with Host IP text field and Join button inside
        JPanel borderPanel = new JPanel();
        borderPanel.setBorder(new LineBorder(Color.BLACK, 2));
        borderPanel.setBounds(150, 200, 500, 500);
        borderPanel.setLayout(null);
        joinFrame.add(borderPanel);

        // Labels for Join party and Host IP inside bordered box
        JLabel joinPartyLabel = new JLabel("Join Party", SwingConstants.CENTER);
        joinPartyLabel.setBounds(160, 20, 180, 30);
        joinPartyLabel.setFont(labelFont);
        borderPanel.add(joinPartyLabel);

        JLabel enterIPLabel = new JLabel("Enter Host IP:");
        enterIPLabel.setBounds(40, 80, 180, 30);
        enterIPLabel.setFont(labelFont);
        borderPanel.add(enterIPLabel);

        // Text field for entering Host IP
        JTextField hostIPText = new JTextField();
        hostIPText.setBounds(240, 80, 200, 30);
        borderPanel.add(hostIPText);

        // Join button
        JButton joinButton = new JButton("Join");
        joinButton.setBounds(150, 150, 200, 40);
        joinButton.setFont(labelFont);
        borderPanel.add(joinButton);

        joinFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                joinFrame.dispose();
                mainMenu();
            }
        });

        joinFrame.setVisible(true);
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
        mainMenu();
        //joinScreen();
        //hostScreen();
    }


}
