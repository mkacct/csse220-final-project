package UIComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import Exceptions.DomainException;
import Exceptions.FileFormatException;
import Main.App;
import SimComponents.FitnessFunction;
import SimComponents.Individual;
import SimComponents.Sim;

/**
 * Window for actually using the sim
 * 
 * @author R_002
 */
public class SimUI extends AppWindow {
	private final int popSize, chromosomeSize;
	private final String ffName, selectionMode, crossoverMode;
	private final double mutationRate, elitism;
	private final FitnessFunction fitnessFunction;

	private final Graph graph;
	private final Controls controls;
	private final PopulationDisplay populationDisplay;
	private final OneIndivDisplay fittestDisplay;
	private Timer timer;
	private int genCount;
	private int tempGenCount;

	// 0 = not started, 1 = running, 2 = paused
	private int simState;

	private Sim sim;

	/**
	 * Header, with text describing the properties of the sim
	 */
	private class Header extends JPanel {
		public Header() {
			this.add(new JLabel("Population size: " + SimUI.this.popSize + " \u2013\u2013 Chromosome size: "
					+ SimUI.this.chromosomeSize + " \u2013\u2013 Fitness function: " + SimUI.this.ffName
					+ " \u2013\u2013 Selection: " + SimUI.this.selectionMode + " \u2013\u2013 Crossover: "
					+ SimUI.this.crossoverMode + " \u2013\u2013 Mutation rate: " + SimUI.this.mutationRate + " \u2013\u2013 Elitism: " + SimUI.this.elitism));
		}
	}


	/**
	 * Displays one individual based on a chromosome
	 * 
	 * @author R_002
	 *
	 */
	private class ChromosomeDisplay extends JComponent {
		private char[] chromosome;

		/**
		 * Constructs a chromosome display
		 * 
		 * @param chromosome
		 */
		public ChromosomeDisplay(char[] chromosome) {
			super();
			this.chromosome = chromosome;
			this.setPreferredSize(new Dimension(30, 30));
		}

		/**
		 * Draws a grid representation of the chromosome
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (int num = 0; num < chromosome.length; num++) {
				Color c;
				switch (chromosome[num]) {
				case '0':
					c = App.ZERO_COLOR;
					break;
				case '1':
					c = App.ONE_COLOR;
					break;
				case '?':
					c = App.Q_COLOR;
					break;
				default:
					c = Color.black;
				}
				g.setColor(c);
				int x = (int) (num % Math.sqrt(chromosome.length)
						* (int) (this.getWidth() / Math.sqrt(chromosome.length)));
				int y = (int) (num / Math.sqrt(chromosome.length))
						* (int) (this.getHeight() / Math.sqrt(chromosome.length));
				int width = (int) (this.getWidth() / Math.sqrt(chromosome.length));
				int height = (int) (this.getHeight() / Math.sqrt(chromosome.length));
				g.fillRect(x, y, width, height);
			}
			g.setColor(Color.black);
			g.drawRect(0, 0, this.getWidth(), this.getHeight());
		}

		/**
		 * Force the component to be square
		 */
		@Override
		public void setBounds(int x, int y, int width, int height) {
			int sideLength = Math.min(width, height);
			super.setBounds(x, y, sideLength, sideLength);
		}
	}

	/**
	 * Controls shown at the bottom of the sim Includes start/stop button, step and
	 * reset buttons, and fields for customizing running
	 */
	private class Controls extends JPanel {
		private final String[] startStopButtonText = {"Start", "Pause", "Resume"};
		private final String[] runModes = {"Run forever", "Number of gens:", "Max gen:", "Max fitness:"};

		private JButton startStop, step, reset;
		private JComboBox<String> runMode;
		private JTextField tickRate, endCondition;

