package scrabble.view;

import javax.swing.*;
import java.awt.*;

public class JoinScreen extends JPanel {
    public JoinScreen() {

        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setBorder(BorderFactory.createTitledBorder("Join A Game"));
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("IP"));
        JPanel southPanel = new JPanel(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField enterName = new JTextField();
        enterName.setColumns(10);
        JLabel hostIPLabel = new JLabel("Enter Host IP:", SwingConstants.RIGHT);
        JTextField enterIP = new JTextField();
        enterIP.setColumns(10);
        JButton joinButton = new JButton("Join");

        JPanel namePanel = new JPanel(new GridLayout(1,2, 7, 0));
        namePanel.add(nameLabel);
        namePanel.add(enterName);
        northPanel.add(namePanel);
        this.add(northPanel, BorderLayout.NORTH);

        JPanel ipPanel = new JPanel(new GridLayout(1,2,7,0));
        ipPanel.add(hostIPLabel);
        ipPanel.add(enterIP);
        centerPanel.add(ipPanel);
        this.add(centerPanel, BorderLayout.CENTER);

        southPanel.add(joinButton);
        this.add(southPanel, BorderLayout.SOUTH);


    }
}
