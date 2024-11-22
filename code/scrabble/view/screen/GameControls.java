package scrabble.view.screen;

import scrabble.controller.GameScreenController;
import scrabble.model.Tile;
import scrabble.view.TileButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static scrabble.view.screen.GameScreen.RACK_SIZE;

public class GameControls extends JPanel {
	private static final String RACK_PANEL = "RACK";
	private static final String EXCHANGE_PANEL = "EXCHANGE";
	private static final String BLANK_PANEL = "BLANK";

	private final CardLayout layout;

	private final MainControlsPanel mainControlsPanel;
	private final ExchangePanel exchangePanel;
//	private final

	public GameControls() {
		layout = new CardLayout();
		this.setLayout(layout);
		mainControlsPanel = new MainControlsPanel();
		exchangePanel = new ExchangePanel();
		layout.addLayoutComponent(setupBlankPanel(), BLANK_PANEL);
		layout.addLayoutComponent(mainControlsPanel, RACK_PANEL);
		layout.addLayoutComponent(exchangePanel, EXCHANGE_PANEL);
		layout.show(this, RACK_PANEL);
	}

	public void showExchange() {layout.show(this, EXCHANGE_PANEL);}
	public void showRack() {layout.show(this, RACK_PANEL);}
	public void showBlank() {layout.show(this, BLANK_PANEL);}


	private JPanel setupBlankPanel() {
		JPanel basePanel = new JPanel(new FlowLayout());

		JPanel controlsPanel = new JPanel(new GridLayout(3,1,0,10));
		JLabel chooseLabel = new JLabel("Which Letter would you like?");
		JComboBox<String> letterSelect = new JComboBox<>(new String[] {"A", "B", "C", "D"});
		JButton submitButton = new JButton("Submit");

		controlsPanel.add(chooseLabel);
		controlsPanel.add(letterSelect);
		controlsPanel.add(submitButton);
		basePanel.add(controlsPanel);
		return basePanel;
	}

	private static class ExchangePanel extends JPanel {
		private static final String ONE = "One";
		private static final String ALL = "All";

		private final JButton backButton;
		private final JButton submitButton;
		private final JComboBox<String> numberSelect;
		private final JComboBox<Character> letterSelect;


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

		public JButton getBackButton() {
			return backButton;
		}

		public JButton getSubmitButton() {
			return submitButton;
		}

		public void removeAllLetters() {
			letterSelect.removeAllItems();
		}

		public void addLetters(Character[] letters) {
			for (Character l : letters) {
				letterSelect.addItem(l);
			}
		}

		public Character getSelectedLetter() {
			return (Character) letterSelect.getSelectedItem();
		}

		public int getNumberToExchange() {
			String selected = (String) numberSelect.getSelectedItem();
			return (selected.equals(ONE) ? 1 : RACK_SIZE);
		}
	}

	private static class MainControlsPanel extends JPanel {
		private final RackPanel rackPanel;

		private final JButton passButton;
		private final JButton submitButton;
		private final JButton exchangeButton;
		private final JButton challengeButton;


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

		public JButton getPassButton() { return passButton; }
		public JButton getSubmitButton() { return submitButton; }
		public JButton getExchangeButton() { return exchangeButton; }
		public JButton getChallengeButton() { return challengeButton; }

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
			 * @param tilePanels an array of TilePanels to be displayed in the rack
			 */
			public RackPanel(TilePanel[] tilePanels){
				// Assigns the given array of TilePanels to the class field
				this.tilePanels = tilePanels;

				// Sets the layout of the panel to a GridLayout with 1 row, RACK_SIZE columns,
				// and a horizontal gap of 10 pixels between components
				this.setLayout(new GridLayout(1, RACK_SIZE, 10, 0));

				// Adds each TilePanel to the RackPanel
				for (TilePanel tp : this.tilePanels) {
					this.add(tp);
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

			public void setRackButtonEnabled (boolean enabled) {
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
			public static class TilePanel extends JPanel {
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
