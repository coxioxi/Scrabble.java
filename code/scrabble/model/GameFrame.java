package scrabble.model;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame {
    private static final Font labelFont = new Font("Arial", Font.PLAIN, 20);
    private static final Font titleFont = new Font("Times New Roman", Font.BOLD, 40);

    public static void mainMenu() {
        JFrame menuFrame = new JFrame();
        JLabel menuTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        JPanel background = new JPanel();
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
        hostFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel centerBackground = new JPanel();

        JLabel hostTitle = new JLabel("Host Game", SwingConstants.CENTER);
        hostTitle.setFont(new Font("Arial", Font.BOLD, 40));
        hostFrame.add(hostTitle, BorderLayout.NORTH);

        JLabel yourIP = new JLabel("Your IP Address:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel hostsIP = new JLabel("**Host's IP**");
        JTextField nameInput = new JTextField();
        JButton hostButton = new JButton("Host");

        hostTitle.setFont(titleFont);
        yourIP.setFont(labelFont);
        nameLabel.setFont(labelFont);
        hostsIP.setFont(labelFont);
        nameInput.setFont(labelFont);
        hostButton.setFont(labelFont);

        yourIP.setBounds(25, 80, 240, 40);
        nameLabel.setBounds(25, 150, 100, 40);
        hostsIP.setBounds(245, 80, 200, 40);
        nameInput.setBounds(115, 150, 225, 40);
        hostButton.setBounds(325, 490, 150, 50);

        hostFrame.add(yourIP);
        hostFrame.add(nameLabel);
        hostFrame.add(hostsIP);
        hostFrame.add(nameInput);
        hostFrame.add(hostButton);
        hostFrame.add(centerBackground, BorderLayout.CENTER);

        hostFrame.setVisible(true);
    }

    public static void joinScreen() {
        // Highest level frame and panel
        JFrame joinFrame = new JFrame("Player Joining Screen");
        joinFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        joinFrame.setSize(800, 800);
        joinFrame.setLayout(null);

        // Add title at the top of the frame
        JLabel joinTitle = new JLabel("Player Joining Screen", SwingConstants.CENTER);
        joinTitle.setFont(titleFont);
        joinTitle.setBounds(200, 30, 400, 50);
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
        JLabel joinPartyLabel = new JLabel("Join Party");
        joinPartyLabel.setBounds(200, 20, 100, 30);
        joinPartyLabel.setFont(labelFont);
        borderPanel.add(joinPartyLabel);

        JLabel enterIPLabel = new JLabel("Enter Host IP:");
        enterIPLabel.setBounds(50, 80, 150, 30);
        enterIPLabel.setFont(labelFont);
        borderPanel.add(enterIPLabel);

        // Text field for entering Host IP
        JTextField hostIPText = new JTextField();
        hostIPText.setBounds(200, 80, 200, 30);
        borderPanel.add(hostIPText);

        // Join button
        JButton joinButton = new JButton("Join");
        joinButton.setBounds(150, 150, 200, 40);
        joinButton.setFont(labelFont);
        borderPanel.add(joinButton);

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
        joinScreen();
        //hostScreen();
    }


}
