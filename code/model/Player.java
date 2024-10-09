package model;

/**
 * Generalized representation of a Player.
 */
public class Player {
	public static int DEFAULT_SCORE = 0;	// the starting score

	private String name;	// player name
	private int score;		// player score
	private int ID;			// player ID, their turn in play

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

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public boolean isHasPassedLastTurn() {
		return hasPassedLastTurn;
	}

	public void setHasPassedLastTurn(boolean hasPassedLastTurn) {
		this.hasPassedLastTurn = hasPassedLastTurn;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}
}
