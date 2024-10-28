package scrabble.view.panel;

import scrabble.model.Board;
import scrabble.model.ModifierType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class GameScreen extends JPanel {

	private JLabel gameTime;
	private JButton[][] gameCells;
	private JButton[] rack;
	private JButton submitButton;
	private Board board = new Board();
	private String value = " ";
	private final Color doubleWord = new Color(255, 102, 102);
	private final Color tripleWord = new Color(255, 0, 0);
	private final Color doubleLetter = new Color(88, 117, 255);
	private final Color getTripleLetter = new Color(0, 41, 255);
	private final Color normalCell = new Color(255, 255, 255);

	public GameScreen() {
		this.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel(new FlowLayout());
		gameTime = new JLabel("00:00");
		gameTime.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTime);

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));
		JPanel gamePanel = new JPanel(new GridLayout(15,15,1,1));
		gameCells = new JButton[15][15];
		for (int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {

				// Get modifier for special cells like Double Word, Triple Word, etc.
				JButton boardTile = new JButton(" ");

				ModifierType mt = board.getBoardSpecialCell().get(new Point(i, j));
				if(mt != null) {
					if (mt == ModifierType.DOUBLE_WORD)
						boardTile.setBackground(doubleWord);
					else if (mt == ModifierType.TRIPLE_WORD)
						boardTile.setBackground(tripleWord);
					else if (mt == ModifierType.DOUBLE_LETTER)
						boardTile.setBackground(doubleLetter);
					else if (mt == ModifierType.TRIPLE_LETTER)
						boardTile.setBackground(getTripleLetter);
					else
						boardTile.setBackground(normalCell);
				}

				gameCells[i][j] = boardTile;
				//boardTile.setBorder(BorderFactory.createEtchedBorder());
				gamePanel.add(boardTile);
			}
		}
		centerPanel.add(gamePanel);

		JPanel eastPanel = new JPanel(new GridLayout(2,1,0,175));
		JPanel westPanel = new JPanel(new GridLayout(2,1,0,175));

		JPanel player1Panel = setupPlayerPanel();
		JPanel player2Panel = setupPlayerPanel();
		JPanel player3Panel = setupPlayerPanel();
		JPanel player4Panel = setupPlayerPanel();

		westPanel.add(player1Panel);
		westPanel.add(player4Panel);

		eastPanel.add(player2Panel);
		eastPanel.add(player3Panel);

		JPanel southPanel = new JPanel(new FlowLayout());
		JPanel submitAndRack = new JPanel(new GridLayout(2,1,0,10));
		submitButton = new JButton("Submit");
		JPanel rackPanel = new JPanel(new GridLayout(1,7,10,0));
		rack = new JButton[7];
		for (int i = 0; i < 7; i++) {
			JButton rackTile = new JButton(""+i);
			rack[i] = rackTile;
			//rackTile.setBorder(BorderFactory.createEtchedBorder());
			rackPanel.add(rackTile);
		}
		submitAndRack.add(submitButton);
		submitAndRack.add(rackPanel);
		southPanel.add(submitAndRack);

		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);

		boardTilesActionListener();
		rackTilesActionListener();
	}

	public void boardTilesActionListener(){
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				JButton boardTile = gameCells[i][j];
				ModifierType mt = board.getBoardSpecialCell().get(new Point(i, j));
				if(mt == null)
					mt = ModifierType.NONE;
				ModifierType finalMt = mt;

				boardTile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if((boardTile.getText().equals(" ") || !boardTile.getText().equals(finalMt.name())) && !value.equals(" ")){
							for (int k = 0; k < 7; k++) {
								if(rack[k].getText().equals(" ")){
									rack[k].setText(boardTile.getText());
									break;
								}
							}
							boardTile.setText(value);
							value = " ";
						}
						else if(value.equals(" ")){
							for (int k = 0; k < 7; k++) {
								if(rack[k].getText().equals(" ")){
									rack[k].setText(boardTile.getText());
									value = " ";
									break;
								}
							}
							if(!boardTile.getText().equals(finalMt.name())){
								boardTile.setText(" ");
							}
							else
								boardTile.setText(finalMt.name());
						}
                    }
				});
			}
		}
	}

	public void rackTilesActionListener(){
		for (int i = 0; i < 7; i++) {
			JButton rackTile = rack[i];
			int finalI = i;
			rackTile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!value.equals(" ")){
						for (int j = 0; j < 7; j++){
							if(rack[j].getText().equals(" ")){
								rack[j].setText(value);
								break;
							}
						}
					}
					value = rackTile.getText();
					rack[finalI].setText(" ");
				}
			});
		}
	}

	public JLabel getGameTime() {
		return gameTime;
	}

	public JButton[][] getGameCells() {
		return gameCells;
	}

	public JButton[] getRack() {
		return rack;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	private JPanel setupPlayerPanel() {
		return new PlayerPanel("Player", 0, 0);
	}
}
