package UIComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import Exceptions.DomainException;
import Main.App;
import SimComponents.FitnessFunction;
import SimComponents.Sim;

/**
 * Window for actually using the sim
 * 
 * @author R_002
 */
public class SimUI extends JFrame {
	private final int popSize, chromosomeSize;
	private final String ffName, selectionMode, crossoverMode;
	private final double mutationRate;
	private final FitnessFunction fitnessFunction;

	private final Graph graph;
	private final Controls controls;
	private final PopulationDisplay populationDisplay;
	private Timer timer;
	private int loops;

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
					+ SimUI.this.crossoverMode + " \u2013\u2013 Mutation rate: " + SimUI.this.mutationRate));
		}
	}

	/**
	 * Shows line graph of min, average, and max fitness over time
	 * 
	 * @author R_002
	 *
	 */
	private class Graph extends JComponent {
		ArrayList<double[]> dataPoints;
		ArrayList<Line2D> minLine;
		ArrayList<Line2D> avgLine;
		ArrayList<Line2D> maxLine;
		int originX;
		int originY;
		int graphHeight;
		int graphWidth;

		/**
		 * Constructs a graph
		 * 
		 * @param initialData <br>
		 *                    a double array of length 3 that has the initial min, max,
		 *                    and average fitnesses for the population
		 */
		public Graph(double[] initialData) {
			dataPoints = new ArrayList<double[]>();
			dataPoints.add(initialData);
			this.setPreferredSize(new Dimension(110, 100));
			minLine = new ArrayList<Line2D>();
			avgLine = new ArrayList<Line2D>();
			maxLine = new ArrayList<Line2D>();
		}

		/**
		 * Adds a set of min, avg, and max data to the graph
		 * 
		 * @param data
		 */
		void updateGraph(double[] data) {
			dataPoints.add(data);
		}

		/**
		 * Clears all the lines from the graph
		 */
		void clearGraph() {
			dataPoints = new ArrayList<double[]>();
		}

		/**
		 * Draws the graph based on its stored lines
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			minLine = new ArrayList<Line2D>();
			avgLine = new ArrayList<Line2D>();
			maxLine = new ArrayList<Line2D>();
			Graphics2D g2d = (Graphics2D) g;
			this.removeAll();
			this.setPreferredSize(new Dimension(130, 30 + 20 * (Math.max(dataPoints.size(), 10))));
			this.drawAxes(g2d);
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < dataPoints.size() - 1; j++) {
					this.addLine(j, i);
				}
			}
			for (int i = 0; i < this.minLine.size(); i++) {
				g2d.setColor(Color.red);
				g2d.draw(this.minLine.get(i));
				g2d.setColor(Color.blue);
				g2d.draw(this.avgLine.get(i));
				g2d.setColor(Color.green);
				g2d.draw(this.maxLine.get(i));
			}
		}

		/**
		 * Draws axes for the graph
		 * 
		 * @param g
		 */
		private void drawAxes(Graphics2D g) {
			g.setColor(Color.black);
			graphWidth = this.getWidth() - 30;
			graphHeight = this.getHeight() - 20;
			originX = 20;
			originY = graphHeight + 10;
			g.drawRect(20, 10, graphWidth, graphHeight);
			for (int i = 0; i <= 10; i++) {
				g.drawString(Integer.toString(i * 10), 0, originY + 10 - (int) (i * graphHeight / 10));
				g.drawLine(17, originY - (int) (i * graphHeight / 10), 23, originY - (int) (i * graphHeight / 10));
			}
			for (int i = 1; i <= Math.max(dataPoints.size(), 10); i++) {
				g.drawString(Integer.toString(i * 10),
						10 + (int) (i * graphWidth / Math.max(dataPoints.size() / 10, 10)), this.getHeight());
				g.drawLine(20 + (int) (i * graphWidth / Math.max(dataPoints.size() / 10, 10)), originY - 3,
						20 + (int) (i * graphWidth / Math.max(dataPoints.size() / 10, 10)), originY + 3);
			}
		}

		/**
		 * Draws the lines on the graph
		 * 
		 * @param index
		 * @param line
		 */
		private void addLine(int index, int line) {
			double startFitness = this.dataPoints.get(index)[line];
			double endFitness = this.dataPoints.get(index + 1)[line];
			int xInitial = originX + index * graphWidth / (Math.max(dataPoints.size(), 100));
			int xFinal = originX + (index + 1) * graphWidth / (Math.max(dataPoints.size(), 100));
			int yInitial = originY - (int) (startFitness * graphHeight / 100);
			int yFinal = originY - (int) (endFitness * graphHeight / 100);
			Line2D lineToAdd = new Line2D.Double(xInitial, yInitial, xFinal, yFinal);
			switch (line) {
			case 0:
				minLine.add(lineToAdd);
				break;
			case 1:
				avgLine.add(lineToAdd);
				break;
			case 2:
				maxLine.add(lineToAdd);
				break;
			}
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
	}

	/**
	 * Controls shown at the bottom of the sim Includes start/stop button, step and
	 * reset buttons, and fields for customizing running
	 */
	private class Controls extends JPanel {
		private static final String[] START_STOP_BUTTON_TEXT = { "Start", "Pause", "Resume" };
		private static final String[] RUN_MODES = { "Run forever", "Number of gens:", "Max fitness:" };

		private JButton startStop, step, reset;
		private JComboBox runMode;
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

			this.runMode = new JComboBox<String>(Controls.RUN_MODES);
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
			this.tickRate = new JTextField("2", 3);
			this.add(this.tickRate);

			this.updateElements();
		}

		/**
		 * Updates the state (mostly enabled state) of the control UI elements
		 */
		public void updateElements() {
			this.startStop.setEnabled(true);
			this.startStop.setText(Controls.START_STOP_BUTTON_TEXT[SimUI.this.simState]);
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
		ArrayList<char[]> chromosomes;

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
			this.chromosomes = chromosomes;
			this.removeAll();
			for (int i = 0; i < chromosomes.size(); i++) {
				this.add(new ChromosomeDisplay(chromosomes.get(i)));
			}
		}
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
				this.selectionMode);
		this.loops = 0;
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
		this.updateDisplay();
	}

	/**
	 * Updates the display
	 */
	private void updateDisplay() {
		this.populationDisplay.updatePopulation(sim.getChromosomes());
		double[] data = { this.sim.getMinFitness(), this.sim.getAvgFitness(), this.sim.getMaxFitness() };
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
				if (SimUI.this.loops >= SimUI.this.controls.getEndConditionValue()) {
					SimUI.this.startStop();
					return;
				}
				break;
			case 2: // max fitness
				if (SimUI.this.sim.getMaxFitness() >= SimUI.this.controls.getEndConditionValue()) {
					SimUI.this.startStop();
					return;
				}
			}
			SimUI.this.tick();
			SimUI.this.loops++;
		}
	}

	/**
	 * Starts or stops the sim If starting, does validation of the user input first
	 */
	private void startStop() {
		if (this.simState != 1) {
			// start
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
			this.loops = 0;
			this.timer.start();
		} else {
			// pause
			this.setSimState(2);
			this.timer.stop();
			this.timer = null;
			this.loops = 0;
		}
	}

	/**
	 * Asks if the user wants to reset, and if so does
	 */
	private void promptReset() {
		int option = JOptionPane.showConfirmDialog(this, "All data and indivs will be discarded!", "You sure?",
				JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			this.createSim();
			this.updateDisplay();
			this.graph.clearGraph();
		}
		;
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
	 */
	public SimUI(int popSize, int chromosomeSize, String ffName, String selectionMode, String crossoverMode,
			double mutationRate) {
		super();

		this.popSize = popSize;
		this.chromosomeSize = chromosomeSize;
		this.ffName = ffName;
		this.fitnessFunction = Sim.ffByName(this.ffName);
		this.selectionMode = selectionMode;
		this.crossoverMode = crossoverMode;
		this.mutationRate = mutationRate;

		this.setTitle("Sim");
		this.add(new SimUI.Header(), BorderLayout.NORTH);

		this.controls = new SimUI.Controls();
		this.add(this.controls, BorderLayout.SOUTH);

		createSim();

		this.populationDisplay = new PopulationDisplay(this.sim.getChromosomes());
		this.add(this.populationDisplay, BorderLayout.EAST);
		double[] data = { this.sim.getMinFitness(), this.sim.getAvgFitness(), this.sim.getMaxFitness() };
		this.graph = new SimUI.Graph(data);
		this.add(this.graph, BorderLayout.CENTER);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
}