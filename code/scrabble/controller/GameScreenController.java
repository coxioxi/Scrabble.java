package scrabble.controller;

import scrabble.model.Board;
import scrabble.model.Tile;
import scrabble.network.messages.PlayTiles;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.frame.TileButton;
import scrabble.view.screen.*;
import scrabble.view.screen.component.BoardCellPanel;
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

	public void addToBoard(Tile[] tiles) {
		gameScreen.addToBoard(tiles);
	}

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

	private void removeRackTileListeners() {
		RackPanel rackPanel = gameScreen.getRackPanel();
		for (int i = 0; i < 7; i++) {
			TilePanel tilePanel = rackPanel.getTilePanels()[i];
			removeActionListeners(tilePanel.getButton());
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
				BoardCellPanel boardCellPanel = boardPanel.getBoardCell(row, col);
				addBoardCellPanelListener(boardCellPanel, row, col);
			}
		}
	}

	public void addRackTileListeners(){
		RackPanel rackPanel = gameScreen.getRackPanel();
		for (int i = 0; i < 7; i++) {
			TilePanel tilePanel = rackPanel.getTilePanels()[i];
			addTilePanelListener(tilePanel, i);
		}
	}

	private void addSubmitActionListener(){ gameScreen.getSubmitButton().addActionListener(e -> submitClick()); }
	private void submitClick() {
		if (gameScreen.getValue() instanceof TileButton) {
			gameScreen.getRackPanel().addToRack((TileButton) gameScreen.getValue());
		}
		int playerID = parent.getSelfID();
		PlayTiles playTiles = new PlayTiles(playerID, playerID, gameScreen.getPlayedTiles().toArray(new Tile[0]));
		playTiles.execute(parent);
	}

	private void addTilePanelListener(TilePanel tilePanel, int col) { tilePanel.getButton().addActionListener(e -> tilePanelClick(col)); }
	private void tilePanelClick(int col) {
		RackPanel rackPanel = gameScreen.getRackPanel();
		TilePanel tilePanel = rackPanel.getTilePanels()[col];
		JButton value = gameScreen.getValue();
		if(value instanceof TileButton){
			boolean foundBlank = false;
			for (int j = 0; j < 7 && !foundBlank; j++){
				TilePanel tile = rackPanel.getTilePanels()[j];
				if(!(tile.getButton() instanceof TileButton)){
					tile.setButton(value);
					foundBlank = true;
				}
			}
		}
		gameScreen.setValue(tilePanel.getButton());
		tilePanel.setButton(new JButton(" "));
//		System.out.println(gameScreen.getValue());
	}

	private void addBoardCellPanelListener(BoardCellPanel boardCellPanel, int row, int col) { boardCellPanel.getBoardButton().addActionListener(e -> boardCellClick(row, col)); }
	private void boardCellClick(int row, int col) {
		/*
		if this button is a TileButton, put it in the rack.
		if value is a tileButton put it in this panel
		 */
		BoardPanel boardPanel = gameScreen.getBoardPanel();
		BoardCellPanel boardCellPanel = boardPanel.getBoardCell(row, col);
		if (boardCellPanel.getBoardButton() instanceof TileButton) {
			//put in rack
			TilePanel[] tilePanels = gameScreen.getRackPanel().getTilePanels();
			boolean foundBlank = false;
			for (int i = 0; i < tilePanels.length && !foundBlank; i++) {
				TilePanel tp = tilePanels[i];
				if (!(tp.getButton() instanceof TileButton)) {
					JButton toAdd = boardCellPanel.getBoardButton();
					removeActionListeners(toAdd);
					gameScreen.playedTiles.remove(
							new Tile(toAdd.getText().charAt(0), new Point(row, col))
					);
					tp.setButton(toAdd);
					addTilePanelListener(tp, i);
					foundBlank = true;
				}
			}
		}
		// add value to panel
		JButton toAdd = gameScreen.getValue();
		removeActionListeners(toAdd);
		if (toAdd instanceof TileButton) {
			gameScreen.playedTiles.add(
					new Tile(toAdd.getText().charAt(0), new Point(row, col))
			);
		}
		boardPanel.setBoardCell(toAdd, row, col);
		addBoardCellPanelListener(boardCellPanel, row, col);
		gameScreen.setValue(new JButton(" "));
	}
}