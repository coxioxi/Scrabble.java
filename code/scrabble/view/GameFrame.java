package scrabble.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {
    // Static variables to control audio and FX settings
    public static boolean audioOn = true;
    public static boolean fxOn = true;

    // Main Menu screen method
    public static void mainMenu() {
        // Create the main frame and set default close operation
        JFrame frame = new JFrame("Scrabble");
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel setup for the menu layout
        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Main Menu"));
        JPanel menuFrame = new JPanel(new GridLayout(5,1, 0,15));

        // Main Menu buttons and checkboxes
        JButton hostButton = new JButton("Host");
        JButton joinButton = new JButton("Join");
        JCheckBox audioCheck = new JCheckBox("Game Audio", audioOn);
        JCheckBox fxCheck = new JCheckBox("Game FX", fxOn);
        JButton quitButton = new JButton("Quit");

        // Add components to menu panel and frame
        menuFrame.add(hostButton);
        menuFrame.add(joinButton);
        menuFrame.add(audioCheck);
        menuFrame.add(fxCheck);
        menuFrame.add(quitButton);
        mainPanel.add(menuFrame);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Action Listeners for each button
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

        // Set frame properties and display it
        frame.setMinimumSize(new Dimension(250,250));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void hostScreen() {
        JFrame frame = new JFrame("Scrabble");

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
        frame.add(northPanel, BorderLayout.NORTH);

        JPanel playersWaiting = new JPanel(new GridLayout(4,1,0,10));
        playersWaiting.add(player1Waiting);
        playersWaiting.add(player2Waiting);
        playersWaiting.add(player3Waiting);
        playersWaiting.add(player4Waiting);
        westPanel.add(playersWaiting);
        frame.add(westPanel, BorderLayout.WEST);

        southPanel.add(hostButton);
        frame.add(southPanel, BorderLayout.SOUTH);

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
        JFrame frame = new JFrame("Scrabble");

        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setBorder(BorderFactory.createTitledBorder("Join A Game"));
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("IP"));
        JPanel southPanel = new JPanel(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField enterName = new JTextField();
        enterName.setColumns(10);
        JLabel hostIPLabel = new JLabel("Enter Host IP:", SwingConstants.RIGHT);
        JTextField enterIP = new JTextField();
        enterIP.setColumns(10);
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

        player1Label.setBorder(BorderFactory.createEtchedBorder());
        player2Label.setBorder(BorderFactory.createEtchedBorder());
        player3Label.setBorder(BorderFactory.createEtchedBorder());
        player4Label.setBorder(BorderFactory.createEtchedBorder());

        JPanel playersWaiting = new JPanel(new GridLayout(5,1,0,15));
        playersWaiting.add(playerTitle);
        playersWaiting.add(player1Label);
        playersWaiting.add(player2Label);
        playersWaiting.add(player3Label);
        playersWaiting.add(player4Label);
        centerPanel.add(playersWaiting);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setMinimumSize(new Dimension(250,250));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                joinScreen();
            }
        });
    }

    public static void gameScreen() {
        JFrame frame = new JFrame("Scrabble");

        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem rulesItem = new JMenuItem("Rules");
        JMenuItem gameAudioItem = new JMenuItem("Game Audio");
        JMenuItem gameFXItem = new JMenuItem("Game FX");
        JMenuItem quitItem = new JMenuItem("Quit");
        optionsMenu.add(rulesItem);
        optionsMenu.add(gameAudioItem);
        optionsMenu.add(gameFXItem);
        optionsMenu.add(quitItem);
        menuBar.add(optionsMenu);

        JPanel northPanel = new JPanel(new FlowLayout());
        JLabel gameTime = new JLabel("00:00");
        gameTime.setBorder(BorderFactory.createEtchedBorder());
        northPanel.add(gameTime);

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));
        JPanel gamePanel = new JPanel(new GridLayout(15,15,1,1));
        for (int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                JLabel boardTile = new JLabel("$$");
                boardTile.setBorder(BorderFactory.createEtchedBorder());
                gamePanel.add(boardTile);
            }
        }
        centerPanel.add(gamePanel);

        JPanel westPanel = new JPanel(new GridLayout(2,1,0,175));
        JPanel player1Panel = new JPanel(new GridLayout(3,2,7,10));
        player1Panel.setBorder(BorderFactory.createEtchedBorder());
        JLabel player1NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
        JLabel player1Name = new JLabel("*Player Name*");
        JLabel player1TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
        JLabel player1Time = new JLabel("0:00 ");
        JLabel player1ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
        JLabel player1Score = new JLabel("0000");
        player1Panel.add(player1NameLabel);
        player1Panel.add(player1Name);
        player1Panel.add(player1TimeLabel);
        player1Panel.add(player1Time);
        player1Panel.add(player1ScoreLabel);
        player1Panel.add(player1Score);
        westPanel.add(player1Panel);

        JPanel player4Panel = new JPanel(new GridLayout(3,2,7,10));
        player4Panel.setBorder(BorderFactory.createEtchedBorder());
        JLabel player4NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
        JLabel player4Name = new JLabel("*Player Name*");
        JLabel player4TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
        JLabel player4Time = new JLabel("0:00");
        JLabel player4ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
        JLabel player4Score = new JLabel("0000");
        player4Panel.add(player4NameLabel);
        player4Panel.add(player4Name);
        player4Panel.add(player4TimeLabel);
        player4Panel.add(player4Time);
        player4Panel.add(player4ScoreLabel);
        player4Panel.add(player4Score);
        westPanel.add(player4Panel);

        JPanel eastPanel = new JPanel(new GridLayout(2,1,0,175));
        JPanel player2Panel = new JPanel(new GridLayout(3,2,7,10));
        player2Panel.setBorder(BorderFactory.createEtchedBorder());
        JLabel player2NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
        JLabel player2Name = new JLabel("*Player Name*");
        JLabel player2TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
        JLabel player2Time = new JLabel("0:00");
        JLabel player2ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
        JLabel player2Score = new JLabel("0000");
        player2Panel.add(player2NameLabel);
        player2Panel.add(player2Name);
        player2Panel.add(player2TimeLabel);
        player2Panel.add(player2Time);
        player2Panel.add(player2ScoreLabel);
        player2Panel.add(player2Score);
        eastPanel.add(player2Panel);

        JPanel player3Panel = new JPanel(new GridLayout(3,2,7,10));
        player3Panel.setBorder(BorderFactory.createEtchedBorder());
        JLabel player3NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
        JLabel player3Name = new JLabel("*Player Name*");
        JLabel player3TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
        JLabel player3Time = new JLabel("0:00");
        JLabel player3ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
        JLabel player3Score = new JLabel("0000");
        player3Panel.add(player3NameLabel);
        player3Panel.add(player3Name);
        player3Panel.add(player3TimeLabel);
        player3Panel.add(player3Time);
        player3Panel.add(player3ScoreLabel);
        player3Panel.add(player3Score);
        eastPanel.add(player3Panel);

        JPanel southPanel = new JPanel(new FlowLayout());
        JPanel submitAndRack = new JPanel(new GridLayout(2,1,0,10));
        JButton submitButton = new JButton("Submit");
        JPanel rackPanel = new JPanel(new GridLayout(1,7,10,0));
        for (int i = 0; i < 7; i++) {
            JLabel rackTile = new JLabel("$$");
            rackTile.setBorder(BorderFactory.createEtchedBorder());
            rackPanel.add(rackTile);
        }
        submitAndRack.add(submitButton);
        submitAndRack.add(rackPanel);
        southPanel.add(submitAndRack);

        frame.setJMenuBar(menuBar);
        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(westPanel, BorderLayout.WEST);
        frame.add(eastPanel, BorderLayout.EAST);
        frame.add(southPanel, BorderLayout.SOUTH);

        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                mainMenu();
            }
        });

        frame.setMinimumSize(new Dimension(400,400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void winnerScreen() {
        JFrame frame = new JFrame("Scrabble");

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Podium"));
        JPanel southPanel = new JPanel(new FlowLayout());
        JPanel podium = new JPanel(new GridLayout(4,1, 7, 10));

        JLabel firstPlaceName = new JLabel("1st: *Player* | *Score*");
        JLabel secondPlaceName = new JLabel("2nd: *Player* | *Score*");
        JLabel thirdPlaceName = new JLabel("3rd: *Player* | *Score*");
        JLabel fourthPlaceName = new JLabel("4th: *Player* | *Score*");
        JButton returnToMain = new JButton("Return to Main Menu");

        firstPlaceName.setBorder(BorderFactory.createEtchedBorder());
        secondPlaceName.setBorder(BorderFactory.createEtchedBorder());
        thirdPlaceName.setBorder(BorderFactory.createEtchedBorder());
        fourthPlaceName.setBorder(BorderFactory.createEtchedBorder());

        //podium.add(firstPlaceLabel);
        podium.add(firstPlaceName);
        //podium.add(secondPlaceLabel);
        podium.add(secondPlaceName);
        //podium.add(thirdPlaceLabel);
        podium.add(thirdPlaceName);
        //podium.add(fourthPlaceLabel);
        podium.add(fourthPlaceName);
        centerPanel.add(podium);
        frame.add(centerPanel, BorderLayout.CENTER);

        southPanel.add(returnToMain);
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setMinimumSize(new Dimension(250,175));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Makes join button switch to the waiting screen
        returnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the main menu
                mainMenu();  // Open join screen
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
    }

    public static void main(String[] args) {
        try {
            // Set the look and feel to the system's default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignore) {}
        //mainMenu();
        //joinScreen();
        //hostScreen();
        //waitingScreen();
        winnerScreen();
        //gameScreen();
    }


}
