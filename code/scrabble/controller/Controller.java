package scrabble.controller;



import scrabble.model.Board;
import scrabble.model.Game;
import scrabble.model.Tile;
import scrabble.network.client.ClientMessenger;
import scrabble.network.messages.PlayTiles;
import scrabble.network.networkPrototype.PartyHost;
import scrabble.view.frame.GameFrame;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.frame.ScrabbleView;
import scrabble.view.panel.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static scrabble.view.panel.GameScreen.*;

/**
 * Run the other classes
 */

public class Controller implements PropertyChangeListener  {
	private ScrabbleGUI view;
	private Game model;

	private ClientMessenger messenger;
	private Socket hostSocket;

	/*
	reference to the party host
	when this controller is the manager of the party.
	note that this field is null when this computer is not the host
	 */
	private PartyHost host;

	public static void main(String[] args) {
		new Controller();
	}

	public Controller() {
		view = new ScrabbleGUI();
		addListeners(view);
		view.showGame();
	}

	public ScrabbleGUI getView() {
		return view;
	}

	public Game getModel() {
		return model;
	}

	public ClientMessenger getMessenger() {
		return messenger;
	}

	public Socket getHostSocket() {
		return hostSocket;
	}

	public PartyHost getHost() {
		return host;
	}

	private void addListeners(ScrabbleGUI view) {
		addMenuListeners(view.getMainMenu());
		addHostListeners(view.getHost());
		addJoinListeners(view.getJoin());
		addWaitingListeners(view.getWaiting());
		addGameListeners(view.getGame());
	}

	private void addGameListeners(JPanel game) {
		// add listeners to the buttons on the main game screen
		GameScreen gameScreen = (GameScreen) game;
		addBoardCellListeners(gameScreen);
		addRackTileListeners(gameScreen);
		submitActionListener(gameScreen);
	}

	public void addRackTileListeners(GameScreen gameScreen){
		for (int i = 0; i < 7; i++) {
			JButton rackTile = gameScreen.getRack()[i];
			int finalI = i;
			rackTile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton[] rack = gameScreen.getRack();
					String value = gameScreen.getValue();
					if(!value.equals(" ")){
						for (int j = 0; j < 7; j++){
							if(rack[j].getText().equals(" ")){
								rack[j].setText(value);
								break;
							}
						}
					}
					gameScreen.setValue(rackTile.getText());
					rack[finalI].setText(" ");
				}
			});
		}
	}

	private void addBoardCellListeners(GameScreen gameScreen) {
		for (int i = 0; i < Board.BOARD_ROWS; i++) {
			for (int j = 0; j < Board.BOARD_COLUMNS; j++) {
				JButton boardTile = gameScreen.getGameCells()[i][j];

				int row = i;
				int col = j;
				boardTile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						List<String> modType = new ArrayList<>(Arrays.asList("DW", "TW", "DL", "TL"));

						//adding tiles from the rack to the board
						if((boardTile.getText().equals(" ") || boardTile.getBackground() != normalCell) && !gameScreen.getValue().equals(" ")) {
							for (int k = 0; k < 7; k++) {
								if(!boardTile.getText().equals(" ") && !modType.contains(boardTile.getText())) {
									if (gameScreen.getRack()[k].getText().equals(" ")) {
										if (boardTile.getBackground() != normalCell) {
											gameScreen.getRack()[k].setText(boardTile.getText());
											char tile = boardTile.getText().charAt(0);
											Point point = new Point(row, col);
											gameScreen.getPlayedTiles().remove(new Tile(tile, point));
											break;
										}
									}
								}
							}
							boardTile.setText(gameScreen.getValue());
							char tile = gameScreen.getValue().charAt(0);
							Point point = new Point(row, col);
							gameScreen.playedTiles.add(new Tile(tile, point));
							gameScreen.setValue(" ");
						}
						//adding tiles from the board back to the rack
						else if(gameScreen.getValue().equals(" ")){
							for (int k = 0; k < 7; k++) {
								if(gameScreen.getRack()[k].getText().equals(" ") && !modType.contains(boardTile.getText())){
									gameScreen.getRack()[k].setText(boardTile.getText());
									char tile = boardTile.getText().charAt(0);
									Point point = new Point(row, col);
									gameScreen.playedTiles.remove(new Tile(tile, point));
									if(gameScreen.getValue().equals(" ")){
										Color color = boardTile.getBackground();
										if (color.equals(doubleWord)) {
											boardTile.setText("DW");
										} else if (color.equals(doubleLetter)) {
											boardTile.setText("DL");
										} else if (color.equals(tripleWord)) {
											boardTile.setText("TW");
										} else if (color.equals(tripleLetter))
											boardTile.setText("TL");
										else
											boardTile.setText(" ");
									}
									gameScreen.setValue(" ");
									break;
								}
							}
						}
					}
				});
			}
		}
	}

	private void submitActionListener(GameScreen gameScreen){
		gameScreen.getSubmitButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int playerID = Controller.this.model.getSelf().getID();
				PlayTiles playTiles = new PlayTiles(playerID, playerID, gameScreen.getPlayedTiles().toArray(new Tile[0]));
				playTiles.execute(Controller.this);
			}
		});
	}

	private void addWaitingListeners(JPanel waiting) {
		// add listeners to the buttons on the waiting players screen
	}

	private void addJoinListeners(JPanel join) {
		// add listeners to the buttons on the join game screen
	}

	private void addHostListeners(JPanel host) {
		// add listeners to the buttons on the host screen
	}

	private void addMenuListeners(JPanel mainMenu) {
		// add listeners to the buttons on the main menu
	}

	private void hostGame() {}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {}

}
