package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.util.*;

/**
 * This class represents the scrabble board. It maintains the placement of
 * scrabble.model.Tile objects on the board and allows for changes to be made.
 * When changes are made, scrabble.model.Board scores the word(s) played and allows them to be accessed
 * in the future. Note that scrabble.model.Board does not check the validity of words, only their values.
 */
public class Board {
	/**
	 * How many rows are on the board.
	 */
	public static final int BOARD_ROWS = 15;
	/**
	 * How many columns are on the board.
	 */
	public static final int BOARD_COLUMNS = 15;

	/**
	 * A map of {@link ModifierType modifier cells} accessible by {@link Point}.
	 * Calling this.get(Point) returns the type of modifier cell at that point.
	 * Note that a null value should be treated as a non-modifier cell.
	 */
	public static final Map<Point,ModifierType> MODIFIER_HASH_MAP = initializeModifierCells();


	private Tile[][] board;  // where Tile objects are placed
    private ArrayList<String> lastWordsPlayed
			= new ArrayList<>();   // the words which have most recently been played

    public static void main(String[] args) {
        /*
        Usage: Follow prompts in console. initial play is made,
        subsequent plays are made by user.
        User specifies starting row and column, orientation of play,
        then the letters/tiles to be played. Exclude board tiles, only
        input new tiles to be placed.
         */
        Board board = new Board();

        // Initial play of "TILE", horizontal, starting at 7,7
		/*
        Tile[] firstPlay = new Tile[4];
        firstPlay[0] = new Tile('T', new Point(7,7 ));
        firstPlay[1] = new Tile('I', new Point(7,8 ));
        firstPlay[2] = new Tile('L', new Point(7,9 ));
        firstPlay[3] = new Tile('E', new Point(7,10));

		int score = board.playTiles(firstPlay);
		System.out.println("Score:" + score);

        System.out.println(board);

		 */

		int score;

        // Allow user to make plays on board
        Scanner in = new Scanner (System.in);
        System.out.print("Make a play (Y) or quit (Q): ");
        char userInput = in.next().toUpperCase().charAt(0);
        while (userInput != 'Q') {
            // User makes a play at a starting row and column,
            // specifies direction, then this function calculates
            // the points of all letters given the board's state.
            System.out.println();
            System.out.println("Starting point");
            System.out.print("\tEnter row (integer): ");
            int startingRow = in.nextInt();
            System.out.print("\tEnter column (integer): ");
            int startingColumn = in.nextInt();

            // Determines whether the word iss vertical or horizontal
            System.out.println();
            System.out.println("Orientation");
            System.out.print("\tEnter 'V' for vertical, 'H' for horizontal: ");
            boolean isVertical = 'V' == in.next().toUpperCase().charAt(0);

            // Accepts the new word to be placed on the board
            System.out.println();
            System.out.print("Enter letters on this line: ");
            char[] letters = in.next().trim().toUpperCase().toCharArray();


            // Calculate Point object of each tile
            Tile[] tiles = new Tile[letters.length];
            int gap = 0;    // Specifies gap from starting point to this tile
            for (int i = 0; i < letters.length; i++) {
                char letter = letters[i];
                if (i == 0) {
                    tiles[i] = new Tile(letter,
                            new Point(startingRow, startingColumn));
                    gap = 1;
                }
                else if (isVertical) {
                    while (board.board[startingRow+gap][startingColumn] != null) {
                        gap++;
                    }
                    tiles[i] = new Tile(letter,
                            new Point(startingRow+gap, startingColumn));
                    gap++;
                }
                else {
                    while (board.board[startingRow][startingColumn+gap] != null) {
                        gap++;
                    }
                    tiles[i] = new Tile(letter,
                            new Point(startingRow, startingColumn+gap));
                    gap++;
                }
            }

            // Plays the tiles and calculates the score
            score = board.playTiles(tiles);
            if (score != -1) {
                System.out.println("Score: " + score);
                System.out.println("Words made: ");
                for (String s : board.lastWordsPlayed) {
                    System.out.println("\t" + s);
                }
                System.out.println();

                // Options to display the current board state
                System.out.print("Show board? ('Y' or 'N'): ");
                boolean showBoard = 'Y' == in.next().toUpperCase().charAt(0);
                System.out.println();

                if (showBoard)
                    System.out.println(board);
                System.out.println();
            }
            else {
                System.out.println("Play failed.");
                System.out.println();
            }

            // Options to either make another play or quit
            System.out.print("Make a play (Y) or quit (Q): ");
            userInput = in.next().toUpperCase().charAt(0);
        }
    }

    /**
     * Constructs a new Board object.
     */
    public Board() {
        board = new Tile[BOARD_ROWS][BOARD_COLUMNS];
    }

