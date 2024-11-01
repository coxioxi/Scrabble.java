package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

import static scrabble.view.frame.GameFrame.audioOn;
import static scrabble.view.frame.GameFrame.fxOn;

public class MainMenuScreen extends JPanel {

    private JButton hostButton;
    private JButton joinButton;
    private JCheckBox audioCheck;
    private JCheckBox fxCheck;
    private JButton quitButton;

    public MainMenuScreen() {


        // Panel setup for the menu layout
//        JPanel mainPanel = new JPanel(new FlowLayout());
//        mainPanel.setBorder(BorderFactory.createTitledBorder("Main Menu"));
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

    public JButton getHostButton() {
        return hostButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public JCheckBox getFxCheck() {
        return fxCheck;
    }

    public JCheckBox getAudioCheck() {
        return audioCheck;
    }

    public JButton getJoinButton() {
        return joinButton;
    }
}
