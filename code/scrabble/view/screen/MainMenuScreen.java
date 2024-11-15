package scrabble.view.screen;

import javax.swing.*;
import java.awt.*;

import static scrabble.view.frame.ScrabbleGUI.audioOn;
import static scrabble.view.frame.ScrabbleGUI.fxOn;

/**
 * MainMenuScreen represents the main menu panel in the Scrabble game,
 * containing buttons to host or join a game, as well as options for audio settings.
 */
public class MainMenuScreen extends JPanel {

    // Buttons for hosting, joining, and quitting the game
    private JButton hostButton;
    private JButton joinButton;
    private JButton quitButton;

    // Checkboxes for toggling game audio and sound effects
    private JCheckBox audioCheck;
    private JCheckBox fxCheck;

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

    // Getter methods for each button and checkbox
    public JButton getHostButton() {
        return hostButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public JButton getJoinButton() {
        return joinButton;
    }

    public JCheckBox getFxCheck() {
        return fxCheck;
    }

    public JCheckBox getAudioCheck() {
        return audioCheck;
    }
}
