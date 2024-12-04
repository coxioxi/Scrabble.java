package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents the Scrabble game.
 * It is responsible for modeling the game and all the components.
 */
public class Game {

		// the players of the game. The order is significant and represents the order in
	// which turns are taken
	private Player[] players;
	private Board board; 	    // The game board
	private final Ruleset ruleset;	// The game ruleset
	private Player.LocalPlayer self;	// The local player

	private int currentPlayerTime;	// How much time (in seconds) the current player has
	private int gameTime;			// How much time (in seconds) remains in the game
	private int currentPlayer;		// Whose turn it is. Their ID and
									// The player at players[currentPlayer]
	private boolean isGameOver;		// Whether the game has met any end conditions

	/**
	 * Constructor for the Game class.
	 * Initializes players, board, ruleset, and the local player.
	 *
	 * @param players Array of players participating in the game
	 * @param board The game board
	 * @param ruleset The ruleset for the game
	 * @param me The local player
	 */
	public Game(Player[] players, Board board, Ruleset ruleset, Player.LocalPlayer me){
		this.players = players;
		this.board = board;
		this.ruleset = ruleset;
		this.self = me;
		// TODO: From ruleset, set times
	}

	/**
	 * @return this machines player
	 */
	public Player.LocalPlayer getSelf() {
		return self;
	}
	/**
	 * @return the array of players in the current session
	 */
	public Player[] getPlayers() {
		return players;
	}
	/**
	 * @return the board object
	 */
	public Board getBoard() {
		return board;
	}
	/**
	 * @return the ruleset from the current session
	 */
	public Ruleset getRuleset() {
		return ruleset;
	}


	/**
	 * Plays tiles on the board for a player
	 * @param playerID the ID of the player for whom to make a play
	 * @param tiles the tiles to be placed. must have at least on tile
	 * @return true if the play is successful, false otherwise
	 * See Board.playTiles()
	 */
	public int playTiles(int playerID, Tile[] tiles) {
		int score;
		Player player = getPlayer(playerID);
		if (player.isActive()) {
			score = board.playTiles(tiles);
			if(score >= 0) {
				boolean	areValid;
				areValid = ruleset.isWordInDictionary(
						board.getLastWordsPlayed().toArray(new String[0]));
				if(areValid) {
					player.increaseScore(score);
					if (playerID == self.getID()) self.increaseScore(score);
					this.nextTurn();
				}
				else{
					board.removeTiles(tiles);
					return -1;
				}
			}
			return score;
		}
		return -1;
	}

	/**
	 * This method will initiate a challenge on the
	 * most recent turn from the local player.
	 * The challenge will fail if any word from the turn
	 * is not in the game dictionary.
	 * The game object updates the Player and Board objects
	 * according to the result of the challenge
	 *
	 * @return true if the challenge passes. False if it fails
	 */
	public boolean challenge() {
		// TODO: Implement challenge logic
		return true;
	}

	/**
	 * this helper method changes whose turn it is to the next
	 * person in the turn list.
	 */
	public void nextTurn() {
		this.currentPlayer = (this.currentPlayer + 1) % this.players.length;
	}

	// Getter and setter methods for the current player time
	public int getCurrentPlayerTime() {
		return currentPlayerTime;
	}

	// Set current player time
	public void setCurrentPlayerTime(int currentPlayerTime) {
		this.currentPlayerTime = currentPlayerTime;
	}

	// Getter and setter methods for the game time
	public int getGameTime() {
		return gameTime;
	}

	// Set total game time
	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	// Getter for the current player's index
	public int getCurrentPlayer() {
		return players[currentPlayer].getID();
	}

	// Set the current player index
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




	/**
	 * Passes the turn from this player to the next player
	 * This method updates the player's status to inactive if they
	 * have passed their previous turn, otherwise only their
	 * passedLastTurn field is updated.
	 * The current player changes to the next player in the order.
	 * @param ID The ID of the player for whom to pass a turn
	 *           This ID must correspond to the ID of the current player
	 */
	public void passTurn(int ID) {
		Player player = players[ID]; // Retrieve the current player
		if(player.passedLastTurn()){ // Check if they have already passed
			player.setActive(false); // Set to inactive
		}else{
			player.setPassedLastTurn(true); // Mark that they have passed this turn
		}
		this.nextTurn();
	}

