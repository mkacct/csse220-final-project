package UIComponents;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import SimComponents.Sim;

/**
 * Window for setting up a new sim
 * @author R_002
 */
public class SimConfigUI extends JFrame {
	private SimConfigForm form;
	
	/**
	 * The form fields that the user puts their preferences in
	 */
	private class SimConfigForm extends JPanel {
		private JTextField chromosomeSize;
		private JTextField popSize;
		private JComboBox<String> fitnessFunction;
		private JComboBox<String> selector;
		private JComboBox<String> crossover;
		private JTextField mutationRate;

		SimConfigForm() {
			super();

			this.add(new JLabel("Chromosome size"));
			this.chromosomeSize = new JTextField("100", 5);
			this.add(this.chromosomeSize);

			this.add(new JLabel("Population size"));
			this.popSize = new JTextField("100", 5);
			this.add(this.popSize);

			this.add(new JLabel("Fitness function"));
			this.fitnessFunction = new JComboBox<String>(Sim.FF_NAMES);
			this.add(this.fitnessFunction);

			this.add(new JLabel("Selection mode"));
			this.selector = new JComboBox<String>(Sim.SELECTOR_NAMES);
			this.add(this.selector);
			
			this.add(new JLabel("Crossover mode"));
			this.crossover = new JComboBox<String>(Sim.CROSSOVER_NAMES);
			this.add(this.crossover);

			this.add(new JLabel("Mutation rate"));
			this.mutationRate = new JTextField("0.01", 5);
			this.add(this.mutationRate);

			this.setLayout(new GridLayout(this.getComponentCount() / 2, 2));
		}

		// don't actually know if I want to do it this way...

		// public int getChromosomeSize() {}
		// public int getPopSize() {}
		// public String getFitnessFunction() {}
		// public String getSelectionMode() {}
		// public String getCrossoverMode() {}
		// public double getMutationRate() {}
	}

	/**
	 * The "Cancel" and "OK" buttons at the bottom of the config window
	 */
	private class SimConfigConfirmation extends JPanel {
		SimConfigConfirmation() {
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {SimConfigUI.this.dispose();}
			});
			this.add(cancel);

			JButton submit = new JButton("Submit");
			submit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {SimConfigUI.this.submit();}
			});
			this.add(submit);
		}
	}
	
	public SimConfigUI() {
		super();
		this.setTitle("Create New Sim");
		this.form = new SimConfigForm();
		this.add(this.form, BorderLayout.NORTH);
		this.add(new SimConfigConfirmation(), BorderLayout.SOUTH);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	private void submit() {
		JOptionPane.showMessageDialog(this, "you can't do that");
	}
}