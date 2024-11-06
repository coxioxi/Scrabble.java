package scrabble.view.frame;
import scrabble.model.Player;
import scrabble.view.panel.*;

import javax.swing.*;
import java.awt.*;

public class ScrabbleGUI extends JFrame{
	public static final float PREFERRED_SIZE_PERCENT = .8f;
	public static final float MINIMUM_SIZE_PERCENT = .6f;
	public static final float MAXIMUM_SIZE_PERCENT = 1;

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


	private JPanel[] panels = new JPanel[]{
			mainMenu, host, join, waiting, game, winner
	};

	private Dimension preferredSize, maximumSize, minimumSize;

	public static void main(String[] args) throws InterruptedException {
		ScrabbleGUI frame = new ScrabbleGUI();
		Thread.sleep(3000);
		frame.showGame();
		Thread.sleep(10000);
		frame.showHost();
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

		try {
			// Set the look and feel to the system's default
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException |
				 IllegalAccessException | UnsupportedLookAndFeelException ignore) {}

		setupFrame();
	}

	/*
	getters
	 */


	public CardLayout getLayoutManager() {
		return layoutManager;
	}

	@Override
	public Container getContentPane() {
		return contentPane;
	}

	public JPanel getMainMenu() {
		return mainMenu;
	}

	public JPanel getHost() {
		return host;
	}

	public JPanel getJoin() {
		return join;
	}

	public JPanel getWaiting() {
		return waiting;
	}

	public JPanel getGame() {
		return game;
	}

	public JPanel getWinner() {
		return winner;
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
	}

	public void showHost() {
		layoutManager.show(this.contentPane, HOST);
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
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
