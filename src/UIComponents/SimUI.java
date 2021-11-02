package UIComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	public SimUI(Sim sim) {
		super();
		this.sim = sim;
		this.setTitle("Sim");
		this.add(new SimUI.Header(), BorderLayout.NORTH);
		this.add(new SimUI.Graph(), BorderLayout.CENTER);
		this.add(new SimUI.Controls(), BorderLayout.SOUTH);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
}