    /**
     * getter for lastWordsPlayed, the list of words added to the
	 * board on the previous call to {@link #playTiles}
     * @return an array list of most recent words
     */
    public ArrayList<String> getLastWordsPlayed() {
        return lastWordsPlayed;
    }

    /**
     * Clears the board of all tiles, puts null values in their place
     */
    public void clearBoard(){
        for (Tile[] tiles : board) {
            Arrays.fill(tiles, null);
        }
    }

    /**
     * Returns the tile object played on a given x and y.
     * @param x row index
     * @param y column index
     * @return the tile at the specified (x, y) position. Null if none has been played here.
     */
    public Tile getTile(int x, int y){
        return board[x][y];
    }

	/**
	 * Removes the specified tiles from the board.
	 * All tiles in parameter tiles must have been placed on the board already.
	 * @param tiles the tiles to remove from the board. Each tile must have a location specified
	 * @throws NullPointerException when not all specified tiles have already been placed
	 * 								on the board
	 */
	public void removeTiles(Tile[] tiles)
			throws NullPointerException {
		for (Tile tile : tiles) {
			int x = tile.getLocation().x;
			int y = tile.getLocation().y;

			// check if position is null, then check letter value
			if (board[x][y] != null && board[x][y].getLetter() == tile.getLetter()) {
				board[x][y] = null;
			} else {
				throw new NullPointerException(
						"No tile on board position"
				);
			}
		}
	}

    /**
     * A caller method for testing purposes
     */
   	public boolean hasAdjacentCaller(Point t){
		return hasAdjacentTile(t);
	}

    /**
	 * Validates position of tiles, scores the word(s) made, and adds the tiles to the board.
	 * <p>
	 *     Parameter tiles must conform to the following requirements:
	 *     <ul>
	 *         <li>One tile in tiles must either: <ul>
	 *             <li>be adjacent to an already placed tile, or</li>
	 *             <li>be placed at the starting tile (7, 7).</li>
	 *         </ul></li>
	 *         <li>Each {@link Tile#getLocation tile point} must correspond to an empty
	 *         position on the board. That is, no tile has been placed at this point.</li>
	 *         <li>Each tile point must be within the board bounds (x,y in {0,1,...,14}).</li>
	 *         <li>All tiles must have either the same x/row or y/column value.</li>
	 *         <li>Each tile location must be unique. That is, no two tiles in parameter
	 *         tiles may have the same x and y values.</li>
	 *         <li>All tiles must be connected. That is, either a tile in tiles is
	 *         adjacent to another tile in tiles, or it is adjacent to a board tile
	 *         which is adjacent to a tile in tiles. In other words, there may be no
	 *         empty spaces in a word played.</li>
	 *     </ul>
	 *     If any of these conditions is not met, a negative number will be returned.
	 * </p>
     *
     * @param tiles the tiles which are being placed on the board
     *               Note the array may not be empty, but arrays of size 1 are allowed.
     * @return the score of the word(s) played as an integer. A negative number is
	 * returned if the play is invalid (see above). Note that a score of 0 is possible.
	 * There is no known limit to a scrabble score, though the highest calculated is in
	 * the lower 1000's.
     */
    public int playTiles(Tile[] tiles) {
        if (!validatePositions(tiles))
			return -1;       // ensure positions are allowed
        int score = score(tiles);       // calculate score of play
        addToBoard(tiles);              // add to board
        return score;
    }


