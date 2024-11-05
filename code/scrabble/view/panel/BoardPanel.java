package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
	private BoardCellPanel[][] boardCells;

	public BoardPanel(BoardCellPanel[][] boardCells) {
		this.setLayout(new GridLayout(15,15,3,3));
		this.boardCells = boardCells;
		for (int row = 0; row < boardCells.length; row++) {
			for (int col = 0; col < boardCells[0].length; col++) {
				this.add(boardCells[row][col]);
			}
		}
	}

	public void setBoardCell(JButton button, int row, int col) {
		this.boardCells[row][col].setBoardButton(button);
	}

	public BoardCellPanel getBoardCell(int row, int col) {
		return boardCells[row][col];
	}

	public int getNumXPanels() {
		return this.boardCells.length;
	}

	public int getNumYPanels() {
		return this.boardCells[0].length;
	}
}
