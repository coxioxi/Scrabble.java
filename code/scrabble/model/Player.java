package scrabble.model;

/**
 * Generalized representation of a Player.
 * Players have a name, and ID, a score, and fields for
 * ability to make plays and whether they passed their previous turn.
 * Clients can change the score of the player and set their
 * activity status and their behavior on their previous turn.
 */
public class Player {
	public static int DEFAULT_SCORE = 0;  // Default starting score for players

	private final String name;	// Player's name
	private int score;			// Player's score
	private final int ID;
	private int turnID;			// Player's ID, their turn in play
	/*
		passedLastTurn and isActive are for the requirements on turn passing.
		If a player passes two consecutive turns, they will become
		inactive, not able to make plays on the board.
	 */
	private boolean passedLastTurn;	// Did they pass their last turn?
	private boolean isActive;		// Can they make plays on the board?

	/**
	 * Constructs a Player object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the id of the player, their order in play
	 */
	public Player(String name, int ID, int turnID) {
		this.score = DEFAULT_SCORE; // Initializes score to default
		this.name = name;			// Set the player's name
		this.ID = ID;				// Assign the player's ID
		this.turnID = turnID;
		passedLastTurn = false;	// Initialize passed last turn status
		isActive = true;			// Set player as active by default
	}

	/**
	 * Getter for player name
	 * @return name of player as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the player's current score.
	 * @return the player's score as an integer.
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
	 * Getter for the player's unique ID.
	 * @return the player's ID as an integer.
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Setter for the player's unique turnID.
	 */
	public void setTurnID(int turnID) {
		this.turnID = turnID;
	}

	/**
	 * Getter for the player's unique turnID.
	 * @return the player's turnID as an integer.
	 */
	public int getTurnID() {
		return turnID;
	}

	/**
	 * getter for hasPassedLastTurn.
	 * @return boolean value of the field
	 */
	public boolean passedLastTurn() {
		return passedLastTurn;
	}

	/**
	 * changes the value of hasPassedLastTurn.
	 * @param passedLastTurn the new value of this object's field
	 */
	public void setPassedLastTurn(boolean passedLastTurn) {
		this.passedLastTurn = passedLastTurn;
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
