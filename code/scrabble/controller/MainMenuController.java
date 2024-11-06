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

	}
}
