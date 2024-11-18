package scrabble.controller;

import scrabble.view.screen.MainMenuScreen;

import javax.swing.*;
import java.io.IOException;

/**
 * MainMenuController is the controller that takes care of actions on the main menu panel
 */
public class MainMenuController {
	private final Controller parent;			// An instance of the Controller
	private final MainMenuScreen menuScreen;	// An instance of the MainMenuScreen Object

	/**
	 * Constructor that initializes the parent and menu screen. Then it adds the action listeners
	 * 		to the items of the menu screen
	 *
	 * @param parent the Controller object that represents the games controller
	 * @param menuScreen the MainMenuScreen object that represents the screen for the main menu
	 */
	public MainMenuController(Controller parent, MainMenuScreen menuScreen) {
		this.parent = parent;
		this.menuScreen = menuScreen;
		addActionListeners();
	}

	/**
	 * Adds the action listeners to every button on the main menu screen
	 */
	private void addActionListeners() {
		menuScreen.getHostButton().addActionListener(e -> hostButtonClick());
		menuScreen.getJoinButton().addActionListener(e -> parent.showJoin());
		menuScreen.getAudioCheck().addActionListener(e -> audioCheckChanged());
		menuScreen.getFxCheck().addActionListener(e -> fxCheckChanged());
		menuScreen.getQuitButton().addActionListener(e -> parent.exit());
	}

	/**
	 * Action that happens when a user clicks "Host" button
	 * Sets up the host for the game, and moves user to the HostScreen
	 */
	private void hostButtonClick() {
		// setup the partyhost and change screen
		String name = JOptionPane.showInputDialog(parent.getView(), "Enter your name: ");
		if (name != null && !name.isBlank()) {
			try {
				parent.setUpHost(name);
				parent.showHost();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else {
			parent.showNoNameDialog();
		}
	}

	/**
	 * Turns the game fx on or off based on the state of the "fx" checkbox
	 */
	private void fxCheckChanged() {
		//fx is not yet implemented
	}

	/**
	 * Turns the game audio on or off based on the state of the "Audio" checkbox
	 */
	private void audioCheckChanged() {
		//Audio is not yet implemented
	}

}
