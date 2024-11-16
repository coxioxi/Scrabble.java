package scrabble.view.screen.component;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.GameScreenController;
import scrabble.model.Board;
import scrabble.model.ModifierType;
import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;

/**
 * BoardPanel is a JPanel that represents the game board in Scrabble.
 * It initializes and displays a grid of BoardCellPanels with appropriate colors and labels.
 */
public class BoardPanel extends JPanel {
	// Constant colors for different cell types on the board
	public static final Color DOUBLE_WORD_COLOR = new Color(154, 75, 75);
	public static final Color TRIPLE_WORD_COLOR = new Color(177, 19, 19);
	public static final Color DOUBLE_LETTER_COLOR = new Color(57, 70, 140);
	public static final Color TRIPLE_LETTER_COLOR = new Color(25, 43, 147);
	public static final Color NORMAL_CELL_COLOR = new Color(221, 221, 221);
	public static final Color MODIFIER_CELL_TEXT_COLOR = new Color(255, 255, 255);

	// Text for various cell types
	public static final String DOUBLE_WORD_TEXT = "DW";
	public static final String TRIPLE_WORD_TEXT = "TW";
	public static final String DOUBLE_LETTER_TEXT = "DL";
	public static final String TRIPLE_LETTER_TEXT = "TL";
	public static final String NORMAL_CELL_TEXT = " ";

	// Constants for panel size percentages
	public static final float MAXIMUM_PANEL_SIZE_PERCENT = .55f;
	public static final float PREFERRED_PANEL_SIZE_PERCENT = .40f;
	public static final float MINIMUM_PANEL_SIZE_PERCENT = .2f;
	public static final int SPACING = 3;
	public static final float MAXIMUM_CELL_PERCENT = .04f;
	public static final float PREFERRED_CELL_PERCENT = .025f;
	public static final float MINIMUM_CELL_PERCENT = .02f;

	// Array of BoardCellPanels representing the game board cells
	private BoardCellPanel[][] boardCells;
	private int environmentHeight;
	private int environmentWidth;

	// Dimensions for panel and cell sizes
	private Dimension maxPanelSize, preferredPanelSize, minPanelSize,
				maxCellSize, preferredCellSize, minCellSize;

	/**
	 * Constructor for the BoardPanel class. Initializes the board panel,
	 * sets up dimensions, and adds cell panels.
	 */
	public BoardPanel() {
		try {
			// Set the look and feel to the system's appearance
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException |
				 IllegalAccessException | UnsupportedLookAndFeelException ignore) {}

		// Get the screen dimensions from the default graphics environment
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		environmentWidth = gd.getDisplayMode().getWidth();
		environmentHeight = gd.getDisplayMode().getHeight();

		// Set up panel and cell dimensions
		setupDimensions();

		// Set the preferred and maximum size of the panel
		this.setPreferredSize(preferredPanelSize);
		this.setMaximumSize(maxPanelSize);

		// Set the background color of the board panel
		this.setBackground(new Color(112, 109, 109));

		// Set the layout as a grid for the board cells
		this.setLayout(new GridLayout(Board.BOARD_ROWS,Board.BOARD_COLUMNS,SPACING,SPACING));
		setupBoardCellPanels();
	}

	/**
	 * Sets up the dimensions for the panel and cells based on screen size.
	 */
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

	/**
	 * Initializes the board cells and adds them to the panel.
	 */
	private void setupBoardCellPanels() {
		boardCells = new BoardCellPanel[Board.BOARD_ROWS][Board.BOARD_COLUMNS];
		for (int row = 0; row < Board.BOARD_ROWS; row++) {
			for (int col = 0; col < Board.BOARD_COLUMNS; col++) {
				JButton cell = new JButton();
//				setButtonSizes(cell);
//				cell.setFont(getFont().deriveFont(4f));
				// Sets color and text based on the type of modifier for the cell
				setColorAndText(cell, row, col);

				// Create a BoardCellPanel for each cell and add it to the panel
				boardCells[row][col] = new BoardCellPanel(cell);
				//setButtonSizes(boardCells[row][col]);
				this.add(boardCells[row][col]);
			}
		}
	}


	/**
	 * Sets the sizes for a given button to match the cell dimensions.
	 *
	 * @param button the button whose size is to be set
	 */
	private void setButtonSizes(JComponent button) {
		button.setMaximumSize(maxCellSize);
		button.setPreferredSize(preferredCellSize);
		button.setMinimumSize(minCellSize);
	}

	/**
	 * Sets the color and text of a button based on its modifier type.
	 *
	 * @param button the button to customize
	 * @param row    the row index of the cell
	 * @param col    the column index of the cell
	 */
	private void setColorAndText(JButton button, int row, int col) {
		ModifierType mt = Board.MODIFIER_HASH_MAP.get(new Point(row, col));
		button.setBackground(getColor(mt));
		button.setText(getText(mt));
		button.setForeground(MODIFIER_CELL_TEXT_COLOR);
		button.setBorderPainted(false);
	}


	/**
	 * Returns the color associated with a given modifier type.
	 *
	 * @param mt the modifier type
	 * @return the color representing the modifier type
	 */
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

	/**
	 * Returns the text associated with a given modifier type.
	 *
	 * @param mt the modifier type
	 * @return the text representing the modifier type
	 */
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

	/**
	 * Updates a cell at a specified row and column with a new button.
	 *
	 * @param button the new button to set
	 * @param row    the row index of the cell
	 * @param col    the column index of the cell
	 */
	public void setBoardCell(JButton button, int row, int col) {
		if (!(button instanceof TileButton)) {
			//setButtonSizes(button);
			setColorAndText(button, row, col);
		}
		this.boardCells[row][col].setBoardButton(button);
		this.boardCells[row][col].revalidate();
		this.boardCells[row][col].repaint();
	}

	/**
	 * Disables a cell at a specific position on the board.
	 *
	 * @param row the row index of the cell
	 * @param col the column index of the cell
	 */
	public void disableBoardCell(int row, int col) {
		JButton button = boardCells[row][col].getBoardButton();
		 button.setEnabled(false);
		GameScreenController.removeActionListeners(button);
	}

	/**
	 * Retrieves a specific BoardCellPanel from the board.
	 *
	 * @param row the row index of the cell
	 * @param col the column index of the cell
	 * @return the BoardCellPanel at the specified position
	 */
	public BoardCellPanel getBoardCell(int row, int col) {
		return boardCells[row][col];
	}
}
