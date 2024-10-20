package scrabble.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Scrabble");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        initializeComponents();

        mainMenu();

    }

    public void initializeComponents() {
        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.LIGHT_GRAY);
        boardPanel.setPreferredSize(new Dimension(600, 400));
        add(boardPanel, BorderLayout.CENTER);

    }

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

        /* quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        */
        menuFrame.setSize(500, 700);
        menuFrame.setVisible(true);
    }


    public static void hostScreen() {
        JFrame hostFrame = new JFrame();
        hostFrame.setSize(800,600);

        JLabel hostTitle = new JLabel("Host Game", SwingConstants.CENTER);
        hostTitle.setFont(new Font("Arial", Font.BOLD, 24));
        hostFrame.add(hostTitle, BorderLayout.NORTH);

        JLabel yourIP = new JLabel("Your IP Address");
        JLabel name = new JLabel("Name:");
        JLabel hostsIP = new JLabel("**Hosts IP**");
        JTextField nameInput = new JTextField("Your Name");
        JButton hostButton = new JButton("Host");

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
        mainMenu();

        //hostScreen();
    }


}
