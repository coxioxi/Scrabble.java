package scrabble.controller;

import scrabble.view.screen.MainMenuScreen;

import javax.swing.*;
import java.io.IOException;

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
		menuScreen.getQuitButton().addActionListener(e -> parent.exit());
	}

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

	private void fxCheckChanged() {
		//fx is not yet implemented
	}

	private void audioCheckChanged() {
		//Audio is not yet implemented
	}

}
