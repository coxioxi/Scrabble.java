package scrabble.view.panel;

import scrabble.model.Board;
import scrabble.model.Tile;
import scrabble.model.TileScore;
import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JPanel {
	public static final int GAP = 175;
	public static final int RACK_SIZE = 7;

	private JLabel gameTime;
	private BoardPanel boardPanel;
	public Tile[][] letters = new Tile[Board.BOARD_ROWS][Board.BOARD_COLUMNS];

	private RackPanel rackPanel;
	private JButton submitButton;
	private JButton passButton;

	public List<Tile> playedTiles = new ArrayList<>();
	private JButton value = new JButton(" ");

	public GameScreen() {
		this.setLayout(new BorderLayout());

		JPanel northPanel = setupNorthPanel();
		centerPanel = setupCenterPanel();

		JPanel eastPanel = new JPanel(new GridLayout(2,1,0,GAP));
		JPanel westPanel = new JPanel(new GridLayout(2,1,0,GAP));

		JPanel player1Panel = setupPlayerPanel();
		JPanel player2Panel = setupPlayerPanel();
		JPanel player3Panel = setupPlayerPanel();
		JPanel player4Panel = setupPlayerPanel();

		westPanel.add(player1Panel);
		westPanel.add(player4Panel);

		eastPanel.add(player2Panel);
		eastPanel.add(player3Panel);

		JPanel southPanel = setupSouthPanel();

		//Drop down menu
		/*JComboBox<String> comboBox = getStringJComboBox();

		this.add(comboBox);
		 */
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);
	}

	private JPanel centerPanel;
	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public List<Tile> getPlayedTiles() {
		return playedTiles;
	}

	public void setValue(JButton value) {
		this.value = value;
	}

	public JButton getValue() {
		return value;
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public JLabel getGameTime() {
		return gameTime;
	}

	public RackPanel getRackPanel() {
		return rackPanel;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	private JPanel setupSouthPanel() {
		JPanel southPanel = new JPanel(new FlowLayout());
		JPanel submitAndRack = new JPanel(new GridLayout(2,1,0,10));
		JPanel subAndPass = new JPanel(new GridLayout(1,2,10,0));
		passButton = new JButton("Pass Turn");
		submitButton = new JButton("Submit");
		submitButton.setPreferredSize(new Dimension(50, 10));
		TilePanel[] tilePanels = new TilePanel[RACK_SIZE];
		for (int i = 0; i < tilePanels.length; i++) {
			tilePanels[i] = new TilePanel(new TileButton(TileScore.values()[i]));
		}
		this.rackPanel = new RackPanel(tilePanels);

		subAndPass.add(submitButton);
		subAndPass.add(passButton);
		submitAndRack.add(subAndPass);
		submitAndRack.add(rackPanel);
		southPanel.add(submitAndRack);
		return southPanel;
	}

	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setMaximumSize(
				new Dimension(1410, 875)
		);
		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));

		boardPanel = new BoardPanel();
		centerPanel.add(boardPanel);
		return centerPanel;
	}

	private JPanel setupNorthPanel() {
		JPanel northPanel = new JPanel(new FlowLayout());
		gameTime = new JLabel("00:00");
		gameTime.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTime);
		return northPanel;
	}

	private static JComboBox<String> getStringJComboBox() {
		String[] options = {"Rules", "Game Audio", "Game FX", "Quit"};
		JComboBox<String> comboBox = new JComboBox<>(options);
		comboBox.setBounds(0, 0, 100, 25);

		comboBox.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == comboBox) {
					String menu = options[comboBox.getSelectedIndex()];
					switch (menu) {
						case "Rules" -> System.out.println("There are no rules man, We laust!");
						case "Game Audio" -> System.out.println("No audio for now :( ");
						case "Game FX" -> System.out.println("No FX either :( ");
						case "Quit" -> System.out.println("I wish dude!");
					}
				}
			}
		});
		return comboBox;
	}

	private JPanel setupPlayerPanel() {
		return new PlayerPanel("Player", 0, 0);
	}
}
