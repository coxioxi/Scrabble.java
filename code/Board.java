import java.lang.reflect.Array;

public class Board {
    public static void main(String[] args) {
        Array[][] board = new Array[15][15];

    }
    public Array addToBoard(Array[][] board, Tile letter, int row, int column) {
        return board[row][column] = letter;
    }

}
