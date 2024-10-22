package scrabble.model;

/**
 * Generalized representation of a Player in the Scrabble game.
 */
public class Player {
	public static int DEFAULT_SCORE = 0;  // Default starting score for players

	private final String name;	// Player's name
	private int score;			// Player's score
	private final int ID;		// Player's ID, their turn in play

	/*
		hasPassedLastTurn and isActive are for the requirements on turn passing.
		If a player passes two consecutive turns, they will become
		inactive, not able to make plays on the board.
	 */
	private boolean hasPassedLastTurn;	// Did they pass their last turn?
	private boolean isActive;		// Can they make plays on the board?

	/**
	 * Constructs a Player object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the id of the player, their order in play
	 */
	public Player(String name, int ID) {
		this.score = DEFAULT_SCORE; // Initializes score to default
		this.name = name;			// Set the player's name
		this.ID = ID;				// Assign the player's ID
		hasPassedLastTurn = false;	// Initialize passed last turn status
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
	 * Increases the player's score by the specified amount.
	 * @param score the amount to increase the player's score.
	 */
	public void increaseScore(int score) {
		this.score += score;
	}

	/**
	 * Getter for the player's unique ID.
	 * @return the player's ID as an integer.
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Checks if the player passed their last turn.
	 * @return true if the player passed their last turn, false otherwise.
	 */
	public boolean isHasPassedLastTurn() {
		return hasPassedLastTurn;
	}

	/**
	 * Sets the status of whether the player passed their last turn.
	 * @param hasPassedLastTurn the new status of the player's last turn pass.
	 */
	public void setHasPassedLastTurn(boolean hasPassedLastTurn) {
		this.hasPassedLastTurn = hasPassedLastTurn;
	}

	/**
	 * Checks if the player is currently active.
	 * @return true if the player can make plays, false if inactive.
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Sets the activity status of the player.
	 * @param active the new activity status for the player.
	 */
	public void setActive(boolean active) {
		isActive = active;
	}
}
