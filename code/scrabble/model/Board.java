package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class represents the scrabble board. It maintains the placement of
 * model.Tile objects on the board and allows for changes to be made.
 * When changes are made, model.Board scores the word(s) played and allows them to be accessed
 * in the future. Note that model.Board does not check the validity of words, only their values.
 */
public class Board {
    private Tile[][] board;  // Where scrabble.model.Tile objects are placed
    private Map<Point,ModifierType> boardSpecialCell;   // Map of modifier cells
    private ArrayList<String> lastWordsPlayed = new ArrayList<>();   // The words which have most recently been played
    private final ArrayList<String> dictionary; // Stores the Scrabble dictionary used to check work logic
    public ArrayList<String> allWordsPlayed = new ArrayList<>();
    public ArrayList<Point> newTileLocations = new ArrayList<>();
    private final String HORIZONTAL = "horizontal";
    private final String VERTICAL = "vertical";

    // Constants for the number of rows and columns on the Scrabble board
    public static final int BOARD_ROWS = 15;
    public static final int BOARD_COLUMNS = 15;

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
        Tile[] firstPlay = new Tile[4];
        firstPlay[0] = new Tile('N', new Point(7,7 ));
        firstPlay[1] = new Tile('I', new Point(7,8 ));
        firstPlay[2] = new Tile('C', new Point(7,9 ));
        firstPlay[3] = new Tile('E', new Point(7,10));

        int score = board.playTiles(firstPlay);
        System.out.println("Score:" + score);

