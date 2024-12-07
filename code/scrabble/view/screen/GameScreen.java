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
	private static final int GAP = 150; // Spacing used between panels
	public static final int RACK_SIZE = 7; // Number of tiles in a player's rack

	/**
	 * Represents the <code>Tile</code>s which have been played on the board during the current
	 * player's turn.
	 */
	public List<Tile> playedTiles = new ArrayList<>();  // List of tiles that have been played in the current turn
	/**
	 * The button which has been removed from the GUI but should be tracked.
	 */
	public JButton value = new JButton(" ");

	public final GameControls gameControls;

	public final PlayerPanel[] playerPanels;
	public int currentPlayerIndex;
	public BoardPanel boardPanel; // Panel representing the game board

	private JLabel gameTimeLabel; // Label displaying the game timer
	private int gameTimeRemaining; 	// in seconds;


	/**
	 * Creates a formatted string from a specified number of seconds.
	 * The <code>String</code> will be in the format "[number of minutes]:[number of seconds]",
	 * with a leading 0 if the minutes or seconds are under 10.
	 * @param timeRemaining the number of seconds for which to create a string.
	 * @return The string with specified formatting.
	 */
	public static String formatTime(int timeRemaining) {
		return String.valueOf(timeRemaining / 60 < 10 ? "0" + timeRemaining / 60 : timeRemaining / 60) + ':' +
				(timeRemaining % 60 < 10 ? "0" + timeRemaining % 60 : timeRemaining % 60);
	}

	/**
	 * Constructs GameScreen from the names of players, the amount of time for the game,
	 * the amount of time for each turn, and the starting tiles for this player.
	 *
	 * @param playerNames the names of the players in the order of turn
	 * @param gameTime in minutes
	 * @param playerTime in minutes
	 * @param rackTiles starting tiles
	 */
	public GameScreen(String[] playerNames, int gameTime, int playerTime, Tile[] rackTiles) {
		this.setLayout(new BorderLayout()); // Set layout for the main panel
		gameControls = new GameControls();

		// Initialize and add the panels for different sections of the game screen
		JPanel northPanel = setupNorthPanel();
		JPanel centerPanel = setupCenterPanel();
		JPanel eastPanel = new JPanel(new GridLayout(2, 1, 0, GAP));
		JPanel westPanel = new JPanel(new GridLayout(2, 1, 0, GAP));

		//Drop down menu
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(gameControls, BorderLayout.SOUTH);

		playerPanels = new PlayerPanel[playerNames.length];
		for (int i = 0; i < playerNames.length; i++) {
			playerPanels[i] = setupPlayerPanel(playerNames[i], playerTime);
			if (i == 1 || i == 2) {
				eastPanel.add(playerPanels[i]);
			} else {
				westPanel.add(playerPanels[i]);
			}
		}
		gameControls.getMainControlsPanel().getRackPanel().resetRack();
		gameControls.getMainControlsPanel().getRackPanel().addTilesToRack(rackTiles);
		gameTimeRemaining = gameTime*60;
		this.gameTimeLabel.setText(formatTime(gameTimeRemaining));
		this.revalidate();
		this.repaint();

		currentPlayerIndex = 0;
	}

	/**
	 * Decreases both the game time label and the current player's time label by one second.
	 * The formatting of the string is maintained by calls to {@link #formatTime}.
	 * A time of -1 is not allowed, so decreasing the time when it is 0 has no effect on that label.
	 */
	public void decrementTime() { this.decreaseTime(1); }

	/**
	 * Decreases both the game time label and the current player's time label by the specified amount.
	 * The formatting of the string is maintained by calls to {@link #formatTime}.
	 * A time of -1 is not allowed, so decreasing the time when it is 0 has no effect on that label.
	 */
	public void decreaseTime(int amount) {
		this.gameTimeRemaining = Math.max(0, gameTimeRemaining-amount);
		gameTimeLabel.setText(formatTime(gameTimeRemaining));
		playerPanels[currentPlayerIndex].decreaseTime(amount);
	}

	/**
	 * Gets the current player's time remaining.
	 * @return The amount of time which the current player has, in seconds.
	 */
	public int currentPlayerTime() { return playerPanels[currentPlayerIndex].getTimeRemaining(); }

	/**
	 * Gets the panel this uses to display game controls.
	 * @return The <code>GameControls</code> panel of this instance.
	 * @see GameControls
	 */
	public GameControls getGameControls() { return gameControls; }

	/**
	 * Disables the last played tiles on the board and resets <code>playedTiles</code>.
	 * @see #removeFromPlayedTiles
	 * @see #addToPlayedTiles
	 */
	public void disableLastPlayedTiles() {
		for (Tile t : playedTiles) {
			boardPanel.disableBoardCell(t.getLocation().x, t.getLocation().y);
		}
		playedTiles = new ArrayList<>();
	}

	/**
	 * Removes a specified <code>Tile</code> from the list of played tiles.
	 * The list of played tiles should be used to track the tiles which have been moved onto
	 * the game board.
	 * @param tile the tile to remove from the list.
	 */
	public void removeFromPlayedTiles(Tile tile) { playedTiles.remove(tile); }

	/**
	 * Adds a <code>Tile</code> to the list of played tiles.
	 * The list of played tiles should be used to track the tiles which have been moved onto
	 * the game board.
	 * @param tile the tile to add to the list.
	 * @see #removeFromPlayedTiles
	 * @see #getPlayedTiles
	 */
	public void addToPlayedTiles(Tile tile) { playedTiles.add(tile); }

	/**
	 * Gets the list of played tiles. This list should be used to track the tiles which have
	 * been placed on the game board by the user.
	 * @return List of tiles that have been played in the current turn.
	 * @see #removeFromPlayedTiles
	 * @see #addToPlayedTiles
	 * @see #disableLastPlayedTiles
	 */
	public List<Tile> getPlayedTiles() {
		return playedTiles;
	}

	/**
	 * Sets the <code>value</code> variable to the button specified.
	 *
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
	 * Sets the score of the player specified by name.
	 * @param playerName the name of the player for which to set the score.
	 *                   Must exactly match one of the strings from parameter <code>playerNames</code>
	 *                   in {@link #GameScreen}.
	 * @param score the score to set the player.
	 */
	public void updateScore(String playerName, int score) {
		for (PlayerPanel player : playerPanels) {
			if (player != null && Objects.equals(player.getPlayerName(), playerName)) {
				player.setScoreLabel(score);
			}
		}
	}

	/**
	 * Updates the state of the GUI to be the next player as was specified in the constructor.
	 */
	public void nextPlayer() {
		playerPanels[currentPlayerIndex].resetTime();
		playerPanels[currentPlayerIndex].setEnabled(false);
		playerPanels[currentPlayerIndex].setStatus(PlayerPanel.Status.ACTIVE);
		this.currentPlayerIndex++;
		this.currentPlayerIndex %= playerPanels.length;
		playerPanels[currentPlayerIndex].setEnabled(true);
		playerPanels[currentPlayerIndex].setStatus(PlayerPanel.Status.PLAYING);
	}

	public void passTurn() {


	}

	/**
	 * Adds tiles to the board as <code>TileButton</code>s, at the locations specified by
	 * each tile's <code>location</code> field. If location is not set, a null pointer will be thrown.
	 * @param tiles the tiles to add to the board. Each location must be set.
	 */
	public void addToBoard(Tile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
			TileButton tb = new TileButton(tiles[i]);
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

	/* creates a player panel with the default score of 0. */
	private PlayerPanel setupPlayerPanel(String name, int playerTime) {
		return new PlayerPanel(name, 0, playerTime*60);
	}

	/* Center panel: contains the Game Board (in all its glory). */
	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new FlowLayout());

		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));

		boardPanel = new BoardPanel();
		centerPanel.add(boardPanel);
		return centerPanel;
	}

	/* North panel: contains the game time label. */
	private JPanel setupNorthPanel() {
		JPanel northPanel = new JPanel(new FlowLayout());
		gameTimeLabel = new JLabel("00:00");
		gameTimeLabel.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTimeLabel);
		return northPanel;
	}

	public void setDisconnected(String playerName) {
		for (PlayerPanel p : playerPanels) {
			if (p.getPlayerName().equals(playerName)) {
				p.setStatus(PlayerPanel.Status.DISCONNECTED);
			}
		}
	}

	/**
	 * PlayerPanel displays the details of a player in the game, showing the player's
	 * name, current score, and remaining time.
	 */
	public static class PlayerPanel extends JPanel {
		// Labels to display player's name, time, and score
		private final JLabel timeLabel;
		private final JLabel scoreLabel;
		private final JLabel statusLabel;
		private final String name;
		private final int defaultTime;
		private int time;
		private Status status;

		/**
		 * Constructor for PlayerPanel. Sets up the panel layout and populates it with player information.
		 *
		 * @param playerName The name of the player.
		 * @param score The player's current score.
		 * @param time The player's remaining time in seconds.
		 */
		public PlayerPanel(String playerName, int score, int time) {
			this.setLayout(new GridLayout(3,2,7,10));

			this.name = playerName;
			this.setBorder(BorderFactory.createTitledBorder(playerName));

			JLabel playerTimeLabel = new JLabel("Time:", SwingConstants.RIGHT);
			this.time = this.defaultTime = time;
			this.timeLabel = new JLabel(GameScreen.formatTime(time));

			JLabel playerScoreLabel = new JLabel("Score:", SwingConstants.RIGHT);
			this.scoreLabel = new JLabel("" + score);

			JLabel emptyLabel = new JLabel("");
			statusLabel = new JLabel("Active");
			statusLabel.setForeground(Color.GREEN);

			this.add(playerTimeLabel);
			this.add(this.timeLabel);
			this.add(playerScoreLabel);
			this.add(this.scoreLabel);
			this.add(emptyLabel);
			this.add(statusLabel);
		}

		/**
		 * Gets the label displaying the player's name.
		 *
		 * @return JLabel labeling the player's name.
		 */
		public String getPlayerName() { return name; }

		public void decrementTime() { decreaseTime(1); }

		public void decreaseTime(int amount) {
			this.time = Math.max(0, this.time-amount);
			timeLabel.setText(GameScreen.formatTime(time));
		}

		public int getTimeRemaining() { return time; }

		public void increaseScore(int amount) { this.scoreLabel.setText(Integer.parseInt(scoreLabel.getText()) + amount + "");}

		public void setScoreLabel(int score) { this.scoreLabel.setText(score+""); }

		public void resetTime() {this.time = defaultTime; timeLabel.setText(GameScreen.formatTime(time));}

		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			this.timeLabel.setEnabled(enabled);
			this.scoreLabel.setEnabled(enabled);
		}

		public Status getStatus() {return status; }

		public void setStatus(Status status) {
			this.status = status;
			this.statusLabel.setText(status.name);
			this.statusLabel.setForeground(status.color);
		}

		public enum Status {
			ACTIVE("Active", Color.GREEN), INACTIVE("Inactive", Color.darkGray),
			DISCONNECTED("Disconnected", Color.red.darker()), PLAYING("Playing", Color.BLUE);

			public final String name;
			public final Color color;

			Status(String name, Color color) {
				this.name = name;
				this.color = color;
			}

			public Color getColor() {return this.color;}
			public String getName() {return this.name; }
		}
	}

	/**
	 * BoardPanel is a JPanel that represents the game board in Scrabble.
	 * It initializes and displays a grid of BoardCellPanels with appropriate colors and labels.
	 */
	public static class BoardPanel extends JPanel {
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
		private final int environmentHeight;

		// Dimensions for panel and cell sizes
		private Dimension maxPanelSize;
		private Dimension preferredPanelSize;

		/**
		 * Initializes the board panel, sets up dimensions, and adds cell panels.
		 */
		public BoardPanel() {
			// Get the screen dimensions from the default graphics environment
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
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
			Dimension minPanelSize = new Dimension((int) (MINIMUM_PANEL_SIZE_PERCENT * environmentHeight),
					(int) (MINIMUM_PANEL_SIZE_PERCENT * environmentHeight));
			preferredPanelSize = new Dimension((int)(PREFERRED_PANEL_SIZE_PERCENT*environmentHeight),
					(int)(PREFERRED_PANEL_SIZE_PERCENT*environmentHeight));
			maxPanelSize = new Dimension((int)(MAXIMUM_PANEL_SIZE_PERCENT*environmentHeight),
					(int)(MAXIMUM_PANEL_SIZE_PERCENT*environmentHeight));
			Dimension minCellSize = new Dimension((int) (MINIMUM_CELL_PERCENT * environmentHeight),
					(int) (MINIMUM_CELL_PERCENT * environmentHeight));
			Dimension preferredCellSize = new Dimension((int) (PREFERRED_CELL_PERCENT * environmentHeight),
					(int) (PREFERRED_CELL_PERCENT * environmentHeight));
			Dimension maxCellSize = new Dimension((int) (MAXIMUM_CELL_PERCENT * environmentHeight),
					(int) (MAXIMUM_CELL_PERCENT * environmentHeight));
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
		public static class BoardCellPanel extends JPanel {
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

	/**
	 * Allows controls to be swapped out on the bottom of the Game Screen.
	 * <p>
	 *     Uses a <code>CardLayout</code> to display an exchange tiles, main controls, and
	 *     blank tile panel depending on the user's inputs.
	 * </p>
	 */
	public static class GameControls extends JPanel {
		private static final String RACK_PANEL = "RACK";
		private static final String EXCHANGE_PANEL = "EXCHANGE";
		private static final String BLANK_PANEL = "BLANK";

		private final CardLayout layout;

		private final MainControlsPanel mainControlsPanel;
		private final ExchangePanel exchangePanel;
		private final BlankPanel blankPanel;

		/**
		 * Constructs a <code>GameControls</code> object with the three different views.
		 */
		public GameControls() {
			layout = new CardLayout();
			this.setLayout(layout);
			mainControlsPanel = new MainControlsPanel();
			exchangePanel = new ExchangePanel();
			blankPanel = new BlankPanel();
			this.add(blankPanel, BLANK_PANEL);
			this.add(mainControlsPanel, RACK_PANEL);
			this.add(exchangePanel, EXCHANGE_PANEL);
			layout.show(this, RACK_PANEL);
		}

		/** Shows the Exchange screen on this component. */
		public void showExchange() { layout.show(this, EXCHANGE_PANEL); }
		/** Shows the Main controls (with tile rack) screen on this component. */
		public void showRack() { layout.show(this, RACK_PANEL); }
		/** Shows the Blank Tile screen on this component. */
		public void showBlank() { layout.show(this, BLANK_PANEL); }

		/* * * * * * * * * * * * *
		 * 		  Getters 		 *
		 * * * * * * * * * * * * */

		/**
		 * Gets the <code>MainControlsPanel</code>.
		 * @return The panel which is used for the primary controls.
		 * @see MainControlsPanel
		 */
		public MainControlsPanel getMainControlsPanel() {
			return mainControlsPanel;
		}

		/**
		 * Gets the <code>ExchangePanel</code>.
		 * @return The panel which is used to exchange tile(s).
		 * @see ExchangePanel
		 */
		public ExchangePanel getExchangePanel() {
			return exchangePanel;
		}

		/**
		 * Gets the <code>BlankPanel</code>.
		 * @return The panel which is used to set the letter of a blank tile.
		 * @see BlankPanel
		 */
		public BlankPanel getBlankPanel() {
			return blankPanel;
		}

		/**
		 * The panel for setting the letter of a blank tile to some letter.
		 * <p>
		 *     Uses a <code>JComboBox</code> to allow a letter to be selected.
		 *     The user must press the submit button to trigger the letter being set.
		 * </p>
		 */
		public static class BlankPanel extends JPanel {
			private Character[] alphabet;		// the options for the blank tile.

			private final JComboBox<Character> letterSelect;		// box for selecting a letter.
			private final JButton submitButton;		// Submit button for setting the letter.

			/**
			 * Constructs a panel for setting a blank tile.
			 * Contains the combo box and submit button.
			 */
			public BlankPanel () {
				this.setLayout(new FlowLayout());

				JPanel controlsPanel = new JPanel(new GridLayout(3,1,0,10));
				JLabel chooseLabel = new JLabel("Which Letter would you like?");
				setAlphabet();
				letterSelect = new JComboBox<>(alphabet);
				submitButton = new JButton("Submit");
				controlsPanel.add(chooseLabel);
				controlsPanel.add(letterSelect);
				controlsPanel.add(submitButton);
				this.add(controlsPanel);
			}

			public void removeSubmitListeners() {GameScreenController.removeActionListeners(submitButton); }
			public void addSubmitActionListener(ActionListener al) { submitButton.addActionListener(al); }

			/*
			 * stores all letters of the English alphabet in the Character array.
			 */
			private void setAlphabet() {
				this.alphabet = new Character[26];
				for (int i = 0; i < Tile.TileScore.values().length - 1; i++) {
					alphabet[i] = Tile.TileScore.values()[i].getLetter();
				}
			}

			/**
			 * Gets the letter which has been selected in the combo box.
			 * @return The letter which was selected, as a <code>Character</code>.
			 */
			public Character getSelectedLetter() {
				return (Character) letterSelect.getSelectedItem();
			}
		}

		/**
		 * The panel for choosing a letter or all letters to be exchanged for new tile(s).
		 * <p>
		 *     Uses two <code>JComboBox</code> to select letters, one to set the number of tiles to exchange,
		 *     one to select a letter if only one is to be exchanged.
		 * </p>
		 */
		public static class ExchangePanel extends JPanel {
			private static final String ONE = "One";
			private static final String ALL = "All";

			private final JButton backButton;
			private final JButton submitButton;
			private final JComboBox<String> numberSelect;
			private final JComboBox<Character> letterSelect;


			/**
			 * Constructs a panel for exchanging tiles.
			 */
			public ExchangePanel() {
				this.setLayout(new FlowLayout());
				JPanel exchangePanel = new JPanel(new GridLayout(1, 2, 30, 0));

				JPanel centerPanel = new JPanel(new GridLayout(3,1,20,10));
				JLabel exchangeText = new JLabel("Exchange One or All:");
				numberSelect = new JComboBox<>(new String[]{ONE, ALL});
				letterSelect = new JComboBox<>(new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'});
				centerPanel.add(exchangeText);
				centerPanel.add(numberSelect);
				centerPanel.add(letterSelect);

				JPanel eastPanel = new JPanel(new GridLayout(2,1,10, 20));
				backButton = new JButton("Go Back");
				submitButton = new JButton("Submit");
				eastPanel.add(backButton, 0);
				eastPanel.add(submitButton,1);

				exchangePanel.add(centerPanel);
				exchangePanel.add(eastPanel);
				this.add(exchangePanel);
			}

			public void addBackActionListener(ActionListener al) { backButton.addActionListener(al); }
			public void removeBackListeners() {GameScreenController.removeActionListeners(backButton);}

			public void addSubmitActionListener(ActionListener al) { submitButton.addActionListener(al); }
			public void removeSubmitListeners() {GameScreenController.removeActionListeners(submitButton);}

			/**
			 * Removes all letters stored in the <code>JComboBox</code> used for selecting a
			 * single letter.
			 */
			public void removeAllLetters() {
				letterSelect.removeAllItems();
			}

			/**
			 * Adds letters into the <code>JComboBox</code> used for selecting a single letter.
			 * @param letters the letters to put into the combo box.
			 */
			public void addLetters(char[] letters) {
				for (Character l : letters) {
					letterSelect.addItem(l);
				}
			}

			/**
			 * Gets the letter which was selected in the box.
			 * @return The letter selected as a <code>Character</code>.
			 */
			public Character getSelectedLetter() {
				return (Character) letterSelect.getSelectedItem();
			}

			public void addNumberSelectActionListener(ActionListener al) {numberSelect.addActionListener(al);}

			/**
			 * Gets the number of tiles which are to be exchanged.
			 * @return The number of tiles to exchange. 1 or <code>RACK_SIZE</code>.
			 */
			public int getNumberToExchange() {
				String selected = (String) numberSelect.getSelectedItem();
				return (Objects.equals(selected, ONE) ? 1 : RACK_SIZE);
			}

			/**
			 * Sets the letter select box to be enabled or disabled. When the combo box is disabled,
			 * items cannot be selected and values cannot be typed into its field (if it is editable).
			 * @param enable whether to enable or disable the box. True to enable.
			 */
			public void enableLetterSelect(boolean enable) {
				letterSelect.setEnabled(enable);
			}
		}

		/**
		 * The panel for choosing options and for rack tiles.
		 * Allows users to choose to pass turn, play tiles on the board, and exchange tiles.
		 */
		public static class MainControlsPanel extends JPanel {
			private final RackPanel rackPanel;		//

			private final JButton passButton;
			private final JButton submitButton;
			private final JButton exchangeButton;
			private final JButton challengeButton;


			/**
			 * Constructs a main controls panel with buttons for passing, exchanging, submitting, and challenging (disabled).
			 */
			public MainControlsPanel() {
				this.setLayout(new FlowLayout());
				JPanel gameControlsPanel = new JPanel(new BorderLayout());
				JPanel subAndPass = new JPanel(new GridLayout(2, 1, 0, 10));
				JPanel centerRack = new JPanel(new FlowLayout());
				centerRack.setBorder(new TitledBorder("Rack"));
				JPanel exAndChall = new JPanel(new GridLayout(2,1,0,10));
				passButton = new JButton("Pass Turn");
				submitButton = new JButton("Submit");
				submitButton.setPreferredSize(new Dimension(50, 10));
				exchangeButton = new JButton("Exchange Tiles");
				challengeButton = new JButton("Challenge");
				challengeButton.setEnabled(false);

				this.rackPanel = new RackPanel();

				subAndPass.add(submitButton);
				subAndPass.add(passButton);
				gameControlsPanel.add(subAndPass, BorderLayout.WEST);
				centerRack.add(rackPanel);
				gameControlsPanel.add(centerRack, BorderLayout.CENTER);
				exAndChall.add(exchangeButton);
				exAndChall.add(challengeButton);
				gameControlsPanel.add(exAndChall, BorderLayout.EAST);
				this.add(gameControlsPanel);
			}


			public void addSubmitActionListener(ActionListener al) {
				submitButton.addActionListener(al);
			}

			public void removeSubmitListeners() {
				GameScreenController.removeActionListeners(submitButton);
			}

			public void addExchangeActionListener(ActionListener al) { exchangeButton.addActionListener(al);}
			public void removeExchangeListeners() { GameScreenController.removeActionListeners(exchangeButton); }

			public void addPassActionListener(ActionListener al) { passButton.addActionListener(al);}
			public void removePassListeners() { GameScreenController.removeActionListeners(passButton); }

			public JButton getChallengeButton() { return challengeButton; }

			public RackPanel getRackPanel() { return rackPanel; }

			public void setButtonsEnabled(boolean enabled) {
				submitButton.setEnabled(enabled);
				passButton.setEnabled(enabled);
				exchangeButton.setEnabled(enabled);
				rackPanel.setRackButtonsEnabled(enabled);
			}

			public char[] getRackLetters() {
				RackPanel.TilePanel[] panels = rackPanel.tilePanels;
				char[] letters = new char[RACK_SIZE];
				for (int i = 0; i < panels.length; i++) {
					letters[i] = panels[i].getButton().getText().charAt(0);
				}
				return letters;
			}

			public Tile[] getRackTiles() {
				char[] characters = getRackLetters();
				ArrayList<Tile> tiles = new ArrayList<>(RACK_SIZE);
				for (int i = 0; i < characters.length; i++) {
					boolean cInValues = false;
					for (int j = 0; j < Tile.TileScore.values().length && !cInValues; j++) {
						if (characters[i] == Tile.TileScore.values()[j].getLetter()) cInValues = true;
					}
					if (cInValues) tiles.add(new Tile(Tile.TileScore.scoreValueOf(characters[i] + "")));
				}
				return tiles.toArray(new Tile[0]);
			}


			/**
			 * RackPanel is a JPanel that represents a rack of TilePanels,
			 * displaying the tiles a player currently holds.
			 */
			public static class RackPanel extends JPanel {
				// Array to hold the TilePanels that make up the player's rack

				private final TilePanel[] tilePanels;

				public RackPanel() {
					tilePanels = new TilePanel[RACK_SIZE];
					this.setLayout(new GridLayout(1, RACK_SIZE, 10, 0));

					for (int i = 0; i < tilePanels.length; i++) {
						tilePanels[i] = new TilePanel(new TileButton(new Tile(Tile.TileScore.values()[i])));
						this.add(tilePanels[i]);
					}
				}

				/**
				 * Constructor to initialize the RackPanel with an array of TilePanels.
				 *
				 * @param tileButtons an array of TileButtons to be displayed in the rack.
				 */
				public RackPanel(TileButton[] tileButtons){
					// Assigns the given array of TilePanels to the class field
					this.tilePanels = new TilePanel[tileButtons.length];

					// Sets the layout of the panel to a GridLayout with 1 row, RACK_SIZE columns,
					// and a horizontal gap of 10 pixels between components
					this.setLayout(new GridLayout(1, RACK_SIZE, 10, 0));

					// Adds each TilePanel to the RackPanel
					for (int i = 0; i < tileButtons.length; i++) {
						this.tilePanels[i] = new TilePanel(tileButtons[i]);
						this.add(this.tilePanels[i]);
					}
				}

				/**
				 * Replaces the button at a specific position in the rack.
				 * Invoking this method revalidates and repaints the rack.
				 *
				 * @param tileButton the new JButton to be set in the specified TilePanel
				 * @param i the index of the TilePanel where the button will be set
				 */
				public void setButton(JButton tileButton, int i) {
					// Sets a new button in the specified TilePanel at index i
					tilePanels[i].setButton(tileButton);

					// Revalidates the panel to ensure the new button is displayed properly
					this.revalidate();

					// Repaints the panel
					this.repaint();
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

				/**
				 * Adds all specified tiles into the <code>GameScreen</code>'s rack as buttons.
				 *
				 * @param tiles Array of tiles to be added.
				 */
				public void addTilesToRack(Tile[] tiles) {
					for(Tile t : tiles) {
						TileButton button = new TileButton(t);
						addTileButtonToRack(button);
					}
				}

				public void removeFromRack(String letter) {
					for(int i = 0; i < tilePanels.length; i++){
						if(tilePanels[i].getButton().getText().equals(letter)){
							tilePanels[i].setButton(new JButton(" "));
							return;
						}
					}
				}

				public JButton removeButtonFromRack(int col) {
					JButton b = tilePanels[col].getButton();
					tilePanels[col].setButton(new JButton(" "));
					return b;
				}

				public void setRackButtonsEnabled (boolean enabled) {
					for (TilePanel tp : tilePanels) {
						tp.getButton().setEnabled(enabled);
					}
				}

				public void addRackTileActionListener(ActionListener al, int index) {
					tilePanels[index].addButtonActionListener(al);
				}

				public void removeActionListeners(int index) {
					tilePanels[index].removeButtonActionListeners();
				}

				public void resetRack() {
					for (TilePanel tp : tilePanels) {
						tp.setButton(new JButton(" "));
					}
				}

				public void soutRack() {
					for (TilePanel p : tilePanels) {
						System.out.println(p.getButton().getText());
					}
				}

				/**
				 * TilePanel is a JPanel component that holds a TileButton and provides
				 * methods for adding and updating the button in the panel.
				 */
				private static class TilePanel extends JPanel {
					// A JButton to represent the tile displayed in this panel
					private JButton tileButton;

					/**
					 * Constructor that initializes the TilePanel with a given TileButton.
					 *
					 * @param tileButton the initial TileButton to be displayed in this panel
					 */
					public TilePanel(TileButton tileButton) {
						// Set the layout to a flow layout to manage the placement of the button
						this.setLayout(new FlowLayout());
						this.setButton(tileButton); // Set the button in the panel
					}

					/**
					 * Getter for the current TileButton in the panel.
					 *
					 * @return the current JButton instance
					 */
					public JButton getButton() { return tileButton; }

					/**
					 * Sets a new button in the panel. If a button already exists, it removes the old one
					 * before adding the new button.
					 *
					 * @param tileButton the new JButton to be added to the panel
					 */
					public void setButton(JButton tileButton) {
						// Remove the existing button if there is one
						if (this.tileButton != null) this.remove(this.tileButton);

						// Assign the new button and set its font properties
						this.tileButton = tileButton;
						this.tileButton.setFont(getFont().deriveFont(Font.BOLD, 12f));

						// Add the button to the panel and refresh the panel display
						this.add(this.tileButton);
						this.revalidate(); // Revalidates the component hierarchy
						this.repaint(); // Repaints the component to reflect changes
					}

					public void addButtonActionListener(ActionListener al) {
						this.tileButton.addActionListener(al);
					}

					public void removeButtonActionListeners() {
						GameScreenController.removeActionListeners(this.tileButton);
					}
				}
			}
		}
	}
}
