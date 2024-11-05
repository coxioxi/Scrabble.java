package scrabble.controller;



import scrabble.model.Board;
import scrabble.model.Game;
import scrabble.model.Tile;
import scrabble.network.client.ClientMessenger;
import scrabble.network.messages.PlayTiles;
import scrabble.network.networkPrototype.PartyHost;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.frame.TileButton;
import scrabble.view.panel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Socket;

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
		RackPanel rackPanel = gameScreen.getRackPanel();
		for (int i = 0; i < 7; i++) {
			TilePanel tilePanel = rackPanel.getTilePanels()[i];

			tilePanel.getButton().addActionListener(e -> {
				JButton value = gameScreen.getValue();
				if(value instanceof TileButton){
					boolean foundBlank = false;
					for (int j = 0; j < 7 && !foundBlank; j++){
						TilePanel tile = gameScreen.getRackPanel().getTilePanels()[j];
						if(!(tile.getButton() instanceof TileButton)){
							tile.setButton(value);
							foundBlank = true;
						}
					}
				}
				gameScreen.setValue(tilePanel.getButton());
				tilePanel.setButton(new JButton(" "));
				System.out.println(gameScreen.getValue());
			});
		}
	}


	private void addBoardCellListeners(GameScreen gameScreen) {
		BoardPanel boardPanel = gameScreen.getBoardPanel();
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				BoardCellPanel boardCellPanel = boardPanel.getBoardCell(row, col);

				// we have clicked a board tile...
				int finalRow = row;
				int finalCol = col;
				boardCellPanel.getBoardButton().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Pressed board button");
						/*
						if this button is a TileButton, put it in the rack.
						if value is a tileButton put it in this panel
						 */
						if (boardCellPanel.getBoardButton() instanceof TileButton) {
							//put in rack
							for (TilePanel tp : gameScreen.getRackPanel().getTilePanels()) {
								if (!(tp.getButton() instanceof TileButton)) {
									tp.setButton(boardCellPanel.getBoardButton());
								}
							}
						}
						// add value to panel
						boardPanel.setBoardCell(gameScreen.getValue(), finalRow, finalCol);
						gameScreen.setValue(new JButton(" "));
						System.out.println("X"+boardPanel.getNumXPanels());
						System.out.println("Y"+boardPanel.getNumYPanels());
					}

					/*public void actionPerformed(ActionEvent e) {
//						System.out.println("Board listener");
//						JButton boardTile = gameScreen.getGameCells()[row][col];
						if (gameScreen.getValue() instanceof TileButton) {
							// we have selected a Rack Tile.
							removeTileButtonFromBoard(button);
							// put the new tile on the board
							boardCellPanel.setBoardButton(gameScreen.getValue());
							gameScreen.getPlayedTiles().add(
									new Tile(gameScreen.getValue().getText().charAt(0), new Point(finalRow, finalCol))
							);
							gameScreen.setValue(new JButton(" "));
						}
						else {
							// no TileButton stored in value; remove from board if present
							if (button instanceof TileButton) {
								removeTileButtonFromBoard(button);
								Color color = .getBackground();
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
						}
						System.out.println("Exit");
					}

					private void removeTileButtonFromBoard(JButton boardTile) {
						if (boardTile instanceof TileButton) {
							// taking tile off the board, put on rack.
							for (int k = 0; k < 7; k++) {
								if (!(gameScreen.getRack()[k] instanceof TileButton)) {
									gameScreen.getRack()[k] = boardTile;
									gameScreen.getPlayedTiles().remove(
											new Tile(boardTile.getText().charAt(0), new Point(finalRow, finalCol))
									);
									break;
								}
							}
						}
					}
					/*public void actionPerformed(ActionEvent e) {
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
*/
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
