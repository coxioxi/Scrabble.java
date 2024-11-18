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

import static scrabble.view.ScrabbleGUI.audioOn;
import static scrabble.view.ScrabbleGUI.fxOn;

/**
 * MainMenuScreen represents the main menu panel in the Scrabble game,
 * containing buttons to host or join a game, as well as options for audio settings.
 */
public class MainMenuScreen extends JPanel {

    // Buttons for hosting, joining, and quitting the game
    private JButton hostButton;     //
    private JButton joinButton;     //
    private JButton quitButton;     //

    // Checkboxes for toggling game audio and sound effects
    private JCheckBox audioCheck;   //
    private JCheckBox fxCheck;      //

    /**
     * Constructor for MainMenuScreen. Initializes the layout, buttons, and checkboxes.
     */
    public MainMenuScreen() {
        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createTitledBorder("Main Menu"));

        JPanel menuFrame = new JPanel(new GridLayout(5,1, 0,15));

        // Main Menu buttons and checkboxes
        hostButton = new JButton("Host");
        joinButton = new JButton("Join");
        audioCheck = new JCheckBox("Game Audio", audioOn);
        fxCheck = new JCheckBox("Game FX", fxOn);
        quitButton = new JButton("Quit");

        // Add components to menu panel and frame
        menuFrame.add(hostButton);
        menuFrame.add(joinButton);
        menuFrame.add(audioCheck);
        menuFrame.add(fxCheck);
        menuFrame.add(quitButton);
        this.add(menuFrame, BorderLayout.CENTER);
    }

    /**
     * getter method for the Host button
     *
     * @return the JButton related to the "Host" button
     */
    public JButton getHostButton() {
        return hostButton;
    }

    /**
     * getter method for the Quit button
     *
     * @return the JButton related to the "Quit" button
     */
    public JButton getQuitButton() {
        return quitButton;
    }

    /**
     * getter method for the Join button
     *
     * @return the JButton related to the "Join" button
     */
    public JButton getJoinButton() {
        return joinButton;
    }

    /**
     * getter method for the FX check box
     *
     * @return The JCheckBox related to the "FX" check box
     */
    public JCheckBox getFxCheck() {
        return fxCheck;
    }

    /**
     * getter method for the audio check box
     *
     * @return The JCheckBox related to the "Audio" check box
     */
    public JCheckBox getAudioCheck() {
        return audioCheck;
    }
}
