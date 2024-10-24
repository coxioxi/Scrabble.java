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
        // Frame setup
        JFrame hostFrame = new JFrame("Scrabble");
        hostFrame.setSize(800,600);
        hostFrame.setLayout(null);

        // Main Panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0,0,800,600);
        mainPanel.setLayout(null);

        // Page Title
        JLabel hostTitle = new JLabel("Host Game", SwingConstants.CENTER);
        hostTitle.setFont(titleFont);
        hostTitle.setBounds(275,5,250,45);
        mainPanel.add(hostTitle);

        // Main Frame details
        // IP Address Labels setup
        JLabel yourIP = new JLabel("Your IP Address:");
        yourIP.setFont(labelFont);
        yourIP.setBounds(25, 80, 240, 30);

        JLabel hostsIP = new JLabel("**Host's IP**");
        hostsIP.setFont(labelFont);
        hostsIP.setBounds(245, 80, 200, 30);

        // Name Label and TextField setup
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setBounds(25, 150, 100, 30);

        JTextField nameInput = new JTextField();
        nameInput.setFont(labelFont);
        nameInput.setBounds(115, 150, 220, 30);

        // Host Button setup
        JButton hostButton = new JButton("Host");
        hostButton.setFont(labelFont);
        hostButton.setBounds(325, 490, 150, 40);

        // Player Waiting Label setup
        JLabel waitingLabel = new JLabel("Players Waiting");
        waitingLabel.setFont(labelFont);
        waitingLabel.setBounds(50, 215, 200, 30);

        // Player Names Waiting labels setup
        JLabel player1Waiting = new JLabel("**Player 1 Name**", SwingConstants.CENTER);
        JLabel player2Waiting = new JLabel("**Player 2 Name**", SwingConstants.CENTER);
        JLabel player3Waiting = new JLabel("**Player 3 Name**", SwingConstants.CENTER);
        JLabel player4Waiting = new JLabel("**Player 4 Name**", SwingConstants.CENTER);

        Font waitingFont = new Font("Arial", Font.PLAIN, 20);
        player1Waiting.setFont(waitingFont);
        player2Waiting.setFont(waitingFont);
        player3Waiting.setFont(waitingFont);
        player4Waiting.setFont(waitingFont);

        player1Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player2Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player3Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player4Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        player1Waiting.setBounds(25, 270, 250, 40);
        player2Waiting.setBounds(25, 330, 250, 40);
        player3Waiting.setBounds(25, 390, 250, 40);
        player4Waiting.setBounds(25, 450, 250, 40);

        // Customization Panel details
        JPanel customizations = new JPanel();
        customizations.setBounds(450, 150, 300, 250);
        customizations.setBorder(new LineBorder(Color.BLACK, 2));
        customizations.setLayout(null);

        Font customizationFont = new Font("Arial", Font.PLAIN, 16);

        // Customizations Label setup
        JLabel customizationLabel = new JLabel("Customizations");
        customizationLabel.setFont(labelFont);
        customizationLabel.setBounds(50, 10, 200, 30);

        // Challenge Label and Combo Box setup
        JLabel challengeLabel = new JLabel("Challenges Allowed:");
        challengeLabel.setFont(customizationFont);
        challengeLabel.setBounds(10, 80, 150, 20);

        String[] challengeChoices = {"Challenges on", "Challenges off"};
        JComboBox<String> challengeComboBox = new JComboBox<>(challengeChoices);
        challengeComboBox.setFont(customizationFont);
        challengeComboBox.setBounds(160, 78, 130, 25);

        // Dictionary Label and Combo Box setup
        JLabel dictionaryLabel = new JLabel("Dictionary Used:");
        dictionaryLabel.setFont(customizationFont);
        dictionaryLabel.setBounds(10, 120, 150, 20);

        String[] dictionaryChoices = {"Dictionary 1", "Dictionary 2"};
        JComboBox<String> dictionaryComboBox = new JComboBox<>(dictionaryChoices);
        dictionaryComboBox.setFont(customizationFont);
        dictionaryComboBox.setBounds(133, 118, 120, 25);

        // Player Time Label and Combo Box setup
        JLabel playerTimeLabel = new JLabel("Player Time:");
        playerTimeLabel.setFont(customizationFont);
        playerTimeLabel.setBounds(10, 160, 150, 20);

        String[] playerTimeChoices = {"2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
        JComboBox<String> playerTimeComboBox = new JComboBox<>(playerTimeChoices);
        playerTimeComboBox.setFont(customizationFont);
        playerTimeComboBox.setBounds(105, 158, 105, 25);

        // Game Time Label and Combo Box setup
        JLabel gameTimeLabel = new JLabel("Game Time:");
        gameTimeLabel.setFont(customizationFont);
        gameTimeLabel.setBounds(10, 200, 150, 20);

        String[] gameTimeChoices = {"30 Minutes", "45 Minutes", "60 Minutes", "75 Minutes"};
        JComboBox<String> gameTimeComboBox = new JComboBox<>(gameTimeChoices);
        gameTimeComboBox.setFont(customizationFont);
        gameTimeComboBox.setBounds(105, 198, 105, 25);

        // adds items to the main panel
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

        // Changes the "X" in the top right to make the Host Screen return to the Main Menu
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
        joinFrame.setSize(700, 500);
        joinFrame.setLayout(null);

        // Add title at the top of the frame
        JLabel joinTitle = new JLabel("Player Joining Screen", SwingConstants.CENTER);
        joinTitle.setFont(titleFont);
        joinTitle.setBounds(160, 5, 380, 50);
        joinFrame.add(joinTitle);

        // Name label and text field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(190, 80, 80, 30);
        nameLabel.setFont(labelFont);
        joinFrame.add(nameLabel);

        JTextField enterName = new JTextField();
        enterName.setBounds(290, 75, 220, 40);
        enterName.setFont(labelFont);
        joinFrame.add(enterName);

        // Panel for border with Host IP text field and Join button inside
        JPanel borderPanel = new JPanel();
        borderPanel.setBorder(new LineBorder(Color.BLACK, 2));
        borderPanel.setBounds(125, 140, 450, 300);
        borderPanel.setLayout(null);
        joinFrame.add(borderPanel);

        // Labels for Join party and Host IP inside bordered box
        JLabel joinPartyLabel = new JLabel("Join Party", SwingConstants.CENTER);
        joinPartyLabel.setBounds(135, 15, 180, 30);
        joinPartyLabel.setFont(labelFont);
        borderPanel.add(joinPartyLabel);

        JLabel enterIPLabel = new JLabel("Enter Host IP:");
        enterIPLabel.setBounds(30, 80, 180, 30);
        enterIPLabel.setFont(labelFont);
        borderPanel.add(enterIPLabel);

        // Text field for entering Host IP
        JTextField hostIPText = new JTextField();
        hostIPText.setBounds(220, 75, 200, 40);
        borderPanel.add(hostIPText);

        // Join button
        JButton joinButton = new JButton("Join");
        joinButton.setBounds(150, 150, 150, 50);
        joinButton.setFont(labelFont);
        borderPanel.add(joinButton);

        // Makes join button switch to the waiting screen
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinFrame.dispose();  // Close the main menu
                waitingScreen();  // Open join screen
            }
        });

        // Changes the "X" in the top right to make the Join Screen return to the Main Menu
        joinFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                joinFrame.dispose();
                mainMenu();
            }
        });

        joinFrame.setVisible(true);
    }

    public static void waitingScreen() {
        JFrame waitFrame = new JFrame("Scrabble");
        waitFrame.setSize(500, 500);
        waitFrame.setLayout(null);

        // Add title at the top of the frame
        JLabel waitTitle = new JLabel("Player Waiting Screen", SwingConstants.CENTER);
        waitTitle.setFont(titleFont);
        waitTitle.setBounds(55, 5, 390, 50);
        waitFrame.add(waitTitle);

        // Players title label setup
        JLabel playerTitle = new JLabel("Players in Game", SwingConstants.CENTER);
        playerTitle.setBounds(145, 80, 210, 40);
        playerTitle.setFont(labelFont);

        // Players Names in this game setup
        JLabel player1Label = new JLabel("**Player 1**", SwingConstants.CENTER);
        JLabel player2Label = new JLabel("**Player 2**", SwingConstants.CENTER);
        JLabel player3Label = new JLabel("**Player 3**", SwingConstants.CENTER);
        JLabel player4Label = new JLabel("**Player 4**", SwingConstants.CENTER);

        player1Label.setBounds(100, 150, 300, 45);
        player2Label.setBounds(100, 220, 300, 45);
        player3Label.setBounds(100, 290, 300, 45);
        player4Label.setBounds(100, 360, 300, 45);

        player1Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player2Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player3Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player4Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        player1Label.setFont(labelFont);
        player2Label.setFont(labelFont);
        player3Label.setFont(labelFont);
        player4Label.setFont(labelFont);

        // Adding labels to the screen
        waitFrame.add(playerTitle);
        waitFrame.add(player1Label);
        waitFrame.add(player2Label);
        waitFrame.add(player3Label);
        waitFrame.add(player4Label);

        waitFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                waitFrame.dispose();
                joinScreen();
            }
        });

        waitFrame.setVisible(true);
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
        //waitingScreen();
    }


}
