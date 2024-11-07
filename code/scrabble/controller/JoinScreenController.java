package scrabble.controller;

import scrabble.view.panel.JoinScreen;

import java.io.IOException;

public class JoinScreenController {
    private final Controller parent;
    private final JoinScreen joinScreen;
    private String userName;
    private String hostsIP;

    public JoinScreenController (Controller parent, JoinScreen joinScreen) {
        this.parent = parent;
        this.joinScreen = joinScreen;

        joinScreen.getJoinButton().addActionListener(e -> joinClick());
    }

    private void joinClick() {
        userName = joinScreen.getNameTextField().getText();
        hostsIP = joinScreen.getIPTextField().getText().trim();
        if (userName.isBlank()) {
            parent.getView().showNoNameDialog();
        } else if (hostsIP.isBlank()) {
            parent.getView().showNoIPDialog();
        } else {
            try {
                parent.setupSocket(hostsIP);
                parent.getView().showWaiting();
            } catch (IOException e) {
                System.out.println(e);
                parent.getView().showIPErrorDialog();
            }

        }
    }

}
