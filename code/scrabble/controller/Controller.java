package scrabble.controller;

import scrabble.model.*;
import scrabble.network.client.ClientMessenger;
import scrabble.network.messages.ExchangeTiles;
import scrabble.network.messages.PlayTiles;
import scrabble.network.messages.StartGame;
import scrabble.network.host.PartyHost;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.frame.TileButton;
import scrabble.view.panel.*;
import scrabble.view.panel.GameScreen;
import scrabble.view.panel.subpanel.RackPanel;
import scrabble.view.panel.subpanel.TilePanel;

import javax.swing.*;
import java.awt.*;
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

	private Ruleset ruleset;

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
		view.showMain();
	}

	public void setupSocket(String ip) throws IOException {
		hostSocket = new Socket(ip, PORT);
		messenger = new ClientMessenger(hostSocket, this);
	}

	public void setUpHost() {
		host = new PartyHost(PORT);
		host.start();
		view.getHost().getHostsIP().setText(host.getIPAddress());
	}

	public void sendRules(boolean challengesAllowed, String dictionary, int playerTime, int gameTime) {
		ruleset = new Ruleset(gameTime, playerTime, challengesAllowed, dictionary);
		host.startGame(ruleset);
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

		// stub, not for active game. see propertyChangeListener
		this.gameScreenController = new GameScreenController(this, (GameScreen) view.getGame());
		gameScreenController.setupMenuListeners(view);
		this.mainMenuController = new MainMenuController(this, (MainMenuScreen) view.getMainMenu());

	}


	private void addWaitingListeners(JPanel waiting) {
		// add listeners to the buttons on the waiting players screen
	}

	private void addJoinListeners(JoinScreen join) {
		// add listeners to the buttons on the join game screen
		joinScreenController = new JoinScreenController(this, join);
	}

	private void addHostListeners(HostScreen host) {
		// add listeners to the buttons on the host screen
		hostScreenController = new HostScreenController(this, host);
	}

	private void addMenuListeners(MainMenuScreen mainMenu) {
		// add listeners to the buttons on the main menu
		mainMenuController = new MainMenuController(this, mainMenu);

	}

	public void resetRack(GameScreen gameScreen){
		//loop through the rack
		for (int i = 0; i < gameScreen.playedTiles.size(); ++i){
			gameScreenController.removeTile(gameScreen.playedTiles.get(i));
		}
	}

	public void removeRackTile(Tile tile) {
		RackPanel rackPanel = ((GameScreen) view.getGame()).getRackPanel();
		for(TilePanel tp: rackPanel.getTilePanels()){
			if(tp.getButton().getText().equals(""+tile.getLetter())){
				tp.setButton(new JButton(" "));
				break;
			}
		}
	}

	public void addRack(Tile[] toAdd){
		JButton toAddTile = new JButton();
		RackPanel rackPanel = ((GameScreen) view.getGame()).getRackPanel();
		//loop through the rack
		for (int i = 0; i < toAdd.length; ++i){
			TilePanel tp = rackPanel.getTilePanels()[i];
			//find empty rack location
			if(tp.getButton().getText().equals(" ")){
				//swap letters with exchange tile array location
				tp.setButton(new TileButton(TileScore.values()[(toAdd[i].getLetter()-'A')]));
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
