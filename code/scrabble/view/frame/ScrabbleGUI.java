package scrabble.view.frame;

import scrabble.model.Player;
import scrabble.view.panel.*;

import javax.swing.*;
import java.awt.*;

public class ScrabbleGUI extends JFrame{
	public static final String  MAIN_MENU = "MAIN-MENU";
	public static final String HOST = "HOST";
	public static final String JOIN = "JOIN";
	public static final String WAIT = "WAITING";
	public static final String GAME = "GAME";
	public static final String PODIUM = "WINNER";

	public static final String[] SCREEN_NAMES = new String[] {
			MAIN_MENU, HOST, JOIN, WAIT, GAME, PODIUM
	};

	private CardLayout layoutManager;
	private Container contentPane;

	private JPanel mainMenu;
	private JPanel host;
	private JPanel join;
	private JPanel waiting;
	private JPanel game;
	private JPanel winner;


	private JPanel[] panels = new JPanel[]{
			mainMenu, host, join, waiting, game, winner
	};

	public static void main(String[] args) {
		new ScrabbleGUI();
	}

	public ScrabbleGUI() {
		super();
		this.setupFrame();
		this.setLayout(new BorderLayout());
		layoutManager = new CardLayout();
		contentPane = this.getContentPane();
		contentPane.setLayout(layoutManager);

		mainMenu = new MainMenuScreen();
		host = new HostScreen();
		join = new JoinScreen();
		waiting = new WaitingScreen();
		game = new GameScreen();

		for (int i = 0; i < this.panels.length; i++) {
			contentPane.add(panels[i], SCREEN_NAMES[i]);
		}
		layoutManager.first(contentPane);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			// Set the look and feel to the system's default
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException |
				 IllegalAccessException | UnsupportedLookAndFeelException ignore) {}
	}

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

	public void showGame() {
		layoutManager.show(this.contentPane, GAME);
//		this.setupFrame();
	}

	public void showHost() {
		layoutManager.show(this.contentPane, HOST);
	}

	public void showJoin() {
		layoutManager.show(this.contentPane, JOIN);
//		this.setupFrame();
	}

	public void showMain() {
		layoutManager.show(this.contentPane, MAIN_MENU);
		/*
		JPanel mainPanel = new JPanel(new FlowLayout());
		mainPanel.setBorder(BorderFactory.createTitledBorder("Main Menu"));
		mainPanel.add(mainMenu);
		this.add(mainPanel);
		this.setupFrame();

		 */
	}

	public void showWaiting() {
		layoutManager.show(this.contentPane, WAIT);
//		this.setupFrame();
	}

	public void showWinner() {
		layoutManager.show(this.contentPane, PODIUM);
	}

	private void setupFrame() {
		this.setTitle("Scrabble");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.setMinimumSize(new Dimension(250,150));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
