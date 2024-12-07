package scrabble.controller;

import scrabble.model.Board;
import scrabble.model.Tile;
import scrabble.network.messages.*;
import scrabble.view.ScrabbleGUI;
import scrabble.view.TileButton;
import scrabble.view.screen.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.*;
import java.util.Timer;

/**
 * GameScreenController is the controller that takes care of all actions of the Game screen
 */
public class GameScreenController {
	private final Controller parent;
	private final ScrabbleGUI gui;
	private GameScreen gameScreen;
	private GameScreen.GameControls gameControls;

	private GameTimeController gameTimeController;
	private boolean isRackEnabled;
	private JButton value;
	private final List<Tile> lastPlayedTiles = new ArrayList<>();
	private Point blankTileLocation;

	/**
	 * Removes the action listener for a tile that has been played and will not be able
	 * 		to be removed from the board
	 *
	 * @param button the JButton that the action listener is being removed from
	 */
	public static void removeActionListeners(JButton button) {
		for (ActionListener al : button.getActionListeners()) {
			button.removeActionListener(al);
		}
	}

	/**
	 * Constructor for the Game Screen
	 *
	 * @param parent the player's game controller
	 * @param gui the gui which has a game screen to which listeners will be added
	 */
	public GameScreenController(Controller parent, ScrabbleGUI gui) {
		this.parent = parent;
		this.gui = gui;
	}

	/**
	 * Sets the names, times, and starting tiles for a player
	 *
	 * @param names the array of names of the players playing
	 * @param gameTime the total time set for the entire game
	 * @param turnTime the time each individual player will have on each turn
	 * @param startingTiles the tiles that will be on the player's starting rack
	 */
	public void setupGameItems(String[] names, int gameTime, int turnTime, Tile[] startingTiles) {
		gui.makeGameScreen(names, gameTime, turnTime, startingTiles);
		this.gameScreen = gui.getGame();
		this.gameControls = gameScreen.getGameControls();
		gameTimeController = new GameTimeController(gameTime);
		addActionListeners();
		gameScreen.repaint();
	}

	public void halt() { if (gameTimeController!=null) gameTimeController.cancel(); }

	/**
	 * Sets up the action listeners for the menu items
	 *
	 * @param view the frame that holds the menu seen on the Game Screen
	 */
	public void setupMenuListeners(ScrabbleGUI view) {
		view.getRulesItem().addActionListener(e -> rulesMenuClick());
		view.getAudioItem().addActionListener(e -> audioMenuClick());
		view.getFxItem().addActionListener(e -> fxMenuClick());
		view.getQuitItem().addActionListener(e -> quitMenuClick());
	}

	/**
	 * Calls the method in the gameScreen class that shows the values of the tiles on the board
	 *
	 * @param tiles an array of Tile objects to be displayed on the board
	 */
	public void addToBoard(Tile[] tiles) { SwingUtilities.invokeLater(()->gameScreen.addToBoard(tiles)); }

	/**
	 * Removes a singular tile from the board and places it in the rack.
	 *
	 * @param tile the individual Tile object to be removed from the board. The location of the tile
	 *             is the location of the board cell to remove.
	 */
	public void removeTileFromBoard(Tile tile){ boardCellClick(tile.getLocation().x, tile.getLocation().y); }

	/**
	 * Changes the displayed score for a player on the Game Screen
	 *
	 * @param name the name of the player whose score is to be changed
	 * @param newScore the score the player now has
	 */
	public void updateScore(String name, int newScore) { SwingUtilities.invokeLater(()->gameScreen.updateScore(name, newScore)); }

	/**
	 * Disables the tiles that were just played on the board
	 */
	public void disableLastPlayedTiles() { SwingUtilities.invokeLater(gameScreen::disableLastPlayedTiles); }

	public void nextPlayer() {
		SwingUtilities.invokeLater(gameScreen::nextPlayer);
	}

	/**
	 * Adds the tiles for the player to their rack
	 *
	 * @param tiles the Tile objects that are the player's new tiles
	 */
	public void addRackTiles(Tile[] tiles) {
		SwingUtilities.invokeLater(() -> {
			removeRackTileListeners();
			gameControls.getMainControlsPanel().getRackPanel().addTilesToRack(tiles);
			addRackTileListeners();
			setGameControlButtonsEnabled(this.isRackEnabled);
		});

		/*
		removeRackTileListeners();
		gameControls.getMainControlsPanel().getRackPanel().addTilesToRack(tiles);
		addRackTileListeners();
		setGameControlButtonsEnabled(this.isRackEnabled);
		 */
	}

