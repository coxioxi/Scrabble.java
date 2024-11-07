package scrabble.controller;

import scrabble.model.Game;
import scrabble.network.client.ClientMessenger;
import scrabble.network.messages.ExchangeTiles;
import scrabble.network.messages.PlayTiles;
import scrabble.network.messages.StartGame;
import scrabble.network.networkPrototype.PartyHost;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.frame.TileButton;
import scrabble.view.panel.GameScreen;
import scrabble.view.panel.GameScreen;
import scrabble.view.panel.JoinScreen;
import scrabble.view.panel.MainMenuScreen;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;


/**
 * Run the other classes
 */

public class Controller implements PropertyChangeListener  {
	public static final int PORT = 5000;

	private ScrabbleGUI view;
	private Game model;

	private ClientMessenger messenger;
	private Socket hostSocket;
	private GameScreenController gameScreenController;
	private MainMenuController mainMenuController;
	private HostScreenController hostScreenController;
	private JoinScreenController joinScreenController;


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
		view.showHost();
	}

	public void setupSocket(String ip) throws IOException {
		hostSocket = new Socket(ip, PORT);
		messenger = new ClientMessenger(hostSocket, this);
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

		// stub, not for active game. see propertyChangeListener
		this.gameScreenController = new GameScreenController(this, (GameScreen) view.getGame());
		gameScreenController.setupMenuListeners(view);
		this.mainMenuController = new MainMenuController(this, (MainMenuScreen) view.getMainMenu());

	}


	private void addWaitingListeners(JPanel waiting) {
		// add listeners to the buttons on the waiting players screen
	}

	private void addJoinListeners(JPanel join) {
		// add listeners to the buttons on the join game screen
		joinScreenController = new JoinScreenController(this, (JoinScreen) join);
	}

	private void addHostListeners(JPanel host) {
		// add listeners to the buttons on the host screen
	}

	private void addMenuListeners(JPanel mainMenu) {
		// add listeners to the buttons on the main menu
		mainMenuController = new MainMenuController(this, (MainMenuScreen) mainMenu);

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
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof StartGame) {
			this.view.setupGameScreen(((StartGame) evt.getNewValue()).getRuleset());
			this.gameScreenController = new GameScreenController(this, (GameScreen) view.getGame());
			this.gameScreenController.setupMenuListeners(view);
		}
	}

}
