package Main;
import java.awt.Color;

import UIComponents.MainUI;

/**
 * Contains the main method of the program, and constants
 * @author R_002
 */
public class App {
	public static final String TITLE = "Genetic Algorithm Sim";
	public static final String LONG_TITLE = "Genetic Algorithm Simulator";
	public static final String TEAM_ID = "R_002";
	public static final String AUTHOR = "Sarah Collins and Madeline Kahn";

	public static final String SAVE_DIR = "saved-data/";

	public static final Color ZERO_COLOR = new Color(79, 132, 189);
	public static final Color ONE_COLOR = new Color(229, 119, 225);
	public static final Color Q_COLOR = new Color(65, 63, 176);

	public static void main(String[] args) {
		new MainUI();
	}
}