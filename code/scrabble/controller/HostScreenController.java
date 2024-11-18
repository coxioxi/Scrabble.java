package scrabble.controller;

import scrabble.view.screen.HostScreen;

/**
 * Handles components being clicked on the <code>HostScreen</code> and provides convenience
 * methods for changes.
 */
public class HostScreenController {
	private final Controller parent;
	private final HostScreen hostScreen;


	/**
	 * Constructs a HostScreenController from a parent and a <code>HostScreen</code>.
	 *
	 * @param parent the <code>Controller</code> on which changes are made based on user input.
	 * @param hostScreen the screen to which listeners are added.
	 */
	public HostScreenController(Controller parent, HostScreen hostScreen) {
		this.parent = parent;
		this.hostScreen = hostScreen;
		hostScreen.getHostButton().addActionListener(e -> hostButtonClick());
	}

	/**
	 * Sets the IP and Port label of this object's <code>HostScreen</code>.
	 *
	 * @param IP the String representation of the Host's IP.
	 * @param port the port number on which the host is listening.
	 */
	public void setIPandPort(String IP, int port) {
		hostScreen.getHostsIP().setText(IP);
		hostScreen.getHostPort().setText(port+"");
	}

	/**
	 * Adds the name of a player to the list of players waiting.
	 * @param name the name of the player to add.
	 * @see HostScreen#addPlayerName
	 */
	public void addPlayer(String name) { hostScreen.addPlayerName(name); }

	/*
	 * handles the submit button being clicked. Gets the options selected, then sends them to parent.
	 */
	private void hostButtonClick() {
		boolean challengesEnabled = hostScreen.getChallengeBox();
		String dictionaryFile = hostScreen.getDictionaryPath();
		int playerTime = Integer.parseInt(hostScreen.getPlayerTimeBox().split(" ")[0]);
		int gameTime = Integer.parseInt(hostScreen.getGameTimeBox().split(" ")[0]);

		parent.sendRulesToHost(challengesEnabled, dictionaryFile, playerTime, gameTime);
		parent.showGame();
	}

}
