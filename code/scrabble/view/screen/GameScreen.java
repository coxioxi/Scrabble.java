package scrabble.view.screen;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.GameScreenController;
import scrabble.model.Board;
import scrabble.model.Tile;
import scrabble.view.TileButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
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

	private final JPanel eastPanel; // Panel for player panels on the right side
	private final JPanel westPanel; // Panel for player panels on the left side

	private final GameControls gameControls;

	private final PlayerPanel[] playerPanels;

	// List of tiles that have been played in the current turn
	private List<Tile> playedTiles = new ArrayList<>();
	private JButton value = new JButton(" ");

	/**
	 * Default constructor that sets up the GameScreen layout and initializes the UI components.
	 */
	public GameScreen() {
		this.setLayout(new BorderLayout()); // Set layout for the main panel
		playerPanels = new PlayerPanel[4];
		gameControls = new GameControls();

		// Initialize and add the panels for different sections of the game screen
		// Panel for the game timer
		JPanel northPanel = setupNorthPanel();
		// Panel for the game board
		JPanel centerPanel = setupCenterPanel();
		eastPanel = new JPanel(new GridLayout(2, 1, 0, GAP));
		westPanel = new JPanel(new GridLayout(2, 1, 0, GAP));

		//Drop down menu
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		System.out.println("num components: " + this.getComponentCount() + "\n" + "adding gameControls...");
		this.add(gameControls, BorderLayout.SOUTH);
		System.out.println("num components: " + this.getComponentCount());
	}

	public GameControls getGameControls() { return gameControls; }

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
	 * @return The label representing the game timer.
	 */
	public JLabel getGameTime() {
		return gameTime;
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
		gameControls.getMainControlsPanel().getRackPanel().resetRack();
		gameControls.getMainControlsPanel().getRackPanel().addTilesToRack(rackTiles);
		this.gameTime.setText(gameTime + ":00");
		this.revalidate();
		this.repaint();
	}

	public void addToBoard(Tile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
			TileButton tb = (tiles[i].isBlank() ?
					new TileButton() :
					new TileButton(Tile.TileScore.valueOf(tiles[i].getLetter()+""))
			);
			int x = tiles[i].getLocation().x;
			int y = tiles[i].getLocation().y;
			boardPanel.setBoardCell(tb, x, y);
			boardPanel.disableBoardCell(x, y);
		}
	}

	public void setBoardCellOfBoardPanel(JButton button, int row, int col) {
		boardPanel.setBoardCell(button, row, col);
	}

	public void addActionListenerToBoardCell(ActionListener al, int row, int col) {
		boardPanel.addActionListener(al, row, col);
	}

	public boolean instanceOfTileButton(int row, int col) {
		return boardPanel.instanceOfTileButton(row, col);
	}

	public void removeBoardPanelActionListeners (int row, int col) {
		boardPanel.removeActionListeners(row, col);
	}

	public String getBoardButtonText(int row, int col) {
		return boardPanel.getButtonText(row, col);
	}

	public JButton getBoardButton(int row, int col) {
		return boardPanel.getButton(row, col);
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
	 * PlayerPanel displays the details of a player in the game, showing the player's
	 * name, current score, and remaining time.
	 */
	private static class PlayerPanel extends JPanel {

		// Labels to display player's name, time, and score
		private JLabel name;
		private JLabel timeLabel;
		private JLabel score;
		private int time;

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
			this.timeLabel = new JLabel(time / 60 + ":" + (time % 60 < 10 ? "0":"") + time % 60);

			// Label for player's score
			JLabel playerScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
			this.score = new JLabel("" + score);

			// Add labels to the panel in the specified layout order
			this.add(playerNameLabel);
			this.add(name);
			this.add(playerTimeLabel);
			this.add(this.timeLabel);
			this.add(playerScoreLabel);
			this.add(this.score);
		}

		/**
		 * Gets the label displaying the player's name.
		 *
		 * @return JLabel labeling the player's name.
		 */
		public JLabel getNameLabel() {
			return name;
		}

		/**
		 * Gets the label displaying the remaining time.
		 *
		 * @return JLabel representing the player's remaining time.
		 */
		public JLabel getTimeLabel() {
			return timeLabel;
		}

		/**
		 * Gets the label displaying the player's score.
		 *
		 * @return JLabel labeling the player's score.
		 */
		public JLabel getScore() {
			return score;
		}
	}

	/**
	 * BoardPanel is a JPanel that represents the game board in Scrabble.
	 * It initializes and displays a grid of BoardCellPanels with appropriate colors and labels.
	 */
	private static class BoardPanel extends JPanel {
		/** The color of the text used for board modifier cells. */
		public static final Color MODIFIER_CELL_TEXT_COLOR = new Color(255, 255, 255);
		/**
		 * The color of the <code>JPanel</code> holding cells.
		 * This color fills in the gaps between cells and surrounds the board.
		 */
		public static final Color BOARDER_COLOR = new Color(112, 109, 109);

		// Constants for panel size percentages
		public static final float MAXIMUM_PANEL_SIZE_PERCENT = .55f;
		public static final float PREFERRED_PANEL_SIZE_PERCENT = .40f;
		public static final float MINIMUM_PANEL_SIZE_PERCENT = .2f;
		/** Spacing between board cells. */
		public static final int SPACING = 3;
		public static final float MAXIMUM_CELL_PERCENT = .04f;
		public static final float PREFERRED_CELL_PERCENT = .025f;
		public static final float MINIMUM_CELL_PERCENT = .02f;

		// Array of BoardCellPanels representing the game board cells
		private BoardCellPanel[][] boardCells;
		private int environmentHeight;
		private int environmentWidth;

		// Dimensions for panel and cell sizes
		private Dimension maxPanelSize, preferredPanelSize, minPanelSize,
					maxCellSize, preferredCellSize, minCellSize;

		/**
		 * Initializes the board panel, sets up dimensions, and adds cell panels.
		 */
		public BoardPanel() {
			// Get the screen dimensions from the default graphics environment
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			environmentWidth = gd.getDisplayMode().getWidth();
			environmentHeight = gd.getDisplayMode().getHeight();

			// Set up panel and cell dimensions
			setupDimensions();

			// Set the preferred and maximum size of the panel
			this.setPreferredSize(preferredPanelSize);
			this.setMaximumSize(maxPanelSize);

			// Set the background color of the board panel
			this.setBackground(BOARDER_COLOR);

			// Set the layout as a grid for the board cells
			this.setLayout(new GridLayout(Board.BOARD_ROWS,Board.BOARD_COLUMNS,SPACING,SPACING));
			setupBoardCellPanels();
		}

		/**
		 * Sets up the dimensions for the panel and cells based on screen size.
		 */
		private void setupDimensions() {
			minPanelSize = new Dimension((int)(MINIMUM_PANEL_SIZE_PERCENT*environmentHeight),
					(int)(MINIMUM_PANEL_SIZE_PERCENT*environmentHeight));
			preferredPanelSize = new Dimension((int)(PREFERRED_PANEL_SIZE_PERCENT*environmentHeight),
					(int)(PREFERRED_PANEL_SIZE_PERCENT*environmentHeight));
			maxPanelSize = new Dimension((int)(MAXIMUM_PANEL_SIZE_PERCENT*environmentHeight),
					(int)(MAXIMUM_PANEL_SIZE_PERCENT*environmentHeight));
			minCellSize = new Dimension((int)(MINIMUM_CELL_PERCENT*environmentHeight),
					(int)(MINIMUM_CELL_PERCENT*environmentHeight));
			preferredCellSize = new Dimension((int)(PREFERRED_CELL_PERCENT*environmentHeight),
					(int)(PREFERRED_CELL_PERCENT*environmentHeight));
			maxCellSize = new Dimension((int)(MAXIMUM_CELL_PERCENT*environmentHeight),
					(int)(MAXIMUM_CELL_PERCENT*environmentHeight));
		}

		/**
		 * Initializes the board cells and adds them to the panel.
		 */
		private void setupBoardCellPanels() {
			boardCells = new BoardCellPanel[Board.BOARD_ROWS][Board.BOARD_COLUMNS];
			for (int row = 0; row < Board.BOARD_ROWS; row++) {
				for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
					// Sets color and text based on the type of modifier for the cell
					JButton cell = new JButton();
					setColorAndText(cell, row, col);

					// Create a BoardCellPanel for each cell and add it to the panel
					boardCells[row][col] = new BoardCellPanel(cell);
					//setButtonSizes(boardCells[row][col]);
					this.add(boardCells[row][col]);
				}
			}
		}

		/**
		 * Sets the color and text of a button on the oard based on its modifier type.
		 *
		 * @param button the button that is being changed
		 * @param row the row of the button
		 * @param col the column of the button
		 */
		private void setColorAndText(JButton button, int row, int col) {
			Board.ModifierType mt = Board.MODIFIER_HASH_MAP.get(new Point(row, col));
			button.setBackground(mt.getColor());
			button.setText(mt.getAbbreviation());
			button.setForeground(MODIFIER_CELL_TEXT_COLOR);
			button.setBorderPainted(false);
		}

		/**
		 * Updates a cell at a specified row and column with a new button.
		 *
		 * @param button the new button to set
		 * @param row the row index of the cell
		 * @param col the column index of the cell
		 */
		public void setBoardCell(JButton button, int row, int col) {
			if (!(button instanceof TileButton)) {
				setColorAndText(button, row, col);
			}
			this.boardCells[row][col].setBoardButton(button);
			this.boardCells[row][col].revalidate();
			this.boardCells[row][col].repaint();
		}

		/**
		 * Disables a cell at a specific position on the board.
		 *
		 * @param row the row index of the cell.
		 * @param col the column index of the cell.
		 */
		public void disableBoardCell(int row, int col) {
			JButton button = boardCells[row][col].getBoardButton();
			button.setEnabled(false);
			GameScreenController.removeActionListeners(button);
		}

		/**
		 * Removes all <code>ActionListener</code>s from the button at a location.
		 *
		 * @param row the row of the button.
		 * @param col the column of the button.
		 */
		public void removeActionListeners(int row, int col) {
			JButton button = boardCells[row][col].getBoardButton();
			for (ActionListener al : button.getActionListeners()) {
				button.removeActionListener(al);
			}
		}

		/**
		 * Adds an action listener to a button at a location.
		 *
		 * @param al the <code>ActionListener</code> to add to the button.
		 * @param row the row of the button.
		 * @param col the column of the button.
		 */
		public void addActionListener(ActionListener al, int row, int col) {
			boardCells[row][col].addActionListener(al);
		}

		/**
		 * Gets whether a button at a location is a {@link TileButton}.
		 *
		 * @param row the row of the button to check.
		 * @param col the column of the button to check.
		 * @return True if the button is an instance of <code>TileButton</code>. False otherwise.
		 */
		public boolean instanceOfTileButton(int row, int col) {
			return (boardCells[row][col].getBoardButton() instanceof TileButton);
		}

		/**
		 * Gets the text of the button at a location.
		 *
		 * @param row the row of the button.
		 * @param col the column of the button.
		 * @return The text of the button at the location.
		 */
		public String getButtonText(int row, int col) {
			return boardCells[row][col].getBoardButton().getText();
		}

		/**
		 * Gets the button at a location.
		 * @param row the row the button to get.
		 * @param col the column the button to get.
		 * @return The <code>JButton</code> at the location.
		 * @see #getButtonText
		 * @see #instanceOfTileButton
		 * @see #addActionListener
		 * @see #removeActionListeners
		 */
		public JButton getButton(int row, int col) { return boardCells[row][col].getBoardButton(); }

		/**
		 * A panel that holds a single JButton representing a cell on the Scrabble board.
		 */
		private static class BoardCellPanel extends JPanel {
			// The button representing the cell in the panel.
			private JButton boardButton;

			/**
			 * Constructor that initializes the panel with a given button.
			 *
			 * @param button The button to be added to this panel.
			 */
			public BoardCellPanel(JButton button) {
				// Set a layout manager with zero horizontal and vertical gaps.
				FlowLayout manager = new FlowLayout();
				manager.setHgap(0);
				manager.setVgap(0);
				this.setLayout(manager);

				// Set the button for the panel.
				this.setBoardButton(button);
				//this.setPreferredSize(new Dimension(25, 25));
			}

			/**
			 * Replaces the current button in the panel with a new button.
			 *
			 * @param boardButton The new button to be set in the panel.
			 */
			public void setBoardButton(JButton boardButton) {
				// Remove the existing button, if present.
				if (this.boardButton != null) this.remove(this.boardButton);

				// Set the new button and adjust its font properties.
				this.boardButton = boardButton;
				this.boardButton.setFont(getFont().deriveFont(Font.BOLD, 15f));
				//this.boardButton.setPreferredSize(new Dimension(22, 22));

				// Add the new button to the panel and refresh the panel's state.
				this.add(this.boardButton);
				this.revalidate();
				this.repaint();
			}

			/**
			 * Gets the button currently in the panel.
			 *
			 * @return The JButton in the panel.
			 */
			public JButton getBoardButton() {
				return boardButton;
			}

			/**
			 * Adds an <code>ActionListener</code> to the button contained in this panel.
			 * @param al the action listener to add to the button.
			 */
			public void addActionListener(ActionListener al) {
				boardButton.addActionListener(al);
			}
		}
	}


}
