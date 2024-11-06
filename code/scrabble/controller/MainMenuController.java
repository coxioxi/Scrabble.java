package scrabble.controller;

import scrabble.view.panel.MainMenuScreen;

public class MainMenuController {
	private final Controller parent;
	private final MainMenuScreen menuScreen;

	public MainMenuController(Controller parent, MainMenuScreen menuScreen) {
		this.parent = parent;
		this.menuScreen = menuScreen;
		addActionListeners();
	}

	private void addActionListeners() {
		menuScreen.getHostButton().addActionListener(e -> parent.getView().showHost());
		menuScreen.getJoinButton().addActionListener(e -> parent.getView().showJoin());
		menuScreen.getAudioCheck().addActionListener(e -> audioCheckChanged());
		menuScreen.getFxCheck().addActionListener(e -> fxCheckChanged());
		menuScreen.getQuitButton().addActionListener(e -> parent.getView().dispose());
	}

	private void fxCheckChanged() {
		//fx is not yet implemented
	}

	private void audioCheckChanged() {
		//Audio is not yet implemented
	}

}
