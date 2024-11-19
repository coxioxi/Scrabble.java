package scrabble.view.screen;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import javax.swing.*;
import java.awt.*;

/**
 * JoinScreen provides a user interface for a player to join a game by entering their name and the host's IP address.
 */
public class JoinScreen extends JPanel {

    // Input fields for player's name and host IP, host's port and a button to initiate joining the game
    private final JTextField name;
    private final JTextField IP;
    private final JTextField port;
    private final JButton join;

    /**
     * Constructor for JoinScreen. Sets up the layout with input fields for player name and IP address,
     * and a button to join the game.
     */
    public JoinScreen() {
        // Set the main layout for the JoinScreen
        this.setLayout(new BorderLayout());

        // Create a panel for the name input section, with a titled border
        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setBorder(BorderFactory.createTitledBorder("Join A Game"));
        // Label and text field for entering the player's name
        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        name = new JTextField();
        name.setColumns(10); // Set the text field width to 10 columns
        // Create sub-panel for arranging the name label and text field in a grid
        JPanel namePanel = new JPanel(new GridLayout(1,2, 7, 0));
        namePanel.add(nameLabel);
        namePanel.add(name);
        northPanel.add(namePanel); // Add the name panel to the north panel
        this.add(northPanel, BorderLayout.NORTH); // Place the north panel at the top of the layout


        // Create a panel for the IP input section, with a titled border
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Enter Host's Information"));

        // Label and text field for entering the host IP address
        JLabel hostIPLabel = new JLabel("Enter Host IP:", SwingConstants.RIGHT);
        IP = new JTextField();
        IP.setColumns(10); // Set the text field width to 10 columns

        // Label and text field for entering host port
        JLabel portLabel = new JLabel("Enter Host Port:", SwingConstants.RIGHT);
        port = new JTextField();
        port.setColumns(10); // Set the text field width to 10 columns
        // Create sub-panel for arranging the IP and port label and text field in a grid
        JPanel hostInfoPanel = new JPanel(new GridLayout(2,2,7,10));
        hostInfoPanel.add(hostIPLabel);
        hostInfoPanel.add(IP);
        hostInfoPanel.add(portLabel);
        hostInfoPanel.add(port);
        centerPanel.add(hostInfoPanel); // Add the IP panel to the center panel
        this.add(centerPanel, BorderLayout.CENTER); // Place the center panel in the middle of the layout

        // Create a panel for the join button
        JPanel southPanel = new JPanel(new FlowLayout());
        // Button to join the game
        join = new JButton("Join");

        // Add the join button to the south panel and place it at the bottom of the layout
        southPanel.add(join);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Returns the text field for entering the player's name.
     * @return JTextField for player name input
     */
    public String getNameText() { return name.getText().trim(); }

    /**
     * Returns the text entered into the host IP address.
     * @return The String entered in the IP text field.
     */
    public String getIPText() { return IP.getText().trim(); }

    /**
     * Returns the text entered into the host port number field.
     *
     * @return The String entered in the port text field.
     */
    public String getPortText() { return port.getText().trim(); }

    /**
     * Returns the button for joining the game.
     * @return JButton to join the game
     */
    public JButton getJoinButton() { return join; }
}
