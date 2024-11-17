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

public class GameScreenController {
	private final Controller parent;
	private final GameScreen gameScreen;
	private boolean isRackEnabled;

	public static void removeActionListeners(JButton button) {
		for (ActionListener al : button.getActionListeners()) {
			button.removeActionListener(al);
		}
	}

	public GameScreenController(Controller parent, GameScreen gameScreen) {
		this.parent = parent;
		this.gameScreen = gameScreen;
		addActionListeners();
	}

	public void setupGameItems(String[] names, int gameTime, int turnTime, Tile[] startingTiles) {
		gameScreen.setupGameItems(names, gameTime, turnTime, startingTiles);
		addRackTileListeners();
		gameScreen.repaint();
	}

	public void setupMenuListeners(ScrabbleGUI view) {
		view.getRulesItem().addActionListener(e -> rulesMenuClick());
		view.getAudioItem().addActionListener(e -> audioMenuClick());
		view.getFxItem().addActionListener(e -> fxMenuClick());
		view.getQuitItem().addActionListener(e -> quitMenuClick());
	}

	public void addToBoard(Tile[] tiles) { gameScreen.addToBoard(tiles); }

	public void removeTileFromBoard(Tile tile){ boardCellClick(tile.getLocation().x, tile.getLocation().y); }

	public void updateScore(String name, int newScore) { gameScreen.updateScore(name, newScore); }

	public void disableLastPlayedTiles() { gameScreen.disableLastPlayedTiles(); }

	public void addRackTiles(Tile[] tiles) {
		gameScreen.addTilesToRack(tiles);
		removeRackTileListeners();
		addRackTileListeners();
		setRackButtonsEnabled(this.isRackEnabled);
	}

	public void removeRackTile(Tile tile) {
		RackPanel rackPanel = gameScreen.getRackPanel();
		for(TilePanel tp: rackPanel.getTilePanels()){
			if(tp.getButton().getText().equals(""+tile.getLetter())){
				tp.setButton(new JButton(" "));
				break;
			}
		}
	}

	public void resetLastPlay() {
		int size = gameScreen.getPlayedTiles().size();
		for (int i = 0; i < size; ++i){
			removeTileFromBoard(gameScreen.getPlayedTiles().get(0));
		}
	}

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

	private void removeRackTileListeners() {
		for (int i = 0; i < GameScreen.RACK_SIZE; i++) {
			gameScreen.removeRackTileActionListeners(i);
		}
	}

	private void rulesMenuClick() { parent.showRulesDialog(); }

	private void audioMenuClick() {
		// haha, great question
	}

	private void fxMenuClick() {
		// haha, great question
	}

	private void quitMenuClick() { if (parent.showQuitDialog() == JOptionPane.YES_OPTION) parent.exit(); }

	private void addActionListeners() {
		addRackTileListeners();
		addBoardCellListeners();
		addSubmitActionListener();
	}

	private void addBoardCellListeners() {
		BoardPanel boardPanel = gameScreen.getBoardPanel();
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				addBoardCellPanelListener(boardPanel, row, col);
			}
		}
	}

	private void addRackTileListeners(){
		for (int col = 0; col < 7; col++) {
			addTilePanelListener(col);
		}
	}

	private void addSubmitActionListener(){ gameScreen.getSubmitButton().addActionListener(e -> submitClick()); }
	private void submitClick() {
		if (gameScreen.getValue() instanceof TileButton) {
			gameScreen.addTileButtonToRack((TileButton) gameScreen.getValue());
		}
		int playerID = parent.getSelfID();
		PlayTiles playTiles = new PlayTiles(playerID, playerID, gameScreen.getPlayedTiles().toArray(new Tile[0]));
		playTiles.execute(parent);
	}

	private void addTilePanelListener(int col) { gameScreen.addRackTileActionListener(col, e -> tilePanelClick(col)); }
	private void tilePanelClick(int col) {
		if (gameScreen.getValue() instanceof TileButton) {
			int index = gameScreen.addTileButtonToRack((TileButton) gameScreen.getValue());
			gameScreen.removeRackTileActionListeners(index);
			gameScreen.addRackTileActionListener(index, e -> tilePanelClick(index));
		}
		JButton removed = gameScreen.removeButtonFromRack(col);
		gameScreen.setValue(removed);
	}

	private void addBoardCellPanelListener(BoardPanel boardPanel, int row, int col) { boardPanel.addActionListener(e -> boardCellClick(row, col), row, col); }
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
}