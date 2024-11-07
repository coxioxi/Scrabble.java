package scrabble.controller;



import scrabble.model.Board;
import scrabble.model.Game;
import scrabble.model.NotBlankException;
import scrabble.model.Tile;
import scrabble.network.client.ClientMessenger;
import scrabble.network.messages.ExchangeTiles;
import scrabble.network.messages.PlayTiles;
import scrabble.network.networkPrototype.PartyHost;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.frame.TileButton;
import scrabble.view.panel.GameScreen;

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

	public Tile[] getRack(GameScreen gameScreen) throws NotBlankException {
		Tile[] tiles = new Tile[gameScreen.getRack().length];
		if(gameScreen.getRack().length > 0) {
			for (int i = 0; i < gameScreen.getRack().length; i++)
				tiles[i] = new Tile(gameScreen.getRack()[i].getText().charAt(0));

			return tiles;
		}
		return tiles;
	}

	private void addListeners(ScrabbleGUI view) {
		addMenuListeners(view.getMainMenu());
		addHostListeners(view.getHost());
		addJoinListeners(view.getJoin());
		addWaitingListeners(view.getWaiting());
		addGameListeners();
	}

	private void addGameListeners() {
		// add listeners to the buttons on the main game screen
		new GameScreenController(this, (GameScreen) view.getGame());
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

	public void resetRack(GameScreen gameScreen){
		//loop through the rack
		for (int i = 0; i < gameScreen.getRack().length; ++i){
			Point point = new Point(gameScreen.playedTiles.get(i).getLocation());
			//find empty rack location
			if(!(gameScreen.getRack()[i] instanceof TileButton)){
				//swap with board location
				swap(gameScreen.getRack(), gameScreen.getGameCells(), point);
			}
		}
	}

	public void exchangeRack(GameScreen gameScreen, Tile[] toAdd){
		JButton toAddTile = new JButton();

		//loop through the rack
		for (int i = 0; i < gameScreen.getRack().length; ++i){
			//find empty rack location
			if(!(gameScreen.getRack()[i] instanceof TileButton)){
				//swap letters with exchange tile array location
				gameScreen.getRack()[i].setText(""+toAdd[i].getLetter());
			}
		}
	}

	private void swap(JButton[] rack, JButton[][] board, Point point){
		JButton temp;
		for (int i = 0; i < rack.length; ++i) {
			temp = rack[i];
			rack[i] = board[point.x][point.y];
			board[point.x][point.y] = temp;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {}

}
