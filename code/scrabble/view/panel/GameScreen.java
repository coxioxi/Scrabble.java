package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {


	public GameScreen() {
		/*
		JMenuBar menuBar = new JMenuBar();

		JMenu optionsMenu = new JMenu("Options");
		JMenuItem rulesItem = new JMenuItem("Rules");
		JMenuItem gameAudioItem = new JMenuItem("Game Audio");
		JMenuItem gameFXItem = new JMenuItem("Game FX");
		JMenuItem quitItem = new JMenuItem("Quit");
		optionsMenu.add(rulesItem);
		optionsMenu.add(gameAudioItem);
		optionsMenu.add(gameFXItem);
		optionsMenu.add(quitItem);
		menuBar.add(optionsMenu);
		 */

		this.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel(new FlowLayout());
		JLabel gameTime = new JLabel("00:00");
		gameTime.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTime);

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));
		JPanel gamePanel = new JPanel(new GridLayout(15,15,1,1));
		for (int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				JButton boardTile = new JButton("$$");
				//boardTile.setBorder(BorderFactory.createEtchedBorder());
				gamePanel.add(boardTile);
			}
		}
		centerPanel.add(gamePanel);

		JPanel westPanel = new JPanel(new GridLayout(2,1,0,175));
		JPanel player1Panel = new JPanel(new GridLayout(3,2,7,10));
		player1Panel.setBorder(BorderFactory.createEtchedBorder());
		JLabel player1NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
		JLabel player1Name = new JLabel("*Player Name*");
		JLabel player1TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
		JLabel player1Time = new JLabel("0:00 ");
		JLabel player1ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
		JLabel player1Score = new JLabel("0000");
		player1Panel.add(player1NameLabel);
		player1Panel.add(player1Name);
		player1Panel.add(player1TimeLabel);
		player1Panel.add(player1Time);
		player1Panel.add(player1ScoreLabel);
		player1Panel.add(player1Score);
		westPanel.add(player1Panel);

		JPanel player4Panel = new JPanel(new GridLayout(3,2,7,10));
		player4Panel.setBorder(BorderFactory.createEtchedBorder());
		JLabel player4NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
		JLabel player4Name = new JLabel("*Player Name*");
		JLabel player4TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
		JLabel player4Time = new JLabel("0:00");
		JLabel player4ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
		JLabel player4Score = new JLabel("0000");
		player4Panel.add(player4NameLabel);
		player4Panel.add(player4Name);
		player4Panel.add(player4TimeLabel);
		player4Panel.add(player4Time);
		player4Panel.add(player4ScoreLabel);
		player4Panel.add(player4Score);
		westPanel.add(player4Panel);

		JPanel eastPanel = new JPanel(new GridLayout(2,1,0,175));
		JPanel player2Panel = new JPanel(new GridLayout(3,2,7,10));
		player2Panel.setBorder(BorderFactory.createEtchedBorder());
		JLabel player2NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
		JLabel player2Name = new JLabel("*Player Name*");
		JLabel player2TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
		JLabel player2Time = new JLabel("0:00");
		JLabel player2ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
		JLabel player2Score = new JLabel("0000");
		player2Panel.add(player2NameLabel);
		player2Panel.add(player2Name);
		player2Panel.add(player2TimeLabel);
		player2Panel.add(player2Time);
		player2Panel.add(player2ScoreLabel);
		player2Panel.add(player2Score);
		eastPanel.add(player2Panel);

		JPanel player3Panel = new JPanel(new GridLayout(3,2,7,10));
		player3Panel.setBorder(BorderFactory.createEtchedBorder());
		JLabel player3NameLabel = new JLabel("Player Name:", SwingConstants.RIGHT);
		JLabel player3Name = new JLabel("*Player Name*");
		JLabel player3TimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
		JLabel player3Time = new JLabel("0:00");
		JLabel player3ScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
		JLabel player3Score = new JLabel("0000");
		player3Panel.add(player3NameLabel);
		player3Panel.add(player3Name);
		player3Panel.add(player3TimeLabel);
		player3Panel.add(player3Time);
		player3Panel.add(player3ScoreLabel);
		player3Panel.add(player3Score);
		eastPanel.add(player3Panel);

		JPanel southPanel = new JPanel(new FlowLayout());
		JPanel submitAndRack = new JPanel(new GridLayout(2,1,0,10));
		JButton submitButton = new JButton("Submit");
		JPanel rackPanel = new JPanel(new GridLayout(1,7,10,0));
		for (int i = 0; i < 7; i++) {
			JLabel rackTile = new JLabel("$$");
			rackTile.setBorder(BorderFactory.createEtchedBorder());
			rackPanel.add(rackTile);
		}
		submitAndRack.add(submitButton);
		submitAndRack.add(rackPanel);
		southPanel.add(submitAndRack);


//		frame.setJMenuBar(menuBar);
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);


		/*
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				mainMenu();
			}
		});

		frame.setMinimumSize(new Dimension(400,400));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		 */
	}
}