        System.out.println(board);

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
                    while (board.board[startingRow+gap][startingColumn] != null
                                && !board.board[startingRow+gap][startingColumn].getIsNew()) {
                        gap++;  // Increment gap while board tiles exist
                    }
                    tiles[i] = new Tile(letter,
                            new Point(startingRow+gap, startingColumn));
                    gap++;
                }
                else {
                    while (board.board[startingRow][startingColumn+gap] != null
                            && !board.board[startingRow+gap][startingColumn].getIsNew()) {
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
     * Constructs a new model.Board object
     * Getter for dictionary for testing purposes
     */
    public ArrayList<String> getDictionary() {
        return dictionary;
    }

    /**
     * Getter for isValidWord for testing purposes
     */
    public boolean isValidWordCaller(Set<Point> originPoints) {
        return isValidWord(originPoints);
    }

    /**
     * Constructs a new scrabble.model.Board object
     */
    public Board() {
        initializeModifierCells();
        board = new Tile[BOARD_ROWS][BOARD_COLUMNS];
        dictionary = importDictionary();
    }

    /**
     * Getter for the last words played in the most recent move
     * @return a list of the words played on the most recent board change
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
     * Returns tiles inside of given x and y locations
     * @param x row index
     * @param y column index
     * @return the tile at the specified (x, y) position
     */
    public Tile getTile(int x, int y){
        return board[x][y];
    }

    /**
     * Removes the specified tiles from the board.
     * All tiles in parameter tiles must have been placed on the board already
     * @param tiles the tiles to remove from the board
     * @throws NullPointerException when not all tiles have already been placed
     * 								on the board
     */
    public void removeTiles(Tile[] tiles)
            throws NullPointerException {
        for (Tile tile : tiles) {
            Point p = tile.getLocation();
            int x = (int) p.getX();
            int y = (int) p.getY();

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
    public boolean hasAdjacentCaller(Tile tile){
        return hasAdjacentTile(tile);
    }

    public boolean hasAdjacentCaller(Point tile){
        return hasAdjacentTile(tile);
    }

    /**
     * This method places tiles at positions on the board and returns the score of
     * the play made
     *
     * @param tiles the tiles which are being placed on the board
     * @return the score of the word(s) played as an integer
     * 			-1 if position is fails to meet following conditions:
     * 					At least one tile must be adjacent to some other
     * 					previously placed tile, or
     *                  one of the tiles must be at the starting tile (7,7).
     *                  No tile may be placed on an already occupied cell
     *                  All tiles must be within the bounds of the board
     *                  All tiles must have the same row (x) or column (y)
     *                  All tiles must have differing points (no duplicates)
     *                  All tiles must be adjacent to each other, or must have board tiles
     *                  	in between them.
     */
    public int playTiles(Tile[] tiles) {
        if (!validatePositions(tiles))
            return -1;       // ensure positions are allowed
        int score = score(findOrigin(tiles));       // calculate score of play
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
     * {@code @returns} a String representation, with formatting as stated above
	 */
    public String toString() {
        // String representation of the board
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) {

                    // Get modifier for special cells like Double Word, Triple Word, etc.
                    ModifierType mt = boardSpecialCell.get(new Point(i, j));
                    if (mt == ModifierType.DOUBLE_WORD)
                        sb.append(" DW ");
                    else if (mt == ModifierType.TRIPLE_WORD)
                        sb.append(" TW ");
                    else if (mt == ModifierType.DOUBLE_LETTER)
                        sb.append(" DL ");
                    else if (mt == ModifierType.TRIPLE_LETTER)
                        sb.append(" TL ");
                    else
                        sb.append(" __ ");
                }
                else {
                    // Append the letter of the tile at the current position
                    sb.append(" " + board[i][j].getLetter() + "  ");
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
    Helper method which adds tiles to the board at specified points.
    Does not check scoring or validity of play.
     */
    public void addToBoard(Tile[] tiles) {
		for (Tile tile : tiles) board[(int) tile.getLocation().getX()][(int) tile.getLocation().getY()] = tile;
    }

    /*
    calculates the score of tiles played with words and modifier cells.
    returns score as an int
     */
    public int score(Tile[] originTiles) {
        int finalSum = 0;

        for (Tile originTile : originTiles) {
            Point location = originTile.getLocation();
            int row = location.x;
            int col = location.y;

            for(Point point: newTileLocations)
                board[point.x][point.y].setIsNew(false);

            // Check for horizontal words
            if(isHorizontal(row,col).equals(HORIZONTAL)) {

                for (Point point : newTileLocations)
                    board[point.x][point.y].setIsNew(true);

                finalSum += calculateWordScore(row, col, true);  // Horizontal
            }
            // Check for vertical words
            else if(isHorizontal(row,col).equals(VERTICAL)) {

                for (Point point : newTileLocations)
                    board[point.x][point.y].setIsNew(true);

                finalSum += calculateWordScore(row, col, false); // Vertical
            }
        }

        return finalSum;
    }

    //Helper method for score
    private int calculateWordScore(int row, int col, boolean isHorizontal) {
        int wordPoints = 0;
        int totalMultiplier = 1;
        boolean newWord = false;
        StringBuilder stringBuilder = new StringBuilder();

        int tempRow = row;
        int tempCol = col;
        int newTileCount = 0;
        while (board[tempRow][tempCol] != null) {
            if (board[tempRow][tempCol].getIsNew())
                ++newTileCount;

            if (isHorizontal) {
                tempCol++;
            } else {
                tempRow++;
            }
        }

        // Scan through the tiles in the desired direction
        while (board[row][col] != null) {
            Tile tile = board[row][col];
            int tileScore = tile.getScore();
            boolean isNewTile = tile.getIsNew();

            // Apply modifiers if the tile is new
            if (isNewTile) {
                newWord = true;

                // Check for modifiers
                if (boardSpecialCell.containsKey(new Point(row, col))) {
                    ModifierType modifier = boardSpecialCell.get(new Point(row, col));
                    switch (modifier) {
                        case DOUBLE_LETTER:
                            wordPoints += tileScore * 2; // Double the letter score
                            break;
                        case TRIPLE_LETTER:
                            wordPoints += tileScore * 3; // Triple the letter score
                            break;
                        case DOUBLE_WORD:
                            wordPoints += tileScore; // No change to letter score
                            totalMultiplier *= 2; // Double the word score
                            break;
                        case TRIPLE_WORD:
                            wordPoints += tileScore; // No change to letter score
                            totalMultiplier *= 3; // Triple the word score
                            break;
                    }
                    if ((newTileCount == 1 && ((isHorizontal && isWithinBounds(row+1,col) && isWithinBounds(row-1,col) && (board[row+1][col] != null || board[row-1][col] != null))
                            || (!isHorizontal && isWithinBounds(row, col+1) && isWithinBounds(row, col-1) && (board[row][col+1] != null || board[row][col-1] != null)))) ||
                            ((newTileCount > 1 && ((isHorizontal && isWithinBounds(row+1,col) && isWithinBounds(row-1,col) && (board[row+1][col] != null || board[row-1][col] != null))
                                    || (!isHorizontal && isWithinBounds(row, col+1) && isWithinBounds(row, col-1) && (board[row][col+1] != null || board[row][col-1] != null)))))){
                        newTileLocations.add(tile.getLocation());
                    }
                    else
                        tile.setIsNew(false);

                } else {
                    wordPoints += tileScore; // Normal tile without modifier

                    if(newTileCount == 1 && ((isHorizontal && isWithinBounds(row+1,col) && isWithinBounds(row-1,col) && (board[row+1][col] != null || board[row-1][col] != null))
                            || (!isHorizontal && isWithinBounds(row, col+1) && isWithinBounds(row, col-1) && (board[row][col+1] != null || board[row][col-1] != null))))
                        newTileLocations.add(tile.getLocation());
                    else
                        tile.setIsNew(false);
                }
            } else {
                wordPoints += tileScore; // Add points for existing tiles
            }

            stringBuilder.append(tile.getLetter());

            // Update row or column based on direction
            if (isHorizontal) {
                col++;
            } else {
                row++;
            }
        }
        // Update lastWordsPlayed
        lastWordsPlayed.add(stringBuilder.toString());

        // Return the final word score, applying any word multipliers
        if (newWord) {
            return wordPoints * totalMultiplier;
        } else {
            return 0; // No score if no new word was created
        }
    }

    //helper method for score
    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    //helper method for score
    private String isHorizontal(int row, int col) {
        // Check for horizontal
        for (int i = col; i < board[0].length && board[row][i] != null; i++) {
            if (board[row][i].getIsNew())
                return HORIZONTAL; // Found a new tile in a horizontal line
        }

        // Check for vertical
        for (int j = row; j < board.length && board[j][col] != null; j++) {
            if (board[j][col].getIsNew())
                return VERTICAL; // Found a new tile in a vertical line
        }

        return VERTICAL;
    }

    // Validates if a given set of points forms valid words based on the dictionary
    private boolean isValidWord(Set<Point> originPoints){
        ArrayList<String> strings = stringBuild(originPoints); // Build strings from tiles
        for(String string: strings) {
            if (!dictionary.contains(string))
                return false; // Invalid if any word isn't in the dictionary
        }
        return true;
    }

    // Helper method to build a list of words from a set of origin tiles
    public ArrayList<String> stringBuild(Set<Point> originTiles) {
        ArrayList<String> string = new ArrayList<>();
        for(Point originPoint : originTiles){
            StringBuilder tempString = new StringBuilder();
            int row = (int)originPoint.getX();
            int column = (int)originPoint.getY();

            if(isHorizontal(row,column).equals(HORIZONTAL)) {
                while (board[row][column] != null) {
                    tempString.append(board[row][column].getLetter());
                    ++column;
                }

            }
            else if (isHorizontal(row,column).equals(VERTICAL)){
                while (board[row][column] != null) {
                    tempString.append(board[row][column].getLetter());
                    ++row;
                }

            }

            if (tempString.length() > 1) {
                string.add(tempString.toString());
            }
        }
        return string;
    }

    /*
    Helper method; checks that points meet valid positions requirements of Scrabble.
    (Here, tiles are represented by the points) Checks that:
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

    /*
	Helper method which checks that all tiles are adjacent
	to each other or have gaps filled with board tiles
	 */
    private boolean arePointsConnected(Tile[] tiles) {
        // Check if they are all connected

        // Steps to check connection status:
        // Determine orientation of new tiles (vertical, horizontal)
        // Sort tiles by x or y component based on orientation
        // Start with top left tile. move to next tile, check that change is equal to 1;
        //      If the change is greater, check that in between cells on the board are occupied.
        //          Check fails if any are blank. return false
        // Repeat with remainder of the list
        // Return true if all checks clear
        if (allSameRow(tiles)) {     // Horizontal
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
        else {                      // Vertical
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
    Sorts the tiles so that each subsequent tile has a smaller
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
    Sorts the tiles so that each subsequent tile has a smaller
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
    Helper method which checks if any spaces are not blank which are for new tiles
    Returns true if play is acceptable
    False if one point already has a tile on it
     */
    private boolean pointsNotOccupied(Tile[] tiles) {
        // Are any points already occupied?
        for (Tile t : tiles) {
            if (board[(int) t.getLocation().getX()][(int) t.getLocation().getY()] != null &&
                        !board[t.getLocation().x][t.getLocation().y].getIsNew())
                return false;
        }
        return true;
    }

    /*
    Returns false if the new tiles are neither
    adjacent to an old tile nor on the starting tile
     */
    private boolean arePointsStartingOrAdjacent(Tile[] tiles) {

        // Is any tile played on the starting tile?
        boolean isStarting = false;
        for (Tile t : tiles) {
            if (t.getLocation().getY() == 7 && t.getLocation().getX() == 7)
                isStarting = true;
        }

        // Is any tile next to an already placed tile?
        boolean hasAdjacentTile = false;
        if (!isStarting) {
            for (Tile t : tiles) {
                if (hasAdjacentTile(t))
                    hasAdjacentTile = true;
            }
        }

        // Did both of last two checks fail?
        return (hasAdjacentTile || isStarting);
    }

    /*
    Checks that all the new tiles are within the confines of the Board
    Returns false if one tile is out of bounds
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
    Helper method; checks if any of the four adjacent cells to tile are occupied
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
    Checks if each tile in tiles has the same column (y) value as
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

    /*
    Takes a players chosen tiles and returns
    the top most and left most tiles of the given list
    and adds to board
    */
    public Tile[] findOrigin(Tile[] tiles) {
        Set<Tile> parentTile = new HashSet<>();
        // Adds tile to board for the purpose of finding previous tile location
        addToBoard(tiles);

        for( Tile tile : tiles){
            int row = (int)tile.getLocation().getX();
            int column = (int)tile.getLocation().getY();
            int tempRow = 0;
            int tempColumn = 0;
            // Gets new tiles top and left most row and column
            while(board[row][column] != null){
                row = row - 1;

                if(board[row][column] == null){
                    tempRow = row+1;
                    String letter = board[tempRow][column].getLetter() + "";
                }
            }
            row = (int)tile.getLocation().getX();
            while(board[row][column] != null){
                column = column - 1;

                if(board[row][column] == null){
                    tempColumn = column+1;
                    String letter = board[row][tempColumn].getLetter() + "";
                }
            }
            column = (int)tile.getLocation().getY();
            Tile top = board[tempRow][column];
            Tile left = board[row][tempColumn];
            if(!top.getLocation().equals(tile.getLocation()) ) {
                parentTile.add(top);
            }
            if(!left.getLocation().equals(tile.getLocation())){
                parentTile.add(left);
            }
        }
        // Convert the parentTile set to an array and return it
        Tile[] parent = new Tile[parentTile.size()];
        parentTile.toArray(parent);
        return parent;
    }

    /*
    This method sets up the boardSpecialCell field with all the correct placements
    for modifier cells using Point objects and model.ModifierType enumerations.
    */
    private void initializeModifierCells() {
        boardSpecialCell = new HashMap<>();
        boardSpecialCell.put(new Point(0,0), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(3,0), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(7,0), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(11,0), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(14,0), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(1,1), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(5,1), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,1), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,1), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(2,2), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(6,2), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,2), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,2), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(0,3), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(3,3), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(7,3), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(11,3), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(14,3), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(4,4), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(10,4), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(1,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(5,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(2,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(6,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(0,7), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(3,7), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(7,7), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(11,7), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(14,7), ModifierType.TRIPLE_WORD);

        boardSpecialCell.put(new Point(0,14), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(3,14), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(7,14), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(11,14), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(14,14), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(1,13), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(5,13), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,13), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,13), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(2,12), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(6,12), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,12), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,12), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(0,11), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(3,11), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(7,11), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(11,11), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(14,11), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(4,10), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(10,10), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(1,9), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(5,9), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,9), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,8), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(2,8), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(6,8), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,8), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,8), ModifierType.DOUBLE_LETTER);
    }

    /*
       Checks if all words formed by placing new tiles are valid words
       Returns false if any word is not found in the dictionary
    */
    private boolean isValidWord(Set<Point> originPoints, Tile[] newTiles, Point[] newTilePoints) {
        ArrayList<String> strings = stringBuild(originPoints);
                for(String string: strings) {
            if (!dictionary.contains(string)) {
                return false;
            }
        }
        return true;
    }

    /*
        Reads a dictionary file and imports each word into a list
        Returns the list of valid words from the dictionary
     */
    private ArrayList<String> importDictionary(){
        ArrayList<String> list = new ArrayList<>();
        try {
            File dictionary = new File("./code/dictionary.txt");
            Scanner scanner = new Scanner(dictionary);
            while(scanner.hasNext()){
                list.add(scanner.nextLine());
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}