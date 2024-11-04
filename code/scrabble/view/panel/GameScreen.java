package scrabble.view.panel;

import scrabble.controller.Controller;
import scrabble.model.Board;
import scrabble.model.ModifierType;
import scrabble.model.Tile;
import scrabble.model.TileScore;
import scrabble.network.messages.PlayTiles;
import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameScreen extends JPanel {

	private JLabel gameTime;
	private JButton[][] gameCells;
	public Tile[][] letters = new Tile[Board.BOARD_ROWS][Board.BOARD_COLUMNS];
	private JButton[] rack;
	private JButton submitButton;
	private Board board = new Board();

	public List<Tile> getPlayedTiles() {
		return playedTiles;
	}

	public List<Tile> playedTiles = new ArrayList<>();
	private String value = " ";
	private final Color doubleWord = new Color(255, 102, 102);
	private final Color tripleWord = new Color(255, 0, 0);
	private final Color doubleLetter = new Color(88, 117, 255);
	private final Color tripleLetter = new Color(0, 41, 255);
	private final Color normalCell = new Color(255, 255, 255);
	private final static int GAP = 175;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public final static Color normalCell = new Color(255, 255, 255);

	public GameScreen() {
		this.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel(new FlowLayout());
		gameTime = new JLabel("00:00");
		gameTime.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTime);

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));
		JPanel gamePanel = new JPanel(new GridLayout(15,15,3,3));
		gameCells = new JButton[15][15];
		for (int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {

				// Get modifier for special cells like Double Word, Triple Word, etc.
				JButton boardTile = new JButton(" ");

				ModifierType mt = board.getBoardSpecialCell().get(new Point(i, j));
				if(mt != null) {
					if (mt == ModifierType.DOUBLE_WORD) {
						boardTile.setBackground(doubleWord);
						boardTile.setText("DW");
						boardTile.setBorderPainted(false);
					} else if (mt == ModifierType.TRIPLE_WORD) {
						boardTile.setBackground(tripleWord);
						boardTile.setText("TW");
						boardTile.setBorderPainted(false);
					} else if (mt == ModifierType.DOUBLE_LETTER) {
						boardTile.setBackground(doubleLetter);
						boardTile.setText("DL");
						boardTile.setBorderPainted(false);
					} else if (mt == ModifierType.TRIPLE_LETTER) {
						boardTile.setBackground(tripleLetter);
						boardTile.setText("TL");
						boardTile.setBorderPainted(false);
					} else {
						boardTile.setBackground(normalCell);
					}
				}

				gameCells[i][j] = boardTile;
				gamePanel.add(boardTile);
			}
		}
		centerPanel.add(gamePanel);

		JPanel eastPanel = new JPanel(new GridLayout(2,1,0,GAP));
		JPanel westPanel = new JPanel(new GridLayout(2,1,0,GAP));

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
			rack[i] = new TileButton(TileScore.values()[i]);
			//rackTile.setBorder(BorderFactory.createEtchedBorder());
			rackPanel.add(rack[i]);
		}
		submitAndRack.add(submitButton);
		submitAndRack.add(rackPanel);
		southPanel.add(submitAndRack);

		//Drop down menu
		JComboBox<String> comboBox = getStringJComboBox();

		this.add(comboBox);
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);

		boardTilesActionListener();
		rackTilesActionListener();
		submitActionListener();
	}

	private static JComboBox<String> getStringJComboBox() {
		String[] options = {"Rules", "Game Audio", "Game FX", "Quit"};
		JComboBox<String> comboBox = new JComboBox<>(options);
		comboBox.setBounds(0, 0, 100, 25);

		comboBox.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == comboBox) {
					String menu = options[comboBox.getSelectedIndex()];
					switch (menu) {
						case "Rules" -> System.out.println("There are no rules man, We laust!");
						case "Game Audio" -> System.out.println("No audio for now :( ");
						case "Game FX" -> System.out.println("No FX either :( ");
						case "Quit" -> System.out.println("I wish dude!");
                    }
				}
			}
		});
		return comboBox;
	}

	public void boardTilesActionListener(){
		for (int i = 0; i < Board.BOARD_ROWS; i++) {
			for (int j = 0; j < Board.BOARD_COLUMNS; j++) {
				JButton boardTile = gameCells[i][j];

				int row = i;
				int col = j;
				boardTile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						List<String> modType = new ArrayList<>(Arrays.asList("DW", "TW", "DL", "TL"));

						//adding tiles from the rack to the board
						if((boardTile.getText().equals(" ") || boardTile.getBackground() != normalCell) && !value.equals(" ")) {
							for (int k = 0; k < 7; k++) {
								if(!boardTile.getText().equals(" ") && !modType.contains(boardTile.getText())) {
									if (rack[k].getText().equals(" ")) {
										if (boardTile.getBackground() != normalCell) {
											rack[k].setText(boardTile.getText());
											char tile = boardTile.getText().charAt(0);
											Point point = new Point(row, col);
											playedTiles.remove(new Tile(tile, point));
											break;
										}
									}
								}
							}
							boardTile.setText(value);
							char tile = value.charAt(0);
							Point point = new Point(row, col);
							playedTiles.add(new Tile(tile, point));
							value = " ";
						}
						//adding tiles from the board back to the rack
						else if(value.equals(" ")){
							for (int k = 0; k < 7; k++) {
								if(rack[k].getText().equals(" ") && !modType.contains(boardTile.getText())){
									rack[k].setText(boardTile.getText());
									char tile = boardTile.getText().charAt(0);
									Point point = new Point(row, col);

									if(!boardTile.getText().equals(" "))
										playedTiles.remove(new Tile(tile, point));

									//add the value of the special cell back to the board cell if the player puts tiles back on the rack
									if(value.equals(" ")){
										Color color = boardTile.getBackground();
										if (color.equals(doubleWord)) {
											boardTile.setText("DW");
										} else if (color.equals(doubleLetter)) {
											boardTile.setText("DL");
										} else if (color.equals(tripleWord)) {
											boardTile.setText("TW");
										} else if (color.equals(tripleLetter))
											boardTile.setText("TL");
										else
											boardTile.setText(" ");
									}
									value = " ";
									break;
								}
							}
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

	public void submitActionListener(){
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(Arrays.toString(playedTiles.toArray(new Tile[0])));
				PlayTiles playTiles = new PlayTiles(0,0,playedTiles.toArray(new Tile[0]));
				//playTiles.execute(this.controller);
			}
		});
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
