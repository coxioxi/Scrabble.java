package scrabble.view.screen;

import scrabble.controller.GameScreenController;
import scrabble.model.Tile;
import scrabble.view.TileButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

import static scrabble.view.screen.GameScreen.RACK_SIZE;


/**
 * Allows controls to be swapped out on the bottom of the Game Screen.
 * <p>
 *     Uses a <code>CardLayout</code> to display an exchange tiles, main controls, and
 *     blank tile panel depending on the user's inputs.
 * </p>
 */
public class GameControls extends JPanel {
	private static final String RACK_PANEL = "RACK";
	private static final String EXCHANGE_PANEL = "EXCHANGE";
	private static final String BLANK_PANEL = "BLANK";

	private final CardLayout layout;

	private final MainControlsPanel mainControlsPanel;
	private final ExchangePanel exchangePanel;
	private final BlankPanel blankPanel;
//	private final

	public static void main(String[] args) {
		JFrame frame = new JFrame("hello.");
		frame.add(new GameControls());
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(new Dimension(100, 200));
	}

	/**
	 * Constructs a <code>GameControls</code> object with the three different views.
	 */
	public GameControls() {
		layout = new CardLayout();
		this.setLayout(layout);
		mainControlsPanel = new MainControlsPanel();
		exchangePanel = new ExchangePanel();
		blankPanel = new BlankPanel();
		this.add(blankPanel, BLANK_PANEL);
		this.add(mainControlsPanel, RACK_PANEL);
		this.add(exchangePanel, EXCHANGE_PANEL);
		layout.show(this, RACK_PANEL);
	}

	/** Shows the Exchange screen on this component. */
	public void showExchange() {layout.show(this, EXCHANGE_PANEL);}
	/** Shows the Main controls (with tile rack) screen on this component. */
	public void showRack() {layout.show(this, RACK_PANEL);}
	/** Shows the Blank Tile screen on this component. */
	public void showBlank() {layout.show(this, BLANK_PANEL);}

	public MainControlsPanel getMainControlsPanel() {
		return mainControlsPanel;
	}

	public ExchangePanel getExchangePanel() {
		return exchangePanel;
	}

	public BlankPanel getBlankPanel() {
		return blankPanel;
	}

	/**
	 * The panel for setting the letter of a blank tile to some letter.
	 * <p>
	 *     Uses a <code>JComboBox</code> to allow a letter to be selected.
	 *     The user must press the submit button to trigger the letter being set.
	 * </p>
	 */
	public static class BlankPanel extends JPanel {
		private Character[] alphabet;		// the options for the blank tile.

		private final JComboBox<Character> letterSelect;		// box for selecting a letter.
		private final JButton submitButton;		// Submit button for setting the letter.

		/**
		 * Constructs a panel for setting a blank tile.
		 * Contains the combo box and submit button.
		 */
		public BlankPanel () {
			this.setLayout(new FlowLayout());

			JPanel controlsPanel = new JPanel(new GridLayout(3,1,0,10));
			JLabel chooseLabel = new JLabel("Which Letter would you like?");
			setAlphabet();
			letterSelect = new JComboBox<>(alphabet);
			submitButton = new JButton("Submit");
			controlsPanel.add(chooseLabel);
			controlsPanel.add(letterSelect);
			controlsPanel.add(submitButton);
			this.add(controlsPanel);
		}

		public void removeSubmitListeners() {GameScreenController.removeActionListeners(submitButton); }
		public void addSubmitActionListener(ActionListener al) { submitButton.addActionListener(al); }

		/*
		 * stores all letters of the English alphabet in the Character array.
		 */
		private void setAlphabet() {
			this.alphabet = new Character[26];
			for (int i = 0; i < Tile.TileScore.values().length - 1; i++) {
				alphabet[i] = Tile.TileScore.values()[i].getLetter();
			}
		}

		/**
		 * Gets the letter which has been selected in the combo box.
		 * @return The letter which was selected, as a <code>Character</code>.
		 */
		public Character getSelectedLetter() {
			return (Character) letterSelect.getSelectedItem();
		}
	}

