package UIComponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import SimComponents.Individual;

/**
 * Class ChromosomeEditor
 * 
 * @author R002 <br>
 *         Takes an individual and displays it as a grid of buttons. Buttons are
 *         colored based on their bit value and clicking a button flips the bit.
 */
public class ChromosomeEditor extends JPanel {
	public static final Color ZERO_COLOR = new Color(79, 132, 189);
	public static final Color ONE_COLOR = new Color(189, 79, 185);
	public static final Color Q_COLOR = new Color(65, 63, 176);

	Individual indiv;
	ArrayList<JButton> buttons = new ArrayList<JButton>();

	/**
	 * Creates a ChromosomeEditor. Takes an individual and displays it as a series
	 * of buttons. Buttons are colored based on their bit value and clicking a
	 * button flips the bit.
	 * 
	 * @param indiv
	 */
	public ChromosomeEditor(Individual indiv) {
		super();
		this.indiv = indiv;
		GridLayout grid = new GridLayout(0, (int) Math.sqrt(this.indiv.getChromosome().length));
		grid.setHgap(0);
		grid.setVgap(0);
		this.setLayout(grid);
		// Creates an actionListener to monitor buttons and flip their bits when they're
		// clicked
		ActionListener listen = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(e.getActionCommand());
				indiv.flipBit(index);
				char[] chromosome = indiv.getChromosome();
				// sets button color according to updated bit value
				switch (chromosome[index]) {
				case '0':
					buttons.get(index).setBackground(ZERO_COLOR);
					break;
				case '1':
					buttons.get(index).setBackground(ONE_COLOR);
					break;
				case '?':
					buttons.get(index).setBackground(Q_COLOR);
					break;
				}
			}
		};

		// Adds buttons of the correct index
		char[] chromosome = this.indiv.getChromosome();
		for (int i = 0; i < chromosome.length; i++) {
			buttons.add(new JButton(Integer.toString(i)));
			buttons.get(i).setActionCommand(Integer.toString(i));
			this.add(buttons.get(i));
			buttons.get(i).addActionListener(listen);
		}
		this.updateButtons();
	}

	/**
	 * Updates the colors of the buttons to match the individual's chromosome
	 */
	private void updateButtons() {
		char[] chromosome = this.indiv.getChromosome();
		for (int i = 0; i < chromosome.length; i++) {
			switch (chromosome[i]) {
			case '0':
				buttons.get(i).setBackground(ZERO_COLOR);
				break;
			case '1':
				buttons.get(i).setBackground(ONE_COLOR);
				break;
			case '?':
				buttons.get(i).setBackground(Q_COLOR);
				break;
			}
		}
	}

	/**
	 * Mutates the given individual at the given rate and updates the buttons
	 * accordingly
	 * 
	 * @param rate <br>
	 *             Requires: rate between 0 and 1 (inclusive)
	 */
	public void handleMutate(double rate) {
		this.indiv.mutate(rate);
		this.updateButtons();
	}
}
