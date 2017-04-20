package game_snake;

import javax.swing.JFrame;

public class Snake extends JFrame {

	public Snake() {
		
		add(Grid.getInstance());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(420, 440);
		setLocationRelativeTo(null);
		setTitle("Game Snake");
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Snake();
	}

}
