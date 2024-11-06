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

	public static final float MAXIMUM_PANEL_SIZE_PERCENT = .55f;
	public static final float PREFERRED_PANEL_SIZE_PERCENT = .4f;
	public static final float MINIMUM_PANEL_SIZE_PERCENT = .2f;
	public static final int SPACING = 3;
	public static final float MAXIMUM_CELL_PERCENT = .04f;
	public static final float PREFERRED_CELL_PERCENT = .03f;
	public static final float MINIMUM_CELL_PERCENT = .02f;


	private BoardCellPanel[][] boardCells;
	private int environmentHeight;
	private int environmentWidth;

	private Dimension maxPanelSize, preferredPanelSize, minPanelSize,
				maxCellSize, preferredCellSize, minCellSize;

	public BoardPanel() {
		try {
			// Set the look and feel to the system's default
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException |
				 IllegalAccessException | UnsupportedLookAndFeelException ignore) {}

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		environmentWidth = gd.getDisplayMode().getWidth();
		environmentHeight = gd.getDisplayMode().getHeight();

		setupDimensions();

		this.setPreferredSize(preferredPanelSize);
		this.setMaximumSize(maxPanelSize);

		this.setLayout(new GridLayout(Board.BOARD_ROWS,Board.BOARD_COLUMNS,SPACING,SPACING));
		setupBoardCellPanels();
	}

	private void setupDimensions() {
		minPanelSize = new Dimension((int)(MINIMUM_PANEL_SIZE_PERCENT*environmentHeight),
				(int)(MINIMUM_PANEL_SIZE_PERCENT*environmentHeight));
		preferredPanelSize = new Dimension((int)(PREFERRED_PANEL_SIZE_PERCENT*environmentHeight),
				(int)(PREFERRED_PANEL_SIZE_PERCENT*environmentHeight));
		maxPanelSize = new Dimension((int)(MAXIMUM_PANEL_SIZE_PERCENT*environmentHeight),
				(int)(MAXIMUM_PANEL_SIZE_PERCENT*environmentHeight));
		minCellSize = new Dimension((int)(MINIMUM_CELL_PERCENT*environmentHeight),
				(int)(MINIMUM_CELL_PERCENT*environmentHeight));
		preferredCellSize = new Dimension((int)(PREFERRED_CELL_PERCENT*environmentHeight),
				(int)(PREFERRED_CELL_PERCENT*environmentHeight));
		maxCellSize = new Dimension((int)(MAXIMUM_CELL_PERCENT*environmentHeight),
				(int)(MAXIMUM_CELL_PERCENT*environmentHeight));
	}

	private void setupBoardCellPanels() {
		boardCells = new BoardCellPanel[Board.BOARD_ROWS][Board.BOARD_COLUMNS];
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				JButton cell = new JButton();
				cell.setMaximumSize(maxCellSize);
				cell.setPreferredSize(preferredCellSize);
				cell.setMinimumSize(minCellSize);
				setColorAndText(cell, row, col);
				boardCells[row][col] = new BoardCellPanel(cell);
				boardCells[row][col].setMaximumSize(maxCellSize);
				boardCells[row][col].setPreferredSize(preferredCellSize);
				boardCells[row][col].setMinimumSize(minCellSize);
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

			button.setMaximumSize(maxCellSize);
			button.setPreferredSize(preferredCellSize);
			button.setMinimumSize(minCellSize);
			setColorAndText(button, row, col);
		}
		this.boardCells[row][col].setBoardButton(button);
		this.boardCells[row][col].revalidate();
		this.boardCells[row][col].repaint();
	}

	public BoardCellPanel getBoardCell(int row, int col) {
		return boardCells[row][col];
	}
}
