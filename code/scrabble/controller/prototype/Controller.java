package scrabble.controller.prototype;

public class Controller {
	private Frame frame;

	public Controller() {
		frame = new Frame();
		addListeners();
	}

	private void addListeners() {
		addButtonListener();
	}

	private void addButtonListener() {
		this.frame.getButton().addActionListener(e -> {
			System.out.println("Hello World??");
		});
	}

	public static void main(String[] args) {
		new Controller();
	}

}
