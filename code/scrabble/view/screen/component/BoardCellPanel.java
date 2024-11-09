package scrabble.view.screen.component;

import javax.swing.*;
import java.awt.*;

/**
 * A panel that holds a single JButton representing a cell on the Scrabble board.
 */
public class BoardCellPanel extends JPanel {
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
}