    /**
     * Creates a String representation of this Board object.
     * For each cell on the board, the letter value of the tile is placed;
     * or, the modifier cell is shown when no tile is placed;
     * or, simply "__" is shown when neither condition is met.
     * each cell is padded with spaces in the String. A newline is
     * added to the end of board rows.
     * @returns a String representation, with formatting as stated above
	 */
    public String toString() {
        // String representation of the board
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) {
					ModifierType mt = MODIFIER_HASH_MAP.get(new Point(i, j));
                    if (mt == ModifierType.NONE)
						sb.append(" __ ");
                    else
                        sb.append(' ').append(mt.getAbbreviation()).append(' ');
                }
                else {
                    // Append the letter of the tile at the current position
                    sb.append(" ").append(board[i][j].getLetter()).append("  ");
                }
            }
            sb.append("\n"); // New line for each row
        }
        return sb.toString();
    }

    /**********************************************
                    Private methods
     **********************************************/


    /*
    helper method which adds tiles to the board at specified points.
    does not check scoring or validity of play.
     */
    public void addToBoard(Tile[] tiles) {
		for (Tile tile : tiles)
			board[(int) tile.getLocation().getX()][(int) tile.getLocation().getY()] = tile;
    }

	/*
	this method sets up the boardSpecialCell field with all the correct placements
	for modifier cells using Point objects and scrabble.model.ModifierType enumerations.
	*/
	private static HashMap<Point, ModifierType> initializeModifierCells() {
		HashMap<Point, ModifierType> cells = new HashMap<>();
		cells.put(new Point(0,0), ModifierType.TRIPLE_WORD);
		cells.put(new Point(3,0), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(7,0), ModifierType.TRIPLE_WORD);
		cells.put(new Point(11,0), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(14,0), ModifierType.TRIPLE_WORD);
		cells.put(new Point(1,1), ModifierType.DOUBLE_WORD);
		cells.put(new Point(5,1), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(9,1), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(13,1), ModifierType.DOUBLE_WORD);
		cells.put(new Point(2,2), ModifierType.DOUBLE_WORD);
		cells.put(new Point(6,2), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(8,2), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(12,2), ModifierType.DOUBLE_WORD);
		cells.put(new Point(0,3), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(3,3), ModifierType.DOUBLE_WORD);
		cells.put(new Point(7,3), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(11,3), ModifierType.DOUBLE_WORD);
		cells.put(new Point(14,3), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(4,4), ModifierType.DOUBLE_WORD);
		cells.put(new Point(10,4), ModifierType.DOUBLE_WORD);
		cells.put(new Point(1,5), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(5,5), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(9,5), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(13,5), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(2,6), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(6,6), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(8,6), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(12,6), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(0,7), ModifierType.TRIPLE_WORD);
		cells.put(new Point(3,7), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(7,7), ModifierType.DOUBLE_WORD);
		cells.put(new Point(11,7), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(14,7), ModifierType.TRIPLE_WORD);

		cells.put(new Point(0,14), ModifierType.TRIPLE_WORD);
		cells.put(new Point(3,14), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(7,14), ModifierType.TRIPLE_WORD);
		cells.put(new Point(11,14), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(14,14), ModifierType.TRIPLE_WORD);
		cells.put(new Point(1,13), ModifierType.DOUBLE_WORD);
		cells.put(new Point(5,13), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(9,13), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(13,13), ModifierType.DOUBLE_WORD);
		cells.put(new Point(2,12), ModifierType.DOUBLE_WORD);
		cells.put(new Point(6,12), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(8,12), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(12,12), ModifierType.DOUBLE_WORD);
		cells.put(new Point(0,11), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(3,11), ModifierType.DOUBLE_WORD);
		cells.put(new Point(7,11), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(11,11), ModifierType.DOUBLE_WORD);
		cells.put(new Point(14,11), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(4,10), ModifierType.DOUBLE_WORD);
		cells.put(new Point(10,10), ModifierType.DOUBLE_WORD);
		cells.put(new Point(1,9), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(5,9), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(9,9), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(13,9), ModifierType.TRIPLE_LETTER);
		cells.put(new Point(2,8), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(6,8), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(8,8), ModifierType.DOUBLE_LETTER);
		cells.put(new Point(12,8), ModifierType.DOUBLE_LETTER);
		return cells;
	}


	/*
	All remaining helper methods pertain to score.
	 */

    /*
        helper method; calculates the score of tiles played with words and modifier cells.
        returns score as an int
        also updates lastWordsPlayed, so it should only be called when the positions have been
        validated through all necessary methods.
     */
    private int score(Tile[] tiles) {

        /*
        figure out orientation of tiles (vertical/horizontal);
        all collateral words will be perpendicular.
        find the score of the main word first, move on to collaterals.
        update last words played and return turn score
        (note that single letter plays work with this method of calculation;
            the main word can have an arbitrary orientation and adjacent
            board tiles will be added into score correctly)
         */
        int turnScore;
        ScoreData collateralWords, mainWord;

		if (tiles.length == 1) {
			collateralWords = getHorizontalCollateralWordsScore(tiles);
			mainWord = getVerticalCollateralWordsScore(tiles);
		}
		else {
			boolean isVertical = allSameCol(tiles);           // check verticality

			if (isVertical) {       // Vertical tiles
				sortAscendingByRow(tiles);       // sort by row

				mainWord = getVerticalMainWordScore(tiles);       // score word made by tiles
				collateralWords = getVerticalCollateralWordsScore(tiles);
			} else {              // Horizontal tiles
				sortAscendingByCol(tiles);       // sort by column

				mainWord = getHorizontalMainWordScore(tiles);     // score word made by tiles
				collateralWords = getHorizontalCollateralWordsScore(tiles);       // score perpendicular words
			}
		}

        lastWordsPlayed = new ArrayList<>();

		lastWordsPlayed.addAll(mainWord.getWords());
		lastWordsPlayed.addAll(collateralWords.getWords());

		turnScore = collateralWords.getScore() + mainWord.getScore();       // update turn score
		return turnScore;
    }

    /*
    helper method: takes tiles sorted descending by column
    and returns the word made and score accumulated in ScoreData object.
     */
    private ScoreData getHorizontalCollateralWordsScore(Tile[] tiles) {
        /*
        the score of collateral words can be found by starting with the first tile,
            moving up over all connected board tiles, adding the current tile,
            then moving down from current tile.
            start with first tile in tiles.
            search up for board tiles, add in board tile value to
            current word score. then, handle new tile modifier cell appropriately, and add
            tile score to current word score. search down, adding in board tile values.
            when there are no more connected board tiles (null found), add
            word score to collateral word score and repeat for next tile in tiles.
            repeat this process for each tile to get the score of collateral words.

        (note that modifier cells only apply to tiles on the turn in which a tile is placed on the cell.
            therefore, board tiles are NOT checked for modifier cells.)

        as the value of individual tiles is added to the scoring variable,
            the letter is added to a string builder. this process generates
            a list of collateral words resulting from the play
        return a ScoreData object with the words made and score accumulated
         */
        ArrayList<String> words = new ArrayList<>();
        int collateralWordsScore = 0;
        int wordMultiplier = 1;
		for (Tile tile : tiles) {
			// initialize variables
			StringBuilder currentWord = new StringBuilder();
			Point placement = tile.getLocation();
			int x = placement.x;
			int y = placement.y;

			// for this tile, find the topmost connected tile
			int topMostTile = x;
			while (topMostTile - 1 >= 0 && board[topMostTile - 1][y] != null) {
				topMostTile--;
			}

			// move down from topmost, accumulating score.
			// only stop when no letters below current tile
			int currentWordScore = 0;
			while (topMostTile < BOARD_ROWS &&
					(board[topMostTile][y] != null || x == topMostTile)) {
				Tile boardTile = board[topMostTile][y];
				if (boardTile != null) {        // if topMost is position on board
					currentWordScore += boardTile.getScore();
					currentWord.append(boardTile.getLetter());
				} else {                          // if topMost is tile to be placed
					ModifierType cellMod = MODIFIER_HASH_MAP.get(placement);

					wordMultiplier *= (cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);
					currentWordScore += tile.getScore() * (!cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);

					currentWord.append(tile.getLetter());
				}
				topMostTile++;      // move down
			}
			// final updates to collateral word variables
			if (currentWord.length() > 1) {
				currentWordScore *= wordMultiplier;
				wordMultiplier = 1;
				collateralWordsScore += currentWordScore;
				words.add(currentWord.toString());
			}
			else if (!words.isEmpty()){
				System.out.println("Removing Letter...");
				words.remove(0);
				collateralWordsScore = 0;
			}
		}

        return new ScoreData(words, collateralWordsScore);
    }

    /*
    helper method: takes tiles sorted descending by column
    and returns the collateral (perpendicular) words
    made and score accumulated in ScoreData object.
     */
    private ScoreData getHorizontalMainWordScore(Tile[] tiles) {
        /*
        the score of the main word can be found by starting at the left (vertical) and moving
              right.
              first, identify the column of the highest connected board tile;
              move right, adding the value of board tiles to the current word score

              now, start with the first tile in tiles;
              handle cell modifiers appropriately, add letter value to wordScore

              move on to the next tile.
              if there is a greater than 1 difference in column between this tile and the previous tile,
                then there are board tiles in between. calculate the distance and add
                these tiles to the word score.
              repeat until the end of tiles, then check for board tiles on the right and add to score.

              note that when checking for modifier cells on new tiles,
                letter multipliers can be applied immediately, while word multipliers
                must be noted and applied at the end of word score accumulation.
        (note that modifier cells only apply to tiles on the turn in which a tile is placed on the cell.
            therefore, board tiles are NOT checked for modifier cells.)

        as the value of individual tiles is added to the scoring variable,
            the letter is added to a string builder. this process generates
            the main word resulting from the play
        return a ScoreData object with the word created and the score accumulated
         */
        // initialize variables
        StringBuilder mainWordString = new StringBuilder();     // for word from "tiles"
        int mainWordScore = 0;
        int wordMultiplier = 1;
        // check for tiles on left, add to score
        int firstCol = tiles[0].getLocation().y;
        int row = tiles[0].getLocation().x;
        int onLeft = firstCol-1;
        while (onLeft >= 0 && board[row][onLeft] != null) {
            // add current highest known placed tile to score and string. move upwards
            Tile current = board[row][onLeft];
            mainWordScore += current.getScore();
            mainWordString.append(current.getLetter());
            onLeft--;
        }
		mainWordString.reverse();

        // MAIN WORD SCORE
        for (int i = 0; i < tiles.length; i++) {
            // initialize variables
            Tile current = tiles[i];
            Point placement = current.getLocation();   // current tile new cell location
            int x = (int) placement.getX();
            int y = (int) placement.getY();

            //figure out if there are old tiles in between this tile and previous
            int numOldTiles = 0;
            if (i > 0) {
                // if there is a difference between current Y and previous Y which is more than 1,
                // then there are old tiles. (-1 makes sure to count only for gaps)
                int previousTileY = (int) tiles[i-1].getLocation().getY();
                numOldTiles = y - previousTileY - 1;        // Adjust by one for old tiles
            }

            // cycle through tiles on board in between, add in letter value
            for (int j = y - numOldTiles; j < y; j++) {
                mainWordScore += board[x][j].getScore();
                mainWordString.append(board[x][j].getLetter());
            }

            // Handle modifiers on current tile's cell, add tile value to counter
            // Add letter to string at end
            ModifierType cellMod = MODIFIER_HASH_MAP.get(placement);

			wordMultiplier *= (cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);
			mainWordScore += current.getScore() * (!cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);

            mainWordString.append(current.getLetter());
        }
        // check if tiles are to right, add to score
        int lastCol = tiles[tiles.length-1].getLocation().y;
        row = tiles[0].getLocation().x;
        int onRight = lastCol+1;
        while (onRight < BOARD_COLUMNS && board[row][onRight] != null) {
            mainWordScore += board[row][onRight].getScore();
            mainWordString.append(board[row][onRight].getLetter());
            onRight++;
        }

        // final scoring for the main word...
        mainWordScore *= wordMultiplier;
		if (tiles.length == 7) {
			mainWordScore += 50;
		}

        // add this main word to string list
        ArrayList<String> words = new ArrayList<>();
        words.add(mainWordString.toString());

        return new ScoreData(words, mainWordScore);
    }

    /*
    helper method: takes tiles sorted descending by row
    and returns the word made and score accumulated in ScoreData object.
     */
    private ScoreData getVerticalCollateralWordsScore(Tile[] tiles) {
        /*
        the score of collateral words can be found by starting with the first tile,
            moving left over all connected board tiles, adding the current tile,
            then moving right from current tile.
            start with first tile in tiles.
            search left for board tiles, add in board tile value to
            current word score. then, handle new tile modifier cell appropriately and add
            tile score to current word score. search right, adding in board tile values.
            when there are no more connected board tiles (null found), add
            word score to collateral word score and repeat for next tile in tiles.
            repeat this process for each tile to get the score of collateral words.

        (note that modifier cells only apply to tiles on the turn in which a tile is placed on the cell.
            therefore, board tiles are NOT checked for modifier cells.)

        as the value of individual tiles is added to the scoring variable,
            the letter is added to a string builder. this process generates
            a list of collateral words resulting from the play
        return a ScoreData object with the words made and score accumulated
         */
        // figure out any collateral word total
        int collateralWordsScore = 0;
        ArrayList<String> words = new ArrayList<>();
        int wordMultiplier = 1;
		for (Tile tile : tiles) {
			// initialize variables
			StringBuilder currentWord = new StringBuilder();
			Point placement = tile.getLocation();
			int x = placement.x;
			int y = placement.y;

			// x = row
			// y = column

			// for this tile, find the leftmost connected tile
			int leftMostTile = y;
			while (leftMostTile - 1 >= 0 && board[x][leftMostTile - 1] != null) {
				leftMostTile--;
			}

//			System.out.println("Vertical collateral. leftmost for tile " +
//					tile.getLocation().x + ", " + tile.getLocation().y +
//					" is location " + leftMostTile + " with value " +
//					(board[x][leftMostTile] == null ? "none" : board[x][leftMostTile].getLetter()));
			// move right from leftmost, accumulating score.
			// only stop when no letters to right of current
			int currentWordScore = 0;
			while (leftMostTile < BOARD_COLUMNS &&
					(board[x][leftMostTile] != null || y == leftMostTile)) {
				Tile boardTile = board[x][leftMostTile];
				if (boardTile != null) {        // if leftMost is position on board
					currentWordScore += boardTile.getScore();
					currentWord.append(boardTile.getLetter());
				} else {                          // if leftMost is tile to be placed
					ModifierType cellMod = MODIFIER_HASH_MAP.get(placement);

					wordMultiplier *= (cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);
					currentWordScore += tile.getScore() * (!cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);

					currentWord.append(tile.getLetter());
				}
				leftMostTile++;
			}


			// final updates to collateral word variables
			if (currentWord.length() > 1) {

				currentWordScore *= wordMultiplier;
				wordMultiplier = 1;
				collateralWordsScore += currentWordScore;
				words.add(currentWord.toString());
			}
			else if (!words.isEmpty()){
				System.out.println("Removing Letter...");
				words.remove(0);
				collateralWordsScore = 0;
			}
		}

        return new ScoreData(words, collateralWordsScore);
    }

    /*
    helper method: takes tiles sorted descending by row
    and returns the collateral (perpendicular) words
    made and score accumulated in ScoreData object.
     */
    private ScoreData getVerticalMainWordScore(Tile[] tiles) {
        /*
        the score of the main word can be found by starting at the top (vertical) and moving
              down.
              first, identify the row of the highest connected board tile;
              move down, adding the value of board tiles to the current word score

              now, start with the first tile in tiles;
              handle cell modifiers appropriately, add letter value to wordScore

              move on to the next tile.
              if there is a greater than 1 difference in row between this tile and the previous tile,
                then there are board tiles in between. calculate the distance and add
                these tiles to the word score.
              repeat until the end of tiles, then check for board tiles at the bottom and add to score.

              note that when checking for modifier cells on new tiles,
                letter multipliers can be applied immediately, while word multipliers
                must be noted and applied at the end of word score accumulation.

        (note that modifier cells only apply to tiles on the turn in which a tile is placed on the cell.
            therefore, board tiles are NOT checked for modifier cells.)

        as the value of individual tiles is added to the scoring variable,
            the letter is added to a string builder. this process generates
            the main word resulting from the play
        return a ScoreData object with the word created and the score accumulated
         */
        // initialize variables
        StringBuilder mainWordString = new StringBuilder();     // for word from "tiles"
        int mainWordScore = 0;
        int wordMultiplier = 1;
        // check for tiles above, add to score
        int firstRow = tiles[0].getLocation().x;
        int col = tiles[0].getLocation().y;
        int above = firstRow-1;
        while (above >= 0 && board[above][col] != null) {
            // add current highest known placed tile to score and string. move upwards
            Tile current = board[above][col];
            mainWordScore += current.getScore();
            mainWordString.append(current.getLetter());
            above--;
        }
		mainWordString.reverse();

        // START MAIN WORD SCORE CODE
        for (int i = 0; i < tiles.length; i++) {
            // initialize variables
            Tile current = tiles[i];
            Point placement = current.getLocation();   // current tile new cell location
            int x = (int) placement.getX();
            int y = (int) placement.getY();

            //figure out if there are old tiles in between this tile and previous
            int numOldTiles = 0;
            if (i > 0) {
                // if there is a difference between current X and previous X which is more than 1,
                // then there are old tiles. (-1 makes sure to count only for gaps)
                int previousTileX = (int) tiles[i-1].getLocation().getX();
                numOldTiles = x - previousTileX - 1;        // Adjust by one for old tiles
            }

            // cycle through tiles on board in between, add in letter value
            for (int j = x - numOldTiles; j < x; j++) {
                mainWordScore += board[j][y].getScore();
                mainWordString.append(board[j][y].getLetter());
            }

            // Handle modifiers on current tile's cell, add tile value to counter
            // Add letter to string at end
			ModifierType cellMod = MODIFIER_HASH_MAP.get(placement);

			wordMultiplier *= (cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);
			mainWordScore += current.getScore() * (!cellMod.isAppliesToWord() ? cellMod.getMultiplier() : 1);

            mainWordString.append(current.getLetter());
        }
        // check if tiles are below, add to score
        int lastRow = tiles[tiles.length-1].getLocation().x;
        col = tiles[0].getLocation().y;
        int below = lastRow+1;
        while (below < BOARD_ROWS && board[below][col] != null) {
            mainWordScore += board[below][col].getScore();
            mainWordString.append(board[below][col].getLetter());
            below++;
        }

        // final scoring for the main word...
        //System.out.println("Word multiplier: " + wordMultiplier);
        mainWordScore *= wordMultiplier;
		if (tiles.length == 7) {
			mainWordScore += 50;
		}

        // add this main word to string list
        ArrayList<String> words = new ArrayList<>();
        words.add(mainWordString.toString());

        return new ScoreData(words, mainWordScore);
    }


    /*
    sorts the tiles so that each subsequent tile has a smaller
    column (x) value, with smallest at tiles[0]
    */
    private void sortAscendingByCol(Tile[] tiles) {
        for (int i = 0; i < tiles.length-1; i++) {
            for (int j = i; j < tiles.length-1; j++) {
                if (tiles[j].getLocation().y > tiles[j+1].getLocation().y) {
                    Tile temp = tiles[j];
                    tiles[j] = tiles[j+1];
                    tiles[j+1] = temp;
                }
            }
        }
    }

    /*
    sorts the tiles so that each subsequent tile has a smaller
    row (y) value, with smallest at tiles[0]
     */
    private void sortAscendingByRow(Tile[] tiles) {
        for (int i = 0; i < tiles.length-1; i++) {
            for (int j = i; j < tiles.length-1; j++) {
                if (tiles[j].getLocation().x > tiles[j+1].getLocation().x) {
                    Tile temp = tiles[j];
                    tiles[j] = tiles[j+1];
                    tiles[j+1] = temp;
                }
            }
        }
    }

    /*
    helper method; checks that points meet valid positions requirements of Scrabble.
    (here, tiles are represented by the points) checks that:
        no tiles are placed in occupied cell
        1 tile is placed on starting tile
            or 1 tile is adjacent to already placed tile
        all tiles are connected, either by adjacency, or adjacency to adjacency
     */
    private boolean validatePositions(Tile[] tiles) {
        /*
        System.out.println("Inbounds: " + arePointsInbounds(tiles));
        System.out.println("SameRow: " + allSameRow(tiles));
        System.out.println("SameCol: " + allSameCol(tiles));
        System.out.println("notOccupied: " + pointsNotOccupied(tiles));
        System.out.println("Starting or Adjacent: " + arePointsStartingOrAdjacent(tiles));
        System.out.println("arePointsConnected: " + arePointsConnected(tiles));
         */

        return (arePointsInbounds(tiles) &&
			   (allSameRow(tiles) || allSameCol(tiles)) &&
				hasNoDuplicates(tiles) &&
				pointsNotOccupied(tiles) &&
				arePointsStartingOrAdjacent(tiles) &&
				arePointsConnected(tiles));
    }

    //getters for testing purposes
    public boolean getAreInBounds(Tile[] tiles){return arePointsInbounds(tiles);}
    public boolean getHasNoDuplicates(Tile[] tiles){return hasNoDuplicates(tiles);}
    public boolean getPointsNotOccupied(Tile[] tiles){return pointsNotOccupied(tiles);}
    public boolean getArePointsStartingOrAdjacent(Tile[] tiles){return arePointsStartingOrAdjacent(tiles);}
    public boolean getArePointsConnected(Tile[] tiles){return arePointsConnected(tiles);}
    public boolean getAllSameRow(Tile[] tiles){return allSameRow(tiles);}
    public boolean getAllSameCol(Tile[] tiles){return allSameCol(tiles);}

    /*
	helper method which checks that all tiles are adjacent
	to each other or have gaps filled with board tiles
	 */
    private boolean arePointsConnected(Tile[] tiles) {
        // check if they are all connected

        // steps to check connection status:
        // determine orientation of new tiles (vertical, horizontal)
        // sort tiles by x or y component based on orientation
        // start with top left tile. move to next tile, check that change is equal to 1;
        //      if the change is greater, check that in between cells on the board are occupied.
        //          check fails if any are blank. return false
        // repeat with remainder of the list
		// return true if all checks clear
        if (allSameRow(tiles)) {     // horizontal
            sortAscendingByCol(tiles);
            for (int i = 1; i < tiles.length; i++) {
                int oldY = tiles[i-1].getLocation().y;
                int currentY = tiles[i].getLocation().y;
                for (int j = oldY + 1; j < currentY; j++) {
                    if (board[tiles[0].getLocation().x][j] == null) {
                        return false;
                    }
                }
            }
        }
        else {                      // vertical
            sortAscendingByRow(tiles);
            for (int i = 1; i < tiles.length; i++) {
                int oldX = tiles[i-1].getLocation().x;
                int currentX = tiles[i].getLocation().x;
                for (int j = oldX + 1; j < currentX; j++) {
                    if (board[j][tiles[0].getLocation().y] == null) {
                        return false;
                    }
                }
            }
        }
		return true;
    }

    /*
    helper method which checks if any spaces are not blank which are for new tiles
    returns true if play is acceptable
    false if one point already has a tile on it
     */
    private boolean pointsNotOccupied(Tile[] tiles) {
        // are any points already occupied?
        for (Tile t : tiles) {
            if (board[(int) t.getLocation().getX()][(int) t.getLocation().getY()] != null)
                return false;
        }
		return true;
    }

    /*
    returns false if the new tiles are neither
    adjacent to an old tile nor on the starting tile
     */
    private boolean arePointsStartingOrAdjacent(Tile[] tiles) {

        // is any tile played on the starting tile?
        boolean isStarting = false;
        for (Tile t : tiles) {
            if (t.getLocation().getY() == 7 && t.getLocation().getX() == 7)
                isStarting = true;
        }

        // is any tile next to an already placed tile?
        boolean hasAdjacentTile = false;
        if (!isStarting) {
            for (Tile t : tiles) {
                if (hasAdjacentTile(t))
                    hasAdjacentTile = true;
            }
        }

        // did both of last two checks fail?
		return (hasAdjacentTile || isStarting);
	}

    /*
    checks that all the new tiles are within the confines of the Board
    returns false if one tile is out of bounds
     */
    private boolean arePointsInbounds(Tile[] tiles) {
        for (Tile t : tiles) {
            int x = (int) t.getLocation().getX();
            int y = (int) t.getLocation().getY();

            if (x<0 || x>BOARD_ROWS-1 || y<0 || y>BOARD_COLUMNS-1)
                return false;
        }
		return true;
    }

    /*
    helper method; checks if any of the four adjacent cells to point are occupied
    returns true if adjacent is occupied
     */
    private boolean hasAdjacentTile(Tile tile) {
        int x = (int) tile.getLocation().getX();
        int y = (int) tile.getLocation().getY();

        // Check if the adjacent cells (up, down, left, right) are occupied by non-blank tiles.
        if (x - 1 >= 0 && board[x - 1][y] != null && !board[x - 1][y].isBlank()) {
            return true;
        }
        else if (x + 1 < BOARD_ROWS && board[x + 1][y] != null && !board[x + 1][y].isBlank()) {
            return true;
        }
        else if (y - 1 >= 0 && board[x][y - 1] != null && !board[x][y - 1].isBlank()) {
            return true;
        }
        else if (y + 1 < BOARD_COLUMNS && board[x][y + 1] != null && !board[x][y + 1].isBlank()){
            return true;
        }
        else
            return false;
    }

    /*
    helper method; checks if any points have same x and y value
   Helper method; checks if any of the four adjacent cells to point are occupied
   Returns true if adjacent is occupied
	*/
    private boolean hasAdjacentTile(Point tile) {
        int x = (int) tile.getX();
        int y = (int) tile.getY();

        if (x - 1 >= 0 && board[x - 1][y] != null && !board[x - 1][y].isBlank()) { // Check the left adjacent tile
            return true;
        }
        else if (x + 1 < BOARD_ROWS && board[x + 1][y] != null && !board[x + 1][y].isBlank()) { // Check the right adjacent tile
            return true;
        }
        else if (y - 1 >= 0 && board[x][y - 1] != null && !board[x][y - 1].isBlank()) { // Check the top adjacent tile
            return true;
        }
        else if (y + 1 < BOARD_COLUMNS && board[x][y + 1] != null && !board[x][y + 1].isBlank()){ // Check the bottom adjacent tile
            return true;
        }
        else
            return false;
    }

    /*
    Helper method; checks if any points have same x and y value
    returns true if no duplicates found
    false if any two tiles share locations
     */
    private boolean hasNoDuplicates(Tile[] tiles) {
        boolean hasDuplicates = false;
        for (int i = 0; i < tiles.length - 1 && !hasDuplicates; i++) {
            for (int j = i + 1; j < tiles.length && !hasDuplicates; j++) {
                Tile tile1 = tiles[i];
                Tile tile2 = tiles[j];
                // Compare locations of the two tiles
                if (tile1.getLocation().getX() == tile2.getLocation().getX() &&
                        tile1.getLocation().getY() == tile2.getLocation().getY())
                    hasDuplicates = true;
            }
        }
        return !hasDuplicates;
    }

    /*
    checks if each tile in tiles has the same column (y) value as
    each other tile in the array
     */
    private boolean allSameCol(Tile[] tiles) {
        boolean hasSameY = true;
        for (int i = 0; i < tiles.length - 1 && hasSameY; i++) {
            // Compare the Y values of adjacent tiles
            if (tiles[i].getLocation().getY() != tiles[i+1].getLocation().getY())
                hasSameY = false;
        }
        return hasSameY;
    }

	/*
    Checks if each tile in tiles has the same row (x) value as
    each other tile in the array
     */
	private boolean allSameRow(Tile[] tiles) {
		boolean hasSameX = true;
		for (int i = 0; i < tiles.length - 1 && hasSameX; i++) {
            // Compare the X values of adjacent tiles
			if (tiles[i].getLocation().getX() != tiles[i+1].getLocation().getX())
				hasSameX = false;
		}
		return hasSameX;
	}
}