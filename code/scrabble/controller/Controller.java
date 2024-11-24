package scrabble.controller;

import scrabble.model.*;
import scrabble.network.messages.ExitParty;
import scrabble.network.messages.Message;
import scrabble.network.PartyHost;
import scrabble.network.messages.NewPlayer;
import scrabble.network.messages.PlayTiles;
import scrabble.view.ScrabbleGUI;
import scrabble.view.screen.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


/**
 * Run the other classes.
 */
public class Controller implements PropertyChangeListener  {
	/**
	 * The port number to use for socket communication. 0 means the port is automatically allocated.
	 * The port number can be accessed via <code>this.getHost().getPort()</code> when this controller is
	 * hosting, or <code>this.getSocket().getPort()</code> when not hosting.
	 * <br>
	 * @see PartyHost#getPort
	 */
	public static final int PORT = 0;

	private ScrabbleGUI view;	// maintains the GUI
	private Game model;			// maintains Scrabble game data

	private boolean fxEnable = true;
	private boolean musicEnable = true;

	private ClientMessenger messenger;		// inner class for communication with host
	private Socket hostSocket;				// socket to the partyHost
	private GameScreenController gameScreenController;		// makes changes to view.screen.gameScreen
    private HostScreenController hostScreenController;		// makes changes to view.screen.HostScreen

	private final ElevatorMusicPlayer elevatorMusicPlayer = new ElevatorMusicPlayer("Bossa nova.wav");

    /*
    reference to the party host
    when this controller is the manager of the party.
    note that this field is null when this computer is not the host
     */
	private PartyHost host;
	private int selfID;		// Player ID associated with this instance. Assigned by Party's Host.

	/**
	 * Main, to make this class executable.
	 * @param args ignored.
	 */
	public static void main(String[] args) { new Controller(); }

	/**
	 * Constructs a new Controller object with a visible GUI.
	 * The model (<code>Game</code>) is not yet initialized, as on-screen changes must be made.
	 */
	public Controller() {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignore) {}
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
	 * Initializes a <code>PartyHost</code> to manage network server issues and
	 * updates the GUI's state to reflect changes.
	 * <p>
	 *     Invoking this method marks this <code>Controller</code> as being a host
	 *     for other players to join.
	 * </p>
	 *
	 * @param name the name of the player who is hosting.
	 * @throws IOException if an error occurs in setting up the <code>PartyHost</code>
	 * @see PartyHost
	 */
	public void setUpHost(String name) throws IOException {
		host = new PartyHost(PORT);
		host.start();
		String IP = host.getIPAddress();
		int port = host.getPort();
		hostScreenController.setIPandPort(IP, port);
		setupSocket(IP, port);
		try {
			messenger.sendMessage(new NewPlayer(selfID, selfID, name));
		} catch (IOException e) { throw new RuntimeException(e); }
		hostScreenController.addPlayer(name);
	}

	/**
	 * Sends a <code>NewPlayer</code> message to the host. The ID of this player has not yet been decided,
	 * so a stub ID is used.
	 *
	 * @param name the name of the player to add.
	 * @throws IOException if an error occurs in sending the message.
	 */
	public void sendNewPlayer(String name) throws IOException {
		messenger.sendMessage(new NewPlayer(0, 0, name));
	}

	/**
	 * Sends a notification of the selected game options to the <code>PartyHost</code>
	 * responsible for communication.
	 * <p>
	 *     Invoking this method causes the <code>PartyHost</code> running in this instance of
	 *     <code>Controller</code> to send a <code>StartGame</code> message to the connected clients.
	 * </p>
	 *
	 * @param challengesAllowed whether challenges are enabled.
	 * @param dictionary the path to the dictionary.
	 * @param playerTime how many seconds a player has to make their turn.
	 * @param gameTime how many seconds the game may last.
	 * @see scrabble.network.messages.StartGame
	 */
	public void sendRulesToHost(boolean challengesAllowed, String dictionary,
								int playerTime, int gameTime) {
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

		gameScreenController.setupGameItems(playerNames, ruleset.getTotalTime(), ruleset.getTurnTime(), startingTiles);
		Player[] players = new Player[playerNames.length];
		Player.LocalPlayer self = null;
		for (int i = 0; i < players.length; i++) {
			if (playerID[i] == this.selfID) {
				self = new Player.LocalPlayer(playerNames[i], playerID[i], i, new ArrayList<>(List.of(startingTiles)));
			}
			players[i] = new Player(playerNames[i], playerID[i], i);
		}
		model = new Game(players, new Board(), ruleset, self);
		if (model.getCurrentPlayer() != selfID) gameScreenController.setRackButtonsEnabled(false);
		SwingUtilities.invokeLater(this::showGame);
	}

