import javax.swing.JFrame;

/**
 * Contains the main method of the program
 * @author R_002
 */
public class Main {
	public static final String APPLICATION_TITLE = "Genetic algorithm thingy";

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle(APPLICATION_TITLE);
		// make ui
		frame.setSize(frame.getMaximumSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}