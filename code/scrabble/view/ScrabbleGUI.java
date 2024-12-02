package scrabble.view;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.model.Player;
import scrabble.model.Tile;
import scrabble.view.screen.*;

import javax.swing.*;
import java.awt.*;

/**
 * ScrabbleGUI is the JFrame that holds all the different panels for the game
 */
public class ScrabbleGUI extends JFrame{
	public static final float PREFERRED_SIZE_PERCENT = .6f;
	public static final float MINIMUM_SIZE_PERCENT = .5f;
	public static final float MAXIMUM_SIZE_PERCENT = .8f;

	public static final String MAIN_MENU = "MAIN-MENU";
	public static final String HOST 	 = "HOST";
	public static final String JOIN 	 = "JOIN";
	public static final String WAIT	 	 = "WAITING";
	public static final String GAME  	 = "GAME";
	public static final String PODIUM 	 = "WINNER";

	public static final String[] SCREEN_NAMES = new String[] {MAIN_MENU, HOST, JOIN, WAIT, GAME, PODIUM};

	private CardLayout layoutManager;
	private Container contentPane;

	private JPanel mainMenu = new MainMenuScreen();
	private JPanel host		= new HostScreen();
	private JPanel join 	= new JoinScreen();
	private JPanel waiting 	= new WaitingScreen();
	private JPanel game 	= new JPanel();
	private JPanel winner 	= new JPanel();	// temp bc these are not yet decided

	private JMenuBar menuBar;
	private JMenu gameMenu;
	private JMenuItem rulesItem;
	private JCheckBoxMenuItem audioItem;
	private JCheckBoxMenuItem fxItem;
	private JMenuItem quitItem;

	public static boolean audioOn = true;
	public static boolean fxOn = true;

	private Dimension preferredSize, maximumSize, minimumSize;

	/**
	 * Constructor for ScrabbleGUI
	 * Sets up the card layout and adds the screens to the frame
	 */
	public ScrabbleGUI() {
		// Creating the frame
		super();
		layoutManager = new CardLayout();
		contentPane = super.getContentPane();
		contentPane.setLayout(layoutManager);

		// adding screens to frame
		JPanel[] panels = new JPanel[]{mainMenu, host, join, waiting, game, winner};
		for (int i = 0; i < panels.length; i++) {
			contentPane.add(panels[i], SCREEN_NAMES[i]);
		}
		layoutManager.first(contentPane);

		this.setMaximumSize(maximumSize);
		setupFrame();
		menuSetup();
	}

	/**
	 * Sets up all the items for the menu of the game screen
	 */
	private void menuSetup() {
		menuBar = new JMenuBar();
		gameMenu = new JMenu("Game");
		rulesItem = new JMenuItem("Rules");
		audioItem = new JCheckBoxMenuItem("Audio On/Off");
		fxItem = new JCheckBoxMenuItem("Fx On/Off");
		quitItem = new JMenuItem("Quit");

		audioItem.setState(true);
		fxItem.setState(true);

		gameMenu.add(rulesItem);
		gameMenu.add(audioItem);
		gameMenu.add(fxItem);
		gameMenu.add(quitItem);
		menuBar.add(gameMenu);
		this.setJMenuBar(menuBar);
		menuBar.setVisible(false);
	}

	public void makeGameScreen(String[] playerNames, int gameTime, int playerTime, Tile[] rackTiles) {
		this.remove(game);
		game = new GameScreen(playerNames, gameTime, playerTime, rackTiles);
		this.add(game, GAME);
	}

	/**
	 *
	 */
	public void resetWaitingScreen() {
		((WaitingScreen) waiting).resetPlayerNames();
		waiting.revalidate();
		waiting.repaint();
	}

	/**
	 * Makes the menuBar visible or not
	 *
	 * @param enabled the boolean that tells us if the menu bar should be
	 * 		visible to the player or not
	 */
	public void setMenuVisible(boolean enabled) {
		menuBar.setVisible(enabled);
	}

	/*
	getters
	 */

	/**
	 * Getter for the Rules menu item
	 *
	 * @return the JMenuItem for the "Rules"
	 */
	public JMenuItem getRulesItem() {
		return rulesItem;
	}