	/**
	 * The panel for choosing a letter or all letters to be exchanged for new tile(s).
	 * <p>
	 *     Uses two <code>JComboBox</code> to select letters, one to set the number of tiles to exchange,
	 *     one to select a letter if only one is to be exchanged.
	 * </p>
	 */
	public static class ExchangePanel extends JPanel {
		private static final String ONE = "One";
		private static final String ALL = "All";

		private final JButton backButton;
		private final JButton submitButton;
		private final JComboBox<String> numberSelect;
		private final JComboBox<Character> letterSelect;


		/**
		 * Constructs a panel for exchanging tiles.
		 */
		public ExchangePanel() {
			this.setLayout(new FlowLayout());
			JPanel exchangePanel = new JPanel(new GridLayout(1, 2, 30, 0));

			JPanel centerPanel = new JPanel(new GridLayout(3,1,20,10));
			JLabel exchangeText = new JLabel("Exchange One or All:");
			numberSelect = new JComboBox<>(new String[]{ONE, ALL});
			letterSelect = new JComboBox<>(new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'});
			centerPanel.add(exchangeText);
			centerPanel.add(numberSelect);
			centerPanel.add(letterSelect);

			JPanel eastPanel = new JPanel(new GridLayout(2,1,10, 20));
			backButton = new JButton("Go Back");
			submitButton = new JButton("Submit");
			eastPanel.add(backButton, 0);
			eastPanel.add(submitButton,1);

			exchangePanel.add(centerPanel);
			exchangePanel.add(eastPanel);
			this.add(exchangePanel);
		}

		public void addBackActionListener(ActionListener al) { backButton.addActionListener(al); }
		public void removeBackListeners() {GameScreenController.removeActionListeners(backButton);}

		public void addSubmitActionListener(ActionListener al) { submitButton.addActionListener(al); }
		public void removeSubmitListeners() {GameScreenController.removeActionListeners(submitButton);}

		/**
		 * Removes all letters stored in the <code>JComboBox</code> used for selecting a
		 * single letter.
		 */
		public void removeAllLetters() {
			letterSelect.removeAllItems();
		}

		/**
		 * Adds letters into the <code>JComboBox</code> used for selecting a single letter.
		 * @param letters the letters to put into the combo box.
		 */
		public void addLetters(char[] letters) {
			for (Character l : letters) {
				letterSelect.addItem(l);
			}
		}

		/**
		 * Gets the letter which was selected in the box.
		 * @return The letter selected as a <code>Character</code>.
		 */
		public Character getSelectedLetter() {
			return (Character) letterSelect.getSelectedItem();
		}

		public void addNumberSelectActionListener(ActionListener al) {numberSelect.addActionListener(al);}

		/**
		 * Gets the number of tiles which are to be exchanged.
		 * @return The number of tiles to exchange. 1 or <code>RACK_SIZE</code>.
		 */
		public int getNumberToExchange() {
			String selected = (String) numberSelect.getSelectedItem();
			return (Objects.equals(selected, ONE) ? 1 : RACK_SIZE);
		}

		/**
		 * Sets the letter select box to be enabled or disabled. When the combo box is disabled,
		 * items cannot be selected and values cannot be typed into its field (if it is editable).
		 * @param enable whether to enable or disable the box. True to enable.
		 */
		public void enableLetterSelect(boolean enable) {
			letterSelect.setEnabled(enable);
		}
	}

	/**
	 * The panel for choosing options and for rack tiles.
	 * Allows users to choose to pass turn, play tiles on the board, and exchange tiles.
	 */
	public static class MainControlsPanel extends JPanel {
		private final RackPanel rackPanel;		//

		private final JButton passButton;
		private final JButton submitButton;
		private final JButton exchangeButton;
		private final JButton challengeButton;


