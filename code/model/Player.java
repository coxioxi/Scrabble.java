package model;

/**
 * Generalized representation of a Player.
 * Players have a name, and ID, a score, and fields for
 * ability to make plays and whether they passed their previous turn.
 * Clients can change the score of the player and set their
 * activity status and their behavior on their previous turn.
 */
public class Player {
	public static int DEFAULT_SCORE = 0;	// the starting score

	private final String name;	// player name
	private int score;		// player score
	private final int ID;		// player ID, their turn in play

	/*
	hasPassed and isActive are for the requirements on turn passing.
	if a player passes two consecutive turns, they will become
	inactive, not able to make plays on the board.
	 */
	private boolean hasPassedLastTurn;	// did they pass their last turn?
	private boolean isActive;		// can they make plays on the board?

	/**
	 * Constructs a Player object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the id of the player, their order in play
	 */
	public Player(String name, int ID) {
		this.score = DEFAULT_SCORE;
		this.name = name;
		this.ID = ID;
		hasPassedLastTurn = false;
		isActive = true;
	}

	/**
	 * getter for player name
	 * @return name of player as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * getter for score
	 * @return the player's score as an integer
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Increases the player's score by a specified amount.
	 * note that score may be decreased by passing a negative number
	 * no limits are imposed on the score field. Use this method responsibly
	 * @param amount the amount by which to increase this player's
	 *               score. Must be between the negative and positive
	 *               limits of int.
	 */
	public void increaseScore(int amount) {
		this.score += amount;
	}

	/**
	 * getter for ID
	 * @return the player ID, which is unique and represents the order
	 * in which they play.
	 */
	public int getID() {
		return ID;
	}

	/**
	 * getter for hasPassedLastTurn.
	 * @return boolean value of the field
	 */
	public boolean isHasPassedLastTurn() {
		return hasPassedLastTurn;
	}

	/**
	 * changes the value of hasPassedLastTurn.
	 * @param hasPassedLastTurn the new value of this object's field
	 */
	public void setHasPassedLastTurn(boolean hasPassedLastTurn) {
		this.hasPassedLastTurn = hasPassedLastTurn;
	}

	/**
	 * getter for isActive, the ability of the player to make plays
	 * @return the boolean value of isActive. True if they can make plays,
	 * false otherwise
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * setter for isActive, the ability of the player to make plays
	 * @param active the new value of this object's field
	 */
	public void setActive(boolean active) {
		isActive = active;
	}
}
