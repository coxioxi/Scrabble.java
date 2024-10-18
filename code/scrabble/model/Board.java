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
    private Tile[][] board;  // where scrabble.model.Tile objects are placed
    private Map<Point,ModifierType> boardSpecialCell;   // map of modifier cells
    private ArrayList<String> lastWordsPlayed = new ArrayList<>();   // the words which have most recently been played
    private final ArrayList<String> dictionary;
    public ArrayList<String> allWordsPlayed = new ArrayList<>();

    public static final int BOARD_ROWS = 15;
    public static final int BOARD_COLUMNS = 15;

    public static void main(String[] args) {
        Board board = new Board();

        Tile[] tiles = new Tile[4];
        Point[] points = new Point[4];
        tiles[0] = new Tile('T');
        points[0] = new Point(7,7);
        tiles[1] = new Tile('I');
        points[1] = new Point(7,8);
        tiles[2] = new Tile('L');
        points[2] = new Point(7,9);
        tiles[3] = new Tile('E');
        points[3] = new Point(7,10);

        board.playTiles(tiles);

        System.out.println(board);
    }
    /**
     * Constructs a new model.Board object
     * getter for dictionary for testing purposes
     */
    public ArrayList<String> getDictionary() {
        return dictionary;
    }

    /**
     * getter for isValidWord for testing purposes
     */
    public boolean isValidWordCaller(Set<Point> originPoints) throws InvalidPositionException {
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
     * {@code @returns} a String representation, with formatting as stated above
	 */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) {

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
                    sb.append(" " + board[i][j].getLetter() + "  ");
                }
            }
            sb.append("\n");
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
		for (Tile tile : tiles) board[(int) tile.getLocation().getX()][(int) tile.getLocation().getY()] = tile;
    }

    /*
    helper method; calculates the score of tiles played with words and modifier cells.
    returns score as an int
    also updates lastWordsPlayed
     */
    public int score(Tile[] originTiles) {
        int finalSum = 0;

        for (Tile originTile : originTiles) {
            Point location = originTile.getLocation();
            int row = location.x;
            int col = location.y;

            // Check for both horizontal and vertical words
            if(isHorizontal(row,col))
                finalSum += calculateWordScore(row, col, true);  // Horizontal
            else
                finalSum += calculateWordScore(row, col, false); // Vertical
        }

        return finalSum;
    }

    //helper method for score
    private int calculateWordScore(int row, int col, boolean isHorizontal) {
        int wordPoints = 0;
        int totalMultiplier = 1;
        boolean newWord = false;
        StringBuilder stringBuilder = new StringBuilder();

        // Scan in the desired direction
        int startRow = row;
        int startCol = col;

        // Scan left (for horizontal) or up (for vertical) to account for previous tiles
        if (isHorizontal) {
            while (isWithinBounds(startCol - 1, row) && board[row][startCol - 1] != null) {
                startCol--;
            }
        } else {
            while (isWithinBounds(row, startRow - 1) && board[startRow - 1][col] != null) {
                startRow--;
            }
        }

        // Scan through the tiles in the desired direction
        while (isWithinBounds(row, col) && board[row][col] != null) {
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
                            wordPoints += tileScore * 2;
                            break;
                        case TRIPLE_LETTER:
                            wordPoints += tileScore * 3;
                            break;
                        case DOUBLE_WORD:
                            wordPoints += tileScore;
                            totalMultiplier *= 2;
                            break;
                        case TRIPLE_WORD:
                            wordPoints += tileScore;
                            totalMultiplier *= 3;
                            break;
                    }
                    tile.setIsNew(false);
                } else {
                    wordPoints += tileScore;
                    tile.setIsNew(false);
                }
            } else {
                wordPoints += tileScore; // Add points for existing tiles
            }

            stringBuilder.append(tile.getLetter());

            // Update row or col based on direction
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
            return 0;
        }
    }

    //helper method for score
    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    //helper method for score
    private boolean isHorizontal(int row, int col) {
        // Check for horizontal
        for (int i = col; i < board[0].length && board[row][i] != null; i++) {
            if (board[row][i].getIsNew())
                return true; // Found a new tile in a horizontal line
        }

        // Check for vertical
        for (int j = row; j < board.length && board[j][col] != null; j++) {
            if (board[j][col].getIsNew())
                return false; // Found a new tile in a vertical line
        }

        return false;
    }

    private boolean isValidWord(Set<Point> originPoints) throws InvalidPositionException {
        ArrayList<String> strings = stringBuild(originPoints);
        for(String string: strings) {
            if (!dictionary.contains(string))
                return false;
        }
        return true;
    }

    public ArrayList<String> stringBuild(Set<Point> originTiles) {
        ArrayList<String> string = new ArrayList<>();
        for(Point originPoint : originTiles){
            StringBuilder tempString = new StringBuilder();
            int row = (int)originPoint.getX();
            int column = (int)originPoint.getY();

            if(isHorizontal(row,column)) {
                while (board[row][column] != null) {
                    tempString.append(board[row][column].getLetter());
                    ++column;
                }

			}
            else{
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
    helper method; checks that points meet valid positions requirements of Scrabble.
    (here, tiles are represented by the points) checks that:
        no tiles are placed in occupied cell
        1 tile is placed on starting tile
            or 1 tile is adjacent to already placed tile
        all tiles are connected, either by adjacency, or adjacency to adjacency
     */
    private boolean validatePositions(Tile[] tiles) {
        return (arePointsInbounds(tiles) &&
                (allSameRow(tiles) || allSameCol(tiles)) &&
                hasNoDuplicates(tiles) &&
                pointsNotOccupied(tiles) &&
                arePointsStartingOrAdjacent(tiles) &&
                arePointsConnected(tiles));
    }

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
    helper method; checks if any of the four adjacent cells to tile are occupied
    returns true if adjacent is occupied
     */
    private boolean hasAdjacentTile(Tile tile) {
        int x = (int) tile.getLocation().getX();
        int y = (int) tile.getLocation().getY();

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
   helper method; checks if any of the four adjacent cells to point are occupied
   returns true if adjacent is occupied
	*/
    private boolean hasAdjacentTile(Point tile) {
        int x = (int) tile.getX();
        int y = (int) tile.getY();

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
    returns true if no duplicates found
    false if any two tiles share locations
     */
    private boolean hasNoDuplicates(Tile[] tiles) {
        boolean hasDuplicates = false;
        for (int i = 0; i < tiles.length - 1 && !hasDuplicates; i++) {
            for (int j = i + 1; j < tiles.length && !hasDuplicates; j++) {
                Tile tile1 = tiles[i];
                Tile tile2 = tiles[j];
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
            if (tiles[i].getLocation().getY() != tiles[i+1].getLocation().getY())
                hasSameY = false;
        }
        return hasSameY;
    }

	/*
    checks if each tile in tiles has the same row (x) value as
    each other tile in the array
     */
	private boolean allSameRow(Tile[] tiles) {
		boolean hasSameX = true;
		for (int i = 0; i < tiles.length - 1 && hasSameX; i++) {
			if (tiles[i].getLocation().getX() != tiles[i+1].getLocation().getX())
				hasSameX = false;
		}
		return hasSameX;
	}

    /*
    takes a players chosen tiles and returns
    the top most and left most tiles of the given list
    and adds to board
    */
    public Tile[] findOrigin(Tile[] tiles)
            throws InvalidPositionException {
        Set<Tile> parentTile = new HashSet<>();
        //adds tile to board for the purpose of finding previous tile location
        addToBoard(tiles);

        for( Tile tile : tiles){
            int row = (int)tile.getLocation().getX();
            int column = (int)tile.getLocation().getY();
            int tempRow = 0;
            int tempColumn = 0;
            //gets new tiles top and left most row and column
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
        //removeTiles(tiles);
        Tile[] parent = new Tile[parentTile.size()];

        parentTile.toArray(parent);
        return parent;
    }

    /*
    this method sets up the boardSpecialCell field with all the correct placements
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

    private boolean isValidWord(Set<Point> originPoints, Tile[] newTiles, Point[] newTilePoints) {
        ArrayList<String> strings = stringBuild(originPoints);
                for(String string: strings) {
            if (!dictionary.contains(string)) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> importDictionary(){
        ArrayList<String> list = new ArrayList<>();
        try{
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
//    public boolean isValid(Set<Tile> tiles){
//        //take top and left most tile and run down and right creating word
//        ArrayList<String> strings = stringBuild(tiles);
//        for(String string: strings) {
//            if (!dictionary.contains(string)) {
//                return false;
//            }
//        }
//        return true;
//    }
//    /*
//    constructs string from tile set
//     */
//    public ArrayList<String> stringBuild(Set<Tile> tiles){
//        //take top and left most tile and run down and right creating word
//        ArrayList<String> string = new ArrayList<>();
//        for(Tile tile : tiles){
//            String tempString = "";
//            int row = (int)tile.getLocation().getX();
//            int column = (int)tile.getLocation().getY();
//
//            while(board[row][column] != null){
//                tempString += board[row][column].getLetter();
//                row = row + 1;
//            }
//            row = (int)tile.getLocation().getX();
//            if(tempString.length() > 1){
//                string.add(tempString);
//            }
//            tempString = "";
//            while(board[row][column] != null){
//                tempString += board[row][column].getLetter();
//                column = column + 1;
//            }
//            if(tempString.length() > 1){
//                string.add(tempString);
//            }
//        }
//        return string;
//
//    }
 /*

    public model.Board(){
        board = new model.Tile[15][15];
        int point1 = 1;
        int point2 = 2;
        int point3 = 3;
        int point4 = 4;
        int point5 = 5;
        int point6 = 8;
        int point7 = 10;
        int point0 = 0;
        letterKeyValue.put(" ",point0);
        letterKeyValue.put("A",point1);
        letterKeyValue.put("E",point1);
        letterKeyValue.put("I",point1);
        letterKeyValue.put("L",point1);
        letterKeyValue.put("N",point1);
        letterKeyValue.put("O",point1);
        letterKeyValue.put("U",point1);
        letterKeyValue.put("S",point1);
        letterKeyValue.put("T",point1);
        letterKeyValue.put("R",point1);
        letterKeyValue.put("D",point2);
        letterKeyValue.put("G",point2);
        letterKeyValue.put("B",point3);
        letterKeyValue.put("C",point3);
        letterKeyValue.put("M",point3);
        letterKeyValue.put("P",point3);
        letterKeyValue.put("F",point4);
        letterKeyValue.put("H",point4);
        letterKeyValue.put("V",point4);
        letterKeyValue.put("W",point4);
        letterKeyValue.put("Y",point4);
        letterKeyValue.put("K",point5);
        letterKeyValue.put("J",point6);
        letterKeyValue.put("X",point6);
        letterKeyValue.put("Q",point7);
        letterKeyValue.put("Z",point7);

    }

    public void addToBoard(String letter, int row, int column) {
        if (row >= 0 && row < 15 && column >= 0 && column < 15 && board[row][column] == null) {
            board[row][column] = new model.Tile(letter.toUpperCase(),letterKeyValue.get(letter.toUpperCase()), new Point(row,column));
        } else {
            System.out.println("Invalid position or tile already exists at (" + row + ", " + column + ").");
        }
    }

    public static void main(String[] args) {
        model.Board test = new model.Board();
        test.addToBoard("r",5,7);
        test.addToBoard("u",6,7);
        test.addToBoard("n",7,7);
        test.addToBoard("i",7,8);
        test.addToBoard("g",7,9);
        test.addToBoard("h",7,10);
        test.addToBoard("t",7,11);
        test.addToBoard("k",7,6);
        test.addToBoard("a",8,11);
        test.addToBoard("l",9,11);
        test.addToBoard("k",10,11);
        test.addToBoard("t",8,10);
        test.boardView();
        test.boardScan();
        System.out.println();
    }

    public void boardView(){
        for(int row = 0; row < board.length; ++row){
            for(int column = 0; column < board[row].length; ++column){
                if(board[row][column] != null){
                    System.out.print(" "+board[row][column].getLetter()+" ");
                }
                else{
                    System.out.print(" * ");
                }
            }
            System.out.println();
        }
    }
    public void boardScan(){
        List<model.Tile> tiles = new ArrayList<>();
        List<List<model.Tile>> words = new ArrayList<>();
        List<String> newWords = new ArrayList<>();
        for(int row = 0; row < board.length; ++row){
            for(int column = 0; column < board[row].length; ++column){
                if(board[row][column] != null){
                    tiles.add(board[row][column]);
                }
            }
        }
        //
        for(int i = 0; i < tiles.size() - 1; ++i){
            List<model.Tile> tempTiles = new ArrayList<>();
            for(int j = 0; j < tiles.size(); ++j){
                if ((tiles.get(i).getLocation().getX() == tiles.get(j).getLocation().getX()) || (tiles.get(i).getLocation().getY() == tiles.get(j).getLocation().getY())) {
                    tempTiles.add(tiles.get(j));
                }
            }

            String tempString ="";
            for (int k = 0; k < tempTiles.size(); k++) {
                if(tempTiles.get(tempTiles.size() - 1).getLocation().getX() == tempTiles.get(0).getLocation().getX() || tempTiles.get(tempTiles.size() - 1).getLocation().getY() == tempTiles.get(0).getLocation().getY() )
                    tempString += tempTiles.get(k).getLetter();
            }

            if (newWords.isEmpty() && !tempString.isEmpty()) {
                newWords.add(tempString);
            } else if (!newWords.contains(tempString)&& !tempString.isEmpty()) {
                newWords.add(tempString);
            }
        }

        for (int i = 0; i < newWords.size(); i++) {
            System.out.println(newWords.get(i));
        }
    }
  */