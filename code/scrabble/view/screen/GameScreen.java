package scrabble.view.screen;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.GameScreenController;
import scrabble.model.*;
import scrabble.view.frame.TileButton;
import scrabble.view.screen.component.BoardPanel;
import scrabble.view.screen.component.PlayerPanel;
import scrabble.view.screen.component.RackPanel;
import scrabble.view.screen.component.TilePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The GameScreen class represents the main game screen in the Scrabble application.
 * It contains various UI components such as the game board, player panels, and controls for gameplay actions.
 */
public class GameScreen extends JPanel {
	public static final int GAP = 150; // Spacing used between panels
	public static final int RACK_SIZE = 7; // Number of tiles in a player's rack

	private JLabel gameTime; // Label displaying the game timer
	private BoardPanel boardPanel; // Panel representing the game board

	// 2D array representing the board's state
	public Tile[][] letters = new Tile[Board.BOARD_ROWS][Board.BOARD_COLUMNS];

	private JPanel centerPanel; // Panel for the game board
	private JPanel eastPanel; // Panel for player panels on the right side
	private JPanel westPanel; // Panel for player panels on the left side
	private JPanel southPanel; // Panel for the player's rack and action buttons
	private JPanel northPanel; // Panel for the game timer

	private TilePanel[] tilePanels; // Array representing the tile panels
	private RackPanel rackPanel; // Panel representing the player's rack
	private JButton submitButton; // Button for submitting a move
	private JButton passButton; // Button for passing a turn

	private PlayerPanel[] playerPanels;

	// List of tiles that have been played in the current turn
	private List<Tile> playedTiles = new ArrayList<>();
	private JButton value = new JButton(" ");

	/**
	 * Default constructor that sets up the GameScreen layout and initializes the UI components.
	 */
	public GameScreen() {
		this.setLayout(new BorderLayout()); // Set layout for the main panel
		playerPanels = new PlayerPanel[4];

		// Initialize and add the panels for different sections of the game screen
		northPanel = setupNorthPanel();
		centerPanel = setupCenterPanel();
		eastPanel = new JPanel(new GridLayout(2, 1, 0, GAP));
		westPanel = new JPanel(new GridLayout(2, 1, 0, GAP));
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

	/**
	 * Disables the last played tiles on the board by updating the state of the cells.
	 */
	public void disableLastPlayedTiles() {
		for (Tile t : playedTiles) {
			boardPanel.disableBoardCell(t.getLocation().x, t.getLocation().y);
		}
		playedTiles = new ArrayList<>();
	}

	public void removeFromPlayedTiles(Tile tile) {
		playedTiles.remove(tile);
	}

	public void addToPlayedTiles(Tile t) {
		playedTiles.add(t);
	}

	/**
	 * @return The center panel containing the game board.
	 */
	public JPanel getCenterPanel() {
		return centerPanel;
	}

	/**
	 * @return List of tiles that have been played in the current turn.
	 */
	public List<Tile> getPlayedTiles() {
		return playedTiles;
	}

	/**
	 * Sets a button value for internal use.
	 *
	 * @param value The JButton to set.
	 */
	public void setValue(JButton value) {
		this.value = value;
	}

	/**
	 * @return The value button.
	 */
	public JButton getValue() {
		return value;
	}

	/**
	 * @return The panel representing the game board.
	 */
	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	/**
	 * @return The label representing the game timer.
	 */
	public JLabel getGameTime() {
		return gameTime;
	}

	/**
	 * @return The panel representing the player's rack.
	 */
	public RackPanel getRackPanel() {
		return rackPanel;
	}

	/**
	 * @return The button for submitting a move.
	 */
	public JButton getSubmitButton() {
		return submitButton;
	}

	public JButton removeButtonFromRack(int col) {
		JButton b = tilePanels[col].getButton();
		tilePanels[col].setButton(new JButton(" "));
		return b;
	}


	public void updateScore(String playerName, int score) {
		for (PlayerPanel player : playerPanels) {
			if (player != null && Objects.equals(player.getNameLabel().getText(), playerName)) {
				player.getScore().setText("" + score);
			}
		}
	}

	public void setupGameItems(String[] playerNames, int gameTime, int playerTime, Tile[] rackTiles) {
		// Setup player panels
		for (int i = 0; i < playerNames.length; i++) {
			playerPanels[i] = (PlayerPanel) setupPlayerPanel(playerNames[i], playerTime);
			if (i == 1 || i == 2) {
				eastPanel.add(playerPanels[i]);
			} else {
				westPanel.add(playerPanels[i]);
			}
		}
		resetRack();
		addTilesToRack(rackTiles);

		this.gameTime.setText(gameTime + ":00");
		this.revalidate();
		this.repaint();
	}

	public void addToBoard(Tile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
			TileButton tb = (tiles[i].isBlank() ?
					new TileButton() :
					new TileButton(TileScore.values()[tiles[i].getLetter() - 'A'])
			);
			int x = tiles[i].getLocation().x;
			int y = tiles[i].getLocation().y;
			boardPanel.setBoardCell(tb, x, y);
			boardPanel.disableBoardCell(x, y);
		}
	}