	/**
	 * Removes a single tile from the board
	 *
	 * @param tile the Tile object to be taken off of the rack
	 */
	public void removeRackTile(Tile tile) {
		gameControls.getMainControlsPanel().getRackPanel().removeFromRack(tile.getLetter() + "");
	}

	/**
	 * Removes the tiles that were just played on the board
	 * Used when a word played on the board is not a valid word
	 */
	public void resetLastPlay() {

			int size = gameScreen.getPlayedTiles().size();
			for (int i = 0; i < size; ++i){
				removeTileFromBoard(gameScreen.getPlayedTiles().get(0));
			}

//		int size = gameScreen.getPlayedTiles().size();
//		for (int i = 0; i < size; ++i){
//			removeTileFromBoard(gameScreen.getPlayedTiles().get(0));
//		}
	}

	/**
	 * Enables/disables the buttons of the players rack based on if it's their turn or not
	 *
	 * @param enabled the boolean variable that says if it should be disabled or not
	 */
	public void setGameControlButtonsEnabled(boolean enabled) {
		this.isRackEnabled = enabled;
		SwingUtilities.invokeLater(() -> gameControls.getMainControlsPanel().setButtonsEnabled(enabled));
	}

	/*
	 * private methods
	 */

	/**
	 * Adds the action listeners for the buttons on the game screen
	 */
	private void addActionListeners() {
		addBoardCellListeners();

		addMainControlsListeners();
		addExchangePanelListeners();
		addBlankPanelListeners();
	}

	private void addMainControlsListeners() {
		SwingUtilities.invokeLater(()->{
			addRackTileListeners();
			addMainSubmitActionListener();
			addPassTurnActionListener();
			addExchangeButtonActionListener();
			addChallengeButtonActionListener();
		});
	}
	private void addPassTurnActionListener() {
		gameControls.getMainControlsPanel().addPassActionListener(e -> passTurnClick());
	}

	private void passTurnClick() {
		int choice = parent.showConfirmPassTurnDialog();
		if (choice == JOptionPane.YES_OPTION) {
			int playerID = parent.getSelfID();
			PassTurn pass = new PassTurn(playerID, playerID);
			pass.execute(parent);
		}
	}

	private void addExchangeButtonActionListener() {
		gameControls.getMainControlsPanel().addExchangeActionListener(e -> exchangeButtonClick());
	}
	private void exchangeButtonClick() {
		GameScreen.GameControls.ExchangePanel ep = gameControls.getExchangePanel();
		ep.removeAllLetters();

		char[] addToBox = new char[GameScreen.RACK_SIZE];
		int addToBoxNextIndex = 0;
		List<Tile> played = gameScreen.getPlayedTiles();
		if (!played.isEmpty()) {
			for (Tile t : played) {
				addToBox[addToBoxNextIndex] = t.getLetter();
				addToBoxNextIndex++;
			}
		}
		for (char c : gameControls.getMainControlsPanel().getRackLetters()) {
			boolean cInValues = false;
			for (int i = 0; i < Tile.TileScore.values().length && !cInValues; i++) {
				if (c == Tile.TileScore.values()[i].getLetter()) cInValues = true;
			}
			if (cInValues) {
				addToBox[addToBoxNextIndex] = c;
				addToBoxNextIndex++;
			}
		}

		ep.addLetters(addToBox);
		gameControls.showExchange();
	}

	private void addChallengeButtonActionListener() {
		gameControls.getMainControlsPanel().getChallengeButton().addActionListener(e -> System.out.println("get spawn killed :/"));
	}

	private void addExchangePanelListeners() {
		addNumberSelectActionListener();
		addExchangeBackButtonActionListener();
		addExchangeSubmitActionListener();
	}
	private void addNumberSelectActionListener() {
		gameControls.getExchangePanel().addNumberSelectActionListener(e -> numberSelectChange());
	}

	private void numberSelectChange() {
		GameScreen.GameControls.ExchangePanel ep = gameControls.getExchangePanel();
		ep.enableLetterSelect(ep.getNumberToExchange() != GameScreen.RACK_SIZE);
	}

	private void addExchangeBackButtonActionListener() {
		gameControls.getExchangePanel().addBackActionListener(e -> gameControls.showRack());
	}
	private void addExchangeSubmitActionListener() {
		gameControls.getExchangePanel().addSubmitActionListener(e -> exchangeSubmitClick());
	}

