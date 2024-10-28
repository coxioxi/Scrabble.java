package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

	private JLabel gameTime;
	private JButton[][] gameCells;
	private JButton[] rack;
	private JButton submitButton;

	public GameScreen() {
		this.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel(new FlowLayout());
		gameTime = new JLabel("00:00");
		gameTime.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTime);

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));
		JPanel gamePanel = new JPanel(new GridLayout(15,15,1,1));
		gameCells = new JButton[15][15];
		for (int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				JButton boardTile = new JButton("$$");
				gameCells[i][j] = boardTile;
				//boardTile.setBorder(BorderFactory.createEtchedBorder());
				gamePanel.add(boardTile);
			}
		}
		centerPanel.add(gamePanel);

		JPanel eastPanel = new JPanel(new GridLayout(2,1,0,175));
		JPanel westPanel = new JPanel(new GridLayout(2,1,0,175));

		JPanel player1Panel = setupPlayerPanel();
		JPanel player2Panel = setupPlayerPanel();
		JPanel player3Panel = setupPlayerPanel();
		JPanel player4Panel = setupPlayerPanel();

		westPanel.add(player1Panel);
		westPanel.add(player4Panel);

		eastPanel.add(player2Panel);
		eastPanel.add(player3Panel);

		JPanel southPanel = new JPanel(new FlowLayout());
		JPanel submitAndRack = new JPanel(new GridLayout(2,1,0,10));
		submitButton = new JButton("Submit");
		JPanel rackPanel = new JPanel(new GridLayout(1,7,10,0));
		rack = new JButton[7];
		for (int i = 0; i < 7; i++) {
			JButton rackTile = new JButton("$$");
			rack[i] = rackTile;
			//rackTile.setBorder(BorderFactory.createEtchedBorder());
			rackPanel.add(rackTile);
		}
		submitAndRack.add(submitButton);
		submitAndRack.add(rackPanel);
		southPanel.add(submitAndRack);

		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);
	}

	public JLabel getGameTime() {
		return gameTime;
	}

	public JButton[][] getGameCells() {
		return gameCells;
	}

	public JButton[] getRack() {
		return rack;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	private JPanel setupPlayerPanel() {
		return new PlayerPanel("Player", 0, 0);
	}
}
