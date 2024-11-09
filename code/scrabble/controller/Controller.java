package scrabble.controller;

import scrabble.model.*;
import scrabble.network.client.ClientMessenger;
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
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Run the other classes
 */

public class Controller implements PropertyChangeListener  {
	public static final int PORT = 0;

	private ScrabbleGUI view;
	private Game model;

	private ClientMessenger messenger;
	private Socket hostSocket;
	private GameScreenController gameScreenController;
	private MainMenuController mainMenuController;
	private HostScreenController hostScreenController;
	private JoinScreenController joinScreenController;

	private Ruleset ruleset;

	/*
	reference to the party host
	when this controller is the manager of the party.
	note that this field is null when this computer is not the host
	 */
	private PartyHost host;

	public int getSelfID() {
		return selfID;
	}

	private int selfID;

	public static void main(String[] args) {
		new Controller();
	}

	public Controller() {
		view = new ScrabbleGUI();
		view.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addListeners(view);
		this.showMain();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		((Message) evt.getNewValue()).execute(this);
	}

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

	public void setupSocket(String ip, int port) throws IOException {
		hostSocket = new Socket(ip, port);
		messenger = new ClientMessenger(hostSocket, this);
		Thread clientMessenger = new Thread(messenger);
		clientMessenger.start();
	}

	public void sendNewPlayer(String name) throws IOException {
		messenger.sendMessage(new NewPlayer(0, 0, name));
	}

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

	public void sendRulesToHost(boolean challengesAllowed, String dictionary, int playerTime, int gameTime) {
		ruleset = new Ruleset(gameTime, playerTime, challengesAllowed, dictionary);
		host.startGame(ruleset);
	}

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

	public void resetLastPlay(GameScreen gameScreen){
		//loop through the rack
		int size = gameScreen.playedTiles.size();
		for (int i = 0; i < size; ++i){
			gameScreenController.removeTile(gameScreen.playedTiles.get(0));
		}
	}

	public void removeRackTile(Tile tile) {
		RackPanel rackPanel = ((GameScreen) view.getGame()).getRackPanel();
		for(TilePanel tp: rackPanel.getTilePanels()){
			if(tp.getButton().getText().equals(""+tile.getLetter())){
				tp.setButton(new JButton(" "));
				break;
			}
		}
	}

	public void replenishRack(Tile[] toAdd){
		view.getGame().addTilesToRack(toAdd);
	}

	public void addPlayer(int playerID, String name) {
		if (playerID == selfID) {
			try {
				messenger.sendMessage(new NewPlayer(selfID, selfID, name));
			} catch (IOException ignore) {
			}
		}
		if (this.host != null) {
			view.getHost().addPlayerName(name);
		}
		else {
			view.getWaiting().addPlayerName(name);
		}
	}

	public void addTiles(Tile[] tiles) {
		model.addTiles(tiles);
		gameScreenController.addTiles(tiles);
		gameScreenController.setRackButtonsEnabled(false);
	}

	public ScrabbleGUI getView() {
		return view;
	}

	public Game getModel() {
		return model;
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

	public void removeListeners() {
		for (WindowListener wl : view.getWindowListeners()) {
			view.removeWindowListener(wl);
		}
	}

	public int showQuitDialog() {
		return JOptionPane.showConfirmDialog(view, "Are you sure you want to leave?\nYou will not be able to rejoin.", "Quit?", JOptionPane.WARNING_MESSAGE);
	}

	public void showRulesDialog() {
		JOptionPane.showMessageDialog(view, "1.~~~~~~~~~\n2.~~~~~~~~~\n3.~~~~~~~~~~~~\n4.~~~~~~~~~~", "Rules", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showNoNameDialog() {
		JOptionPane.showMessageDialog(view, "You must put in a name!", "No Name", JOptionPane.WARNING_MESSAGE);
	}

	public void showNoIPDialog() {
		JOptionPane.showMessageDialog(view, "You must input the host's IP Address!", "No IP", JOptionPane.WARNING_MESSAGE);
	}

	public void showNoPortDialog() {
		JOptionPane.showMessageDialog(view, "You must enter the host's port number!", "Incorrect Port Input", JOptionPane.WARNING_MESSAGE);
	}

	public void showIPErrorDialog() {
		JOptionPane.showMessageDialog(view, "The Host refused to connect.\nCheck your IP Address!", "No Connection", JOptionPane.WARNING_MESSAGE);
	}

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
			if (messenger!= null) messenger.halt();
			if (this.host != null) {
				host.halt();
			}
			view.dispose();

		}
	}
}