	/**
	 * Adds specified tiles to this player's rack (shown in the GUI).
	 * @param tiles the tiles to add.
	 */
	public void addTiles(Tile[] tiles) {
		model.addTiles(tiles);
		gameScreenController.addRackTiles(tiles);
	}

	/**
	 * Plays specified tiles on the board for a player. The score of the word(s) is calculated
	 * and added to that player's score.
	 * <p>
	 *     This method operates differently depending on what player is playing tiles.
	 *     <ul>
	 *         <li>
	 *             For the local player (that is, the player making changes to this <code>Controller</code>'s GUI),
	 *             the play is checked for validity. The play must adhere to the guidelines in the description for
	 *             {@link Board#playTiles}. If the game's <code>Ruleset</code> has challenges disabled,
	 *             all words resulting from the play are checked against the game's dictionary. When challenges
	 *             are enabled, only the locations must be validated.
	 *         </li>
	 *         <li>
	 *             For a network player making a play, the tiles are placed on the board (both in the model and
	 *             the GUI), and the player's score is updated. Neither the location of the tiles,
	 *             nor the resulting words are checked, as this work has already been done on the side of the
	 *             sending client's application
	 *         </li>
	 *     </ul>
	 *     After the local player's application validates a play, it is sent to the <code>PartyHost</code>
	 *     to be distributed to other clients. Both cases update the score of the player.
	 * </p>
	 *
	 * @param playerID the unique ID of the player who is changing the board.
	 * @param tiles the tiles to be placed on the board
	 */
	public void playTiles(int playerID, Tile[] tiles) {
		if (playerID == selfID) selfPlayTiles(tiles);
		else otherPlayTiles(playerID, tiles);
		gameScreenController.setRackButtonsEnabled(model.getCurrentPlayer() == selfID);
	}

	public void playTileFx(){
		if(fxEnable) {
			FxPlayer fxPlayer = new FxPlayer();
			fxPlayer.tilePlacementFx();
		}
	}
	public void rightTileFx(){
		if(fxEnable) {
			FxPlayer fxPlayer = new FxPlayer();
			fxPlayer.rightPlacementFx();
		}
	}

	public void wrongTileFx(){
		if(fxEnable){
		FxPlayer fxPlayer = new FxPlayer();
		fxPlayer.wrongPlacementFx();
		}
	}

	private void otherPlayTiles(int playerID, Tile[] tiles) {
		// when the player is not from this application.
		model.playTiles(playerID, tiles);
		gameScreenController.addToBoard(tiles);
		Player player = model.getPlayer(playerID);
		gameScreenController.updateScore(player.getName(), player.getScore());
	}
	private void selfPlayTiles(Tile[] tiles) {
		// when the player is this application's player.
		int score = model.playTiles(selfID, tiles);
		if (score >= 0) {
			Player p = model.getSelf();
			gameScreenController.updateScore(p.getName(), p.getScore());
			try {
				messenger.sendMessage(new PlayTiles(selfID, selfID, tiles));
				gameScreenController.disableLastPlayedTiles();
			} catch (IOException e) {
				getMessenger().halt();
			}
			//Play sound cue for when tiles are right
			rightTileFx();
		}
		else {
			resetLastPlay();

			//Play sound cue for when tiles are wrong
			wrongTileFx();
		}
	}

	/**
	 * Removes the most recently played tiles from the board and places them in the rack.
	 * @see GameScreenController#resetLastPlay
	 */
	public void resetLastPlay(){ gameScreenController.resetLastPlay(); }

	/**
	 * Removes a specific tile from this player's rack.
	 * @param tile the tile to be removed.
	 */
	public void removeRackTile(Tile tile) { int removedLocation = gameScreenController.removeRackTile(tile); }

	/**
	 * Gets the view.
	 * @return The view component.
	 */
	public ScrabbleGUI getView() { return view; }

	/**
	 * Gets this controller's model.
	 * @return The game model. Null if this player is not in a game.
	 */
	public Game getModel() { return model; }