		/**
		 * Constructs a main controls panel with buttons for passing, exchanging, submitting, and challenging (disabled).
		 */
		public MainControlsPanel() {
			this.setLayout(new FlowLayout());
			JPanel gameControlsPanel = new JPanel(new BorderLayout());
			JPanel subAndPass = new JPanel(new GridLayout(2, 1, 0, 10));
			JPanel centerRack = new JPanel(new FlowLayout());
			centerRack.setBorder(new TitledBorder("Rack"));
			JPanel exAndChall = new JPanel(new GridLayout(2,1,0,10));
			passButton = new JButton("Pass Turn");
			submitButton = new JButton("Submit");
			submitButton.setPreferredSize(new Dimension(50, 10));
			exchangeButton = new JButton("Exchange Tiles");
			challengeButton = new JButton("Challenge");
			challengeButton.setEnabled(false);

			this.rackPanel = new RackPanel();

			subAndPass.add(submitButton);
			subAndPass.add(passButton);
			gameControlsPanel.add(subAndPass, BorderLayout.WEST);
			centerRack.add(rackPanel);
			gameControlsPanel.add(centerRack, BorderLayout.CENTER);
			exAndChall.add(exchangeButton);
			exAndChall.add(challengeButton);
			gameControlsPanel.add(exAndChall, BorderLayout.EAST);
			this.add(gameControlsPanel);
		}


		public void addSubmitActionListener(ActionListener al) {
			submitButton.addActionListener(al);
		}

		public void removeSubmitListeners() {
			GameScreenController.removeActionListeners(submitButton);
		}

		public void addExchangeActionListener(ActionListener al) { exchangeButton.addActionListener(al);}
		public void removeExchangeListeners() { GameScreenController.removeActionListeners(exchangeButton); }

		public void addPassActionListener(ActionListener al) { passButton.addActionListener(al);}
		public void removePassListeners() { GameScreenController.removeActionListeners(passButton); }

		public JButton getChallengeButton() { return challengeButton; }

		public char[] getRackLetters() {
			RackPanel.TilePanel[] panels = rackPanel.tilePanels;
			char[] letters = new char[RACK_SIZE];
			for (int i = 0; i < panels.length; i++) {
				letters[i] = panels[i].getButton().getText().charAt(0);
			}
			return letters;
		}

		public Tile[] getRackTiles() {
			char[] characters = getRackLetters();
			Tile[] tiles = new Tile[characters.length];
			for (int i = 0; i < characters.length; i++) {
				tiles[i] = new Tile(Tile.TileScore.valueOf(characters[i] + ""));
			}
			return tiles;
		}

		public RackPanel getRackPanel() { return rackPanel; }

		/**
		 * RackPanel is a JPanel that represents a rack of TilePanels,
		 * displaying the tiles a player currently holds.
		 */
		public static class RackPanel extends JPanel {
			// Array to hold the TilePanels that make up the player's rack

			private final TilePanel[] tilePanels;

			public RackPanel() {
				tilePanels = new TilePanel[RACK_SIZE];
				this.setLayout(new GridLayout(1, RACK_SIZE, 10, 0));

				for (int i = 0; i < tilePanels.length; i++) {
					tilePanels[i] = new TilePanel(new TileButton(Tile.TileScore.values()[i]));
					this.add(tilePanels[i]);
				}
			}

			/**
			 * Constructor to initialize the RackPanel with an array of TilePanels.
			 *
			 * @param tileButtons an array of TileButtons to be displayed in the rack.
			 */
			public RackPanel(TileButton[] tileButtons){
				// Assigns the given array of TilePanels to the class field
				this.tilePanels = new TilePanel[tileButtons.length];

				// Sets the layout of the panel to a GridLayout with 1 row, RACK_SIZE columns,
				// and a horizontal gap of 10 pixels between components
				this.setLayout(new GridLayout(1, RACK_SIZE, 10, 0));

				// Adds each TilePanel to the RackPanel
				for (int i = 0; i < tileButtons.length; i++) {
					this.tilePanels[i] = new TilePanel(tileButtons[i]);
					this.add(this.tilePanels[i]);
				}
			}

