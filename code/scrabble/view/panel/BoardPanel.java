package scrabble.view.panel;

import scrabble.model.Board;
import scrabble.model.ModifierType;
import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
	public static final Color DOUBLE_WORD_COLOR = new Color(255, 102, 102);
	public static final Color TRIPLE_WORD_COLOR = new Color(255, 0, 0);
	public static final Color DOUBLE_LETTER_COLOR = new Color(88, 117, 255);
	public static final Color TRIPLE_LETTER_COLOR = new Color(0, 41, 255);
	public static final Color NORMAL_CELL_COLOR = new Color(255, 255, 255);

	public static final String DOUBLE_WORD_TEXT = "DW";
	public static final String TRIPLE_WORD_TEXT = "TW";
	public static final String DOUBLE_LETTER_TEXT = "DL";
	public static final String TRIPLE_LETTER_TEXT = "TL";
	public static final String NORMAL_CELL_TEXT = "";

	private BoardCellPanel[][] boardCells;

	public BoardPanel() {
		this.setLayout(new GridLayout(15,15,0,0));
		setupBoardCellPanels();
	}

	private void setupBoardCellPanels() {
		boardCells = new BoardCellPanel[Board.BOARD_ROWS][Board.BOARD_COLUMNS];
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				JButton cell = new JButton();
				setColorAndText(cell, row, col);
				boardCells[row][col] = new BoardCellPanel(cell);
				this.add(boardCells[row][col]);
			}
		}
	}

	private void setColorAndText(JButton button, int row, int col) {
		ModifierType mt = Board.MODIFIER_HASH_MAP.get(new Point(row, col));
		button.setBackground(getColor(mt));
		button.setText(getText(mt));
		button.setBorderPainted(false);
	}

	private Color getColor(ModifierType mt) {
		Color color;
		if (mt == ModifierType.DOUBLE_WORD) {
			color = DOUBLE_WORD_COLOR;
		} else if (mt == ModifierType.TRIPLE_WORD) {
			color = TRIPLE_WORD_COLOR;
		} else if (mt == ModifierType.DOUBLE_LETTER) {
			color = DOUBLE_LETTER_COLOR;
		} else if (mt == ModifierType.TRIPLE_LETTER) {
			color = TRIPLE_LETTER_COLOR;
		} else {
			color = NORMAL_CELL_COLOR;
		}
		return color;
	}

	private String getText(ModifierType mt) {
		String text;
		if (mt == ModifierType.DOUBLE_WORD) {
			text = DOUBLE_WORD_TEXT;
		} else if (mt == ModifierType.TRIPLE_WORD) {
			text = TRIPLE_WORD_TEXT;
		} else if (mt == ModifierType.DOUBLE_LETTER) {
			text = DOUBLE_LETTER_TEXT;
		} else if (mt == ModifierType.TRIPLE_LETTER) {
			text = TRIPLE_LETTER_TEXT;
		} else {
			text = NORMAL_CELL_TEXT;
		}
		return text;
	}

	public void setBoardCell(JButton button, int row, int col) {
		if (!(button instanceof TileButton)) {
			setColorAndText(button, row, col);
		}
		this.boardCells[row][col].setBoardButton(button);
	}

	public BoardCellPanel getBoardCell(int row, int col) {
		return boardCells[row][col];
	}
}