	/**
	 * The player ID of this player.
	 * @return the integer ID of this player.
	 */
	public int getSelfID() { return selfID; }

	/**
	 * Gets the messenger to the host.
	 * @return This <code>Controller</code>'s <code>ClientMessenger</code>
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
	 * Getter for the music player
	 *
	 * @return The elevator music player responsible for starting the background music
	 */
	public ElevatorMusicPlayer getElevatorMusicPlayer() {
		return elevatorMusicPlayer;
	}

	public boolean isFxEnable() {
		return fxEnable;
	}

	public boolean isMusicEnable() {
		return musicEnable;
	}

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
		removeWindowListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameClose();
			}
		});
		gameScreenController.setAudioEnabled(musicEnable, fxEnable);
		showRulesDialog();
	}

	/**
	 * Changes the state of the GUI to display the host screen, with the appropriate
	 * <code>WindowClosing</code> listener.
	 */
	public void showHost() {
		view.showHost();
		removeWindowListeners();
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
		removeWindowListeners();
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
		removeWindowListeners();
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
		removeWindowListeners();
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
		removeWindowListeners();
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				winnerClose();
			}
		});
	}

	/**
	 * Removes <code>WindowListener</code>s from the view.
	 */
	public void removeWindowListeners() { for (WindowListener wl : view.getWindowListeners()) view.removeWindowListener(wl); }

	/**
	 * Displays a dialog to confirm quitting/leaving a party.
	 * @return An integer representing the choice selected, as specified in <code>JOptionPane</code>.
	 * @see JOptionPane
	 */
	public int showQuitDialog() { return JOptionPane.showConfirmDialog(view, "Are you sure you want to leave?\nYou will not be able to rejoin.", "Quit?", JOptionPane.WARNING_MESSAGE); }

	/**
	 * Displays the set of rules used for the current game, specified by the
	 * game's <code>Ruleset</code>.
	 */
	public void showRulesDialog() { JOptionPane.showMessageDialog(view, "1.~~~~~~~~~\n2.~~~~~~~~~\n3.~~~~~~~~~~~~\n4.~~~~~~~~~~", "Rules", JOptionPane.INFORMATION_MESSAGE); }

	/**
	 * Displays a warning that the player has not entered their name.
	 */
	public void showNoNameDialog() { JOptionPane.showMessageDialog(view, "You must put in a name!", "No Name", JOptionPane.WARNING_MESSAGE); }

	/**
	 * Displays a warning that the player has not entered the host's IP.
	 */
	public void showNoIPDialog() { JOptionPane.showMessageDialog(view, "You must input the host's IP Address!", "No IP", JOptionPane.WARNING_MESSAGE);}

	/**
	 * Displays a warning that the player has not entered the host's port.
	 */
	public void showNoPortDialog() { JOptionPane.showMessageDialog(view, "You must enter the host's port number!", "Incorrect Port Input", JOptionPane.WARNING_MESSAGE); }

	/**
	 * Displays an error that the application could not connect with the host.
	 */
	public void showIPErrorDialog() { JOptionPane.showMessageDialog(view, "The Host refused to connect.\nCheck your IP Address!", "No Connection", JOptionPane.WARNING_MESSAGE); }

	public int showConfirmPassTurnDialog() { return JOptionPane.showConfirmDialog(view, "Are you sure you want to pass your turn?", "pass turn confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); }

	/**
	 * Stops the execution of this application.
	 */
	public void exit() { view.dispose(); haltThreads(); }

	/**
	 * Implements communication with a host, using the objects in
	 * the {@link scrabble.network.messages messages} package.
	 * <p>
	 *     A thread of this class continuously listens for message objects
	 *     from the host which are then sent to the <code>PropertyChangeListener</code>
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
		 * Ceases execution of <code>run</code>.
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

		this.gameScreenController = new GameScreenController(this, view.getGame());
		gameScreenController.setupMenuListeners(view);
	}

	private void addWaitingListeners(JPanel waiting) {}

	/*
	 * adds listeners to the Join screen; allows player to enter name and host info,
	 * then attempt to join.
	 */
	private void addJoinListeners(JoinScreen join) {
        // makes changes to view.screen.JoinScreen
        JoinScreenController joinScreenController = new JoinScreenController(this, join);
	}

	/*
	 * adds listeners to the Host screen; allows player to change options and start the game.
	 */
	private void addHostListeners(HostScreen host) {
		hostScreenController = new HostScreenController(this, host);
	}

	/*
	 * adds listeners to the menu screen; allows player to select join, host, or quit, with options for sound
	 */
	private void addMenuListeners(ScrabbleGUI.MainMenuScreen mainMenu) {
        // makes changes to view.screen.MainMenu
        MainMenuController mainMenuController = new MainMenuController(this, mainMenu);
	}

	/*
	 * handles 'X' being pressed from the main menu.
	 */
	public void mainClose() { exit(); }

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
	private void winnerClose() { this.showMain(); }

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

    /**
     * Handles components being clicked on the <code>HostScreen</code> and provides convenience
     * methods for changes.
     */
    private static class HostScreenController {
        private final Controller parent;
        private final HostScreen hostScreen;


        /**
         * Constructs a HostScreenController from a parent and a <code>HostScreen</code>.
         *
         * @param parent the <code>Controller</code> on which changes are made based on user input.
         * @param hostScreen the screen to which listeners are added.
         */
        public HostScreenController(Controller parent, HostScreen hostScreen) {
            this.parent = parent;
            this.hostScreen = hostScreen;
            hostScreen.getHostButton().addActionListener(e -> hostButtonClick());
        }

        /**
         * Sets the IP and Port label of this object's <code>HostScreen</code>.
         *
         * @param IP the String representation of the Host's IP.
         * @param port the port number on which the host is listening.
         */
        public void setIPandPort(String IP, int port) {
            hostScreen.getHostsIP().setText(IP);
            hostScreen.getHostPort().setText(port+"");
        }

        /**
         * Adds the name of a player to the list of players waiting.
         * @param name the name of the player to add.
         * @see HostScreen#addPlayerName
         */
        public void addPlayer(String name) { hostScreen.addPlayerName(name); }

        /*
         * handles the submit button being clicked. Gets the options selected, then sends them to parent.
         */
        private void hostButtonClick() {
            boolean challengesEnabled = hostScreen.getChallengeBox();
            String dictionaryFile = hostScreen.getDictionaryPath();
            int playerTime = Integer.parseInt(hostScreen.getPlayerTimeBox().split(" ")[0]);
            int gameTime = Integer.parseInt(hostScreen.getGameTimeBox().split(" ")[0]);

            parent.sendRulesToHost(challengesEnabled, dictionaryFile, playerTime, gameTime);
//            parent.showGame();
        }

    }

	/**
	 * Handles <code>JoinScreen</code> events.
	 */
	private static class JoinScreenController {
		private final Controller parent;
		private final JoinScreen joinScreen;

		/**
		 * Constructs a JoinScreenController from a parent and a JoinScreen.
		 * @param parent the <code>Controller</code> on which changes should be made.
		 * @param joinScreen the join screen to which <code>ActionListeners</code> are added.
		 */
		public JoinScreenController (Controller parent, JoinScreen joinScreen) {
			this.parent = parent;
			this.joinScreen = joinScreen;

			joinScreen.getJoinButton().addActionListener(e -> joinClick());
		}

		/*
		 * Handles the Join button being pressed
		 */
		private void joinClick() {
			String userName = joinScreen.getNameText();
			String hostsIP = joinScreen.getIPText();
			String hostsPort = joinScreen.getPortText();
			if (userName.isBlank()) {
				parent.showNoNameDialog();
			} else if (hostsIP.isBlank()) {
				parent.showNoIPDialog();
			} else if (hostsPort.isBlank()) {
				parent.showNoPortDialog();
			} else {
				int portNumber;
				try {
					portNumber = Integer.parseInt(hostsPort);
					parent.setupSocket(hostsIP, portNumber);
					parent.sendNewPlayer(userName);
					parent.showWaiting();
				} catch (NumberFormatException e) {
					parent.showNoPortDialog();
				} catch (IOException e) {
					parent.showIPErrorDialog();
				}
			}
		}

	}

	/**
	 * Handles <code>MainMenu</code> screen events.
	 */
	private static class MainMenuController {
		private final Controller parent;
		private final ScrabbleGUI.MainMenuScreen menuScreen;

		/**
		 * Constructs a MainMenuController with a parent and a MainMenuScreen.
		 * @param parent the main <code>Controller</code> of the application.
		 * @param menuScreen the main menu screen to manage.
		 */
		public MainMenuController(Controller parent, ScrabbleGUI.MainMenuScreen menuScreen) {
			this.parent = parent;
			this.menuScreen = menuScreen;
			addActionListeners(); // Add action listeners to menu screen buttons.

			// Start elevator music in a separate thread.
			Thread elevatorMusic = new Thread(parent.getElevatorMusicPlayer());
			elevatorMusic.start();
		}

		/**
		 * Adds action listeners to menu screen buttons.
		 */
		private void addActionListeners() {
			menuScreen.getHostButton().addActionListener(e -> hostButtonClick());
			menuScreen.getJoinButton().addActionListener(e -> parent.showJoin());
			menuScreen.getAudioCheck().addActionListener(e -> audioCheckChanged());
			menuScreen.getFxCheck().addActionListener(e -> fxCheckChanged());
			menuScreen.getQuitButton().addActionListener(e -> parent.exit());
		}

		/**
		 * Handles logic for when the Host button is clicked.
		 */
		private void hostButtonClick() {
			// set up the partyHost and change screen
			String name = JOptionPane.showInputDialog(parent.getView(), "Enter your name: ");
			if (name != null && !name.isBlank()) {
				try {
					parent.setUpHost(name); // Set up host and navigate to host screen.

					parent.showHost();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				parent.showNoNameDialog(); // Show dialog if no name is entered.
			}
		}

		/**
		 * Toggles FX (sound effects) based on checkbox state.
		 */
		private void fxCheckChanged() {
			parent.toggleFx();
		}

		/**
		 * Toggles background music based on checkbox state.
		 */
		private void audioCheckChanged() {
			parent.toggleMusic();
		}
	}

	/**
	 * Toggles background music on or off.
	 */
	public void toggleMusic(){
		musicEnable = !musicEnable;

		if(musicEnable)
			elevatorMusicPlayer.startMusic();
		else
			elevatorMusicPlayer.stopMusic();
	}

	/**
	 * Toggles sound effects on or off.
	 */
	public void toggleFx(){
		fxEnable = !fxEnable;
	}

	/**
	 * Runnable class that plays elevator music in the background.
	 */
	public static class ElevatorMusicPlayer implements Runnable{
		private final String soundFile;
		Clip clip;

		/**
		 * Constructs an ElevatorMusicPlayer with the given sound file.
		 * @param mainMusicFile the file path of the background music.
		 */
		private ElevatorMusicPlayer(String mainMusicFile) {
			this.soundFile = mainMusicFile;
		}

		/**
		 * Runs the music playback in a loop.
		 */
		@Override
		public void run() {
			try {
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundFile));
				clip = AudioSystem.getClip();
				clip.open(audioStream);

				// Set volume to a low level.
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(0.1));

				clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop music continuously.

			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				System.out.println(e.getMessage());
			}
		}

		/**
		 * Stops the music playback.
		 */
		private void stopMusic(){clip.stop();}

		/**
		 * Starts or resumes the music playback.
		 */
		private void startMusic(){clip.loop(Clip.LOOP_CONTINUOUSLY);}
	}

	/**
	 * Class responsible for playing sound effects for different game actions.
	 */
	private static class FxPlayer {
		AudioInputStream audioStream;
		FloatControl gainControl;
		Clip fxClip;

		/**
		 * Plays sound effect for tile placement.
		 */
		private void tilePlacementFx() {
			try {
				audioStream = AudioSystem.getAudioInputStream(new File("tilePlaySound.wav"));
				fxClip = AudioSystem.getClip();
				fxClip.open(audioStream);
				fxClip.start();

			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				System.out.println(e.getMessage());
			}
		}

		/**
		 * Plays sound effect for correct tile placement.
		 */
		private void rightPlacementFx() {
			try {
				audioStream = AudioSystem.getAudioInputStream(new File("rightPlaySound.wav"));
				fxClip = AudioSystem.getClip();
				fxClip.open(audioStream);

				// Set volume to a medium level.
				gainControl = (FloatControl) fxClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(0.5));
				fxClip.start();

			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				System.out.println(e.getMessage());
			}
		}

		/**
		 * Plays sound effect for incorrect tile placement.
		 */
		private void wrongPlacementFx() {
			try {
				audioStream = AudioSystem.getAudioInputStream(new File("wrongPlaySound.wav"));
				fxClip = AudioSystem.getClip();
				fxClip.open(audioStream);

				// Set volume to a very low level.
				gainControl = (FloatControl) fxClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(0.06));
				fxClip.start();

			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}