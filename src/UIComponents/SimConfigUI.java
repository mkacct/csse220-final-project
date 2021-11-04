package UIComponents;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Exceptions.DomainException;
import Exceptions.FormValidationException;
import Main.MiscUtil;
import SimComponents.Sim;

/**
 * Window for setting up a new sim
 * @author R_002
 */
public class SimConfigUI extends AppWindow {
	private SimConfigUI.ConfigForm form;
	
	/**
	 * The form fields that the user puts their preferences in
	 */
	private class ConfigForm extends JPanel {
		private JTextField chromosomeSize, popSize;
		private JComboBox<String> fitnessFunction, selector, crossover;
		private JTextField mutationRate;

		public ConfigForm() {
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

		// get form values
		public int getChromosomeSize() throws NumberFormatException {return Integer.parseInt(chromosomeSize.getText());}
		public int getPopSize() throws NumberFormatException {return Integer.parseInt(popSize.getText());}
		public String getFitnessFunction() {return (String) fitnessFunction.getSelectedItem();}
		public String getSelectionMode() {return (String) selector.getSelectedItem();}
		public String getCrossoverMode() {return (String) crossover.getSelectedItem();}
		public double getMutationRate() throws NumberFormatException, DomainException {return MiscUtil.parseProportion(mutationRate.getText());}
	}

	/**
	 * The "Cancel" and "OK" buttons at the bottom of the config window
	 */
	private class SimConfigConfirmation extends JPanel {
		public SimConfigConfirmation() {
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
	
	public SimConfigUI(AppWindow parent) {
		super(parent);
		this.setTitle("Create New Sim");
		this.form = new ConfigForm();
		this.add(this.form, BorderLayout.NORTH);
		this.add(new SimConfigUI.SimConfigConfirmation(), BorderLayout.SOUTH);
		this.showWindow();
	}

	/**
	 * Checks if the user input in the form is valid, if it is valid does nothing
	 * @throws FormValidationException if it's invalid
	 */
	private void validateForm() throws FormValidationException {
		try {
			int chromosomeSize = this.form.getChromosomeSize();
			int popSize = this.form.getChromosomeSize();
			if (!((chromosomeSize > 0) && (popSize > 0))) {throw new DomainException();}
		} catch (NumberFormatException | DomainException ex) {
			throw new FormValidationException("Size values must be positive integers");
		}
		try {
			this.form.getMutationRate();
		} catch (NumberFormatException ex) {
			throw new FormValidationException("Mutation rate format is invalid");
		} catch (DomainException ex) {
			throw new FormValidationException("Mutation rate must be between 0 and 1");
		}
	}

	/**
	 * Handles form submission
	 * If the form is invalid, shows an error dialog
	 * If the form is valid, makes a new sim using the configuration, and closes this window
	 */
	private void submit() {
		try {
			validateForm();
		} catch (FormValidationException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
			return;
		}
		// form input is valid
		new SimUI(
			this.getParentWindow(),
			this.form.getPopSize(),
			this.form.getChromosomeSize(),
			this.form.getFitnessFunction(),
			this.form.getSelectionMode(),
			this.form.getCrossoverMode(),
			this.form.getMutationRate()
		);
		this.dispose();
	}
}