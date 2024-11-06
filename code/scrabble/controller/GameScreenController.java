package scrabble.controller;

import scrabble.model.Board;
import scrabble.model.Tile;
import scrabble.network.messages.PlayTiles;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.frame.TileButton;
import scrabble.view.panel.*;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionListener;

public class GameScreenController {
	private final Controller parent;
	private final GameScreen gameScreen;

	public GameScreenController(Controller parent, GameScreen gameScreen) {
		this.parent = parent;
		this.gameScreen = gameScreen;
		addActionListeners();
	}

	public void setupMenuListeners(ScrabbleGUI view) {
		view.getRulesItem().addActionListener(e -> rulesMenuClick(view));
		view.getAudioItem().addActionListener(e -> audioMenuClick(view));
		view.getFxItem().addActionListener(e -> fxMenuClick(view));
		view.getQuitItem().addActionListener(e -> quitMenuClick(view));
	}

	private void quitMenuClick(ScrabbleGUI view) {
		view.showQuitDialog();
	}

	private void fxMenuClick(ScrabbleGUI view) {
		// haha great question
	}

	private void audioMenuClick(ScrabbleGUI view) {
		// haha great question
	}

	private void rulesMenuClick(ScrabbleGUI view) {
		view.showRulesDialog();
	}

	private void addActionListeners() {
		addRackTileListeners();
		addBoardCellListeners();
		addSubmitActionListener();
	}

	private void addRackTileListeners(){
		RackPanel rackPanel = gameScreen.getRackPanel();
		for (int i = 0; i < 7; i++) {
			TilePanel tilePanel = rackPanel.getTilePanels()[i];
			addTilePanelListener(tilePanel, i);
		}
	}

	private void addTilePanelListener(TilePanel tilePanel, int col) {
		tilePanel.getButton().addActionListener(e -> tilePanelClick(col));
	}

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

	private void addBoardCellListeners() {
		BoardPanel boardPanel = gameScreen.getBoardPanel();
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				BoardCellPanel boardCellPanel = boardPanel.getBoardCell(row, col);
				addBoardCellPanelListener(boardCellPanel, row, col);
			}
		}
	}

	private void addBoardCellPanelListener(BoardCellPanel boardCellPanel, int row, int col) {
		boardCellPanel.getBoardButton().addActionListener(e -> boardCellClick(row, col));
	}

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
					tp.setButton(toAdd);
					addTilePanelListener(tp, i);
					foundBlank = true;
				}
			}
		}
		// add value to panel
		JButton toAdd = gameScreen.getValue();
		removeActionListeners(toAdd);
		boardPanel.setBoardCell(toAdd, row, col);
		addBoardCellPanelListener(boardCellPanel, row, col);
		gameScreen.setValue(new JButton(" "));
	}

	private void removeActionListeners(JButton button) {
		for (ActionListener al : button.getActionListeners()) {
			button.removeActionListener(al);
		}
	}

	private void addSubmitActionListener(){
		gameScreen.getSubmitButton().addActionListener(e -> submitClick());
	}

	private void submitClick() {
		int playerID = parent.getModel().getSelf().getID();
		PlayTiles playTiles = new PlayTiles(playerID, playerID, gameScreen.getPlayedTiles().toArray(new Tile[0]));
		playTiles.execute(parent);
	}

}
