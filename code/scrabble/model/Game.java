package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;

/**
 * This class represents the Scrabble game.
 * It is responsible for modeling the game and all the components.
 */
public class Game {

	/*
	TODO:
		Jy'el: please consider end conditions as you implement this class's methods
		I have just added the isGameOver field, as I failed to consider it previously

	 */

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
	private boolean isGameOver;		// whether the game has met any end conditions

	public Game(Player[] players, Board board, Ruleset ruleset, Player me){
		this.players = players;
		this.board = new Board();
		this.ruleset = ruleset;
		this.self = (LocalPlayer) me;
		// TODO: From ruleset, set times
	}

	/**
	 * Plays tiles on the board for a player
	 * @param playerID the ID of the player for whom to make a play
	 * @param tiles the tiles to be placed. must have at least on tile
	 * see Board.playTiles()
	 */
	public boolean playTiles(int playerID, Tile[] tiles) {
		Player player = getPlayer(playerID);
		if (player.isActive()) {
			int score = board.playTiles(tiles);
			if (score < 0) return false;
			player.increaseScore(score);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * This method will initiate a challenge on the
	 * most recent turn from the local player.
	 * The challenge will fail if any word from the turn
	 * is not in the game dictionary.
	 * The game object updates the Player and Board objects
	 * according to the result of the challenge
	 *
	 * @return true if the challenge passes.
	 * 			false if it fails
	 */
	public boolean challenge() {

		return true;
	}

	/*
	this helper method changes whose turn it is to the next
	person in the turn list.
 	*/
	private void nextTurn() {
		this.currentPlayer = (this.currentPlayer + 1) % this.players.length;
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

	/**
	 * Increases the score of the specified player by the specified amount.
	 * Note that the score can be decreased by passing a negative number
	 * @param playerID the ID of the player for whom to increase the score
	 * @param amount by which to increase player score
	 */
	public void increaseScore(int playerID, int amount) {
		Player player = getPlayer(playerID);
		player.increaseScore(amount);
	}

	/*
	this helper method returns a reference to the player object
	whose ID is equal to the parameter
 	*/
	private Player getPlayer(int playerID) {
		Player finalPlayer = null;
		for(Player player: players){
			if(playerID == player.getID()){
				finalPlayer = player;
			}
		}
		return finalPlayer;
		// TODO: implement

	}


	/**
	 * Passes the turn from this player to the next player
	 * This method updates the player's status to inactive if they
	 * have passed their previous turn, otherwise only their
	 * passedLastTurn field is updated.
	 * The current player changes to the next player in the order
	 * @param ID The ID of the player for whom to pass a turn
	 *           This ID must correspond to the ID of the current player
	 */
	public void passTurn(int ID) {
		Player player = players[ID];
		if(player.isHasPassedLastTurn()){
			player.setActive(false);
		}else{
			player.setHasPassedLastTurn(true);
		}
		// TODO: implement
	}

	/**
	 * Changes the status of the player's connection
	 * Note this change should only be made for NetworkPlayers,
	 * and not for the local player. the latter will cause errors
	 * @param playerID the player to change the status of
	 * @param isConnected The new status of the player
	 */
	public void setConnected(int playerID, boolean isConnected) {
		NetworkPlayer player = (NetworkPlayer) players[playerID];
		if(isConnected){
			player.setConnected(true);
		}else{
			player.setActive(false);
		}

		// TODO: implement
		// also change isactive if isconnected is false
	}

	public boolean isConnected(int playerID) {
		NetworkPlayer player = (NetworkPlayer) players[playerID];
		return player.isConnected();
	}

	public boolean isActive(int playerID) {
		NetworkPlayer player = (NetworkPlayer) players[playerID];
		return player.isActive();
	}

	/**
	 * Changes the ability of the specified player to make plays
	 * @param playerID the player for whom to change activity status
	 * @param isActive the status of the player
	 */
	public void setActive(int playerID, boolean isActive) {
		Player player = players[playerID];
		player.setActive(isActive);

	}

	/**
	 * changes the rack size of specified player.
	 * This method should only be called on NetworkPlayers
	 * @param playerID the player for whom to change the rack size.
	 * @param amount the amount to change the rack size by. Note that
	 *               the total size of a player's rack should never exceed 7.
	 */
	public void decreaseRack(int playerID, int amount) {
		NetworkPlayer player = (NetworkPlayer) players[playerID];
		int numTiles = player.getNumTiles() - amount;
		player.setNumTiles(numTiles);

	}

	public int getNumTiles(int playerID) {
		NetworkPlayer player = (NetworkPlayer)players[playerID];
		return player.getNumTiles();
	}

	/**
	 * Adds tiles to the rack of the local player.
	 * the number of tiles added must not overfill the rack
	 *
	 * @param tiles the tiles to add to the player's rack
	 *              tiles.length should be equal to that of the
	 *              most recent call to Board.playTiles() when
	 *              playerID == LocalPlayer.ID
	 */
	public void addTiles(Tile[] tiles) {
		self.addTiles(tiles);

	}

	/**
	 * Removes tiles from the LocalPlayer (necessary in the case of
	 * a failed challenge)
	 * @param tiles the tiles to remove. length <= 7
	 */
	public void removeTiles(Tile[] tiles) {
		self.removeTiles(tiles);

	}

	/**
	 * Gets the tiles of the local player
	 * @return the tiles of the local player
	 */
	public Tile[] getPlayerTiles() {
		return self.getRack();
	}


}
