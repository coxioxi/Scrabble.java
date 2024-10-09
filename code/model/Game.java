package model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
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
	 * @param points where the tiles are to be placed. The size of this array and
	 *      		tiles must be the same and ordered to correspond. points[0] must
	 *      		correspond to tiles[0], points[1] must correspond to tiles[1], etc.
	 * @throws InvalidPositionException if the tiles are not placed correctly.
	 * see Board.playTiles()
	 */
	public void playTiles(int playerID, Tile[] tiles, Point[] points)
			throws InvalidPositionException {
		int score = board.playTiles(tiles, points);
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

	}


	public void passTurn(int ID) {

	}

	public void setConnected(int playerID, boolean isConnected) {

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

	public void addTiles(Tile[] tiles) {

	}

	public void removeTiles(Tile[] tiles) {

	}

	public Tile[] getPlayerTiles() {
		return self.getRack();
	}


}
