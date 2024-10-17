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
 * scrabble.model.Tile objects on the board and allows for changes to be made.
 * When changes are made, scrabble.model.Board scores the word(s) played and allows them to be accessed
 * in the future. Note that scrabble.model.Board does not check the validity of words, only their values.
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

        try {
            board.playTiles(tiles, points);

        }
        catch (InvalidPositionException e) {
            e.printStackTrace();
        }

        System.out.println(board);
    }
    /**
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
     * Returns tile inside of given x and y locations
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
    public boolean hasAdjacentCaller(Point location){
        return hasAdjacentTile(location);
    }

    /**
     * This method places tiles at positions on the board and returns the score of
     * the play made
     *
     * @param tiles the tiles which are being placed on the board
     * @return the score of the word(s) played as an integer
     * @throws InvalidPositionException when placed incorrectly. At least one tile
     *                  must be adjacent to some other previously placed tile, or
     *                  one of the tiles must be at the starting tile (7,7).
     *                  No tile may be placed on an already occupied cell
     */
    public int playTiles(Tile[] tiles) throws InvalidPositionException{
        int score;
        sameXorY(tiles);
        hasDuplicates(tiles);
        //validatePositions(tiles);  // half implemented
        score = score(findOrigin(tiles));
        addToBoard(tiles);
        allWordsPlayed.addAll(lastWordsPlayed);

        return score;
    }

    @Override
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
        for (Tile tile : tiles)
            board[tile.getLocation().x][tile.getLocation().y] = tile;
    }

    /*
    helper method; calculates the score of tiles played with words and modifier cells.
    returns score as an int
    also updates lastWordsPlayed, so it should only be called when the positions have been
    validated through all necessary methods.
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

                if (tempString.length() > 1) {
                    string.add(tempString.toString());
                }
            }
            else{
                while (board[row][column] != null) {
                    tempString.append(board[row][column].getLetter());
                    ++row;
                }

                if (tempString.length() > 1) {
                    string.add(tempString.toString());
                }
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
    private void validatePositions(Tile[] tiles)
            throws InvalidPositionException {

        //TODO: add check that all tiles are connected
        //  this means that all tiles are next to each other, or separated
        //  by an already placed tile. There may not be gaps.
        boolean areValid = true;

        // are any points already occupied?
        for (Tile tile: tiles) {
            if (board[(int) tile.getLocation().getX()][(int) tile.getLocation().getY()] != null)
                throw new InvalidPositionException(
                        "Illegal placement: some cells are already occupied"
                );
        }

        // is any tile played on the starting tile?
        boolean isStarting = false;
        for (Tile tile: tiles) {
            if ((int) tile.getLocation().getY() == 7 && (int) tile.getLocation().getX() == 7)
                isStarting = true;
        }

        // is any tile next to an already placed tile?
        boolean hasAdjacentTile = false;
        if (!isStarting) {
            for (Tile tile: tiles) {
                if (hasAdjacentTile(new Point((int)tile.getLocation().getX(),(int)tile.getLocation().getY())))
                    hasAdjacentTile = true;
            }
        }

        // did both of last two checks fail? throw an exception
        if (!hasAdjacentTile && !isStarting)
            throw new InvalidPositionException(
                    "Invalid placement: not adjacent to a cell and not starting"
            );

        // check if they are all connected

        // steps to check connection status:
        // determine orientation of new tiles (vertical, horizontal)
        // sort tiles by x or y component based on orientation
        // start with top left tile. move to next tile, check that change is equal to 1;
        //      if the change is greater, check that in between cells on the board are occupied.
        //          check fails if any are blank
        // repeat with remainder of the list
        // return out of method if algorithm finishes list with no problems.

    }

    /*
    helper method; checks if any of the four adjacent cells to point are occupied
    returns true if adjacent is occupied
     */
    private boolean hasAdjacentTile(Point location) {
        int x = location.x;
        int y = location.y;

        if (x - 1 >= 0 && board[x - 1][y] != null && !board[x - 1][y].isBlank()) {
            return true;
        }
        else if (x + 1 < BOARD_COLUMNS && board[x + 1][y] != null && !board[x + 1][y].isBlank()) {
            return true;
        }
        else if (y - 1 >= 0 && board[x][y - 1] != null && !board[x][y - 1].isBlank()) {
            return true;
        }
        else if (y + 1 < BOARD_ROWS && board[x][y + 1] != null && !board[x][y + 1].isBlank()){
            return true;
        }
        else
            return false;
    }

    /*
    helper method; checks if any points have same x and y value
    throws exception if duplicates found
     */
    private void hasDuplicates(Tile[] tiles)
            throws InvalidPositionException {
        boolean hasDuplicates = false;
        for (int i = 0; i < tiles.length - 1 && !hasDuplicates; i++) {
            for (int j = i + 1; j < tiles.length && !hasDuplicates; j++) {
                Point point1 = tiles[i].getLocation();
                Point point2 = tiles[j].getLocation();
                if (point1.getX() == point2.getX() && point1.getY() == point2.getY())
                    hasDuplicates = true;
            }
        }
        if (hasDuplicates)
            throw new InvalidPositionException(
                    "Duplicate locations are not allowed"
            );
    }

    /*
    helper method; checks that all points have either same x or y value
    throws exception if points are not in a line.
     */
    private void sameXorY(Tile[] tiles)
            throws InvalidPositionException {
        boolean hasSameX = true;
        boolean hasSameY = true;
        for (int i = 0; i < tiles.length - 1 && (hasSameX || hasSameY); i++) {
            if (tiles[i].getLocation().getX() != tiles[i+1].getLocation().getX())
                hasSameX = false;
            if (tiles[i].getLocation().getY() != tiles[i+1].getLocation().getY())
                hasSameY = false;
        }
        if (!(hasSameX || hasSameY))
            throw new InvalidPositionException(
                    "Illegal orientation: not all tiles are in a line"
            );
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
    for modifier cells using Point objects and scrabble.model.ModifierType enumerations.
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
}