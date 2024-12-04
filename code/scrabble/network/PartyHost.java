package scrabble.network;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.model.Ruleset;
import scrabble.model.Tile;
import scrabble.network.messages.*;
import scrabble.view.screen.GameScreen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;

/*
how does this work...
set up host. player creates a socket, sends their name. host returns their id number.
 */

/**
 * <p>
 *     Accepts and messages clients for the running of a Scrabble game. <code>PartyHost</code> maintains
 *     a representation of a standard Scrabble tile bag to randomly assign tiles to clients.
 *     In case of a client disconnection, <code>PartyHost</code> tracks the tiles assigned to each player
 *     and returns them to the bag.
 * </p>
 * <p>
 *     <h4>Overview</h4>
 *     A thread of <code>PartyHost</code> accepts clients until the game has started or until the number
 *     of connected clients is at the {@link #MAX_NUM_PLAYERS maximum}. Each connection
 *     spawns a new thread for client communication. This thread continuously listens for new messages from
 *     the associated client while also allowing messages to be sent simultaneously. Each thread is passed
 *     the creating instance of <code>PartyHost</code> and invokes the {@link #propertyChange} method
 *     when a message is received. Typically, <code>PartyHost</code> responds to the client
 *     and passes the message on to other connected players.
 * </p>
 * <p>
 *     <h4>Usage</h4>
 *     PartyHost should be used as follows:
 *     	<ol>
 *     	    <li>Create a PartyHost object.</li>
 *     	    <li>Get the PartyHost IP address and port for display ({@link PartyHost#getIPAddress},
 *     	    		{@link PartyHost#getPort}). </li>
 *     	    <li>Invoke {@link PartyHost#run}; this call allows clients to be accepted to the socket.</li>
 *     	    <li>Invoke {@link PartyHost#startGame(scrabble.model.Ruleset)};
 *     	    	this call transitions <code>PartyHost</code> into a game state and
 *     	    	informs clients of the rules of the game.</li>
 *     	</ol>
 * </p>
 */
public class PartyHost extends Thread implements PropertyChangeListener {

	/**
	 * The identifying number of the host. Used in <code>Message</code> objects to
	 * signify the sender.
	 */
	public static final int HOST_ID = -1;
	/**
	 * The maximum number of clients which can be accepted into the game.
	 */
	public static final int MAX_NUM_PLAYERS = 4;

	private final String IPAddress;
	private ServerSocket server;

	private final HashMap<HostReceiver, Integer> receiverToIdMap;
	private final HashMap<Integer, HostReceiver> playerIdToMessenger;

	private final HashMap<HostReceiver, ArrayList<Tile>> receiverToTilesMap;
	private final HashMap<Integer, String> playerIdToNameMap;

	private PropertyChangeEvent evt;
	private Ruleset ruleset;
	private final TileBag tileBag;
	private boolean inGame;

