package scrabble.network.host;

/*
this class is the server side
Usage: 	javac scrabble/network/host/PartyHost.java
		java scrabble/network/host/PartyHost
		Use ../controllers/NetworkController as client
David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"
*/

import scrabble.model.Tile;
import scrabble.network.messages.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

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
	private ArrayList<Socket> clientSockets;
	//private HashMap<ClientHandler, ObjectOutputStream> outputStreamMap;
	private TileBag tileBag;


	public static void main(String[] args) throws UnknownHostException {
		int port = 5000;

		System.out.println("Your IP: " + Inet4Address.getLocalHost().getHostAddress());
		System.out.println("Listening at port " + port);

		PartyHost partyHost = new PartyHost(5000);
		Thread thread = new Thread(partyHost);
		thread.start();
	}

	public PartyHost(int port) {
		inGame = false;
		server = null;
		listeners = new ArrayList<>(4);
		clientSockets = new ArrayList<>(4);
		//outputStreamMap = new HashMap<>(4);
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(1000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public void startGame() {
		this.inGame = true;
	}

	@Override
	public void run() {
		// accept clients if not in a game.
		// once game starts, stop accepting clients.
		System.out.println("Looking for clients...");
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
		ClientHandler handler = (ClientHandler) evt.getSource();
		//ObjectOutputStream outputStream = outputStreamMap.get(handler);
		boolean success = false;
		while (!success) {
			try {
				if (message instanceof Challenge) {
					System.out.println("Challenge");
					handleChallenge(handler, (Challenge) message);
				} else if (message instanceof ExchangeTiles) {
					System.out.println("Exchange");
					handleExchangeTiles(handler, (ExchangeTiles) message);
				} else if (message instanceof ExitParty) {
					System.out.println("Exit");
					handleExitParty(handler, (ExitParty) message);
				} else if (message instanceof NewTiles) {
					System.out.println("NewTiles");
					handleNewTiles(handler, (NewTiles) message);
				} else if (message instanceof PassTurn) {
					System.out.println("Pass");
					handlePassTurn(handler, (PassTurn) message);
				} else if (message instanceof PlayTiles) {
					System.out.println("PlayTiles");
					handlePlayTiles(handler, (PlayTiles) message);
				}
				success = true;
			} catch (IOException e) {
				System.out.println("uhhhhh");
			}
		}
	}

	/*********************************************************
	 * 				Private Methods							 *
	 *********************************************************/

	private void acceptClients() {
		try {
			// establish connection with the client
			Socket client = server.accept();
			System.out.println("New client added");


			//clientSockets.add(client);
			ClientHandler clientHandler = new ClientHandler(client, this);
			//clientHandler.sendMessage(new NewTiles(-1, new Tile[] {
					//new Tile('A', new Point(7, 7))
			//}));
			//this.outputStreamMap.put(clientHandler, outputStream);

			// start the thread
			Thread clientThread = new Thread(clientHandler);
			listeners.add(clientThread);
			clientThread.start();
		}
		catch (SocketTimeoutException e) {
			//System.out.println("\ttrying again..");
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	IMPLEMENTATION NOTES FOR handle_____ METHODS
	often need to relay unchanged message to other clients.
	This can be done by calling outputStreamMap.keys(), iterating
	over the keys and sending to outputStream when key != source

	In other cases, we need to send a different message to the source client, which
	will require tileBag gets.
	 */


	/*
	stubs
	 */

	private void handlePlayTiles(ClientHandler source, PlayTiles newValue) throws IOException {
		source.sendMessage(newValue);
	}

	private void handlePassTurn(ClientHandler source, PassTurn newValue) throws IOException {
		source.sendMessage(newValue);
	}

	private void handleNewTiles(ClientHandler source, NewTiles newValue) throws IOException {
		source.sendMessage(newValue);
	}

	private void handleExitParty(ClientHandler source, ExitParty newValue) throws IOException {
		//source.sendMessage(newValue);
		source.halt();
	}

	private void handleExchangeTiles(ClientHandler source, ExchangeTiles newValue) throws IOException {
		source.sendMessage(newValue);
	}

	private void handleChallenge(ClientHandler source, Challenge newValue) throws IOException {
		source.sendMessage(newValue);
	}
}
