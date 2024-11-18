package scrabble.view;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.model.Player;
import scrabble.view.screen.*;

import javax.swing.*;
import java.awt.*;

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

	public static final String[] SCREEN_NAMES = new String[] {
			MAIN_MENU, HOST, JOIN, WAIT, GAME, PODIUM
	};

	private CardLayout layoutManager;
	private Container contentPane;

	private JPanel mainMenu = new MainMenuScreen();
	private JPanel host		= new HostScreen();
	private JPanel join 	= new JoinScreen();
	private JPanel waiting 	= new WaitingScreen();
	private JPanel game 	= new GameScreen();
	private JPanel winner 	= new JPanel();	// temp bc these are not yet decided

	private JMenuBar menuBar;
	private JMenu gameMenu;
	private JMenuItem rulesItem;
	private JMenuItem audioItem;
	private JMenuItem fxItem;
	private JMenuItem quitItem;

	public static boolean audioOn = false;
	public static boolean fxOn = false;


	private JPanel[] panels = new JPanel[]{
			mainMenu, host, join, waiting, game, winner
	};

	private Dimension preferredSize, maximumSize, minimumSize;

	public static void main(String[] args) throws InterruptedException {
		ScrabbleGUI frame = new ScrabbleGUI();
		Thread.sleep(3000);
		frame.showGame();
		Thread.sleep(4000);
		frame.showWinner();
	}

	public ScrabbleGUI() {
		// Creating the frame
		super();
		layoutManager = new CardLayout();
		contentPane = super.getContentPane();
		contentPane.setLayout(layoutManager);

		// adding screens to frame
		for (int i = 0; i < this.panels.length; i++) {
			contentPane.add(panels[i], SCREEN_NAMES[i]);
		}
		layoutManager.first(contentPane);

		this.setMaximumSize(maximumSize);
		setupFrame();
//		Dimension cpDim = ((GameScreen)game).getCenterPanel().getSize();
//		System.out.println("Center panel dim: " + cpDim.width + "x"+cpDim.height);
		menuSetup();
	}

	private void menuSetup() {
		menuBar = new JMenuBar();
		gameMenu = new JMenu("Game");
		rulesItem = new JMenuItem("Rules");
		audioItem = new JMenuItem("Audio On/Off");
		fxItem = new JMenuItem("Fx On/Off");
		quitItem = new JMenuItem("Quit");

		gameMenu.add(rulesItem);
		gameMenu.add(audioItem);
		gameMenu.add(fxItem);
		gameMenu.add(quitItem);
		menuBar.add(gameMenu);
		this.setJMenuBar(menuBar);
		menuBar.setVisible(false);
	}

	public void resetGameScreen() {
		layoutManager.removeLayoutComponent(game);
		game = new GameScreen();
		layoutManager.addLayoutComponent(game, GAME);
	}

	public void resetWaitingScreen() {
		((WaitingScreen) waiting).resetPlayerNames();
		waiting.revalidate();
		waiting.repaint();
	}

	public void setMenuVisible(boolean enabled) {
		menuBar.setVisible(enabled);
	}

	/*
	getters
	 */

	public JMenuItem getRulesItem() {
		return rulesItem;
	}

	public JMenuItem getAudioItem() {
		return audioItem;
	}

	public JMenuItem getFxItem() {
		return fxItem;
	}

	public JMenuItem getQuitItem() {
		return quitItem;
	}

	public CardLayout getLayoutManager() {
		return layoutManager;
	}

	@Override
	public Container getContentPane() {
		return contentPane;
	}

	public MainMenuScreen getMainMenu() {
		return (MainMenuScreen) mainMenu;
	}

	public HostScreen getHost() {
		return (HostScreen) host;
	}

	public JoinScreen getJoin() {
		return (JoinScreen) join;
	}

	public WaitingScreen getWaiting() {
		return (WaitingScreen) waiting;
	}

	public GameScreen getGame() {
		return (GameScreen) game;
	}

	public WinnerScreen getWinner() {
		return (WinnerScreen) winner;
	}

	public JPanel[] getPanels() {
		return panels;
	}

	/*
	setters: change shown panel
	 */

	public void show(String screen) {
		layoutManager.show(contentPane, screen);
	}

	public void showGame() {
		layoutManager.show(this.contentPane, GAME);
		// change window listener
		menuBar.setVisible(true);
	}

	public void showHost() {
		layoutManager.show(this.contentPane, HOST);
		this.setMinimumSize(new Dimension(600, 400));
		this.pack();
	}

	public void showJoin() {
		layoutManager.show(this.contentPane, JOIN);
	}

	public void showMain() {
		layoutManager.show(this.contentPane, MAIN_MENU);
		this.setupFrame();
	}

	public void showWaiting() {
		layoutManager.show(this.contentPane, WAIT);
		this.setupFrame();
	}

	public void showWinner() {
		layoutManager.show(this.contentPane, PODIUM);
		menuBar.setVisible(false);
	}

	/*
	use this to set up the winner screen with the correct information.
	resets the original temp variable used.
	need to call showWinner or show(PODIUM) to display.
	 */
	public void setupPodium(Player[] players) {
		layoutManager.removeLayoutComponent(winner);
		winner = new WinnerScreen(players);
		layoutManager.addLayoutComponent(winner, PODIUM);
		panels[panels.length-1] = winner;
	}

	// minimum size, title, close op, pack, center in screen, show.
	private void setupFrame() {
		this.setTitle("Scrabble");

		/*GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().setFullScreenWindow(this);
		*/
		setupDimensions();

		this.setMaximumSize(maximumSize);
		this.setPreferredSize(preferredSize);
		this.setMinimumSize(minimumSize);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

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

}
