package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

public class WaitingScreen extends JPanel {

    private JLabel[] players;

    public WaitingScreen() {
        this.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Player Waiting Screen"));

        centerPanel.add(setupPlayersWaiting());
        this.add(centerPanel, BorderLayout.CENTER);

    }

    private JPanel setupPlayersWaiting() {
        JPanel playersWaiting = new JPanel(new GridLayout(5,1,0,15));
        JLabel playerTitle = new JLabel("Players in the Game:", SwingConstants.CENTER);
        playersWaiting.add(playerTitle);
        players = new JLabel[4];

        for (int i = 0; i < players.length; i++) {
            players[i] = new JLabel("**Player "+(i+1)+ " Name**", SwingConstants.CENTER);
            players[i].setBorder(BorderFactory.createEtchedBorder());
            playersWaiting.add(players[i]);
        }
        return playersWaiting;
    }

    public JLabel[] getPlayers() {
        return players;
    }
}
