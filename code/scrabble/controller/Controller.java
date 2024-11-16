package scrabble.controller;

import scrabble.model.*;
import scrabble.network.messages.ExitParty;
import scrabble.network.messages.Message;
import scrabble.network.host.PartyHost;
import scrabble.network.messages.NewPlayer;
import scrabble.network.messages.PlayTiles;
import scrabble.view.frame.ScrabbleGUI;
import scrabble.view.screen.*;
import scrabble.view.screen.GameScreen;
import scrabble.view.screen.component.RackPanel;
import scrabble.view.screen.component.TilePanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


/*
 * TODO:
 *  fix removeTile to not be ugly
 */

/**
 * Run the other classes
 */

public class Controller implements PropertyChangeListener  {
	/**
	 * The port number to use for socket communication. 0 means the port is automatically allocated.
	 * The port number can be accessed via <code>this.getHost().getPort()</code> when this controller is
	 * hosting, or <code>this.getSocket().getPort()</code> when not hosting.
	 * <br>
	 * See also: {@link PartyHost#getPort() PartyHost.getPort()}
	 */
	public static final int PORT = 0;

	private ScrabbleGUI view;	// maintains the GUI
	private Game model;			// maintains Scrabble game data

	private ClientMessenger messenger;		// inner class for communication with host
	private Socket hostSocket;				// socket to the partyHost
	private GameScreenController gameScreenController;		// makes changes to view.screen.gameScreen
	private MainMenuController mainMenuController;			// makes changes to view.screen.MainMenu
	private HostScreenController hostScreenController;		// makes changes to view.screen.HostScreen
	private JoinScreenController joinScreenController;		// makes changes to view.screen.JoinScreen

	/*
	reference to the party host
	when this controller is the manager of the party.
	note that this field is null when this computer is not the host
	 */
	private PartyHost host;
	private int selfID;		// Player ID associated with this instance. Assigned by Party's Host.

	public static void main(String[] args) { new Controller(); }

