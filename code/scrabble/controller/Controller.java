package scrabble.controller;



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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Socket;

/**
 * Run the other classes
 */

public class Controller implements PropertyChangeListener  {
	GameScreen gameScreen = new GameScreen();	//test

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

	public void submitActionListener(){
		gameScreen.getSubmitButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlayTiles playTiles = new PlayTiles(0,0, gameScreen.getPlayedTiles().toArray(new Tile[0]));
				playTiles.execute(Controller.this);
			}
		});
	}

	public static void main(String[] args) {
		new Controller();
	}

	public Controller() {
		view = new ScrabbleGUI();
		addListeners(view);
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