		public Controls() {
			this.startStop = new JButton("Start");
			this.startStop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SimUI.this.startStop();
				}
			});
			this.add(this.startStop);

			this.step = new JButton("Step >");
			this.step.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SimUI.this.tick();
				}
			});
			this.add(this.step);

			this.reset = new JButton("Reset");
			this.reset.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SimUI.this.promptReset();
				}

			});
			this.add(this.reset);

			this.runMode = new JComboBox<String>(this.runModes);
			this.runMode.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Controls.this.updateElements();
				}

			});
			this.add(runMode);
			this.endCondition = new JTextField("100", 3);
			this.add(endCondition);

			this.add(new JLabel("Tick rate (Hz):"));
			this.tickRate = new JTextField("20", 3);
			this.add(this.tickRate);

			this.updateElements();
		}

		/**
		 * Updates the state (mostly enabled state) of the control UI elements
		 */
		public void updateElements() {
			this.startStop.setEnabled(true);
			this.startStop.setText(this.startStopButtonText[SimUI.this.simState]);
			this.step.setEnabled(SimUI.this.simState != 1);
			this.reset.setEnabled(true);
			this.runMode.setEnabled(SimUI.this.simState != 1);
			this.endCondition.setEnabled((SimUI.this.simState != 1) && (this.runMode.getSelectedIndex() != 0));
			this.tickRate.setEnabled(SimUI.this.simState != 1);
		}

		public double getTickRate() throws NumberFormatException {
			return Double.parseDouble(this.tickRate.getText());
		}

		public int getRunMode() {
			return this.runMode.getSelectedIndex();
		}

		public int getEndConditionValue() throws NumberFormatException {
			return Integer.parseInt(this.endCondition.getText());
		}
	}

	/**
	 * Displays the population of chromosomes in a grid
	 * 
	 * @author R_002
	 *
	 */
	private class PopulationDisplay extends JPanel {
		/**
		 * Constructs a population display with the given array of chromosomes
		 * 
		 * @param chromosomes
		 */
		public PopulationDisplay(ArrayList<char[]> chromosomes) {
			GridLayout grid = new GridLayout(0, (int) Math.sqrt(chromosomes.get(0).length));
			grid.setHgap(0);
			grid.setVgap(0);
			this.setLayout(grid);
			this.updatePopulation(chromosomes);
		}

		/**
		 * Updates the chromosome display
		 * 
		 * @param chromosomes
		 */
		void updatePopulation(ArrayList<char[]> chromosomes) {
			this.removeAll();
			for (int i = 0; i < chromosomes.size(); i++) {
				this.add(new ChromosomeDisplay(chromosomes.get(i)));
			}
		}
	}

	/**
	 * A display of one indiv and it's fitness value
	 * For use in the "Visualize the fittest" feature
	 */
	private class OneIndivDisplay extends JPanel {
		/**
		 * Construct one such display
		 * @param indiv to show
		 */
		public OneIndivDisplay(Individual indiv) {
			this.setLayout(new GridLayout(2, 1));
			this.showIndiv(indiv);
		}

		/**
		 * Set the indiv being displayed
		 * @param indiv to show
		 */
		public void showIndiv(Individual indiv) {
			this.removeAll();
			this.add(new ChromosomeDisplay(indiv.getChromosome()));
			this.add(new JLabel("Fitness: " + fitnessFunction.calcFitness(indiv)));
		}
	}

	/**
	 * Creates a new SimUI
	 * 
	 * @param popSize        <br>
	 *                       > 0
	 * @param chromosomeSize <br>
	 *                       > 0
	 * @param ffName         <br>
	 *                       String
	 * @param selectionMode
	 * @param crossoverMode
	 * @param mutationRate
	 * @param elitism
	 */
	public SimUI(AppWindow parent, int popSize, int chromosomeSize, String ffName, String selectionMode, String crossoverMode,
			double mutationRate, double elitism) throws FileNotFoundException, FileFormatException {
		super(parent);

		this.popSize = popSize;
		this.chromosomeSize = chromosomeSize;
		this.ffName = ffName;
		this.fitnessFunction = Sim.ffByName(this.ffName);
		this.selectionMode = selectionMode;
		this.crossoverMode = crossoverMode;
		this.mutationRate = mutationRate;
		this.elitism = elitism;

		this.setTitle("Sim");
		this.add(new SimUI.Header(), BorderLayout.NORTH);

		this.controls = new SimUI.Controls();
		this.add(this.controls, BorderLayout.SOUTH);

		createSim();

		this.populationDisplay = new PopulationDisplay(this.sim.getChromosomes());
		this.add(this.populationDisplay, BorderLayout.WEST);
		this.fittestDisplay = new OneIndivDisplay(this.sim.getBestIndividual());
		this.add(this.fittestDisplay, BorderLayout.EAST);
		double[] data = { this.sim.getMinFitness(), this.sim.getAvgFitness(), this.sim.getMaxFitness(),this.sim.hammingDistance() };
		Color[] colors = {Color.red, Color.blue, Color.green, Color.yellow};
		this.graph = new Graph(data, colors);
		this.add(this.graph, BorderLayout.CENTER);
		
		this.showWindow();
	}
	
	/**
	 * Creates the sim object; also handles some other reset things
	 */
	private void createSim() {
		if (this.timer != null) {
			this.timer.stop();
			this.timer = null;
		}
		this.sim = new Sim(this.popSize, this.chromosomeSize, this.mutationRate, this.fitnessFunction,
				this.selectionMode, this.elitism, this.crossoverMode);
		this.genCount = 0;
		this.tempGenCount = 0;
		this.setSimState(0);
	}

	/**
	 * Sets the sim state value
	 * 
	 * @param state one of: 0 = not started, 1 = running, 2 = paused
	 */
	private void setSimState(int state) {
		this.simState = state;
		this.controls.updateElements();
	}

	/**
	 * Does one tick/step/gen
	 */
	private void tick() {
		if (this.simState == 0) {
			this.setSimState(2);
		}
		this.sim.nextGen();
		SimUI.this.genCount++;
		this.updateDisplay();
	}

	/**
	 * Updates the display
	 */
	private void updateDisplay() {
		this.populationDisplay.updatePopulation(this.sim.getChromosomes());
		this.fittestDisplay.showIndiv(this.sim.getBestIndividual());
		double[] data = { this.sim.getMinFitness(), this.sim.getAvgFitness(), this.sim.getMaxFitness(), this.sim.hammingDistance() };
		this.graph.updateGraph(data);
		this.graph.repaint();
		this.revalidate();
	}

	/**
	 * Listener used by the timer when the sim is running
	 */
	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (SimUI.this.controls.getRunMode()) {
				case 1: // num gens
					if (SimUI.this.tempGenCount >= SimUI.this.controls.getEndConditionValue()) {
						SimUI.this.pause();
						return;
					}
					break;
				case 2: // max gen
					if (SimUI.this.genCount >= SimUI.this.controls.getEndConditionValue()) {
						SimUI.this.pause();
						return;
					}
					break;
				case 3: // max fitness
					if (SimUI.this.sim.getMaxFitness() >= SimUI.this.controls.getEndConditionValue()) {
						SimUI.this.pause();
						return;
					}
			}
			SimUI.this.tick();
			SimUI.this.tempGenCount++;
		}
	}

	/**
	 * Toggles whether the sim is running
	 */
	private void startStop() {
		if (this.simState != 1) {
			start();
		} else {
			pause();
		}
	}

	/**
	 * Does validation to see if the sim can start, and if so starts it
	 */
	private void start() {
		if (this.simState == 1) {return;}
		double tickRate;
		try {
			tickRate = this.controls.getTickRate();
			if (tickRate <= 0) {
				throw new DomainException();
			}
		} catch (NumberFormatException | DomainException ex) {
			JOptionPane.showMessageDialog(this, "Tick rate must be a positive integer");
			return;
		}
		if (SimUI.this.controls.getRunMode() != 0) {
			try {
				double endConditionValue = this.controls.getEndConditionValue();
				if (endConditionValue <= 0) {
					throw new DomainException();
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "End condition value must be a positive integer");
				return;
			}
		}
		// validation ok
		this.setSimState(1);
		this.timer = new Timer((int) ((1. / tickRate) * 1000.), new TimerListener());
		this.tempGenCount = 0;
		this.timer.start();
	}

	/**
	 * Pauses the sim
	 */
	private void pause() {
		if (this.simState != 1) {return;}
		this.setSimState(2);
		this.timer.stop();
		this.timer = null;
	}

	/**
	 * Asks if the user wants to reset, and if so does
	 */
	private void promptReset() {
		this.pause();
		int option = JOptionPane.showConfirmDialog(this, "All data will be discarded!", "You sure?",
				JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			this.createSim();
			this.updateDisplay();
			this.graph.clearGraph();
		}
		;
	}

	@Override
	protected boolean closeWindowCheck() {
		this.pause();
		int option = JOptionPane.showConfirmDialog(this, "The sim will not be saved!", "You sure?", JOptionPane.OK_CANCEL_OPTION);
		return option == JOptionPane.OK_OPTION;
	}
}