	/**
	 * Getter for the Audio menu item
	 * 	 *
	 * 	 * @return the JMenuItem for the "Audio"
	 */
	public JCheckBoxMenuItem getAudioItem() {
		return audioItem;
	}

	/**
	 * Getter for the Fx menu item
	 * 	 *
	 * 	 * @return the JMenuItem for the "Fx"
	 */
	public JCheckBoxMenuItem getFxItem() {
		return fxItem;
	}

	/**
	 * Getter for the Quit menu item
	 * 	 *
	 * 	 * @return the JMenuItem for "Quit"
	 */
	public JMenuItem getQuitItem() {
		return quitItem;
	}

	/**
	 *
	 */
	public CardLayout getLayoutManager() {
		return layoutManager;
	}

	/**
	 * Getter for the contentPane container
	 *
	 * @return the Container that holds all the panels
	 */
	@Override
	public Container getContentPane() {
		return contentPane;
	}

	/**
	 * Getter for the Main Menu panel
	 *
	 * @return MainMenuScreen object for the main menu panel
	 */
	public MainMenuScreen getMainMenu() {
		return (MainMenuScreen) mainMenu;
	}

	/**
	 * Getter for the Host Screen panel
	 *
	 * @return HostScreen object for the host panel
	 */
	public HostScreen getHost() {
		return (HostScreen) host;
	}

	/**
	 * Getter for the Join Screen panel
	 *
	 * @return JoinScreen object for the join panel
	 */
	public JoinScreen getJoin() {
		return (JoinScreen) join;
	}

	/**
	 * Getter for the Waiting Screen panel
	 *
	 * @return WaitingScreen object for the waiting panel
	 */
	public WaitingScreen getWaiting() {
		return (WaitingScreen) waiting;
	}

	/**
	 * Getter for the Game Screen panel
	 *
	 * @return GameScreen object for the game panel
	 */
	public GameScreen getGame() {
		return (GameScreen) game;
	}

	/**
	 * Getter for the Winner Screen panel
	 *
	 * @return WinnerScreen object for the winner panel
	 */
	public WinnerScreen getWinner() {
		return (WinnerScreen) winner;
	}

	/*
	setters: change shown panel
	 */

	/**
	 * Makes the visible screen the one that is passed in
	 *
	 * @param screen the String name of the screen to be displayed
	 */
	public void show(String screen) {
		layoutManager.show(contentPane, screen);
	}

	/**
	 * Makes the Game panel the one that is shown
	 */
	public void showGame() {
		layoutManager.show(this.contentPane, GAME);
		// change window listener
		menuBar.setVisible(true);
	}

	/**
	 * Makes the Host panel the one that is shown
	 */
	public void showHost() {
		layoutManager.show(this.contentPane, HOST);
		this.setMinimumSize(new Dimension(600, 400));
		this.pack();
	}

	/**
	 * Makes the Join panel the one that is shown
	 */
	public void showJoin() {
		layoutManager.show(this.contentPane, JOIN);
	}

	/**
	 * Makes the Main Menu panel the one that is shown
	 */
	public void showMain() {
		layoutManager.show(this.contentPane, MAIN_MENU);
		this.setupFrame();
	}

	/**
	 * Makes the Waiting panel the one that is shown
	 */
	public void showWaiting() {
		layoutManager.show(this.contentPane, WAIT);
		this.setupFrame();
	}

	/**
	 * Makes the Winner panel the one that is shown
	 */
	public void showWinner() {
		layoutManager.show(this.contentPane, PODIUM);
		menuBar.setVisible(false);
	}

	/*
	use this to set up the winner screen with the correct information.
	resets the original temp variable used.
	need to call showWinner or show(PODIUM) to display.
	 */
	/**
	 * Sets up the podium for the players to show the player that won
	 *
	 * @param players the array of Player objects which contain each player's score
	 */
	public void setupPodium(Player[] players) {
		layoutManager.removeLayoutComponent(winner);
		winner = new WinnerScreen(players);
		layoutManager.addLayoutComponent(winner, PODIUM);
	}

