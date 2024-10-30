package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {

	private JLabel name;
	private JLabel time;
	private JLabel score;

	public PlayerPanel(String playerName, int score, int time) {
		this.setLayout(new GridLayout(3,2,7,10));
		this.setBorder(BorderFactory.createEtchedBorder());
		JLabel playerNameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
		name = new JLabel(playerName);
		JLabel playerTimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
		this.time = new JLabel(time/60 + ":" + time%60 + " ");
		JLabel playerScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
		this.score = new JLabel("" + score);
		this.add(playerNameLabel);
		this.add(name);
		this.add(playerTimeLabel);
		this.add(this.time);
		this.add(playerScoreLabel);
		this.add(this.score);
	}

	public PlayerPanel() {

	}

}
