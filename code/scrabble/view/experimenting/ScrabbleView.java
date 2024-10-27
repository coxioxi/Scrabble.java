package scrabble.view.experimenting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleView extends JFrame {

	private ScrabbleView self;
	private JPanel topPanel;
	private CardLayout layoutManager;
	private JPanel[] panels;

	public ScrabbleView() throws HeadlessException {
		self = this;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		layoutManager = new CardLayout();
		self.setLayout(layoutManager);
		panels = new JPanel[5];
		JPanel host = new JPanel();
		JLabel label = new JLabel("HOST");
		label.setVisible(true);
		label.setSize(100, 100);
		host.add(label);
		/*
		JButton next = new JButton("Next");
		next.getParent();
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				System.out.println(button.getParent());
			}
		});
		host.add(next);
		 */
		topPanel = new JPanel(layoutManager);
		this.getContentPane().setLayout(layoutManager);
		panels[0] = host;
		JPanel join = new JPanel();
		join.add(new JLabel("JOIN"));
		panels[1] = join;
		topPanel.add(panels[0], "HOST");
		topPanel.add(panels[1], "JOIN");
		this.add(topPanel);
		layoutManager.first(this.getContentPane());

		this.setSize(100, 100);
		this.setVisible(true);

	}

	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		new ScrabbleView();
	}
}
