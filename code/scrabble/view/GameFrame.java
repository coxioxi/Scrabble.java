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
        JFrame frame = new JFrame("Scrabble");
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Main Menu"));
        JPanel menuFrame = new JPanel(new GridLayout(5,1, 0,15));

        /* JLabel menuTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        menuTitle.setFont(titleFont);
        frame.add(menuTitle, BorderLayout.NORTH);
         */

        JButton hostButton = new JButton("Host");
        JButton joinButton = new JButton("Join");
        JButton audioButton = new JButton("Game Audio");
        JButton fxButton = new JButton("Audio FX");
        JButton quitButton = new JButton("Quit");

        /* hostButton.setBounds(95, 90, 200, 60);
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
         */

        menuFrame.add(hostButton);
        menuFrame.add(joinButton);
        menuFrame.add(audioButton);
        menuFrame.add(fxButton);
        menuFrame.add(quitButton);
        mainPanel.add(menuFrame);
        frame.add(mainPanel, BorderLayout.CENTER);

        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the main menu
                hostScreen();  // Open host screen
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the main menu
                joinScreen();  // Open join screen
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //frame.setSize(400, 500);
        frame.setMinimumSize(new Dimension(250,250));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void hostScreen() {
        // Frame setup
        JFrame frame = new JFrame("Scrabble");

        // Main Panel setup
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

        /*Page Title
        JLabel hostTitle = new JLabel("Host Game", SwingConstants.CENTER);
        hostTitle.setFont(titleFont);
        hostTitle.setBounds(275,5,250,45);
        mainPanel.add(hostTitle);
         */

        // Main Frame details
        // Instantiating the Labels and the TextField
        JLabel yourIP = new JLabel("Your IP Address:", SwingConstants.RIGHT);
        JLabel hostsIP = new JLabel("**Host's IP**");
        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField nameInput = new JTextField();
        JButton hostButton = new JButton("Host");
        JLabel player1Waiting = new JLabel("**Player 1 Name**", SwingConstants.CENTER);
        JLabel player2Waiting = new JLabel("**Player 2 Name**", SwingConstants.CENTER);
        JLabel player3Waiting = new JLabel("**Player 3 Name**", SwingConstants.CENTER);
        JLabel player4Waiting = new JLabel("**Player 4 Name**", SwingConstants.CENTER);

        player1Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player2Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player3Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player4Waiting.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Adds Name and IP items to North
        JPanel nameAndIP = new JPanel(new GridLayout(2,2,7,10));
        //nameAndIP.setBorder(BorderFactory.createTitledBorder("Host A Game"));
        nameAndIP.add(yourIP);
        nameAndIP.add(hostsIP);
        nameAndIP.add(nameLabel);
        nameAndIP.add(nameInput);
        northPanel.add(nameAndIP);
        frame.add(northPanel, BorderLayout.NORTH);

        // Adds Players Waiting to the West
        JPanel playersWaiting = new JPanel(new GridLayout(4,1,0,10));
        //playersWaiting.setBorder(BorderFactory.createTitledBorder("Players Waiting"));
        playersWaiting.add(player1Waiting);
        playersWaiting.add(player2Waiting);
        playersWaiting.add(player3Waiting);
        playersWaiting.add(player4Waiting);
        westPanel.add(playersWaiting);
        frame.add(westPanel, BorderLayout.WEST);

        // Adds Host Button to the South
        southPanel.add(hostButton);
        frame.add(southPanel, BorderLayout.SOUTH);

        /* yourIP.setFont(labelFont);
        yourIP.setBounds(25, 80, 240, 30);
        hostsIP.setFont(labelFont);
        hostsIP.setBounds(245, 80, 200, 30);
        nameLabel.setFont(labelFont);
        nameLabel.setBounds(25, 150, 100, 30);
        nameInput.setFont(labelFont);
        nameInput.setBounds(115, 150, 220, 30);
        hostButton.setFont(labelFont);
        hostButton.setBounds(325, 490, 150, 40);
        waitingLabel.setFont(labelFont);
        waitingLabel.setBounds(50, 215, 200, 30);
        Font waitingFont = new Font("Arial", Font.PLAIN, 20);
        player1Waiting.setFont(waitingFont);
        player2Waiting.setFont(waitingFont);
        player3Waiting.setFont(waitingFont);
        player4Waiting.setFont(waitingFont);
        player1Waiting.setBounds(25, 270, 250, 40);
        player2Waiting.setBounds(25, 330, 250, 40);
        player3Waiting.setBounds(25, 390, 250, 40);
        player4Waiting.setBounds(25, 450, 250, 40);
        customizations.setBounds(450, 150, 300, 250);
        Font customizationFont = new Font("Arial", Font.PLAIN, 16);
         */

        // Instantiating Customizations Labels and Combo Boxes
        // JLabel customizationLabel = new JLabel("Customizations");
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

        /*
        customizationLabel.setFont(labelFont);
        customizationLabel.setBounds(50, 10, 200, 30);
        Challenge Label and Combo Box setup
        challengeLabel.setFont(customizationFont);
        challengeLabel.setBounds(10, 80, 150, 20);
        challengeComboBox.setFont(customizationFont);
        challengeComboBox.setBounds(160, 78, 130, 25);
        Dictionary Label and Combo Box setup
        dictionaryLabel.setFont(customizationFont);
        dictionaryLabel.setBounds(10, 120, 150, 20);
        dictionaryComboBox.setFont(customizationFont);
        dictionaryComboBox.setBounds(133, 118, 120, 25);
        Player Time Label and Combo Box setup
        playerTimeLabel.setFont(customizationFont);
        playerTimeLabel.setBounds(10, 160, 150, 20);
        playerTimeComboBox.setFont(customizationFont);
        playerTimeComboBox.setBounds(105, 158, 105, 25);
        Game Time Label and Combo Box setup
        gameTimeLabel.setBounds(10, 200, 150, 20);
        gameTimeLabel.setFont(customizationFont);
        gameTimeComboBox.setFont(customizationFont);
        gameTimeComboBox.setBounds(105, 198, 105, 25);
         */

        // Adds Customization Items to the East
        JPanel customizations = new JPanel(new GridLayout(4,2, 7, 10));
        //customizations.setBorder(BorderFactory.createTitledBorder("Customizations"));
        customizations.add(challengeLabel);
        customizations.add(challengeComboBox);
        customizations.add(dictionaryLabel);
        customizations.add(dictionaryComboBox);
        customizations.add(playerTimeLabel);
        customizations.add(playerTimeComboBox);
        customizations.add(gameTimeLabel);
        customizations.add(gameTimeComboBox);
        eastPanel.add(customizations);
        frame.add(eastPanel, BorderLayout.EAST);

        frame.setMinimumSize(new Dimension(370,300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Changes the "X" in the top right to make the Host Screen return to the Main Menu
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                mainMenu();
            }
        });
    }

    public static void joinScreen() {
        // Highest level frame and panel
        JFrame frame = new JFrame("Scrabble");

        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setBorder(BorderFactory.createTitledBorder("Join A Game"));
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("IP"));
        JPanel southPanel = new JPanel(new FlowLayout());

        /* JLabel joinTitle = new JLabel("Player Joining Screen");
        joinTitle.setFont(titleFont);
        joinTitle.setBounds(160, 5, 380, 50);
        joinFrame.add(joinTitle);
         */

        JLabel nameLabel = new JLabel("Name:");
        JTextField enterName = new JTextField();
        JLabel hostIPLabel = new JLabel("Enter Host IP:");
        JTextField enterIP = new JTextField();
        JButton joinButton = new JButton("Join");

        JPanel namePanel = new JPanel(new GridLayout(1,2, 7, 0));
        namePanel.add(nameLabel);
        namePanel.add(enterName);
        northPanel.add(namePanel);
        frame.add(northPanel, BorderLayout.NORTH);

        JPanel ipPanel = new JPanel(new GridLayout(1,2,7,0));
        ipPanel.add(hostIPLabel);
        ipPanel.add(enterIP);
        centerPanel.add(ipPanel);
        frame.add(centerPanel, BorderLayout.CENTER);

        southPanel.add(joinButton);
        frame.add(southPanel, BorderLayout.SOUTH);

        /*
        nameLabel.setBounds(190, 80, 80, 30);
        nameLabel.setFont(labelFont);
        joinFrame.add(nameLabel);
        enterName.setBounds(290, 75, 220, 40);
        enterName.setFont(labelFont);
        joinFrame.add(enterName);

        JPanel borderPanel = new JPanel();
        borderPanel.setBorder(new LineBorder(Color.BLACK, 2));
        borderPanel.setBounds(125, 140, 450, 300);
        borderPanel.setLayout(null);
        joinFrame.add(borderPanel);

        JLabel joinPartyLabel = new JLabel("Join Party", SwingConstants.CENTER);
        joinPartyLabel.setBounds(135, 15, 180, 30);
        joinPartyLabel.setFont(labelFont);
        borderPanel.add(joinPartyLabel);

        enterIPLabel.setBounds(30, 80, 180, 30);
        enterIPLabel.setFont(labelFont);
        borderPanel.add(enterIPLabel);
        hostIPText.setBounds(220, 75, 200, 40);
        borderPanel.add(hostIPText);
        joinButton.setBounds(150, 150, 150, 50);
        joinButton.setFont(labelFont);
        borderPanel.add(joinButton);
         */

        // Makes join button switch to the waiting screen
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the main menu
                waitingScreen();  // Open join screen
            }
        });

        // Changes the "X" in the top right to make the Join Screen return to the Main Menu
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                mainMenu();
            }
        });

        frame.setMinimumSize(new Dimension(250,150));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void waitingScreen() {
        JFrame frame = new JFrame("Scrabble");

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Player Waiting Screen"));

        JLabel playerTitle = new JLabel("Players in the Game:", SwingConstants.CENTER);
        JLabel player1Label = new JLabel("**Player 1**", SwingConstants.CENTER);
        JLabel player2Label = new JLabel("**Player 2**", SwingConstants.CENTER);
        JLabel player3Label = new JLabel("**Player 3**", SwingConstants.CENTER);
        JLabel player4Label = new JLabel("**Player 4**", SwingConstants.CENTER);

        player1Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player2Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player3Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        player4Label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        /*
        playerTitle.setBounds(145, 80, 210, 40);
        playerTitle.setFont(labelFont);
        player1Label.setBounds(100, 150, 300, 45);
        player2Label.setBounds(100, 220, 300, 45);
        player3Label.setBounds(100, 290, 300, 45);
        player4Label.setBounds(100, 360, 300, 45);
        player1Label.setFont(labelFont);
        player2Label.setFont(labelFont);
        player3Label.setFont(labelFont);
        player4Label.setFont(labelFont);
         */

        JPanel playersWaiting = new JPanel(new GridLayout(5,1,0,15));
        playersWaiting.add(playerTitle);
        playersWaiting.add(player1Label);
        playersWaiting.add(player2Label);
        playersWaiting.add(player3Label);
        playersWaiting.add(player4Label);
        centerPanel.add(playersWaiting);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                joinScreen();
            }
        });

        frame.setMinimumSize(new Dimension(250,250));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void gameScreen() {
        JPanel gamePanel = new JPanel();

    }

    public void winnerScreen() {
        JFrame winnerFrame = new JFrame("Scrabble");
        winnerFrame.setSize(600,600);

        JButton returnToMain = new JButton("Return to Main Menu");
        returnToMain.setFont(labelFont);
        //returnToMain.setBounds(150, );

        // Makes join button switch to the waiting screen
        returnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                winnerFrame.dispose();  // Close the main menu
                mainMenu();  // Open join screen
            }
        });

        // Changes the "X" in the top right to make the Join Screen return to the Main Menu
        winnerFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                winnerFrame.dispose();
                mainMenu();
            }
        });
    }

    public static void main(String[] args) {
        try {
            // Set the look and feel to the system's default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignore) {}
        mainMenu();
        //joinScreen();
        //hostScreen();
        //waitingScreen();
    }


}
