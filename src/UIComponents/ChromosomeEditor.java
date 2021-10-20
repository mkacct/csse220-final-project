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
 *         Takes an individual and displays it as a grid of buttons. Buttons
 *         are colored based on their bit value and clicking a button flips the
 *         bit.
 */
public class ChromosomeEditor extends JPanel {
	public static final Color ZERO_COLOR = Color.CYAN;
	public static final Color ONE_COLOR = Color.MAGENTA;
	public static final Color Q_COLOR = Color.BLUE;

	Individual indiv;
	char[] chromosome;
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
		this.chromosome = this.indiv.getChromosome();
		GridLayout grid = new GridLayout(0, (int)Math.sqrt(this.chromosome.length));
		grid.setHgap(0);
		grid.setVgap(0);
		this.setLayout(grid);
		//Creates an actionListener to monitor buttons and flip their bits when they're clicked
		ActionListener listen = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(e.getActionCommand());
				indiv.flipBit(index);
				chromosome = indiv.getChromosome();
				//sets button color according to updated bit value
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

		//Adds buttons of the correct color
		for (int i = 0; i < this.chromosome.length; i++) {
			buttons.add(new JButton(Integer.toString(i)));
			buttons.get(i).setActionCommand(Integer.toString(i));
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
			this.add(buttons.get(i));
			buttons.get(i).addActionListener(listen);
		}
	}

}
