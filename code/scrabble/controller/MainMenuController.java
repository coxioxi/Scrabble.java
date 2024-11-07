package scrabble.controller;

import scrabble.view.screen.MainMenuScreen;

import javax.swing.*;

public class MainMenuController {
	private final Controller parent;
	private final MainMenuScreen menuScreen;

	public MainMenuController(Controller parent, MainMenuScreen menuScreen) {
		this.parent = parent;
		this.menuScreen = menuScreen;
		addActionListeners();
	}

	private void addActionListeners() {
		menuScreen.getHostButton().addActionListener(e -> hostButtonClick());
		menuScreen.getJoinButton().addActionListener(e -> parent.showJoin());
		menuScreen.getAudioCheck().addActionListener(e -> audioCheckChanged());
		menuScreen.getFxCheck().addActionListener(e -> fxCheckChanged());
		menuScreen.getQuitButton().addActionListener(e -> parent.getView().dispose());
	}

	private void hostButtonClick() {
		// setup the partyhost and change screen
		String name = JOptionPane.showInputDialog(parent.getView(), "Enter your name: ");
		if (name != null && !name.isBlank()) {
			parent.setUpHost(name);
			parent.showHost();
		}
		else {
			parent.showNoNameDialog();
		}
	}

	private void fxCheckChanged() {
		//fx is not yet implemented
	}

	private void audioCheckChanged() {
		//Audio is not yet implemented
	}

}