	public void addWaitingPlayer(String playerName) {
		getWaiting().addPlayerName(playerName);
    }

	/**
	 * Sets how the Frame should look
	 */
	private void setupFrame() {
		this.setTitle("Scrabble");
		setupDimensions();

		this.setMaximumSize(maximumSize);
		this.setPreferredSize(preferredSize);
		this.setMinimumSize(minimumSize);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Sets the dimensions of the JFrame
	 */
	private void setupDimensions() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		maximumSize = new Dimension(
				(int)(MAXIMUM_SIZE_PERCENT*width), (int)(MAXIMUM_SIZE_PERCENT*height)
		);
		minimumSize = new Dimension(
				(int)(MINIMUM_SIZE_PERCENT*width), (int)(MINIMUM_SIZE_PERCENT*height)
		);
		preferredSize = new Dimension(
				(int)(PREFERRED_SIZE_PERCENT*width), (int)(PREFERRED_SIZE_PERCENT*height)
		);
	}

	/**
	 * WinnerScreen displays the final standings of players at the end of the game,
	 * showing player names and scores in order of rank.
	 */
	private static class WinnerScreen extends JPanel {

		/**
		 * Constructor for WinnerScreen. Sets up the layout with player rankings and a button to return to the main menu.
		 *
		 * @param players Array of Player objects, ordered by their final ranking.
		 */
		public WinnerScreen(Player[] players) {
			// Set layout for the main panel
			this.setLayout(new BorderLayout());

			// Create a central panel with a titled border for the podium display
			JPanel centerPanel = new JPanel(new FlowLayout());
			centerPanel.setBorder(BorderFactory.createTitledBorder("Podium"));

			// Create a south panel for the "Return to Main Menu" button
			JPanel southPanel = new JPanel(new FlowLayout());

			// Create a panel to represent the podium with a vertical layout for player rankings
			JPanel podium = new JPanel(new GridLayout(4,1, 7, 10));

			// Labels to display each player's rank, name, and score
			JLabel firstPlaceName = new JLabel("1st: " + players[0].getName() + " | " + players[0].getScore());
			JLabel secondPlaceName = new JLabel("2nd: " + players[1].getName() + " | " + players[1].getScore());
			JLabel thirdPlaceName = new JLabel("3rd: " + players[2].getName() + " | " + players[2].getScore());
			JLabel fourthPlaceName = new JLabel("4th: " + players[3].getName() + " | " + players[3].getScore());

			// Button to return to the main menu
			JButton returnToMain = new JButton("Return to Main Menu");

			// Set borders for each ranking label to visually separate them
			firstPlaceName.setBorder(BorderFactory.createEtchedBorder());
			secondPlaceName.setBorder(BorderFactory.createEtchedBorder());
			thirdPlaceName.setBorder(BorderFactory.createEtchedBorder());
			fourthPlaceName.setBorder(BorderFactory.createEtchedBorder());

			// Add each ranking label to the podium panel in order
			podium.add(firstPlaceName);
			podium.add(secondPlaceName);
			podium.add(thirdPlaceName);
			podium.add(fourthPlaceName);

			// Add the podium to the center panel and set it to the center of the main layout
			centerPanel.add(podium);
			this.add(centerPanel, BorderLayout.CENTER);

			// Add the "Return to Main Menu" button to the south panel and position it at the bottom of the layout
			southPanel.add(returnToMain);
			this.add(southPanel, BorderLayout.SOUTH);
		}
	}

	/**
	 * WaitingScreen represents the panel displayed while waiting for players to join the game.
	 * It shows a list of player slots that can be updated with player names.
	 */
	public static class WaitingScreen extends JPanel {
		public static final String WAITING = "Waiting...";
		// Array to store labels for each player
		private JLabel[] players;
		private int numPlayers = 0;

		/**
		 * Constructor for WaitingScreen. Sets up the layout and adds components.
		 */
		public WaitingScreen() {

			// Set layout for main panel
			this.setLayout(new BorderLayout());

			// Create a central panel with a titled border for the waiting screen
			JPanel centerPanel = new JPanel(new FlowLayout());
			centerPanel.setBorder(BorderFactory.createTitledBorder("Player Waiting Screen"));

			// Add player waiting list to the center panel
			centerPanel.add(setupPlayersWaiting());
			this.add(centerPanel, BorderLayout.CENTER);

		}

