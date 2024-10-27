package scrabble.view;

import javax.swing.*;
import java.awt.*;

public class JoinScreen extends JPanel {

    private JTextField name;
    private JTextField IP;
    private JButton join;

    public JoinScreen() {
        this.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setBorder(BorderFactory.createTitledBorder("Join A Game"));
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("IP"));
        JPanel southPanel = new JPanel(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        name = new JTextField();
        name.setColumns(10);
        JLabel hostIPLabel = new JLabel("Enter Host IP:", SwingConstants.RIGHT);
        IP = new JTextField();
        IP.setColumns(10);
        join = new JButton("Join");

        JPanel namePanel = new JPanel(new GridLayout(1,2, 7, 0));
        namePanel.add(nameLabel);
        namePanel.add(name);
        northPanel.add(namePanel);
        this.add(northPanel, BorderLayout.NORTH);

        JPanel ipPanel = new JPanel(new GridLayout(1,2,7,0));
        ipPanel.add(hostIPLabel);
        ipPanel.add(IP);
        centerPanel.add(ipPanel);
        this.add(centerPanel, BorderLayout.CENTER);

        southPanel.add(join);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    public JTextField getNameTextField() {
        return name;
    }

    public JTextField getIPTextField() {
        return IP;
    }

    public JButton getJoinButton() {
        return join;
    }

}