			/**
			 * Replaces the button at a specific position in the rack.
			 * Invoking this method revalidates and repaints the rack.
			 *
			 * @param tileButton the new JButton to be set in the specified TilePanel
			 * @param i the index of the TilePanel where the button will be set
			 */
			public void setButton(JButton tileButton, int i) {
				// Sets a new button in the specified TilePanel at index i
				tilePanels[i].setButton(tileButton);

				// Revalidates the panel to ensure the new button is displayed properly
				this.revalidate();

				// Repaints the panel
				this.repaint();
			}

			public void addToRack(TileButton button) {
				boolean searching = true;
				for (int i = 0; i < tilePanels.length && searching; i++) {
					if (!(tilePanels[i].getButton() instanceof TileButton)) {
						setButton(button, i);
						searching = false;
					}
				}
			}

			public int addTileButtonToRack(TileButton t) {
				int index = -1;
				for (int i = 0; i < tilePanels.length && index == -1; i++) {
					if (!(tilePanels[i].getButton() instanceof TileButton)) {
						tilePanels[i].setButton(t);
						index = i;
					}
				}
				return index;
			}

			/**
			 * Adds all specified tiles into the <code>GameScreen</code>'s rack as buttons.
			 *
			 * @param tiles Array of tiles to be added.
			 */
			public void addTilesToRack(Tile[] tiles) {
				for(Tile t : tiles) {
					TileButton button =
							(t.isBlank()
									? new TileButton()
									: new TileButton(Tile.TileScore.valueOf(t.getLetter()+""))
							);
					this.addToRack(button);
				}
			}

			public int removeFromRack(String letter) {
				for(int i = 0; i < tilePanels.length; i++){
					if(tilePanels[i].getButton().getText().equals(letter)){
						tilePanels[i].setButton(new JButton(" "));
						return i;
					}
				}
				return -1;
			}

			public JButton removeButtonFromRack(int col) {
				JButton b = tilePanels[col].getButton();
				tilePanels[col].setButton(new JButton(" "));
				return b;
			}

			public void setRackButtonsEnabled (boolean enabled) {
				for (TilePanel tp : tilePanels) {
					tp.getButton().setEnabled(enabled);
				}
			}

			public void addRackTileActionListener(ActionListener al, int index) {
				tilePanels[index].addButtonActionListener(al);
			}

			public void removeActionListeners(int index) {
				tilePanels[index].removeButtonActionListeners();
			}

			public void resetRack() {
				for (TilePanel tp : tilePanels) {
					tp.setButton(new JButton(" "));
				}
			}

			/**
			 * TilePanel is a JPanel component that holds a TileButton and provides
			 * methods for adding and updating the button in the panel.
			 */
			private static class TilePanel extends JPanel {
				// A JButton to represent the tile displayed in this panel
				private JButton tileButton;

				/**
				 * Constructor that initializes the TilePanel with a given TileButton.
				 *
				 * @param tileButton the initial TileButton to be displayed in this panel
				 */
				public TilePanel(TileButton tileButton) {
					// Set the layout to a flow layout to manage the placement of the button
					this.setLayout(new FlowLayout());
					this.setButton(tileButton); // Set the button in the panel
				}

				/**
				 * Getter for the current TileButton in the panel.
				 *
				 * @return the current JButton instance
				 */
				public JButton getButton() { return tileButton; }

				/**
				 * Sets a new button in the panel. If a button already exists, it removes the old one
				 * before adding the new button.
				 *
				 * @param tileButton the new JButton to be added to the panel
				 */
				public void setButton(JButton tileButton) {
					// Remove the existing button if there is one
					if (this.tileButton != null) this.remove(this.tileButton);

					// Assign the new button and set its font properties
					this.tileButton = tileButton;
					this.tileButton.setFont(getFont().deriveFont(Font.BOLD, 12f));

					// Add the button to the panel and refresh the panel display
					this.add(this.tileButton);
					this.revalidate(); // Revalidates the component hierarchy
					this.repaint(); // Repaints the component to reflect changes
				}

				public void addButtonActionListener(ActionListener al) {
					this.tileButton.addActionListener(al);
				}

				public void removeButtonActionListeners() {
					GameScreenController.removeActionListeners(this.tileButton);
				}
			}
		}
	}



}
