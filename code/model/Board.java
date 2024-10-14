package model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.util.*;

/**
 * This class represents the scrabble board. It maintains the placement of
 * model.Tile objects on the board and allows for changes to be made.
 * When changes are made, model.Board scores the word(s) played and allows them to be accessed
 * in the future. Note that model.Board does not check the validity of words, only their values.
 */
public class Board {
    private Tile[][] board;  // where model.Tile objects are placed
    private Map<Point,ModifierType> boardSpecialCell;   // map of modifier cells
    private ArrayList<String> lastWordsPlayed = new ArrayList<>();   // the words which have most recently been played


    public static final int BOARD_ROWS = 15;
    public static final int BOARD_COLUMNS = 15;

    public static void main(String[] args) {
        Board board = new Board();

        Tile[] firstPlay = new Tile[4];
        firstPlay[0] = new Tile('T', new Point(7,7));
        firstPlay[1] = new Tile('I', new Point(7,8));
        firstPlay[2] = new Tile('L', new Point(7,9));
        firstPlay[3] = new Tile('E', new Point(7,10));

        try {
            int score = board.playTiles(firstPlay);
            System.out.println("Score:" + score);
        }
        catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        System.out.println(board);



        Scanner in = new Scanner (System.in);
        System.out.print("Make a play (Y) or quit (Q): ");
        char userInput = in.next().toUpperCase().charAt(0);
        while (userInput != 'Q') {
            System.out.println();
            System.out.println("Starting point");
            System.out.print("\tEnter row (integer): ");
            int startingRow = in.nextInt();
            System.out.print("\tEnter column (integer): ");
            int startingColumn = in.nextInt();

            System.out.println();
            System.out.println("Orientation");
            System.out.print("\tEnter \'V\' for vertical, \'H\' for horizontal: ");
            boolean isVertical = 'V' == in.next().toUpperCase().charAt(0);

            System.out.println();
            System.out.print("Enter letters on this line: ");
            char[] letters = in.next().trim().toUpperCase().toCharArray();


            Tile[] tiles = new Tile[letters.length];
            int gap = 0;
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

            boolean success = true;
            int score = 0;
            try {
                score = board.playTiles(tiles);
            } catch (InvalidPositionException e) {
                System.out.println("Play unsuccessful: "
                        + e.getMessage());
                success = false;
            }

            if (success) {
                System.out.println("Score: " + score);
                System.out.println("Words made: ");
                Iterator<String> wordsPlayed = board.lastWordsPlayed.iterator();
                while (wordsPlayed.hasNext()) {
                    System.out.println("\t" + wordsPlayed.next());
                }
                System.out.println();

                System.out.print("Show board? (\'Y\' or \'N\'): ");
                boolean showBoard = 'Y' == in.next().toUpperCase().charAt(0);
                System.out.println();

                if (showBoard)
                    System.out.println(board);
                System.out.println();
            }

            System.out.print("Make a play (Y) or quit (Q): ");
            userInput = in.next().toUpperCase().charAt(0);
        }
    }

    /**
     * Constructs a new model.Board object
     */
    public Board() {
        initializeModifierCells();
        board = new Tile[BOARD_ROWS][BOARD_COLUMNS];
    }

    /**
     *
     * @return an array of the words played on the most recent board change
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

    public void removeTiles(Tile[] tiles, Point[] points) {
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            int x = (int) p.getX();
            int y = (int) p.getY();

            // check if position is null, then check letter value
            if (board[x][y] != null && board[x][y].getLetter() == tiles[i].getLetter()) {
                board[x][y] = null;
            }
            else {
                throw new NullPointerException(
                        "No tile on board position"
                );
            }
        }
    }

    /**
     * A caller method for testing purposes
     */
    public boolean hasAdjacentCaller(Tile t){
        return hasAdjacentTile(t);
    }

