package scrabble.controller;

import scrabble.view.screen.HostScreen;

public class HostScreenController {
	private Controller parent;
	private HostScreen hostScreen;
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
		this.challengesEnabled = hostScreen.getChallengeBox();
		this.dictionaryFile = hostScreen.getDictionaryPath();
		this.playerTime = Integer.parseInt(hostScreen.getPlayerTimeBox().split(" ")[0]);
		this.gameTime = Integer.parseInt(hostScreen.getGameTimeBox().split(" ")[0]);

		parent.sendRulesToHost(challengesEnabled, dictionaryFile, playerTime, gameTime);
		parent.showGame();
	}

	public void setIPandPort(String IP, int port) {
		hostScreen.getHostsIP().setText(IP);
		hostScreen.getHostPort().setText(port+"");
	}

	public void addPlayer(String name) {
		hostScreen.addPlayerName(name);
	}
}
