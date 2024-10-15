package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.model.exceptions.InvalidPositionException;

/**
 * This class represents the Scrabble game.
 * It is responsible for modeling the game and all the components.
 */
public class Game {
	// the players of the game. The order is significant and represents the order in
	// which turns are taken
	private Player[] players;
	private Board board; 	// the game board
	private Ruleset ruleset;	// game ruleset
	private LocalPlayer self;	// the local player

	private int currentPlayerTime;	// how much time (in seconds) the current player has
	private int gameTime;			// how much time (in seconds) remains in the game
	private int currentPlayer;		// whose turn it is. Their ID and
									//  	the player at players[currentPlayer]

	public Game(Player[] players, Board board, Ruleset ruleset, Player me){
		this.players = players;
		this.board = board;
		this.ruleset = ruleset;
		this.self = (LocalPlayer) me;
		// TODO: From ruleset, set times
	}

	/**
	 * Plays tiles on the board for a player
	 * @param playerID the ID of the player for whom to make a play
	 * @param tiles the tiles to be placed. must have at least on tile
	 * @throws InvalidPositionException if the tiles are not placed correctly.
	 * see Board.playTiles()
	 */
	public void playTiles(int playerID, Tile[] tiles)
			throws InvalidPositionException {
		int score = board.playTiles(tiles);
	}

	public int getCurrentPlayerTime() {
		return currentPlayerTime;
	}

	public void setCurrentPlayerTime(int currentPlayerTime) {
		this.currentPlayerTime = currentPlayerTime;
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void increaseScore(int playerID, int amount) {
		Player player = getPlayer(playerID);
		player.increaseScore(amount);
	}

	private Player getPlayer(int playerID) {
		return null;
	}


	public void passTurn(int ID) {
		Player player = getPlayer(ID);
		player.setPassedLastTurn(true);
	}

	public void setConnected(int playerID, boolean isConnected) {
		Player player = getPlayer(playerID);
		if (player instanceof NetworkPlayer) {
			NetworkPlayer np = (NetworkPlayer) player;
			np.setConnected(isConnected);
		}
	}

	public boolean isConnected(int playerID) {
		return true;
	}

	public boolean isActive(int playerID) {
		return true;
	}

	public void setActive(int playerID, boolean isActive) {

	}

	public void decreaseRack(int playerID, int amount) {

	}

	public int getNumTiles(int playerID) {
		return 0;
	}

	public Tile[] getLocalPlayerTiles() {
		return self.getRack();
	}


}
