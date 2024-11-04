package scrabble.controller.prototype;

import javax.swing.*;

public class Frame extends JFrame {
	private JButton button;

	public Frame () {
		this.button = new JButton("Hello World!");
		this.add(button);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(240, 180);
		this.setVisible(true);
	}

	public JButton getButton() {
		return button;
	}
}
