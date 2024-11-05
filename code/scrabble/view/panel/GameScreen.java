package scrabble.view.panel;

import scrabble.model.Board;
import scrabble.model.ModifierType;
import scrabble.model.Tile;
import scrabble.model.TileScore;
import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JPanel {

	public static final Color DOUBLE_WORD = new Color(255, 102, 102);
	public static final Color TRIPLE_WORD = new Color(255, 0, 0);
	public static final Color DOUBLE_LETTER = new Color(88, 117, 255);
	public static final Color TRIPLE_LETTER = new Color(0, 41, 255);
	public static final Color NORMAL_CELL = new Color(255, 255, 255);
	public static final int GAP = 175;
	public static final int RACK_SIZE = 7;

	private JLabel gameTime;
	private BoardPanel boardPanel;
	public Tile[][] letters = new Tile[Board.BOARD_ROWS][Board.BOARD_COLUMNS];

	private RackPanel rackPanel;
	private JButton submitButton;
	private Board board = new Board();


	public List<Tile> playedTiles = new ArrayList<>();
	private JButton value = new JButton(" ");

	public GameScreen() {
		this.setLayout(new BorderLayout());

		JPanel northPanel = setupNorthPanel();
		JPanel centerPanel = setupCenterPanel();

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

		JPanel southPanel = setupSouthPanel();

		//Drop down menu
		JComboBox<String> comboBox = getStringJComboBox();

		this.add(comboBox);
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);
	}

	public List<Tile> getPlayedTiles() {
		return playedTiles;
	}

	public void setValue(JButton value) {
		this.value = value;
	}

	public JButton getValue() {
		return value;
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public JLabel getGameTime() {
		return gameTime;
	}

	public RackPanel getRackPanel() {
		return rackPanel;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	private JPanel setupSouthPanel() {
		JPanel southPanel = new JPanel(new FlowLayout());
		JPanel submitAndRack = new JPanel(new GridLayout(2,1,0,10));
		submitButton = new JButton("Submit");
		TilePanel[] tilePanels = new TilePanel[RACK_SIZE];
		for (int i = 0; i < tilePanels.length; i++) {
			tilePanels[i] = new TilePanel(new TileButton(TileScore.values()[i]));
		}
		this.rackPanel = new RackPanel(tilePanels);


		submitAndRack.add(submitButton);
		submitAndRack.add(rackPanel);
		southPanel.add(submitAndRack);
		return southPanel;
	}

	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Game Board"));

//		JPanel gamePanel = new JPanel(new GridLayout(15,15,3,3));
		BoardCellPanel[][] boardCellPanels = new BoardCellPanel[Board.BOARD_ROWS][Board.BOARD_COLUMNS];
//		gameCells = new JButton[15][15];
		for (int i = 0; i < boardCellPanels.length; i++) {
			for(int j = 0; j < boardCellPanels[0].length; j++) {

				// Get modifier for special cells like Double Word, Triple Word, etc.
				JButton boardTile = new JButton(" ");

				ModifierType mt = board.getBoardSpecialCell().get(new Point(i, j));
				if(mt != null) {
					if (mt == ModifierType.DOUBLE_WORD) {
						boardTile.setBackground(DOUBLE_WORD);
						boardTile.setText("DW");
						boardTile.setBorderPainted(false);
					} else if (mt == ModifierType.TRIPLE_WORD) {
						boardTile.setBackground(TRIPLE_WORD);
						boardTile.setText("TW");
						boardTile.setBorderPainted(false);
					} else if (mt == ModifierType.DOUBLE_LETTER) {
						boardTile.setBackground(DOUBLE_LETTER);
						boardTile.setText("DL");
						boardTile.setBorderPainted(false);
					} else if (mt == ModifierType.TRIPLE_LETTER) {
						boardTile.setBackground(TRIPLE_LETTER);
						boardTile.setText("TL");
						boardTile.setBorderPainted(false);
					} else {
						boardTile.setBackground(NORMAL_CELL);
					}
				}
				boardCellPanels[i][j] = new BoardCellPanel(boardTile);

//				gameCells[i][j] = boardTile;
//				gamePanel.add(boardTile);
			}
		}
		boardPanel = new BoardPanel(boardCellPanels);
		centerPanel.add(boardPanel);
		return centerPanel;
	}

	private JPanel setupNorthPanel() {
		JPanel northPanel = new JPanel(new FlowLayout());
		gameTime = new JLabel("00:00");
		gameTime.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(gameTime);
		return northPanel;
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

	private JPanel setupPlayerPanel() {
		return new PlayerPanel("Player", 0, 0);
	}
}
