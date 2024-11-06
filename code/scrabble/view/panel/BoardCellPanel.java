package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

public class BoardCellPanel extends JPanel {
	private JButton boardButton;

	public BoardCellPanel(JButton button) {
		FlowLayout manager = new FlowLayout();
		manager.setHgap(0);
		manager.setVgap(0);
		this.setLayout(manager);
		this.setBoardButton(button);
		//this.setPreferredSize(new Dimension(25, 25));
	}

	public void setBoardButton(JButton boardButton) {
		if (this.boardButton != null) this.remove(this.boardButton);
		this.boardButton = boardButton;
		this.boardButton.setFont(getFont().deriveFont(Font.BOLD, 15f));
		//this.boardButton.setPreferredSize(new Dimension(22, 22));
		this.add(this.boardButton);
		this.revalidate();
		this.repaint();
	}

	public JButton getBoardButton() {
		return boardButton;
	}
}