	/**
	 * Removes a tile from the rack.
	 *
	 * @param tile The tile to be removed.
	 */
	public void removeTileFromRack(Tile tile) {
		RackPanel rackPanel = this.rackPanel;
		for (TilePanel tp : rackPanel.getTilePanels()) {
			if (tp.getButton().getText().equals("" + tile.getLetter())) {
				tp.setButton(new JButton(" "));
				break;
			}
		}
	}

	/**
	 * Adds tiles to the player's rack.
	 *
	 * @param tiles Array of tiles to be added.
	 */
	public void addTilesToRack(Tile[] tiles) {
		int index = 0;
		for (int i = 0; i < tilePanels.length; i++) {
			if (!(tilePanels[i].getButton() instanceof TileButton)) {
				TileButton button =
						(tiles[index].isBlank()
								? new TileButton()
								: new TileButton(TileScore.values()[tiles[index].getLetter() - 'A'])
						);
				tilePanels[i].setButton(button);
				index++;
			}
		}
	}

	public int addTileButtonToRack(TileButton t) {
		int index = -1;
		for (int i = 0; i < tilePanels.length && index == -1; i++) {
			if (!(tilePanels[i].getButton() instanceof TileButton)) {
				tilePanels[i].setButton(t);
				index = i;
			}
		}
		return index;
	}

	public void addRackTileActionListener(int index, ActionListener al) {
		tilePanels[index].getButton().addActionListener(al);
	}

	public void removeRackTileActionListeners(int index) {
		GameScreenController.removeActionListeners(tilePanels[index].getButton());
	}

	/**
	 * Sets up a PlayerPanel with the specified player's name and time.
	 *
	 * @param name       The name of the player.
	 * @param playerTime The time allotted for the player.
	 * @return A new PlayerPanel instance for the player.
	 */
	private JPanel setupPlayerPanel(String name, int playerTime) {
		return new PlayerPanel(name, 0, playerTime*60);
	}

	/**
	 * Sets up the panel at the bottom of the screen, including the rack and action buttons.
	 *
	 * @return The south panel.
	 */
	private JPanel setupSouthPanel() {
		JPanel tempPanel = new JPanel(new FlowLayout());
		JPanel submitAndRack = new JPanel(new GridLayout(2, 1, 0, 10));
		JPanel subAndPass = new JPanel(new GridLayout(1, 2, 10, 0));
		passButton = new JButton("Pass Turn");
		submitButton = new JButton("Submit");
		submitButton.setPreferredSize(new Dimension(50, 10));

		// Initialize tile panels for the rack
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

	/**
	 * Sets up the center panel containing the game board.
	 *
	 * @return The center panel.
	 */
	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new FlowLayout());

		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));

		boardPanel = new BoardPanel();
		centerPanel.add(boardPanel);
		return centerPanel;
	}

	/**
	 * Sets up the panel at the top of the screen, displaying the game timer.
	 *
	 * @return The north panel.
	 */
	private JPanel setupNorthPanel() {
		JPanel northPanel = new JPanel(new FlowLayout());
		gameTime = new JLabel("00:00");
		gameTime.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTime);
		return northPanel;
	}

	/**
	 * Creates a JComboBox with game options.
	 *
	 * @return The JComboBox with game options.
	 */
	private static JComboBox<String> getStringJComboBox() {
		String[] options = {"Rules", "Game Audio", "Game FX", "Quit"};
		JComboBox<String> comboBox = new JComboBox<>(options);
		comboBox.setBounds(0, 0, 100, 25);

		// Add action listener for the combo box
		comboBox.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == comboBox) {
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


	/**
	 * Resets the rack by clearing all tiles.
	 */
	private void resetRack() {
		for (TilePanel tp : tilePanels) {
			tp.setButton(new JButton(" "));
		}
	}
}
