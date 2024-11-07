package scrabble.network.host;

import scrabble.model.Ruleset;
import scrabble.model.Tile;
import scrabble.network.messages.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * This class implements host responsibilities for a Scrabble game. It accepts
 * messages from clients, executes any changes on the host end, and relays messages
 * to other clients. It uses threads of {@link HostReceiver} to listen for new messages
 * and {@link TileBag} to randomly send tiles to replenish player racks.
 * <p>
 *     A running thread of this class continuously accepts new clients until
 *     this is signalled to start the game. Only 4 clients can be accepted into a game.
 * </p>
 * @see scrabble.network.messages
 */
public class PartyHost extends Thread implements PropertyChangeListener {
	/*
	Some message processing is likely to be needed depending on the messages received.
	For example, when a PlayTiles message is received, the host must send a NewTiles message
	to the client who sent the PlayTiles. Host then must send PlayTiles to the other
	clients, with the new number of tiles which the original client has in their rack.

	For example implementation, see the class of the same name in ../networkPrototype
	 */

	/**
	 * The identifying number of the host. Used in <code>Message</code> objects to
	 * signify the sender.
	 */
	public static final int HOST_ID = -1;

	private final String IPAddress;
	private ServerSocket server;
	private TileBag tileBag;
	private HashMap<HostReceiver, Integer> playerIdMap;
	private HashMap<HostReceiver, ArrayList<Tile>> playerTiles;
	private HashMap<Integer, HostReceiver> playerIdToMessenger;
	private Ruleset ruleset;
	private boolean inGame;
	private final int TILE_RACK_SIZE = 7;

	public static void main(String[] args) throws UnknownHostException {
		int port = 0;

		System.out.println("Your IP: " + Inet4Address.getLocalHost().getHostAddress());
		PartyHost partyHost = new PartyHost(port);
		System.out.println("Listening at port " + partyHost.getPort());


		Thread thread = new Thread(partyHost);
		thread.start();
	}

	/**
	 * Constructs a PartyHost object listening to a port.
	 *
	 * @param port the port on which to accept clients
	 */
	public PartyHost(int port) {
		super();
		server = null;
		tileBag = new TileBag();
		playerIdMap = new HashMap<>(4);
		playerTiles = new HashMap<>(4);
		playerIdToMessenger = new HashMap<>(4);
		inGame = false;

		// create a server socket that refreshes every second
		// refreshes allow the run to stop execution once we are no longer looking for clients
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(1000);
			IPAddress = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the local host's IP address.
	 * @return the raw IP address in a String format.
	 */
	public String getIPAddress() {
		return IPAddress;
	}

	/**
	 * Gets the listening port.
	 * @return the port number which accepts clients.
	 */
	public int getPort() { return server.getLocalPort();}

	/**
	 * Starts a game with a given <code>Ruleset</code>.
	 * <p>
	 *     This method assigns random IDs to the connected players and signals them to
	 *     start their <code>Game</code> model instance. This method also prevents
	 *     more clients from joining the game.
	 * </p>
	 * @param ruleset the ruleset which dictates gameplay rules.
	 * @throws IOException if an error occurs in messaging clients.
	 */
	public void startGame(Ruleset ruleset) {
		try {
			this.inGame = true;		// stop looking for clients.
			this.ruleset = ruleset;
			startGame();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void run() {
		// accept clients if not in a game.
		// once game starts, stop accepting clients.
		System.out.println("Looking for clients...");
		while (!inGame) {
			while (!inGame && playerIdMap.size()<4) {
				acceptClients();
			}
		}

		// game has started, stop looking
		// all future changes handled through ClientHandler objects' calls to property change

		try {
			startGame();
		} catch (IOException e) {
			// error in writing to client
			throw new RuntimeException(e);
		}

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
				else if(message instanceof NewPlayer){
					System.out.println("NewPlayer");
					handleNewPlayer(handler, (NewPlayer) message);
				}
				success = true;
			} catch (IOException e) {
				System.out.println("uhhhhh");
			}
		}
	}

	public int getPlayerID(HostReceiver hr) {
		return playerIdMap.get(hr);
	}

	/**
	 * Sends a message to a player specified by ID.
	 *
	 * @param playerID the id of the player to send a message.
	 * @param message the message to be sent.
	 * @throws IOException
	 */
	public void sendMessage(int playerID, Message message) throws IOException {
		playerIdToMessenger.get(playerID).sendMessage(message);
	}

	/**
	 * Sends a message to all clients except the one specified.
	 * @param playerID the player to whom not to send a message
	 * @param message the message to send.
	 * @throws IOException when the message cannot be sent.
	 */
	public void sendToAllButID(int playerID, Message message) throws IOException {
		for (HostReceiver host: playerIdMap.keySet()) {
			if(!host.equals(playerIdToMessenger.get(playerID))) {
				host.sendMessage(message);
			}
		}
	}


	public void startGame() throws IOException {

		// Make a starting rack for each player.
		for (HostReceiver host: playerIdMap.keySet()){
			playerTiles.put(host, (ArrayList<Tile>) Arrays.stream(tileBag.getNext(TILE_RACK_SIZE)).toList());
		}

		int[] randomNumbers = new int[playerIdMap.size()];
		for (int i = 0; i < randomNumbers.length; i++) {
			randomNumbers[i] = i;
		}

		//shuffle randomNumbers array so the player order is randomised
		Random random = new Random();
		for (int i = 0; i < randomNumbers.length;) {
			int index = random.nextInt(randomNumbers.length);
			int temp;
			if (index != i) {
				temp = randomNumbers[index];
				randomNumbers[index] = randomNumbers[i];
				randomNumbers[i] = temp;
				++i;
			}
		}

		int i = 0;
		for (HostReceiver host: playerIdMap.keySet()){
			playerIdToMessenger.put(randomNumbers[i], host);
			playerIdMap.replace(host, randomNumbers[i]);
			++i;
		}

		int j = 0;
		int[] playerID = new int[playerIdMap.size()];
		for (HostReceiver host: playerIdMap.keySet()) {
			playerID[j] = playerIdMap.get(host);
			++j;
		}

		for (HostReceiver host: playerIdMap.keySet()) {
			StartGame startGameMessage = new StartGame(HOST_ID, playerIdMap.get(host), playerID, ruleset, playerTiles.get(host).toArray(new Tile[0]));
			host.sendMessage(startGameMessage);
		}
	}

	public Tile[] getTiles (int size){
		return tileBag.getNext(size);
	}

	/*********************************************************
	 * 					Private Methods						 *
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

			//populate playerIdMap
			playerIdMap.put(clientHandler, playerIdMap.size());
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

	private void handleNewPlayer(HostReceiver source, NewPlayer message) throws IOException {
		for (HostReceiver host: playerIdMap.keySet()) {
			NewPlayer newPlayerMessage = new NewPlayer(HOST_ID, playerIdMap.get(host), message.getPlayerName());

			if(!playerIdMap.get(host).equals(playerIdMap.get(source))){
				host.sendMessage(newPlayerMessage);
			}
		}
	}

	/*
	Leave for later.
	is extension feature.
	 */
	private void handleChallenge(HostReceiver source, Challenge newValue) throws IOException {

		source.sendMessage(newValue);
	}
}
