package scrabble.view.screen;

import scrabble.model.*;
import scrabble.view.frame.TileButton;
import scrabble.view.screen.component.BoardPanel;
import scrabble.view.screen.component.PlayerPanel;
import scrabble.view.screen.component.RackPanel;
import scrabble.view.screen.component.TilePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JPanel {
	public static final int GAP = 150;
	public static final int RACK_SIZE = 7;

	private JLabel gameTime;
	private BoardPanel boardPanel;
	public Tile[][] letters = new Tile[Board.BOARD_ROWS][Board.BOARD_COLUMNS];

	private JPanel centerPanel;
	private JPanel eastPanel;
	private JPanel westPanel;
	private JPanel southPanel;
	private JPanel northPanel;

	private TilePanel[] tilePanels;
	private RackPanel rackPanel;
	private JButton submitButton;
	private JButton passButton;

	public List<Tile> playedTiles = new ArrayList<>();
	private JButton value = new JButton(" ");

	public GameScreen() {
		this.setLayout(new BorderLayout());

		northPanel = setupNorthPanel();
		centerPanel = setupCenterPanel();
		eastPanel = new JPanel(new GridLayout(2,1,0,GAP));
		westPanel = new JPanel(new GridLayout(2,1,0,GAP));
		southPanel = setupSouthPanel();

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

	public void disableLastPlayedTiles() {
		for (Tile t : playedTiles) {
			boardPanel.disableBoardCell(t.getLocation().x, t.getLocation().y);
		}
	}

	//TODO: implement this method
	public GameScreen(Ruleset rules, Player[] player, int playerNum) {

	}

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
		JPanel tempPanel = new JPanel(new FlowLayout());
		JPanel submitAndRack = new JPanel(new GridLayout(2,1,0,10));
		JPanel subAndPass = new JPanel(new GridLayout(1,2,10,0));
		passButton = new JButton("Pass Turn");
		submitButton = new JButton("Submit");
		submitButton.setPreferredSize(new Dimension(50, 10));
		tilePanels = new TilePanel[RACK_SIZE];
		for (int i = 0; i < tilePanels.length; i++) {
			tilePanels[i] = new TilePanel(new TileButton(TileScore.values()[i]));
		}
		this.rackPanel = new RackPanel(tilePanels);

		subAndPass.add(submitButton);
		subAndPass.add(passButton);
		submitAndRack.add(subAndPass);
		submitAndRack.add(rackPanel);
		tempPanel.add(submitAndRack);
		return tempPanel;
	}

	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new FlowLayout());

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

	public void setupGameItems (int numPlayers, String[] playerNames, int gameTime, int playerTime, ArrayList<Tile> rackTiles) {
		for (int i = 0; i < numPlayers; i++) {
			JPanel newPlayer = setupPlayerPanel(playerNames[i], playerTime);
			if (i > 0 && i < 3){
				eastPanel.add(newPlayer);
			} else {
				westPanel.add(newPlayer);
			}
		}
		addTilesToRack(rackTiles.toArray(new Tile[0]));

		this.gameTime.setText(gameTime + ":00");
	}

	public void addTilesToRack (Tile[] tiles) {
		int index = 0;
		for (int i = 0; i < tilePanels.length; i++) {
			if (!(tilePanels[i].getButton() instanceof TileButton)) {
				TileButton button = new TileButton(TileScore.values()[tiles[index].getLetter() - 'A']);
				tilePanels[i] = new TilePanel(button);
				index++;
			}
		}
	}

	private JPanel setupPlayerPanel(String name, int playerTime) {
		return new PlayerPanel(name, 0, playerTime);
	}
}
