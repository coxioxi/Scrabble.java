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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
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

	For example implementation, see the class of the same name in ../networkPrototype
	 */

	public static final int HOST_ID = -1;

	private ServerSocket server;
	private TileBag tileBag;
	private HashMap<HostReceiver, Integer> playerIdMap;
	private HashMap<HostReceiver, ArrayList<Tile>> playerTiles;
	private boolean inGame;

	public static void main(String[] args) throws UnknownHostException {
		int port = 5000;

		System.out.println("Your IP: " + Inet4Address.getLocalHost().getHostAddress());
		System.out.println("Listening at port " + port);

		PartyHost partyHost = new PartyHost(5000);
		Thread thread = new Thread(partyHost);
		thread.start();
	}

	public PartyHost(int port) {
		server = null;
		tileBag = new TileBag();
		playerIdMap = new HashMap<>(4);
		playerTiles = new HashMap<>(4);
		inGame = false;

		// create a server socket that refreshes every second
		// refreshes allow the run to stop execution once we are no longer looking for clients
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(1000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// transfer state to start the game: no longer accepting clients
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
		// game has started, stop looking
		// all future changes handled through ClientHandler objects' calls to property change

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// called from ClientHandler when a new message is received.
		// determine the message subclass then call appropriate helper method for processing

		Message message = (Message) evt.getNewValue();
		HostReceiver handler = (HostReceiver) evt.getSource();
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
		// look for clients. Socket may time out, returns out of method
		try {
			// establish connection with the client
			Socket client = server.accept();
			System.out.println("New client added");

			HostReceiver clientHandler = new HostReceiver(client, this);

			// start the thread
			Thread clientThread = new Thread(clientHandler);
			clientThread.start();
		}
		catch (SocketTimeoutException e) {
			// This is fine. don't do anything.
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	received playTiles message; relay to other clients, then send new tiles to sender
	 */
	private void handlePlayTiles(HostReceiver source, PlayTiles newValue) throws IOException {
		for (HostReceiver host: playerIdMap.keySet()) {
			if(!playerIdMap.get(host).equals(playerIdMap.get(source))) {
				PlayTiles playTilesMessage = new PlayTiles(HOST_ID, newValue.getPlayerID(), newValue.getTiles());
				host.sendMessage(playTilesMessage);
			}
		}

		Tile[] sendTiles = tileBag.getNext(newValue.getTiles().length);
		NewTiles message = new NewTiles(HOST_ID, sendTiles);
		source.sendMessage(message);
	}

	/*
	received PassTurn message; relay.
	 */
	private void handlePassTurn(HostReceiver source, PassTurn newValue) throws IOException {
		for (HostReceiver host: playerIdMap.keySet()) {
			if(!playerIdMap.get(host).equals(playerIdMap.get(source))) {
				PassTurn passTurnMessage = new PassTurn(HOST_ID, newValue.getPlayerID());
				host.sendMessage(passTurnMessage);
			}
		}
	}

	/*
	received ExitParty message; relay and replace player's tiles into tile bag
	 */
	private void handleExitParty(HostReceiver source, ExitParty newValue) throws IOException {
		for (HostReceiver host: playerIdMap.keySet()) {
			if(!playerIdMap.get(host).equals(playerIdMap.get(source))) {
				ExitParty passTurnMessage = new ExitParty(HOST_ID, newValue.getPlayerID());
				host.sendMessage(passTurnMessage);
			}
		}
		ArrayList<Tile> playerRack = playerTiles.get(source);
		tileBag.addTiles(playerRack.toArray(new Tile[0]));
		source.halt();
	}

	/*
	Exchange tiles message received. update this representation of player's rack,
	then send new tiles to player
	 */
	private void handleExchangeTiles(HostReceiver source, ExchangeTiles newValue) throws IOException {
		Tile[] exchangedTiles = newValue.getToExchange();
		for(Tile oldTile: exchangedTiles) {
			playerTiles.get(source).remove(oldTile);
		}
		Tile[] newTiles = tileBag.getNext(exchangedTiles.length);
		tileBag.addTiles(exchangedTiles);
		NewTiles newTilesMessage = new NewTiles(newValue.getPlayerID(),newTiles);
		for(Tile newTile: newTiles) {
			playerTiles.get(source).add(newTile);
		}
		source.sendMessage(newTilesMessage);

	}

	/*
	Leave for later.
	is extension feature.
	 */
	private void handleChallenge(HostReceiver source, Challenge newValue) throws IOException {

		source.sendMessage(newValue);
	}
}
