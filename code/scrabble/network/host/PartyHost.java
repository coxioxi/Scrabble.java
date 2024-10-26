package scrabble.network.host;

/*
this class is the server side
Usage: 	javac scrabble/network/host/PartyHost.java
		java scrabble/network/host/PartyHost
		Use ../controllers/NetworkController as client
David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"
*/

import scrabble.network.messages.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * PartyHost receives messages from clients (via ClientHandler) and sends them to clients
 * This class processes messages as needed depending on the type
 */
public class PartyHost implements Runnable, PropertyChangeListener {
	/*
	Some message processing is likely to be needed depending on the messages received.
	For example, when a PlayTiles message is received, the host must send a NewTiles message
	to the client who sent the PlayTiles. Host then must send PlayTiles to the other
	clients, with the new number of tiles which the original client has in their rack.

	It seems reasonable to me (david) to have helper methods for each individual type of method and
	call it using a switch or if-else-if structure based on the type of class

	For example implementation, see the class of the same name in ../networkPrototype
	 */

	private ServerSocket server;
	private boolean inGame;
	private ArrayList<Thread> listeners;


	public static void main(String[] args) {
		PartyHost partyHost = new PartyHost(5000);
	}


	public void startGame() {
		this.inGame = true;
	}

	public PartyHost(int port) {
		inGame = false;
		server = null;
		listeners = new ArrayList<>(4);
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(1000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		// accept clients if not in a game.
		// once game starts, stop accepting clients.
		while (!inGame) {
			acceptClients();
		}
		// game has started

	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// called from ClientHandler when a new message is received.
		// get the message type, do processing and return a message if necessary,
		// send message to other clients.

		Message message = (Message) evt.getNewValue();

		if (message instanceof Challenge){
			handleChallenge((ClientHandler) evt.getSource(),(Message) evt.getNewValue());
		}
		else if (message instanceof ExchangeTiles){
			hendleExchangeTiles((ClientHandler) evt.getSource(),(Message) evt.getNewValue());
		}
		else if (message instanceof ExitParty){
			hendleExitParty((ClientHandler) evt.getSource(),(Message) evt.getNewValue());
		}
		else if (message instanceof NewTiles){
			hendleNewTiles((ClientHandler) evt.getSource(),(Message) evt.getNewValue());
		}
		else if (message instanceof PassTurn){
			hendlePassTurn((ClientHandler) evt.getSource(),(Message) evt.getNewValue());
		}
		else if (message instanceof PlayTiles) {
			hendlePlayTiles((ClientHandler) evt.getSource(),(Message) evt.getNewValue());
		}
	}

	private void hendlePlayTiles(ClientHandler source, Message newValue) {

	}

	private void hendlePassTurn(ClientHandler source, Message newValue) {

	}

	private void hendleNewTiles(ClientHandler source, Message newValue) {

	}

	private void hendleExitParty(ClientHandler source, Message newValue) {

	}

	private void hendleExchangeTiles(ClientHandler source, Message newValue) {

	}

	private void handleChallenge(ClientHandler source, Message newValue) {

	}

	private void acceptClients() {
	}


}