		/**
		 * Initializes the panel that displays player slots and returns it.
		 * This panel uses a GridLayout to list player slots vertically.
		 *
		 * @return JPanel containing labels for each player's slot.
		 */
		private JPanel setupPlayersWaiting() {
			JPanel playersWaiting = new JPanel(new GridLayout(5,1,0,15));

			// Title label for the player list
			JLabel playerTitle = new JLabel("Players in the Game:", SwingConstants.CENTER);
			playersWaiting.add(playerTitle);

			// Initialize labels for player slots and add them to the panel
			players = new JLabel[4];
			for (int i = 0; i < players.length; i++) {
				players[i] = new JLabel(WAITING, SwingConstants.CENTER);
				players[i].setBorder(BorderFactory.createEtchedBorder()); // Adds a border to each label
				playersWaiting.add(players[i]);
			}
			return playersWaiting;
		}

		/**
		 * Updates the label of the next available player slot with the given player name.
		 *
		 * @param name The name of the player to display.
		 */
		public void addPlayerName(String name) {
			players[numPlayers].setText(name);
			numPlayers++;
		}

		/**
		 * Resets all player labels to the default state.
		 */
		public void resetPlayerNames() {
			for (JLabel player : players) {
				player.setText(WAITING);
			}
			numPlayers = 0;
		}


	}

	/**
	 * MainMenuScreen represents the main menu panel in the Scrabble game,
	 * containing buttons to host or join a game, as well as options for audio settings.
	 */
	public static class MainMenuScreen extends JPanel {

		// Buttons for hosting, joining, and quitting the game
		private JButton hostButton;     //
		private JButton joinButton;     //
		private JButton quitButton;     //

		// Checkboxes for toggling game audio and sound effects
		private JCheckBox audioCheck;   //
		private JCheckBox fxCheck;      //

		/**
		 * Constructor for MainMenuScreen. Initializes the layout, buttons, and checkboxes.
		 */
		public MainMenuScreen() {
			this.setLayout(new FlowLayout());
			this.setBorder(BorderFactory.createTitledBorder("Main Menu"));

			JPanel menuFrame = new JPanel(new GridLayout(5,1, 0,15));

			// Main Menu buttons and checkboxes
			hostButton = new JButton("Host");
			joinButton = new JButton("Join");
			audioCheck = new JCheckBox("Game Audio", audioOn);
			fxCheck = new JCheckBox("Game FX", fxOn);
			quitButton = new JButton("Quit");

			audioCheck.setSelected(audioOn);
			fxCheck.setSelected(fxOn);

			// Add components to menu panel and frame
			menuFrame.add(hostButton);
			menuFrame.add(joinButton);
			menuFrame.add(audioCheck);
			menuFrame.add(fxCheck);
			menuFrame.add(quitButton);
			this.add(menuFrame, BorderLayout.CENTER);
		}

		public void setEnabled(boolean musicEnabled, boolean fxEnabled){
			audioOn = musicEnabled;
			fxOn = fxEnabled;
		}

		/**
		 * getter method for the Host button
		 *
		 * @return the JButton related to the "Host" button
		 */
		public JButton getHostButton() {
			return hostButton;
		}

		/**
		 * getter method for the Quit button
		 *
		 * @return the JButton related to the "Quit" button
		 */
		public JButton getQuitButton() {
			return quitButton;
		}

		/**
		 * getter method for the Join button
		 *
		 * @return the JButton related to the "Join" button
		 */
		public JButton getJoinButton() {
			return joinButton;
		}

		/**
		 * getter method for the FX check box
		 *
		 * @return The JCheckBox related to the "FX" check box
		 */
		public JCheckBox getFxCheck() {
			return fxCheck;
		}

		/**
		 * getter method for the audio check box
		 *
		 * @return The JCheckBox related to the "Audio" check box
		 */
		public JCheckBox getAudioCheck() {
			return audioCheck;
		}
	}
}
