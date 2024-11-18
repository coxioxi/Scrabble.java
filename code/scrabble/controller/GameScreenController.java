package scrabble.controller;

import scrabble.model.Board;
import scrabble.model.Tile;
import scrabble.network.messages.PlayTiles;
import scrabble.view.ScrabbleGUI;
import scrabble.view.TileButton;
import scrabble.view.screen.*;
import scrabble.view.screen.component.BoardPanel;
import scrabble.view.screen.component.RackPanel;
import scrabble.view.screen.component.TilePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * GameScreenController is the controller that takes care of all actions of the Game screen
 */
public class GameScreenController {
	private final Controller parent;
	private final GameScreen gameScreen;
	private boolean isRackEnabled;

	/**
	 * Constructor for the Game Screen
	 *
	 * @param parent the player's game controller
	 * @param gameScreen the game screen panel of the user
	 */
	public GameScreenController(Controller parent, GameScreen gameScreen) {
		this.parent = parent;
		this.gameScreen = gameScreen;
		addActionListeners();
	}

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
	 * Sets the names, times, and starting tiles for a player
	 *
	 * @param names the array of names of the players playing
	 * @param gameTime the total time set for the entire game
	 * @param turnTime the time each individual player will have on each turn
	 * @param startingTiles the tiles that will be on the player's starting rack
	 */
	public void setupGameItems(String[] names, int gameTime, int turnTime, Tile[] startingTiles) {
		gameScreen.setupGameItems(names, gameTime, turnTime, startingTiles);
		addRackTileListeners();
		gameScreen.repaint();
	}

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
	public void addToBoard(Tile[] tiles) { gameScreen.addToBoard(tiles); }

	/**
	 * Removes a singular tile from the board
	 *
	 * @param tile the individual Tile object to be removed from the board
	 */
	public void removeTileFromBoard(Tile tile){ boardCellClick(tile.getLocation().x, tile.getLocation().y); }

	/**
	 * Changes the displayed score for a player on the Game Screen
	 *
	 * @param name the name of the player whose score is to be changed
	 * @param newScore the score the player now has
	 */
	public void updateScore(String name, int newScore) { gameScreen.updateScore(name, newScore); }

	/**
	 * Disables the tiles that were just played on the board
	 */
	public void disableLastPlayedTiles() { gameScreen.disableLastPlayedTiles(); }

	/**
	 * Adds the tiles for the player to their rack
	 *
	 * @param tiles the Tile objects that are the player's new tiles
	 */
	public void addRackTiles(Tile[] tiles) {
		gameScreen.addTilesToRack(tiles);
		removeRackTileListeners();
		addRackTileListeners();
		setRackButtonsEnabled(this.isRackEnabled);
	}

	/**
	 * Removes a single tile from the board
	 *
	 * @param tile the Tile object to be taken off of the rack
	 */
	public void removeRackTile(Tile tile) {
		RackPanel rackPanel = gameScreen.getRackPanel();
		for(TilePanel tp: rackPanel.getTilePanels()){
			if(tp.getButton().getText().equals(""+tile.getLetter())){
				tp.setButton(new JButton(" "));
				break;
			}
		}
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
	}

	/**
	 * Enables/disables the buttons of the players rack based on if it's their turn or not
	 *
	 * @param enabled the boolean variable that says if it should be disabled or not
	 */
	public void setRackButtonsEnabled(boolean enabled) {
		this.isRackEnabled = enabled;
		RackPanel rackPanel = gameScreen.getRackPanel();
		for (TilePanel tp : rackPanel.getTilePanels()) {
			tp.getButton().setEnabled(enabled);
		}
	}

	/*
	 * private methods
	 */

	/**
	 * Removes the action listeners for a tile on the rack when it is removed
	 */
	private void removeRackTileListeners() {
		for (int i = 0; i < GameScreen.RACK_SIZE; i++) {
			gameScreen.removeRackTileActionListeners(i);
		}
	}

	/**
	 * Action listener for the rules item on the menu of the frame
	 */
	private void rulesMenuClick() { parent.showRulesDialog(); }