	private void exchangeSubmitClick() {
		SwingUtilities.invokeLater(()->{
			if (gameScreen.getValue() instanceof TileButton) {
				gameControls.getMainControlsPanel().getRackPanel().addTileButtonToRack((TileButton) gameScreen.getValue());
				gameScreen.setValue(new JButton(" "));
			}
			int playerId = parent.getSelfID();
			resetLastPlay();

			Tile[] tiles = (gameControls.getExchangePanel().getNumberToExchange() == GameScreen.RACK_SIZE
					? gameControls.getMainControlsPanel().getRackTiles()
					: new Tile[] {new Tile(Tile.TileScore.scoreValueOf(gameControls.getExchangePanel().getSelectedLetter() + ""))}
			);

			ExchangeTiles exchangeTiles = new ExchangeTiles(playerId, playerId, tiles);
			exchangeTiles.execute(parent);
			gameControls.showRack();
		});
//		int playerId = parent.getSelfID();
//
//		Tile[] tiles = (gameControls.getExchangePanel().getNumberToExchange() == GameScreen.RACK_SIZE
//				? gameControls.getMainControlsPanel().getRackTiles()
//				: new Tile[] {new Tile(Tile.TileScore.scoreValueOf(gameControls.getExchangePanel().getSelectedLetter() + ""))}
//		);
//
//		ExchangeTiles exchangeTiles = new ExchangeTiles(playerId, playerId, tiles);
//		exchangeTiles.execute(parent);
//		gameControls.showRack();
	}

	private void addBlankPanelListeners() {
		gameControls.getBlankPanel().addSubmitActionListener(e -> blankSubmitClick());
	}
	private void blankSubmitClick() {
		char letter = gameControls.getBlankPanel().getSelectedLetter();
		TileButton newTile = (TileButton)gameScreen.getValue();
		newTile.setTileLetter(Tile.TileScore.scoreValueOf(letter + ""));
		newTile.setTileLocation(blankTileLocation);

		gameScreen.addToPlayedTiles(newTile.getTile());
		gameScreen.setBoardCellOfBoardPanel(newTile, blankTileLocation.x, blankTileLocation.y);
		addBoardCellPanelListener(blankTileLocation.x, blankTileLocation.y);
		blankTileLocation = null;
		gameScreen.setValue(new JButton(" "));

		//Play audio for when tiles are being placed on the board
		parent.playTileFx();

		gameControls.showRack();
	}


