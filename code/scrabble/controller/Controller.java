package scrabble.controller;



import scrabble.model.Game;
import scrabble.network.client.ClientMessenger;
import scrabble.network.networkPrototype.PartyHost;
import scrabble.view.frame.GameFrame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Socket;

/**
 * Run the other classes
 */
public class Controller implements PropertyChangeListener  {

	// Runs View and Game.

	// Main is super important

	// Controller creates the view and adds listeners

	private GameFrame view;
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

	}

	private void addListeners() {

	}

	private void hostGame() {

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}
}