	/**
	 * Changes the status of the player's connection
	 * Note this change should only be made for NetworkPlayers,
	 * and not for the local player. The latter will cause errors.
	 * @param playerID the player to change the status of
	 * @param isConnected The new status of the player
	 */
	public void setConnected(int playerID, boolean isConnected) {
		Player.NetworkPlayer player = (Player.NetworkPlayer) players[playerID]; // Cast player to NetworkPlayer
		if(isConnected){
			player.setConnected(true); // Set connected status to true
		}else{
			player.setActive(false); // Set active status to false if disconnected
		}
	}

	/**
	 * Checks if a player is connected.
	 *
	 * @param playerID The ID of the player to check
	 * @return true if the player is connected, false otherwise
	 */
	public boolean isConnected(int playerID) {
		Player.NetworkPlayer player = (Player.NetworkPlayer) players[playerID]; // Cast player to NetworkPlayer
		return player.isConnected(); // Return network status
	}

	/**
	 * Checks if a player is active.
	 *
	 * @param playerID The ID of the player to check
	 * @return true if the player is active, false otherwise
	 */
	public boolean isActive(int playerID) {
		Player.NetworkPlayer player = (Player.NetworkPlayer) players[playerID]; // Cast player to NetworkPlayer
		return player.isActive(); // Return activity status
	}

	/**
	 * Changes the ability of the specified player to make plays
	 * @param playerID the player for whom to change activity status
	 * @param isActive the status of the player
	 */
	public void setActive(int playerID, boolean isActive) {
		Player player = players[playerID]; // Retrieve the player by ID
		player.setActive(isActive); // Set the player's active status
	}

	/**
	 * changes the rack size of specified player.
	 * This method should only be called on NetworkPlayers.
	 * @param playerID the player for whom to change the rack size.
	 * @param amount the amount to change the rack size by. Note that
	 *               the total size of a player's rack should never exceed 7.
	 */
	public void decreaseRack(int playerID, int amount) {
		Player.NetworkPlayer player = (Player.NetworkPlayer) players[playerID]; // Cast player to NetworkPlayer
		int numTiles = player.getNumTiles() - amount; // Calculate new number of tiles
		player.setNumTiles(numTiles); // Update the player's tile count

	}

	/**
	 * Gets the number of tiles the specified player has.
	 *
	 * @param playerID The ID of the player to check
	 * @return The number of tiles the player has
	 */
	public int getNumTiles(int playerID) {
		Player.NetworkPlayer player = (Player.NetworkPlayer)players[playerID]; // Cast player to NetworkPlayer
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
	 * @param tiles the tiles to remove. length less than or equal to 7
	 */
	public void removeTiles(Tile[] tiles) {
		self.removeTiles(tiles);
	}

	/**
	 * Gets the tiles of the local player
	 * @return the tiles of the local player
	 */
	public ArrayList<Tile> getPlayerTiles() {
		return self.getRack();
	}

	/**
	 * checks end game criteria
	 * @return the state of the game
	 */
	public boolean isGameOver() {
		isGameOver = false;
		if(allPlayersInactive()){
			isGameOver = true;
		} else if (ruleset.getTotalTime() == 0) {
			isGameOver = true;
		}
		return isGameOver;
	}

	/**
	 * checks to see if all players are active
	 * @return returns the state of all players
	 */
	private boolean allPlayersInactive(){
		for(Player player: players){
			if(player.isActive()){
				return false;
			}
		}
		return true;
	}

	/**
	 * this helper method returns a reference to the player object
	 * @param playerID the id assigned by the host
	 * @return reference to player object corresponding to the ID
	 */
	public Player getPlayer(int playerID) {
		Player finalPlayer = null; // Variable to hold the found player
		for(Player player: players){ // Loop through players
			if(playerID == player.getID()){ // Check if the player ID matches
				finalPlayer = player; // Assign the matching player
			}
		}
		return finalPlayer; // Return the found player
	}


}