	/**
	 * Constructs a new Controller object with a visible GUI.
	 * The game is not yet initialized, as on screen changes must be made.
	 */
	public Controller() {
		view = new ScrabbleGUI();
		view.setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE
		);				// Allow window listeners to handle closing events
		addListeners(view);
		this.showMain();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// each message has its own implementation of changes which should be made in controller
		((Message) evt.getNewValue()).execute(this);
	}

	/**
	 * Creates a socket to a listening <code>ServerSocket</code> and creates a
	 * thread to listen for messages.
	 * <p>
	 *    The thread listens for messages from the host then calls {@link Controller#propertyChange}
	 *    to make appropriate changes. This thread also allows messages to be sent to the host.
	 * </p>
	 * @param ip the IP address of the host, represented in the standard IPv4, specified by <code>InetAddress</code>.
	 * @param port the port number on which the host is listening
	 * @throws IOException when the connection is unsuccessful
	 * @see java.net.Inet4Address
	 * @see Controller.ClientMessenger
	 */
	public void setupSocket(String ip, int port) throws IOException {
		hostSocket = new Socket(ip, port);
		messenger = new ClientMessenger(hostSocket, this);
		Thread clientMessenger = new Thread(messenger);
		clientMessenger.start();
	}

	/**
	 * Sends a NewPlayer message to the host. The ID of this player has not yet been decided,
	 * so a stub ID is used.
	 *
	 * @param name the name of the player to add.
	 * @throws IOException if an error occurs in sending the message.
	 */
	public void sendNewPlayer(String name) throws IOException {
		messenger.sendMessage(new NewPlayer(0, 0, name));
	}

	/**
	 * Initializes a <code>PartyHost</code> to manage network server issues and
	 * updates the GUI's state.
	 *
	 * @param name the name of the player who is hosting.
	 * @throws IOException if an error occurs in setting up the <code>PartyHost</code>
	 */
	public void setUpHost(String name) throws IOException {
		host = new PartyHost(PORT);
		host.start();
		HostScreen hostScreen = view.getHost();
		hostScreen.getHostsIP().setText(host.getIPAddress());
		hostScreen.getHostPort().setText(""+host.getPort());
		setupSocket(host.getIPAddress(), host.getPort());
		try {
			messenger.sendMessage(new NewPlayer(selfID, selfID, name));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		hostScreen.addPlayerName(name);
	}

	/**
	 * Sends a notification of the selected game options to the <code>PartyHost</code>
	 * responsible for communication.
	 *
	 * @param challengesAllowed whether challenges are enabled.
	 * @param dictionary the path to the dictionary.
	 * @param playerTime how many seconds a player has to make their turn
	 * @param gameTime how many seconds the game may last
	 */
	public void sendRulesToHost(boolean challengesAllowed, String dictionary, int playerTime, int gameTime) {
		Ruleset ruleset = new Ruleset(gameTime, playerTime, challengesAllowed, dictionary);
		host.startGame(ruleset);
	}

	/**
	 * Initializes the state of the game model and updates the GUI to the board screen.
	 *
	 * @param ruleset the ruleset which applies to the game.
	 * @param playerNames the names of the players in the game. These should be ordered by turn number.
	 * @param playerID the IDs of the players. These should correspond to those in <code>playerNames</code>.
	 * @param startingTiles the tiles which this player has at the start of the game.
	 */
	public void startGame(Ruleset ruleset, String[] playerNames, int[] playerID, Tile[] startingTiles) {
		// add tiles to game and gameScreen
		// pass ruleset and the other stuff to setUpGameScreen
		// use the info provided to make players for the game

		this.getView().getGame().setupGameItems(playerNames, ruleset.getTotalTime(), ruleset.getTurnTime(), startingTiles);
		gameScreenController.addRackTileListeners();
		Player[] players = new Player[playerNames.length];
		LocalPlayer self = null;
		for (int i = 0; i < players.length; i++) {
			if (playerID[i] == this.selfID) {
				self = new LocalPlayer(playerNames[i], playerID[i], i, new ArrayList<>(List.of(startingTiles)));

			}
			players[i] = new Player(playerNames[i], playerID[i], i);
		}
		ruleset.setupDictionary();
		model = new Game(players, new Board(), ruleset, self);
		showGame();
		if (model.getCurrentPlayer() != selfID) gameScreenController.setRackButtonsEnabled(false);
	}

	/**
	 * Adds specified tiles to this player's rack (shown in the GUI).
	 * @param tiles the tiles to add.
	 */
	public void addTiles(Tile[] tiles) {
		model.addTiles(tiles);
		gameScreenController.addTiles(tiles);
//		gameScreenController.setRackButtonsEnabled(false);
	}

	/**
	 * Plays specified tiles on the board for a player. The score of the word(s) is calculated
	 * and added to that player's score.
	 *
	 * @param playerID the unique ID of the player who is changing the board.
	 * @param tiles the tiles to be placed on the board
	 */
	public void playTiles(int playerID, Tile[] tiles) {
		if (playerID == selfID) selfPlayTiles(tiles);
		else otherPlayTiles(playerID, tiles);
		gameScreenController.setRackButtonsEnabled(model.getCurrentPlayer() == selfID);
	}
	private void otherPlayTiles(int playerID, Tile[] tiles) {
		model.playTiles(playerID, tiles);
		view.getGame().addToBoard(tiles);
		Player player = null;
		for (Player player1 : model.getPlayers()) {
			if (player1.getID() == playerID) player = player1;
		}
		view.getGame().updateScore(player.getName(), player.getScore());
	}
	private void selfPlayTiles(Tile[] tiles) {

		int score = model.playTiles(selfID, tiles);
		if (score >= 0) {
			view.getGame().updateScore(model.getSelf().getName(), model.getSelf().getScore());
			try {
				getMessenger().sendMessage(new PlayTiles(selfID, selfID, tiles));
				getView().getGame().disableLastPlayedTiles();
			} catch (IOException e) {
				getMessenger().halt();
			}
		}
		else {
			resetLastPlay();
		}
	}

	/**
	 * Removes the most recently played tiles from the board and places them in the rack.
	 */
	public void resetLastPlay(){
		//loop through the rack
		int size = this.view.getGame().playedTiles.size();
		for (int i = 0; i < size; ++i){
			gameScreenController.removeTile(view.getGame().playedTiles.get(0));
		}
	}

	/**
	 * Removes a specific tiles from this player's rack.
	 * @param tile the tile to be removed.
	 */
	public void removeRackTile(Tile tile) {
		RackPanel rackPanel = view.getGame().getRackPanel();
		for(TilePanel tp: rackPanel.getTilePanels()){
			if(tp.getButton().getText().equals(""+tile.getLetter())){
				tp.setButton(new JButton(" "));
				break;
			}
		}
	}

	/**
	 * Gets the view.
	 * @return the view component.
	 */
	public ScrabbleGUI getView() { return view; }

	/**
	 * Gets this controller's model.
	 * @return the game model. Null if the player is not in game.
	 */
	public Game getModel() { return model; }

	/**
	 * The player ID of this player.
	 * @return the integer ID of this player.
	 */
	public int getSelfID() { return selfID; }

	/**
	 * gets the messenger to the host
	 * @return the <code>ClientMessenger</code>
	 */
	public ClientMessenger getMessenger() { return messenger; }

	/**
	 * Gets the socket to the host.
	 * @return a socket to the host. Null if it has not yet been created.
	 */
	public Socket getHostSocket() { return hostSocket; }

	/**
	 * Gets the host of this party.
	 * @return the <code>PartyHost</code> of this party. Null if this controller is not hosting.
	 */
	public PartyHost getHost() { return host; }

	/**
	 * Sets the ID of this player.
	 * @param selfID the new ID of this player
	 */
	public void setSelfID(int selfID) { this.selfID = selfID; }

	/**
	 * Changes the state of the GUI to display the game, with the appropriate
	 * <code>WindowClosing</code> listener.
	 */
	public void showGame() {
		view.showGame();
		removeListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameClose();
			}
		});
	}

	/**
	 * Changes the state of the GUI to display the host screen, with the appropriate
	 * <code>WindowClosing</code> listener.
	 */
	public void showHost() {
		view.showHost();
		removeListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				hostClose();
			}
		});
	}

	/**
	 * Changes the state of the GUI to display the join screen, with the appropriate
	 * <code>WindowClosing</code> listener.
	 */
	public void showJoin() {
		view.showJoin();
		removeListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				joinClose();
			}
		});
	}

	/**
	 * Changes the state of the GUI to display the main menu, with the appropriate
	 * <code>WindowClosing</code> listener.
	 */
	public void showMain() {
		view.showMain();
		removeListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainClose();
			}
		});
		view.setMenuVisible(false);
	}

	/**
	 * Changes the state of the GUI to display the waiting screen, with the appropriate
	 * <code>WindowClosing</code> listener.
	 */
	public void showWaiting() {
		view.showWaiting();
		removeListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				waitingClose();
			}
		});
	}

	/**
	 * Changes the state of the GUI to display the winner screen, with the appropriate
	 * <code>WindowClosing</code> listener.
	 */
	public void showWinner() {
		view.showWinner();
		removeListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				winnerClose();
			}
		});
	}

	/**
	 * Removes Window listeners from the view.
	 */
	public void removeListeners() { for (WindowListener wl : view.getWindowListeners()) view.removeWindowListener(wl); }

	/**
	 * Displays a dialog to confirm quitting/leaving a party.
	 * @return an integer representing the choice selected
	 * @see JOptionPane
	 */
	public int showQuitDialog() { return JOptionPane.showConfirmDialog(view, "Are you sure you want to leave?\nYou will not be able to rejoin.", "Quit?", JOptionPane.WARNING_MESSAGE); }

	/**
	 * Displays the set of rules used for the current game, specified by the
	 * game's <code>Ruleset</code>.
	 */
	public void showRulesDialog() {
		JOptionPane.showMessageDialog(view, "1.~~~~~~~~~\n2.~~~~~~~~~\n3.~~~~~~~~~~~~\n4.~~~~~~~~~~", "Rules", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays a warning that the player has not entered their name.
	 */
	public void showNoNameDialog() {
		JOptionPane.showMessageDialog(view, "You must put in a name!", "No Name", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Displays a warning that the player has not entered the host's IP.
	 */
	public void showNoIPDialog() {
		JOptionPane.showMessageDialog(view, "You must input the host's IP Address!", "No IP", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Displays a warning that the player has not entered the host's port.
	 */
	public void showNoPortDialog() {
		JOptionPane.showMessageDialog(view, "You must enter the host's port number!", "Incorrect Port Input", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Displays an error that the application could not connect with the host.
	 */
	public void showIPErrorDialog() {
		JOptionPane.showMessageDialog(view, "The Host refused to connect.\nCheck your IP Address!", "No Connection", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Stops the execution of this application.
	 */
	public void exit() {
		view.dispose();
		haltThreads();
	}

	/**
	 * Implements communication with a host, using the objects in
	 * the {@link scrabble.network.messages messages package}.
	 * <p>
	 *     A thread of this class continuously listens for message objects
	 *     from the host which are then sent to the PropertyChangeListener
	 *     passed in at construction. A client of this class can send messages to
	 *     the host by calling {@link #sendMessage}. When this thread is no longer needed,
	 *     calling the method {@link #halt} signals run to cease execution and close the socket.
	 * </p>
	 */
	public static class ClientMessenger implements Runnable {

		private final Socket server;	// socket to server/host

		private final ObjectOutputStream outputStream;		// stream for sending objects to host
		private final ObjectInputStream inputStream;		// stream for receiving objects from host

		private final PropertyChangeSupport notifier;		// notifies listener of property changes
															// that is, when messages are received
		private boolean isListening;		// whether this object is listening for new messages

		/**
		 * Constructs a client-host messenger with an observer.
		 * @param server the socket to the server. This value may not be null.
		 * @param listener the observer which receives updates on new messages.
		 * @throws IOException if an error occurs in getting the <code>server</code>'s
		 * streams.
		 */
		public ClientMessenger(Socket server, PropertyChangeListener listener)
				throws IOException {
			// setting up streams
			this.server = server;
			outputStream = new ObjectOutputStream(server.getOutputStream());
			inputStream = new ObjectInputStream(server.getInputStream());
			notifier = new PropertyChangeSupport(this);
			notifier.addPropertyChangeListener(listener);
			isListening = false;
		}

		/**
		 * Listens for messages from the host and sends them to the observer.
		 * This method will cease execution after <code>halt</code> is called or
		 * if the host closes the socket.
		 */
		public void run() {
			// here we start listening for new messages to come in
			isListening = true;
			// container for new message.
			Message message;
			do {
				try {
					message = (Message) inputStream.readObject();
				}
				catch (SocketException | EOFException e) {
					// Thrown when the host has closed their connection
					message = new ExitParty(PartyHost.HOST_ID, PartyHost.HOST_ID);
					this.halt();
				}
				catch (IOException | ClassNotFoundException e) {
					throw new RuntimeException(e);
				}

				// we have received a message; tell listener so they can do stuff with it
				if (message != null) {
					notifier.firePropertyChange("Message", null, message);
				}
			} while (isListening);

		/*
		 this has stopped listening for new messages, perhaps because we have disconnected,
		 perhaps the host has disconnected. either will have been handled by the controller
		 In the former case, the controller would have sent a disconnect signal; in the latter, this
		 will have notified the controller (see socketException catch clause)

		 now, simply close the streams and end execution of this run().
		 */
			try {
				closeStreams();
			} catch (IOException ignore) {
			}
		}

		/**
		 * Sends a message to the host and flushes the stream.
		 * @param message the message to be sent. Must be non-null.
		 * @throws IOException if an error occurs in output stream.
		 */
		public void sendMessage(Message message) throws IOException {
			outputStream.writeObject(message);
			outputStream.flush();
		}

		/**
		 * Ceases execution of <code>run()</code>.
		 */
		public void halt() {
			isListening = false;
			try {
				inputStream.close();
			} catch (IOException ignore) {
			}
		}

		// close socket and associated streams
		private void closeStreams() throws IOException {
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			if (!server.isClosed()) server.close();
		}
	}

	/* * * * * * * * * * * * * * * * * * * * * *
	* 			Private Methods				   *
	 * * * * * * * * * * * * * * * * * * * * * */

	/*
	 * adds listeners to the individual screens
	 */
	private void addListeners(ScrabbleGUI view) {
		addMenuListeners(view.getMainMenu());
		addHostListeners(view.getHost());
		addJoinListeners(view.getJoin());
		addWaitingListeners(view.getWaiting());

		this.gameScreenController = new GameScreenController(this, view.getGame());
		gameScreenController.setupMenuListeners(view);
	}

	private void addWaitingListeners(JPanel waiting) {}

	/*
	 * adds listeners to the Join screen; allows player to enter name and host info,
	 * then attempt to join.
	 */
	private void addJoinListeners(JoinScreen join) {
		// add listeners to the buttons on the join game screen
		joinScreenController = new JoinScreenController(this, join);
	}

	/*
	 * adds listeners to the Host screen; allows player to change options and start the game.
	 */
	private void addHostListeners(HostScreen host) {
		// add listeners to the buttons on the host screen
		hostScreenController = new HostScreenController(this, host);
	}

	/*
	 * adds listeners to the menu screen; allows player to select join, host, or quit, with options for sound
	 */
	private void addMenuListeners(MainMenuScreen mainMenu) {
		// add listeners to the buttons on the main menu
		mainMenuController = new MainMenuController(this, mainMenu);

	}

	/*
	 * handles 'X' being pressed from the main menu.
	 */
	public void mainClose() {
		exit();
	}

	/*
	 * handles 'X' being pressed from the host screen.
	 */
	private void hostClose() {
		int selected = showQuitDialog();
		if (selected == JOptionPane.YES_OPTION) {
			haltThreads();
			view.getHost().resetPlayerNames();
			this.showMain();
		}
	}

	/*
	 * handles 'X' being pressed from the join screen.
	 */
	private void joinClose() {
		if (host != null) host.halt();
		this.showMain();
	}

	/*
	 * handles 'X' being pressed from the winner screen.
	 */
	private void winnerClose() {
		this.showMain();
	}

	/*
	 * handles 'X' being pressed from the waiting screen.
	 */
	private void waitingClose() {
		int selected = showQuitDialog();
		if (selected == JOptionPane.YES_OPTION) {
			messenger.halt();
			this.showMain();
		}
	}

	/*
	 * handles 'X' being pressed from the game screen.
	 */
	private void gameClose() {
		// double check that they meant to end their game, then quit the application.
		int selected = showQuitDialog();
		if (selected == JOptionPane.YES_OPTION) {
			this.exit();
		}
	}

	/*
	 * stops the execution of threads running in the background.
	 */
	private void haltThreads() {
		if (messenger!= null) messenger.halt();
		if (this.host != null) {
			host.halt();
		}
	}
}
