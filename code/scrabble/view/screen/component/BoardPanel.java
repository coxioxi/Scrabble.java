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
import scrabble.view.TileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * BoardPanel is a JPanel that represents the game board in Scrabble.
 * It initializes and displays a grid of BoardCellPanels with appropriate colors and labels.
 */
public class BoardPanel extends JPanel {
	public static final Color MODIFIER_CELL_TEXT_COLOR = new Color(255, 255, 255);
	public static final Color BOARDER_COLOR = new Color(112, 109, 109);

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
		this.setBackground(BOARDER_COLOR);

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
				// Sets color and text based on the type of modifier for the cell
				JButton cell = new JButton();
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
		button.setBackground(mt.getColor());
		button.setText(mt.getAbbreviation());
		button.setForeground(MODIFIER_CELL_TEXT_COLOR);
		button.setBorderPainted(false);
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

	public void removeActionListeners(int row, int col) {
		JButton button = boardCells[row][col].getBoardButton();
		for (ActionListener al : button.getActionListeners()) {
			button.removeActionListener(al);
		}
	}

	public void addActionListener(ActionListener al, int row, int col) {
		boardCells[row][col].addActionListener(al);
	}

	public boolean instanceOfTileButton(int row, int col) {
		return (boardCells[row][col].getBoardButton() instanceof TileButton);
	}

	public String getButtonText(int row, int col) {
		return boardCells[row][col].getBoardButton().getText();
	}

	public JButton getButton(int row, int col) {
		return boardCells[row][col].getBoardButton();
	}

	/**
	 * A panel that holds a single JButton representing a cell on the Scrabble board.
	 */
	private static class BoardCellPanel extends JPanel {
		// The button representing the cell in the panel.
		private JButton boardButton;

		/**
		 * Constructor that initializes the panel with a given button.
		 *
		 * @param button The button to be added to this panel.
		 */
		public BoardCellPanel(JButton button) {
			// Set a layout manager with zero horizontal and vertical gaps.
			FlowLayout manager = new FlowLayout();
			manager.setHgap(0);
			manager.setVgap(0);
			this.setLayout(manager);

			// Set the button for the panel.
			this.setBoardButton(button);
			//this.setPreferredSize(new Dimension(25, 25));
		}

		/**
		 * Replaces the current button in the panel with a new button.
		 *
		 * @param boardButton The new button to be set in the panel.
		 */
		public void setBoardButton(JButton boardButton) {
			// Remove the existing button, if present.
			if (this.boardButton != null) this.remove(this.boardButton);

			// Set the new button and adjust its font properties.
			this.boardButton = boardButton;
			this.boardButton.setFont(getFont().deriveFont(Font.BOLD, 15f));
			//this.boardButton.setPreferredSize(new Dimension(22, 22));

			// Add the new button to the panel and refresh the panel's state.
			this.add(this.boardButton);
			this.revalidate();
			this.repaint();
		}

		/**
		 * Gets the button currently in the panel.
		 *
		 * @return The JButton in the panel.
		 */
		public JButton getBoardButton() {
			return boardButton;
		}

		/**
		 * Adds an <code>ActionListener</code> to the button contained in this panel.
		 * @param al the action listener to add to the button.
		 */
		public void addActionListener(ActionListener al) {
			boardButton.addActionListener(al);
		}
	}

}
