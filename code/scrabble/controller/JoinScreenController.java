package scrabble.controller;

import scrabble.view.screen.JoinScreen;

import java.io.IOException;

public class JoinScreenController {
    private final Controller parent;
    private final JoinScreen joinScreen;
    private String userName;
    private String hostsIP;
    private String hostsPort;

    public JoinScreenController (Controller parent, JoinScreen joinScreen) {
        this.parent = parent;
        this.joinScreen = joinScreen;

        joinScreen.getJoinButton().addActionListener(e -> joinClick());
    }

    private void joinClick() {
        userName = joinScreen.getNameTextField().getText();
        hostsIP = joinScreen.getIPTextField().getText().trim();
        hostsPort = joinScreen.getPort().getText().trim();
        if (userName.isBlank()) {
            parent.showNoNameDialog();
        } else if (hostsIP.isBlank()) {
            parent.showNoIPDialog();
        } else if (hostsPort.isBlank()) {
            parent.showNoPortDialog();
        } else {
            int portNumber;
            try {
                portNumber = Integer.parseInt(hostsPort);
                parent.setupSocket(hostsIP, portNumber);
                parent.sendNewPlayer(userName);
                parent.showWaiting();
            } catch (NumberFormatException e) {
                parent.showNoPortDialog();
            } catch (IOException e) {
                parent.showIPErrorDialog();
            }
        }
    }

}
