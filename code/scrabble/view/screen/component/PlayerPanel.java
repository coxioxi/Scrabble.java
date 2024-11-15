package scrabble.view.screen.component;
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
 * PlayerPanel displays the details of a player in the game, showing the player's
 * name, current score, and remaining time.
 */
public class PlayerPanel extends JPanel {

	// Labels to display player's name, time, and score
	private JLabel name;
	private JLabel time;
	private JLabel score;

	/**
	 * Constructor for PlayerPanel. Sets up the panel layout and populates it with player information.
	 *
	 * @param playerName The name of the player.
	 * @param score The player's current score.
	 * @param time The player's remaining time in seconds.
	 */
	public PlayerPanel(String playerName, int score, int time) {
		// Set layout for the panel with two columns and spacing between rows
		this.setLayout(new GridLayout(3,2,7,10));

		// Set an etched border for visual separation
		this.setBorder(BorderFactory.createEtchedBorder());

		// Label for player name and value
		JLabel playerNameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
		name = new JLabel(playerName);

		// Label for remaining time, formatted as minutes and seconds
		JLabel playerTimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
		this.time = new JLabel(time / 60 + ":" + time % 60 + " ");

		// Label for player's score
		JLabel playerScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
		this.score = new JLabel("" + score);

		// Add labels to the panel in the specified layout order
		this.add(playerNameLabel);
		this.add(name);
		this.add(playerTimeLabel);
		this.add(this.time);
		this.add(playerScoreLabel);
		this.add(this.score);
	}

	public PlayerPanel() {

	}

	public JLabel getNameLabel() {
		return name;
	}

	public JLabel getTime() {
		return time;
	}

	public JLabel getScore() {
		return score;
	}
}
