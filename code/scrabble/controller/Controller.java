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

	public static void main(String[] args) {
		new Controller();
	}

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
	 *
	 * @param name
	 * @throws IOException
	 */
	public void sendNewPlayer(String name) throws IOException {
		messenger.sendMessage(new NewPlayer(0, 0, name));
	}

	/**
	 *
	 * @param name
	 * @throws IOException
	 */
	public void setUpHost(String name) throws IOException {
		host = new PartyHost(PORT);
		host.start();
//		System.out.println(host.getIPAddress() + " " + host.getPort());
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
	 *
	 * @param challengesAllowed
	 * @param dictionary
	 * @param playerTime
	 * @param gameTime
	 */
	public void sendRulesToHost(boolean challengesAllowed, String dictionary, int playerTime, int gameTime) {
		Ruleset ruleset = new Ruleset(gameTime, playerTime, challengesAllowed, dictionary);
		host.startGame(ruleset);
	}

	/**
	 *
	 * @param ruleset
	 * @param playerNames
	 * @param playerID
	 * @param startingTiles
	 */
	public void startGame(Ruleset ruleset, String[] playerNames, int[] playerID, Tile[] startingTiles) {
		// add tiles to game and gameScreen
		// pass ruleset and the other stuff to setUpGameScreen
		// use the info provided to make players for the game

		this.getView().setupGameScreen(ruleset, playerNames, startingTiles);
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
	 *
	 * @param tiles
	 */
	public void addTiles(Tile[] tiles) {
		model.addTiles(tiles);
		gameScreenController.addTiles(tiles);
//		gameScreenController.setRackButtonsEnabled(false);
	}

	/**
	 *
	 * @param playerID
	 * @param tiles
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
			resetLastPlay(getView().getGame());
		}
	}

	/**
	 *
	 * @param gameScreen
	 */
	public void resetLastPlay(GameScreen gameScreen){
		//loop through the rack
		int size = gameScreen.playedTiles.size();
		for (int i = 0; i < size; ++i){
			gameScreenController.removeTile(gameScreen.playedTiles.get(0));
		}
	}

	/**
	 *
	 * @param tile
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

	public ScrabbleGUI getView() {
		return view;
	}

	public Game getModel() {
		return model;
	}

	public int getSelfID() {
		return selfID;
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

	public void setSelfID(int selfID) {
		this.selfID = selfID;
	}

	/**
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
	 */
	public void removeListeners() {
		for (WindowListener wl : view.getWindowListeners()) {
			view.removeWindowListener(wl);
		}
	}

	/**
	 *
	 * @return
	 */
	public int showQuitDialog() {
		return JOptionPane.showConfirmDialog(view, "Are you sure you want to leave?\nYou will not be able to rejoin.", "Quit?", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 *
	 */
	public void showRulesDialog() {
		JOptionPane.showMessageDialog(view, "1.~~~~~~~~~\n2.~~~~~~~~~\n3.~~~~~~~~~~~~\n4.~~~~~~~~~~", "Rules", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 *
	 */
	public void showNoNameDialog() {
		JOptionPane.showMessageDialog(view, "You must put in a name!", "No Name", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 *
	 */
	public void showNoIPDialog() {
		JOptionPane.showMessageDialog(view, "You must input the host's IP Address!", "No IP", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 *
	 */
	public void showNoPortDialog() {
		JOptionPane.showMessageDialog(view, "You must enter the host's port number!", "Incorrect Port Input", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 *
	 */
	public void showIPErrorDialog() {
		JOptionPane.showMessageDialog(view, "The Host refused to connect.\nCheck your IP Address!", "No Connection", JOptionPane.WARNING_MESSAGE);
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

	private void addListeners(ScrabbleGUI view) {
		addMenuListeners(view.getMainMenu());
		addHostListeners(view.getHost());
		addJoinListeners(view.getJoin());
		addWaitingListeners(view.getWaiting());

		// stub, not for active game. see propertyChangeListener
		this.gameScreenController = new GameScreenController(this, (GameScreen) view.getGame());
		gameScreenController.setupMenuListeners(view);
		//this.mainMenuController = new MainMenuController(this, (MainMenuScreen) view.getMainMenu());

	}

	private void addWaitingListeners(JPanel waiting) {
		// add listeners to the buttons on the waiting players screen
	}

	private void addJoinListeners(JoinScreen join) {
		// add listeners to the buttons on the join game screen
		joinScreenController = new JoinScreenController(this, join);
	}

	private void addHostListeners(HostScreen host) {
		// add listeners to the buttons on the host screen
		hostScreenController = new HostScreenController(this, host);
	}

	private void addMenuListeners(MainMenuScreen mainMenu) {
		// add listeners to the buttons on the main menu
		mainMenuController = new MainMenuController(this, mainMenu);

	}

	private void mainClose() {
		view.dispose();
		if (messenger!=null) messenger.halt();
		if (host != null)
			host.halt();
	}

	private void hostClose() {
		int selected = showQuitDialog();
		if (selected == JOptionPane.YES_OPTION) {
			host.halt();
			view.getHost().resetPlayerNames();
			this.showMain();
		}
	}

	private void joinClose() {
		if (host != null) host.halt();
		this.showMain();
	}

	private void winnerClose() {
		this.showMain();
	}

	private void waitingClose() {
		int selected = showQuitDialog();
		if (selected == JOptionPane.YES_OPTION) {
			messenger.halt();
			this.showMain();
		}
	}

	private void gameClose() {
		int selected = showQuitDialog();
		if (selected == JOptionPane.YES_OPTION) {
			this.exit();
		}
	}

	public void exit() {
		if (messenger!= null) messenger.halt();
		if (this.host != null) {
			host.halt();
		}
		view.dispose();
	}

}
