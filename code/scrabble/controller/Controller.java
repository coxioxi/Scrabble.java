package scrabble.controller;

import scrabble.model.Game;
import scrabble.network.client.ClientMessenger;
import scrabble.network.messages.StartGame;
import scrabble.network.networkPrototype.PartyHost;
import scrabble.view.frame.ScrabbleGUI;
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof StartGame) {
			this.view.setupGameScreen(((StartGame) evt.getNewValue()).getRuleset());
			this.gameScreenController = new GameScreenController(this, (GameScreen) view.getGame());
			this.gameScreenController.setupMenuListeners(view);
		}
	}

}
