package scrabble.view;

import scrabble.model.Player;

import javax.swing.*;
import java.awt.*;

public class WinnerScreen extends JPanel {

    public WinnerScreen(Player[] players) {
        this.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Podium"));
        JPanel southPanel = new JPanel(new FlowLayout());
        JPanel podium = new JPanel(new GridLayout(4,1, 7, 10));


        JLabel firstPlaceName = new JLabel("1st: " + players[0].getName() + " | " + players[0].getScore());
        JLabel secondPlaceName = new JLabel("2nd: " + players[1].getName() + " | " + players[1].getScore());
        JLabel thirdPlaceName = new JLabel("3rd: " + players[2].getName() + " | " + players[2].getScore());
        JLabel fourthPlaceName = new JLabel("4th: " + players[3].getName() + " | " + players[3].getScore());
        JButton returnToMain = new JButton("Return to Main Menu");

        firstPlaceName.setBorder(BorderFactory.createEtchedBorder());
        secondPlaceName.setBorder(BorderFactory.createEtchedBorder());
        thirdPlaceName.setBorder(BorderFactory.createEtchedBorder());
        fourthPlaceName.setBorder(BorderFactory.createEtchedBorder());

        //podium.add(firstPlaceLabel);
        podium.add(firstPlaceName);
        //podium.add(secondPlaceLabel);
        podium.add(secondPlaceName);
        //podium.add(thirdPlaceLabel);
        podium.add(thirdPlaceName);
        //podium.add(fourthPlaceLabel);
        podium.add(fourthPlaceName);
        centerPanel.add(podium);
        this.add(centerPanel, BorderLayout.CENTER);

        southPanel.add(returnToMain);
        this.add(southPanel, BorderLayout.SOUTH);
    }
}