	/**
	 * Constructs a PartyHost object listening to a port.
	 *
	 * @param port the port on which to accept clients.
	 */
	public PartyHost(int port) {
		super();
		server = null;
		tileBag = new TileBag();
		receiverToIdMap = new HashMap<>(MAX_NUM_PLAYERS);
		receiverToTilesMap = new HashMap<>(MAX_NUM_PLAYERS);
		playerIdToMessenger = new HashMap<>(MAX_NUM_PLAYERS);
		playerIdToNameMap = new HashMap<>(MAX_NUM_PLAYERS);
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

	@Override
	public void run() {
		// accept clients if not in a game.
		while (!inGame) {
			while (!inGame && receiverToIdMap.size()<MAX_NUM_PLAYERS) {
				acceptClients();
			}
		}
		// game has started, stop looking
		// all future changes handled through ClientHandler objects' calls to property change
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// let the message cook!!!
		this.evt = evt;
		Message message = (Message) evt.getNewValue();
		message.execute(this);
	}

	/**
	 * Starts a game with a given <code>Ruleset</code>.
	 * <p>
	 *     This method assigns random IDs to the connected players and signals them to
	 *     start their <code>Game</code> model instance. This method also prevents
	 *     more clients from joining the game.
	 * </p>
	 * @param ruleset the ruleset which dictates gameplay rules.
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

	public int getMessagePlayerID() {
		return ((HostReceiver) evt.getSource()).getID();
	}

	public void addPlayerName(String name){
		int index = playerIdToNameMap.size();
		this.playerIdToNameMap.put(playerIdToNameMap.size(), name);
	}

	public String[] getPlayerNames() {
		return playerIdToNameMap.values().toArray(new String[0]);
	}

	private int getPlayerID(HostReceiver hr) {
		return hr.getID();
	}

	/**
	 * Sends a message to a player specified by ID.
	 *
	 * @param playerID the id of the player to send a message.
	 * @param message the message to be sent.
	 * @throws IOException when an error occurs writing to client
	 */
	public void sendMessage(int playerID, Message message) {
		playerIdToMessenger.get(playerID).sendMessage(message);
	}

	/**
	 * Sends a message to all clients except the one specified.
	 * @param playerID the player to whom not to send a message
	 * @param message the message to send.
	 * @throws IOException when the message cannot be sent.
	 */
	public void sendToAllButID(int playerID, Message message) {
		for (HostReceiver host : receiverToIdMap.keySet()) {
			if(!host.equals(playerIdToMessenger.get(playerID))) {
				host.sendMessage(message);
			}
		}
	}

	public void halt() {
		this.inGame = true;
		receiverToIdMap.keySet().forEach(HostReceiver::halt);
	}

	/**
	 * Gets the local host's IP address.
	 * @return the raw IP address in a String format.
	 */
	public String getIPAddress() { return IPAddress; }

	/**
	 * Gets the listening port.
	 * @return the port number which accepts clients.
	 */
	public int getPort() { return server.getLocalPort();}

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

			int index = receiverToIdMap.size();
			HostReceiver clientHandler = new HostReceiver(client, this, index);

			//populate playerIdMap
			receiverToIdMap.put(clientHandler, index);
			playerIdToMessenger.put(index, clientHandler);
			clientHandler.sendMessage(new AssignID(HOST_ID, index));

			new Thread(clientHandler).start();
		}
		catch (SocketTimeoutException ignore) {}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void startGame() throws IOException {
		// Make a starting rack for each player.
		for (HostReceiver host: receiverToIdMap.keySet()){
			receiverToTilesMap.put(host, new ArrayList<>(Arrays.asList(tileBag.getNext(GameScreen.RACK_SIZE))));
		}

		int[] randomNumbers = new int[receiverToIdMap.size()];
		for (int i = 0; i < randomNumbers.length; i++) {
			randomNumbers[i] = i;
		}

		//shuffle randomNumbers array so the player order is randomised
		Random rnd = new Random();
		for (int i = randomNumbers.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = randomNumbers[index];
			randomNumbers[index] = randomNumbers[i];
			randomNumbers[i] = a;
		}

		//This hash map stores the turn as the key with the player ID and player name as a value
		HashMap<Integer, HashMap<Integer, String>> playerInfo = new HashMap<>();
		Iterator<Integer> iterator= playerIdToNameMap.keySet().iterator();

		//populates playerInfo map
		for (int i = 0; i < randomNumbers.length; i++) {
			HashMap<Integer, String> temp = new HashMap<>();
			int ID = iterator.next();
			temp.put(ID, playerIdToNameMap.get(ID));
			playerInfo.put(randomNumbers[i], temp);
		}

		int j = 0;
		int[] playerID = new int[receiverToIdMap.size()];
		for (HostReceiver host: receiverToIdMap.keySet()) {
			playerID[j] = receiverToIdMap.get(host);
			++j;
		}

		for (HostReceiver host: receiverToIdMap.keySet()) {
			StartGame startGameMessage = new StartGame(HOST_ID, receiverToIdMap.get(host), playerInfo, ruleset, receiverToTilesMap.get(host).toArray(new Tile[0]));
			host.sendMessage(startGameMessage);
		}
	}

	public void exit(int playerID) {
		HostReceiver exiting = playerIdToMessenger.remove(playerID);
		playerIdToNameMap.remove(playerID);
		tileBag.addTiles(receiverToTilesMap.remove(exiting).toArray(new Tile[0]));
		receiverToIdMap.remove(exiting);
		exiting.halt();
		sendToAllButID(playerID, new ExitParty(playerID, playerID));
	}

	/**
	 * ClientHandler is responsible for listening for new messages coming in from the clients
	 * It maintains a reference to the <code>PartyHost</code> to notify it when messages are received.
	 * For example,
	 * <code>
	 * 		Message message = inputStream.readObject();
	 * 		partyHost.sendMessage(message)
	 * 	</code>
	 * The party host is then responsible for handling the message that was received.
	 * Party host creates one ClientHandler thread for each client who joins the game
	 *
	 */
	private static class HostReceiver implements Runnable {

		private final Socket socket; 	// the socket to the client
		private final ObjectInputStream inputStream;	// the stream from which message objects are read
		private final ObjectOutputStream outputStream;

		private final PropertyChangeSupport notifier;		// notifies listener of messages received
		private final int ID;
		private boolean listening;	// whether we are listening for new messages from client

		/**
		 * Constructs a host-client messenger with an observer.
		 * @param socket the socket to the client. Must be non-null.
		 * @param listener the observer to which this sends updates.
		 * @throws IOException if an error occurs in getting the <code>socket</code>'s
		 * 	 			streams.
		 */
		public HostReceiver(Socket socket, PropertyChangeListener listener, int ID)
				throws IOException {
			this.socket = socket;
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());

