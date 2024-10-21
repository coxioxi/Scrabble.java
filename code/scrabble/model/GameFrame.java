package scrabble.model;

import javax.swing.*;
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
        hostScreen();
    }


}