    /**
     * This method places tiles at positions on the board and returns the score of
     * the play made
     *
     * @param tiles the tiles which are being placed on the board
     *               Note the array may not be empty, but arrays of size 1 are allowed.
     * @return the score of the word(s) played as an integer
     * @throws InvalidPositionException when placed incorrectly. At least one tile
     *                  must be adjacent to some other previously placed tile, or
     *                  one of the tiles must be at the starting tile (7,7).
     *                  No tile may be placed on an already occupied cell
     */
    public int playTiles(Tile[] tiles)
            throws InvalidPositionException{
        validatePositions(tiles);       // ensure positions are allowed
        int score = score(tiles);       // calculate score of play
        addToBoard(tiles);              // add to board
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
    public void addToBoard(Tile[] tiles) throws InvalidPositionException {
        for(int i = 0; i < tiles.length; ++i)
            board[(int) tiles[i].getLocation().getX()][(int) tiles[i].getLocation().getY()] = tiles[i];
    }


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
        int turnScore = 0;
        ArrayList<String> words = new ArrayList<>();    // stores words made from tiles placed on board
        ScoreData collateralWords, mainWord;
        boolean isVertical = allSameCol(tiles);           // check verticality
        if (isVertical) {       // Vertical tiles
            tiles = sortAscendingByRow(tiles);       // sort by row

            mainWord = getVerticalMainWordScore(tiles);       // score word made by tiles
            collateralWords = getVerticalCollateralWordsScore(tiles);
        }
        else {              // Horizontal tiles
            tiles = sortAscendingByCol(tiles);       // sort by column

            mainWord = getHorizontalMainWordScore(tiles);     // score word made by tiles
            collateralWords = getHorizontalCollateralWordsScore(tiles);       // score perpendicular words
        }
        // Add main word to word list
        Iterator<String> mainIterator = mainWord.getWords().iterator();
        System.out.println("\tMain word: ");
        while (mainIterator.hasNext()) {
            String next = mainIterator.next();
            System.out.println("\t" + next);
            words.add(next);
        }

        // Add collateral words to word list
        Iterator<String> collateralIterator = collateralWords.getWords().iterator();
        System.out.println("\tCollateral Words: ");
        while (collateralIterator.hasNext()) {
            String next = collateralIterator.next();
            System.out.println("\t" + next);
            words.add(next);
        }

        turnScore = collateralWords.getScore() + mainWord.getScore();       // update turn score

        lastWordsPlayed = words;
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
        for (int i = 0; i < tiles.length; i++) {
            // initialize variables
            StringBuilder currentWord = new StringBuilder();
            Tile current = tiles[i];
            Point placement = current.getLocation();
            int x = placement.x;
            int y = placement.y;

            // x = row
            // y = column

            // for this tile, find the topmost connected tile
            int topMostTile = x;
            while (topMostTile-1 >= 0 && board[topMostTile-1][y] != null) {
                topMostTile--;
            }

            System.out.println("Horizontal collateral. topmost for tile " +
                    current.getLocation().x + ", " + current.getLocation().y +
                    " is location " + topMostTile + " with value " +
                    (board[topMostTile][y] == null ? "none" : board[topMostTile][y].getLetter()));

            // move down from topmost, accumulating score.
            // only stop when no letters below current tile
            int currentWordScore = 0;
            while (topMostTile < BOARD_ROWS &&
                    (board[topMostTile][y] != null || x == topMostTile) )
            {
                Tile boardTile = board[topMostTile][y];
                if (boardTile != null) {        // if topMost is position on board
                    currentWordScore += boardTile.getScore();
                    currentWord.append(boardTile.getLetter());
                }
                else {                          // if topMost is tile to be placed
                    ModifierType cellMod = boardSpecialCell.get(placement);
                    int letterMultiplier = 1;
                    if (cellMod == ModifierType.DOUBLE_LETTER) {
                        letterMultiplier *= 2;
                    }
                    else if (cellMod == ModifierType.TRIPLE_LETTER) {
                        letterMultiplier *= 3;
                    }
                    else if (cellMod == ModifierType.DOUBLE_WORD) {
                        wordMultiplier *= 2;
                    }
                    else if (cellMod == ModifierType.TRIPLE_WORD) {
                        wordMultiplier *= 3;
                    }
                    currentWordScore += letterMultiplier*current.getScore();
                    currentWord.append(current.getLetter());
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
            ModifierType cellMod = boardSpecialCell.get(placement);
            int letterMultiplier = 1;
            if (cellMod == ModifierType.DOUBLE_LETTER) {
                letterMultiplier *= 2;
            }
            else if (cellMod == ModifierType.TRIPLE_LETTER) {
                letterMultiplier *= 3;
            }
            else if (cellMod == ModifierType.DOUBLE_WORD) {
                wordMultiplier *= 2;
            }
            else if (cellMod == ModifierType.TRIPLE_WORD) {
                wordMultiplier *= 3;
            }
            mainWordScore += letterMultiplier*current.getScore();
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
        for (int i = 0; i < tiles.length; i++) {
            // initialize variables
            StringBuilder currentWord = new StringBuilder();
            Tile current = tiles[i];
            Point placement = current.getLocation();
            int x = placement.x;
            int y = placement.y;

            // x = row
            // y = column

            // for this tile, find the leftmost connected tile
            int leftMostTile = y;
            while (leftMostTile-1 >= 0 && board[x][leftMostTile-1] != null) {
                leftMostTile--;
            }

            System.out.println("Vertical collateral. leftmost for tile " +
                    current.getLocation().x + ", " + current.getLocation().y +
                    " is location " + leftMostTile + " with value " +
                    (board[x][leftMostTile] == null ? "none" : board[x][leftMostTile].getLetter()));
            // move right from leftmost, accumulating score.
            // only stop when no letters to right of current
            int currentWordScore = 0;
            while (leftMostTile < BOARD_COLUMNS &&
                    (board[x][leftMostTile] != null || y == leftMostTile) )
            {
                Tile boardTile = board[x][leftMostTile];
                if (boardTile != null) {        // if leftMost is position on board
                    currentWordScore += boardTile.getScore();
                    currentWord.append(boardTile.getLetter());
                }
                else {                          // if leftMost is tile to be placed
                    ModifierType cellMod = boardSpecialCell.get(placement);
                    int letterMultiplier = 1;
                    if (cellMod == ModifierType.DOUBLE_LETTER) {
                        letterMultiplier *= 2;
                    }
                    else if (cellMod == ModifierType.TRIPLE_LETTER) {
                        letterMultiplier *= 3;
                    }
                    else if (cellMod == ModifierType.DOUBLE_WORD) {
                        wordMultiplier *= 2;
                    }
                    else if (cellMod == ModifierType.TRIPLE_WORD) {
                        wordMultiplier *= 3;
                    }
                    currentWordScore += letterMultiplier*current.getScore();
                    currentWord.append(current.getLetter());
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
            ModifierType cellMod = boardSpecialCell.get(placement);
            System.out.println(cellMod);
            int letterMultiplier = 1;
            if (cellMod == ModifierType.DOUBLE_LETTER) {
                letterMultiplier *= 2;
            }
            else if (cellMod == ModifierType.TRIPLE_LETTER) {
                letterMultiplier *= 3;
            }
            else if (cellMod == ModifierType.DOUBLE_WORD) {
                wordMultiplier *= 2;
            }
            else if (cellMod == ModifierType.TRIPLE_WORD) {
                wordMultiplier *= 3;
                mainWordScore += current.getScore();
            }
            mainWordScore += letterMultiplier*current.getScore();
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
        System.out.println("Word multiplier: " + wordMultiplier);
        mainWordScore *= wordMultiplier;

        // add this main word to string list
        ArrayList<String> words = new ArrayList<>();
        words.add(mainWordString.toString());

        return new ScoreData(words, mainWordScore);
    }

    private Tile[] sortAscendingByCol(Tile[] tiles) {
        for (int i = 0; i < tiles.length-1; i++) {
            for (int j = i; j < tiles.length-1; j++) {
                if (tiles[j].getLocation().y > tiles[j+1].getLocation().y) {
                    Tile temp = tiles[j];
                    tiles[j] = tiles[j+1];
                    tiles[j+1] = temp;
                }
            }
        }
        return tiles;
    }

    private Tile[] sortAscendingByRow(Tile[] tiles) {
        for (int i = 0; i < tiles.length-1; i++) {
            for (int j = i; j < tiles.length-1; j++) {
                if (tiles[j].getLocation().x > tiles[j+1].getLocation().x) {
                    Tile temp = tiles[j];
                    tiles[j] = tiles[j+1];
                    tiles[j+1] = temp;
                }
            }
        }
        return tiles;
    }

    public ArrayList<String> stringBuild(Set<Point> originTiles, Tile[] newTiles, Point[] newTilePoints) throws InvalidPositionException {
        ArrayList<String> string = new ArrayList<>();
        for(Point originPoint : originTiles){
            String tempString = "";
            int row = (int)originPoint.getX();
            int column = (int)originPoint.getX();

            while(board[row][column] != null){
                tempString += board[row][column].getLetter();
                row = row + 1;
            }
            row = (int)originPoint.getX();
            if(tempString.length() > 1){
                string.add(tempString);
            }
            tempString = "";
            while(board[row][column] != null){
                tempString += board[row][column].getLetter();
                column = column + 1;
            }
            if(tempString.length() > 1){
                string.add(tempString);
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
        pointsInbounds(tiles);
        if (!allSameRow(tiles) && !allSameCol(tiles)) throw new InvalidPositionException(
                "Illegal orientation: not all tiles are in a line"
        );
        hasDuplicates(tiles);
        areAnyPointsOccupied(tiles);
        arePointsStartingOrAdjacent(tiles);
        arePointsConnected(tiles);
    }

    private void arePointsConnected(Tile[] tiles)
            throws InvalidPositionException {
        // check if they are all connected

        // steps to check connection status:
        // determine orientation of new tiles (vertical, horizontal)
        // sort tiles by x or y component based on orientation
        // start with top left tile. move to next tile, check that change is equal to 1;
        //      if the change is greater, check that in between cells on the board are occupied.
        //          check fails if any are blank
        // repeat with remainder of the list
        // return out of method if algorithm finishes list with no problems.
        if (allSameRow(tiles)) {     // horizontal
            tiles = sortAscendingByCol(tiles);
            for (int i = 1; i < tiles.length; i++) {
                int oldY = tiles[i-1].getLocation().y;
                int currentY = tiles[i].getLocation().y;
                for (int j = oldY + 1; j < currentY; j++) {
                    if (board[tiles[0].getLocation().x][j] == null) {
                        throw new InvalidPositionException(
                                "Not all tiles are connected"
                        );
                    }
                }
            }
        }
        else {                      // vertical
            tiles = sortAscendingByRow(tiles);
            for (int i = 1; i < tiles.length; i++) {
                int oldX = tiles[i-1].getLocation().x;
                int currentX = tiles[i].getLocation().x;
                for (int j = oldX + 1; j < currentX; j++) {
                    if (board[j][tiles[0].getLocation().y] == null) {
                        throw new InvalidPositionException(
                                "Not all tiles are connected"
                        );
                    }
                }
            }
        }
    }

    /*
    helper method which checks if any spaces are not blank which are for new tiles
     */
    private void areAnyPointsOccupied(Tile[] tiles)
            throws InvalidPositionException {
        boolean areValid = true;

        // are any points already occupied?
        for (Tile t : tiles) {
            if (board[(int) t.getLocation().getX()][(int) t.getLocation().getY()] != null)
                throw new InvalidPositionException(
                        "Illegal placement: some cells are already occupied"
                );
        }
    }

    /*
    throws an invalidpositionexception if the new tiles are neither
    adjacent to an old tile nor on the starting tile
     */
    private void arePointsStartingOrAdjacent(Tile[] tiles)
            throws InvalidPositionException {

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

        // did both of last two checks fail? throw an exception
        if (!hasAdjacentTile && !isStarting)
            throw new InvalidPositionException(
                    "Invalid placement: not adjacent to a cell and not starting"
            );
    }

    /*
    checks that all the new tiles are within the confines of the Board
     */
    private void pointsInbounds(Tile[] tiles)
            throws InvalidPositionException {
        for (Tile t : tiles) {
            int x = (int) t.getLocation().getX();
            int y = (int) t.getLocation().getY();

            if (x<0 || x>BOARD_ROWS-1 || y<0 || y>BOARD_COLUMNS-1)
                throw new InvalidPositionException(
                        "Tiles must be placed between 0 and 14 x and y"
                );
        }
    }

    /*
    helper method; checks if any of the four adjacent cells to point are occupied
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
    helper method; checks if any points have same x and y value
    throws exception if duplicates found
     */
    private void hasDuplicates(Tile[] tiles) throws InvalidPositionException {
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
        if (hasDuplicates)
            throw new InvalidPositionException(
                    "Duplicate locations are not allowed"
            );
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

}
