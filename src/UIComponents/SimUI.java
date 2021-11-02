package UIComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import SimComponents.Sim;

public class SimUI extends JFrame {
	private Sim sim;

	private class Header extends JPanel {
		Header() {
			this.add(new JLabel("Sim header text here"));
		}
	}

	private class Graph extends JPanel {
		Graph() {
			
		}
		void updateGraph() {
			
		}
	}

	private class Controls extends JPanel {
		Controls() {
			JButton step = new JButton("Step >");
			step.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {SimUI.this.sim.nextGen();}
			});
			this.add(step);
		}
	}
	
	private class PopulationDisplay extends JPanel {
		void updatePopulation() {
			
		}
	}
	
	public SimUI(Sim sim) {
		super();
		this.sim = sim;
		this.setTitle("Sim");
		this.add(new SimUI.Header(), BorderLayout.NORTH);
		Graph graph = new SimUI.Graph();
		this.add(graph, BorderLayout.CENTER);
		this.add(new SimUI.Controls(), BorderLayout.SOUTH);
		PopulationDisplay population = new PopulationDisplay();
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		Timer timer = new Timer(50, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sim.nextGen();
				graph.updateGraph();
				population.updatePopulation();
			}
			
		});
	}
}
