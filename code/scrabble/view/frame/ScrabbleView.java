package scrabble.view.frame;

import javax.swing.*;
import java.awt.*;

import scrabble.model.Player;
import scrabble.view.screen.*;

@Deprecated
public class ScrabbleView extends JFrame {

	private CardLayout layoutManager;
	private Container contentPane;

	public ScrabbleView() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		layoutManager = new CardLayout();
		contentPane = this.getContentPane();
		contentPane.setLayout(layoutManager);
		JPanel mainMenu = new MainMenuScreen();
		JPanel host = new HostScreen();
		JPanel join = new JoinScreen();
		JPanel waiting = new WaitingScreen();
		JPanel game = new GameScreen();
		JPanel winner = new WinnerScreen(new Player[]{new Player("Ian", 1),
				new Player("David", 2),
				new Player("Max", 3),
				new Player("Sam", 4)});
		contentPane.add(mainMenu, "MAIN-MENU");
		contentPane.add(host, "HOST");
		contentPane.add(join, "JOIN");
		contentPane.add(waiting, "WAITING");
		contentPane.add(game, "GAME");
		contentPane.add(winner, "WINNER");
		layoutManager.first(contentPane);

		this.pack();
		this.setVisible(true);

		boolean interrupted = false;
		while (!interrupted) {
			try {
				Thread.sleep(2000);
				layoutManager.next(contentPane);
				this.pack();
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}

	}

	public static void main(String[] args) {
		/*try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		*/
		try {
			// Set the look and feel to the system's default
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignore) {}
		/* Turn off metal's use of bold fonts */
		//UIManager.put("swing.boldMetal", Boolean.FALSE);

		new ScrabbleView();
	}
}