			notifier = new PropertyChangeSupport(this);
			notifier.addPropertyChangeListener(listener);
			this.ID = ID;
			listening = false;
		}

		/**
		 * Gets the socket of this class.
		 * @return the socket.
		 */
		public Socket getSocket() { return socket; }

		/**
		 * Gets the ID of the player to which this object is listening.
		 * @return the ID of the player.
		 */
		public int getID() { return ID; }

		/**
		 * Listens for messages from the client and sends them to the observer.
		 * This method will cease execution after <code>halt</code> is called or
		 * if the client closes the socket.
		 */
		public void run() {
			// listen for objects from the stream
			// use the ObjectInputStream to get the objects.
			// send a notification to the listener via PropertyChangeSupport object

			// listen for new messages. stop when this is told to halt, or when socket has been closed
			listening = true;
			Message newMessage = null;
			while (listening) {
				try {
					newMessage = (Message) inputStream.readObject();
				} catch (EOFException | SocketException e) {
					// Client has closed socket.
					newMessage = new ExitParty(this.ID, this.ID);
					this.halt();
				} catch (IOException | ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
				// got a message. tell the listener
				notifier.firePropertyChange("message", null, newMessage);
			}

			// we have stopped listening. close streams
			try {
				closeStreams();
			} catch (IOException ignore) {}
		}

		// closes socket and associated streams
		private void closeStreams() throws IOException {
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			socket.close();
		}

		/**
		 * Sends a message to the client associated with this object.
		 * @param message the message to send. Must be non-null.
		 */
		public void sendMessage(Message message) {
			try {
				outputStream.writeObject(message);
				outputStream.flush();
			} catch (IOException e) {
				ExitParty exitParty = new ExitParty(this.ID, this.ID);
				notifier.firePropertyChange("message", null, exitParty);
			}
		}

		/**
		 * Stops the execution of the <code>run</code> method, closing the connection to the client.
		 */
		public void halt() { listening = false; }

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof HostReceiver)) return false;
			Socket socket2 = ((HostReceiver)obj).getSocket();
			return (obj.getClass() == this.getClass()) &&
					socket.equals(socket2);
		}
	}

	/**
	 * Maintains the bag of tiles. Allows tiles to be randomly removed and for tiles to be added.
	 */
	public static class TileBag {
		private ArrayList<Tile> tileBag;// stores a number of a given tile

		/**
		 * Constructs a TileBag with the standard 100 tiles.
		 * @see <a href="https://en.wikipedia.org/wiki/Scrabble_letter_distributions">Letter distributions</a>
		 */
		public TileBag() {
			fillTileBag();
		}

		/**
		 * Adds a collection of tiles to the bag.
		 * @param tiles the tiles to add to the bag.
		 */
		public void addTiles(Tile[] tiles) { tileBag.addAll(Arrays.asList(tiles)); }

		/**
		 * Randomly selects the next <code>numTiles</code> tiles from the bag.
		 * @param numTiles how many tiles should be returned. If this number
		 *                 exceeds the number of tiles remaining, the array returned will
		 *                 be of size <code>getRemainingTiles</code>.
		 * @return a randomly picked array of tiles with length <code>numTiles</code>.
		 */
		public Tile[] getNext(int numTiles) {
			Tile[] newTiles = new Tile[numTiles];
			Random random = new Random();
			for(int i = 0; i < numTiles; i++){
				if(!tileBag.isEmpty()) {
					newTiles[i] = tileBag.get(random.nextInt(tileBag.size()));
					tileBag.remove(newTiles[i]);
				}
			}
			return newTiles;
		}

		/**
		 * Gets the number of remaining tiles available to be removed.
		 * @return number of remaining tiles.
		 */
		public int getRemainingTiles() { return tileBag.size(); }

		/*
		 * Fills the tile bag with the correct number of each Tile, as specified by Tile.TileScore.
		 */
		private void fillTileBag() {
			ArrayList<Tile> letterList = new ArrayList<>();
			for (Tile.TileScore tileScore : Tile.TileScore.values()) {
				Collections.addAll(letterList, Tile.getTiles(tileScore));
			}
			tileBag = letterList;
			System.out.println(this);
		}

		/**
		 * Creates a <code>String</code> representation of this <code>TileBag</code>.
		 * The string contains all letters in the bag and their frequencies.
		 * @return A string of this bag.
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();
			char current = this.tileBag.get(0).getLetter();
			int numCurrent = 1;
			for (int i = 1; i < tileBag.size(); i++) {
				char letter = tileBag.get(i).getLetter();
				if (letter == current) numCurrent++;
				else {
					sb.append(current).append(':').append(numCurrent).append("; ");
					current = letter;
					numCurrent = 1;
				}
			}
			sb.append(current).append(": ").append(numCurrent);
			return sb.toString();
		}
	}
}
