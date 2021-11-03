package UIComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import Exceptions.DomainException;
import SimComponents.FitnessFunction;
import SimComponents.Sim;

public class SimUI extends JFrame {
	private final int popSize, chromosomeSize;
	private final String ffName, selectionMode, crossoverMode;
	private final double mutationRate;
	private final FitnessFunction fitnessFunction;

	private final Graph graph;
	private final Controls controls;
	private final PopulationDisplay populationDisplay;
	private Timer timer;

	// 0 = not started, 1 = running, 2 = paused
	private int simState;

	private Sim sim;

	private class Header extends JPanel {
		public Header() {
			this.add(new JLabel(
				"Population size: " + SimUI.this.popSize
				+ " \u2013 Chromosome size: " + SimUI.this.chromosomeSize
				+ " \u2013 Fitness function: " + SimUI.this.ffName
				+ " \u2013 Selection: " + SimUI.this.selectionMode
				+ " \u2013 Crossover: " + SimUI.this.crossoverMode
				+ " \u2013 Mutation rate: " + SimUI.this.mutationRate
			));
		}
	}

	private class Graph extends JPanel {
		public Graph() {
			
		}

		void updateGraph() {

		}
	}

	private class ChromosomeDisplay extends JComponent {
		private char[] chromosome;

		public ChromosomeDisplay(char[] chromosome) {
			super();
			this.chromosome = chromosome;
			this.setPreferredSize(new Dimension(30,30));
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			for(int num = 0; num < chromosome.length; num++) {
					Color c;
					switch(chromosome[num]) {
						case '0':
							c = UIComponents.ChromosomeEditor.ZERO_COLOR;
							break;
						case '1':
							c = UIComponents.ChromosomeEditor.ONE_COLOR;
							break;
						case '?':
							c = UIComponents.ChromosomeEditor.Q_COLOR;
							break;
						default:
							c = Color.black;
					}
					g.setColor(c);
					int x = (int)(num % Math.sqrt(chromosome.length)*(int)(this.getWidth()/Math.sqrt(chromosome.length)));
					int y = (int)(num / Math.sqrt(chromosome.length))*(int)(this.getHeight()/Math.sqrt(chromosome.length));
					int width = (int)(this.getWidth()/Math.sqrt(chromosome.length));
					int height = (int)(this.getHeight()/Math.sqrt(chromosome.length));
					g.fillRect(x, y, width, height);
			}
			g.setColor(Color.black);
			g.drawRect(0,  0,  this.getWidth(), this.getHeight());
		}
	}

	private class Controls extends JPanel {
		private final String[] startStopButtonText = {"Start", "Pause", "Resume"};

		private JButton startStop, step, reset;
		private JTextField tickRate;

		public Controls() {
			this.startStop = new JButton("Start");
			this.startStop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {SimUI.this.startStop();}
			});
			this.add(this.startStop);

			this.step = new JButton("Step >");
			this.step.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {SimUI.this.tick();}
			});
			this.add(this.step);

			this.reset = new JButton("Reset");
			this.reset.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {SimUI.this.promptReset();}

			});
			this.add(this.reset);

			this.add(new JLabel("Tick rate (Hz):"));
			this.tickRate = new JTextField("2", 3);
			this.add(this.tickRate);

			this.updateElements();
		}

		public void updateElements() {
			this.startStop.setEnabled(true);
			this.startStop.setText(this.startStopButtonText[SimUI.this.simState]);
			this.step.setEnabled(SimUI.this.simState != 1);
			this.reset.setEnabled(SimUI.this.simState != 0);
			this.tickRate.setEnabled(SimUI.this.simState != 1);
		}

		public double getTickRate() throws NumberFormatException {return Double.parseDouble(this.tickRate.getText());}
	}

	private class PopulationDisplay extends JPanel {
		ArrayList<char[]>chromosomes;
		
		public PopulationDisplay(ArrayList<char[]>chromosomes) {
			GridLayout grid = new GridLayout(0, (int) Math.sqrt(chromosomes.get(0).length));
			grid.setHgap(0);
			grid.setVgap(0);
			this.setLayout(grid);
			this.updatePopulation(chromosomes);
		}
		
		void updatePopulation(ArrayList<char[]> chromosomes) {
			this.chromosomes = chromosomes;
			this.removeAll();
			for(int i = 0; i < chromosomes.size(); i++) {
				this.add(new ChromosomeDisplay(chromosomes.get(i)));
			}
		}	
	}

	private void createSim() {
		if (this.timer != null) {
			this.timer.stop();
			this.timer = null;
		}
		this.sim = new Sim(
			this.popSize,
			this.chromosomeSize,
			this.mutationRate,
			this.fitnessFunction,
			this.selectionMode
		);
		this.setSimState(0);
	}

	private void setSimState(int state) {
		this.simState = state;
		this.controls.updateElements();
	}

	private void tick() {
		if (this.simState == 0) {this.setSimState(2);}
		this.sim.nextGen();
		this.populationDisplay.updatePopulation(sim.getChromosomes());
	}

	private void startStop() {
		if (this.simState != 1) {
			// start
			double tickRate;
			try {
				tickRate = this.controls.getTickRate();
				if (tickRate <= 0) {throw new DomainException();}
			} catch (NumberFormatException | DomainException ex) {
				JOptionPane.showMessageDialog(this, "Tick rate must be a positive integer");
				return;
			}
			// validation ok
			this.setSimState(1);
			this.timer = new Timer((int) ((1. / tickRate) * 1000.), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("timer at " + System.currentTimeMillis());
					SimUI.this.tick();
				}
			});
			this.timer.start();
		} else {
			// pause
			this.setSimState(2);
			this.timer.stop();
			this.timer = null;
		}
	}

	private void promptReset() {
		int option = JOptionPane.showConfirmDialog(this, "All data and indivs will be discarded!", "You sure?", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			createSim();
		};
	}
	
	public SimUI(int popSize, int chromosomeSize, String ffName, String selectionMode, String crossoverMode, double mutationRate) {
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
		this.graph = new SimUI.Graph();
		this.add(this.graph, BorderLayout.CENTER);
		this.controls = new SimUI.Controls();
		this.add(this.controls, BorderLayout.SOUTH);

		createSim();
		
		this.populationDisplay = new PopulationDisplay(this.sim.getChromosomes());
		this.add(this.populationDisplay, BorderLayout.EAST);

		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
}