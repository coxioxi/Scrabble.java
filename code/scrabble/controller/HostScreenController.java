package scrabble.controller;

import scrabble.view.panel.HostScreen;

public class HostScreenController {
	private Controller parent;
	private HostScreen hostScreen;
	private String name;
	private boolean challengesEnabled;
	private String dictionaryFile;
	private int playerTime;
	private int gameTime;

	public HostScreenController(Controller parent, HostScreen hostScreen) {
		this.parent = parent;
		this.hostScreen = hostScreen;
		hostScreen.getHostButton().addActionListener(e -> hostButtonClick());
	}

	private void hostButtonClick() {
		this.name = hostScreen.getNameTextField().getText();
		this.challengesEnabled = hostScreen.getChallengeBox();
		this.dictionaryFile = hostScreen.getDictionaryPath();
		this.playerTime = Integer.parseInt(hostScreen.getPlayerTimeBox().split(" ")[0]);
		this.gameTime = Integer.parseInt(hostScreen.getGameTimeBox().split(" ")[0]);

		parent.sendRules(challengesEnabled, dictionaryFile, playerTime, gameTime);
	}
}
