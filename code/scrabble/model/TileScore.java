package scrabble.model;

/**
 * An enumeration of the values of the tiles in Scrabble.
 */
public enum TileScore {
	A(1), B(3), C(3), D(2), E(1),
	F(4), G(2), H(4), I(1), J(8),
	K(5), L(1), M(3), N(1), O(1),
	P(3), Q(10), R(1), S(1), T(1),
	U(1), V(4), W(4), X(8), Y(4), Z(10);

	private final int score;	// The value of the letter

	/**
	 * Creates a scrabble.model.TileScore object with a score
	 * @param score value of playing this tile
	 */
	TileScore(int score) {
		this.score = score;
	}

	/**
	 * Gives the value of the letter
	 * @return the value of the letter
	 */
	public int getScore() {
		return score;
	}

	/**
	 * A way to get the value of a letter for any letter
	 * @param letter the letter to get the value of. Must be an alphabetical letter.
	 * @return the value of the letter. Between 1-10
	 */
	public static int getScoreForLetter(char letter) {
		return TileScore.valueOf( String.valueOf(letter).toUpperCase() ).getScore();
	}
}
