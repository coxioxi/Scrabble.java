package scrabble.controllers;


import scrabble.model.Game;
import scrabble.network.client.ClientMessenger;
import scrabble.network.networkPrototype.PartyHost;
import scrabble.view.GameFrame;

import java.net.Socket;

/**
 * Run the other classes
 */
public class Controller {

	// Runs View and Game.

	// Main is super important

	// Controller creates the view and adds listeners
	private GameFrame view;
	private Game model;

	private ClientMessenger messenger;
	private Socket hostSocket;

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
}