	/**
	 * Adds the listeners to each panel of the board
	 */
	private void addBoardCellListeners() {
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				addBoardCellPanelListener(row, col);
			}
		}
	}
	/**
	 * Creates the action listener for each panel of the board
	 *
	 * @param row the row of the given panel
	 * @param col the column of the given panel
	 */
	private void addBoardCellPanelListener(int row, int col) {
		gameScreen.addActionListenerToBoardCell((e -> boardCellClick(row, col)), row, col);
	}

	/**
	 * Puts a tile into the cell that is clicked by the player
	 * If the button in this location is a TileButton, we put it back into the rack
	 * 		then add the new tile in that location
	 * If the button is not a TileButton, we just add the new tile to the board
	 *
	 * @param row the row of the button that is clicked
	 * @param col the column of the button that is clicked
	 */
	private void boardCellClick(int row, int col) {
		if (gameScreen.instanceOfTileButton(row, col)) {
			//put in rack
			TileButton tile = (TileButton) gameScreen.boardPanel.getButton(row, col);
			if (tile.getTile().isBlank() && tile.isEnabled()) {
				tile.getTile().resetLetter();
			}
			gameScreen.removeBoardPanelActionListeners(row, col);
			gameScreen.removeFromPlayedTiles(tile.getTile());
			int index = gameControls.getMainControlsPanel().getRackPanel().addTileButtonToRack(
					(TileButton) gameScreen.getBoardButton(row, col));
			addTilePanelListener(index);
		}
		// add value to panel

		JButton toAdd = gameScreen.getValue();
		removeActionListeners(toAdd);

		boolean toAddIsBlank = false;

		if (toAdd instanceof TileButton tileButton) {
            if (tileButton.getTile().isBlank()) {
				gameControls.showBlank();
				blankTileLocation = new Point(row, col);
				toAddIsBlank = true;
			} else {
				tileButton.setTileLocation(new Point(row, col));
				gameScreen.addToPlayedTiles(tileButton.getTile());
			}
		}
		if (!toAddIsBlank) {
			gameScreen.setBoardCellOfBoardPanel(toAdd, row, col);
			addBoardCellPanelListener(row, col);
			gameScreen.setValue(new JButton(" "));

			//Play audio for when tiles are being placed on the board
			parent.playTileFx();
		}
	}
	/**
	 * Adds the listeners to each panel of the rack
	 */
	private void addRackTileListeners(){
		for (int col = 0; col < 7; col++) {
			addTilePanelListener(col);
		}
	}
	/* Creates the action listener for each button in each panel */
	private void addTilePanelListener(int col) { gameControls.getMainControlsPanel().getRackPanel().addRackTileActionListener(e -> tilePanelClick(col), col); }
	/* Takes the clicked tile off the rack to place it on the board */
	private void tilePanelClick(int col) {
		GameScreen.GameControls.MainControlsPanel.RackPanel rp = gameControls.getMainControlsPanel().getRackPanel();
		if (gameScreen.getValue() instanceof TileButton) {
			int index = rp.addTileButtonToRack((TileButton) gameScreen.getValue());
			rp.removeActionListeners(index);
			rp.addRackTileActionListener(e -> tilePanelClick(index), index);
		}
		JButton removed = rp.removeButtonFromRack(col);
		gameScreen.setValue(removed);
	}
	/* Creates the action listener for the submit button */
	private void addMainSubmitActionListener() {gameControls.getMainControlsPanel().addSubmitActionListener(e -> submitClick());}
	/* Submits the tiles played by the player on the board and plays the tiles into the game */
	private void submitClick() {
		SwingUtilities.invokeLater(()->{
			if (gameScreen.getValue() instanceof TileButton) {
				gameControls.getMainControlsPanel().getRackPanel().addTileButtonToRack((TileButton) gameScreen.getValue());
				gameScreen.setValue(new JButton(" "));
			}
			gameControls.getMainControlsPanel().getRackPanel().soutRack();
			int playerID = parent.getSelfID();
			System.out.println(Arrays.toString(gameScreen.getPlayedTiles().toArray(new Tile[0])));
			PlayTiles playTiles = new PlayTiles(playerID, playerID, gameScreen.getPlayedTiles().toArray(new Tile[0]));
			playTiles.execute(parent);
		});

	}


	/**
	 * Removes the action listeners for a tile on the rack when it is removed
	 */
	private void removeRackTileListeners() {
		for (int i = 0; i < GameScreen.RACK_SIZE; i++) {
			gameControls.getMainControlsPanel().getRackPanel().removeActionListeners(i);
		}
	}

	private void passTurn() {
		PassTurn passTurn = new PassTurn(this.parent.getSelfID(), this.parent.getSelfID());
		passTurn.execute(this.parent);
	}

	/**
	 * Action listener for the rules item on the menu of the frame
	 */
	private void rulesMenuClick() { parent.showRulesDialog(); }

	/**
	 * Action listener for the audio item on the menu of the frame
	 */
	private void audioMenuClick() {
		parent.toggleMusic();
	}

	/**
	 * Action listener for the fx item on the menu of the frame
	 */
	private void fxMenuClick() {
		parent.toggleFx();
	}

	/**
	 * Action listener for the quit item on the menu of the frame
	 */
	private void quitMenuClick() { if (parent.showQuitDialog() == JOptionPane.YES_OPTION) parent.exit(); }

	public void setAudioEnabled(boolean musicEnabled, boolean fxEnabled){
		parent.getView().getAudioItem().setSelected(musicEnabled);
		parent.getView().getFxItem().setSelected(fxEnabled);
	}

	public void setConnected(String playerName) {
		gameScreen.setDisconnected(playerName);
	}

	private class GameTimeController {
		private final Timer scheduler;

		public GameTimeController(int gameLength) {
			// schedule task to run every second for however the game runs.
			scheduler = new Timer();
			scheduler.schedule(
				new TimerTask() {
					public void run() {
						SwingUtilities.invokeLater(GameScreenController.this.gameScreen::decrementTime);
						if (GameScreenController.this.gameScreen.currentPlayerTime() == 0)
							GameScreenController.this.passTurn();
					}
				}, 1000, 1000
			);
		}

		public void cancel() {
			scheduler.cancel();
		}
	}
}