	/**
	 * Action listener for the audio item on the menu of the frame
	 */
	private void audioMenuClick() {
		// haha, great question
	}

	/**
	 * Action listener for the fx item on the menu of the frame
	 */
	private void fxMenuClick() {
		// haha, great question
	}

	/**
	 * Action listener for the quit item on the menu of the frame
	 */
	private void quitMenuClick() { if (parent.showQuitDialog() == JOptionPane.YES_OPTION) parent.exit(); }

	/**
	 * Adds the action listeners for the buttons on the game screen
	 */
	private void addActionListeners() {
		addRackTileListeners();
		addBoardCellListeners();
		addSubmitActionListener();
	}

	/**
	 * Adds the listeners to each panel of the board
	 */
	private void addBoardCellListeners() {
		BoardPanel boardPanel = gameScreen.getBoardPanel();
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				addBoardCellPanelListener(boardPanel, row, col);
			}
		}
	}

	/**
	 * Creates the action listener for each panel of the board
	 *
	 * @param boardPanel the panel which we are adding the listener to
	 * @param row the row of the given panel
	 * @param col the column of the given panel
	 */
	private void addBoardCellPanelListener(BoardPanel boardPanel, int row, int col) {
		boardPanel.addActionListener(e -> boardCellClick(row, col), row, col);}

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
		/*
		if this button is a TileButton, put it in the rack.
		if value is a tileButton put it in this panel
		 */
		BoardPanel boardPanel = gameScreen.getBoardPanel();
		if (boardPanel.instanceOfTileButton(row, col)) {
			//put in rack
			boardPanel.removeActionListeners(row, col);
			gameScreen.removeFromPlayedTiles(
					new Tile(boardPanel.getButtonText(row, col).charAt(0), new Point(row, col))
			);
			int index = gameScreen.addTileButtonToRack((TileButton) boardPanel.getButton(row, col));
			addTilePanelListener(index);
		}
		// add value to panel
		JButton toAdd = gameScreen.getValue();
		removeActionListeners(toAdd);
		if (toAdd instanceof TileButton) {
			gameScreen.addToPlayedTiles(
					new Tile(toAdd.getText().charAt(0), new Point(row, col))
			);
		}
		boardPanel.setBoardCell(toAdd, row, col);
		addBoardCellPanelListener(boardPanel, row, col);
		gameScreen.setValue(new JButton(" "));
	}

	/**
	 * Adds the listeners to each panel of the rack
	 */
	private void addRackTileListeners(){
		for (int col = 0; col < 7; col++) {
			addTilePanelListener(col);
		}
	}

	/**
	 * Creates the action listener for each button in each panel
	 *
	 * @param col the column of the rack tile
	 */
	private void addTilePanelListener(int col) { gameScreen.addRackTileActionListener(col, e -> tilePanelClick(col)); }

	/**
	 * Takes the clicked tile off the rack to place it on the board
	 *
	 * @param col the column of the tile clicked to remove from the rack
	 */
	private void tilePanelClick(int col) {
		if (gameScreen.getValue() instanceof TileButton) {
			int index = gameScreen.addTileButtonToRack((TileButton) gameScreen.getValue());
			gameScreen.removeRackTileActionListeners(index);
			gameScreen.addRackTileActionListener(index, e -> tilePanelClick(index));
		}
		JButton removed = gameScreen.removeButtonFromRack(col);
		gameScreen.setValue(removed);
	}

	/**
	 * Creates the action listener for the submit button
	 */
	private void addSubmitActionListener() {gameScreen.getSubmitButton().addActionListener(e -> submitClick());}

	/**
	 * Submits the tiles played by the player on the board
	 * 		and plays the tiles into the game
	 */
	private void submitClick() {
		if (gameScreen.getValue() instanceof TileButton) {
			gameScreen.addTileButtonToRack((TileButton) gameScreen.getValue());
		}
		int playerID = parent.getSelfID();
		PlayTiles playTiles = new PlayTiles(playerID, playerID, gameScreen.getPlayedTiles().toArray(new Tile[0]));
		playTiles.execute(parent);
	}

}