package scrabble.controller;

import scrabble.view.screen.JoinScreen;

import java.io.IOException;

/**
 * Handles <code>JoinScreen</code> events.
 */
public class JoinScreenController {
    private final Controller parent;
    private final JoinScreen joinScreen;

    /**
     * Constructs a JoinScreenController from a parent and a JoinScreen.
     * @param parent the <code>Controller</code> on which changes should be made.
     * @param joinScreen the join screen to which <code>ActionListeners</code> are added.
     */
	public JoinScreenController (Controller parent, JoinScreen joinScreen) {
        this.parent = parent;
        this.joinScreen = joinScreen;

        joinScreen.getJoinButton().addActionListener(e -> joinClick());
    }

    /*
     * Handles the Join button being pressed
     */
    private void joinClick() {
		String userName = joinScreen.getNameText();
		String hostsIP = joinScreen.getIPText();
		String hostsPort = joinScreen.getPortText();
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
