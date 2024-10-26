package scrabble.network.host;

/*
this class is the server side
Usage: 	javac scrabble/network/host/PartyHost.java
		java scrabble/network/host/PartyHost
		Use ../controllers/NetworkController as client
David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"

 */

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

	private void acceptClients() {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// called from ClientHandler when a new message is received.
		// get the message type, do processing and return a message if necessary,
		// send message to other clients.
	}

	public void startGame() {
		this.inGame = true;
	}

	public static void main(String[] args) {
		PartyHost partyHost = new PartyHost(5000);
	}
}
