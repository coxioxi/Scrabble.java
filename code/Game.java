/**
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.util.Timer;

/**
 * This class represents the Scrabble game.
 * It is responsible for modeling the game and all the components.
 */
public class Game {
	private Player[] players;
	private Board board;
	private Ruleset ruleset;
	private Player self;
	private Timer playerTimer;
	private Timer gameTimer;

	public Game(Player[] players, Board board, Ruleset ruleset, Player me){
		this.players = players;
		this.board = board;
		this.ruleset = ruleset;
		this.self = me;
		//From ruleset, create timer objects
	}

	public void playTiles(Tile[] tiles, Point[] points)
			throws InvalidPositionException {
		int score = board.playTiles(tiles, points);
	}

	public void increaseScore(Player player, int amount) {

	}
}
