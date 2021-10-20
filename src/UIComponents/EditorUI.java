package UIComponents;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import SimComponents.Individual;
import Util.FileUtil;

public class EditorUI extends JFrame {
	private Individual indiv;
	private String filePath;
	private double mutationRate;

	private class EditorOptions extends JPanel {
		EditorOptions() {
			JButton mutate = new JButton("Mutate");
			JButton save = new JButton("Save");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileUtil.save(new File(EditorUI.this.filePath), EditorUI.this.indiv);
				}
			});
			mutate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {indiv.mutate(EditorUI.this.mutationRate);}
			});
			this.add(mutate);
			this.add(save);
		}
	}

	public EditorUI() {
		this(new Individual(20));
	}

	public EditorUI(Individual indiv) {
		super();
		this.indiv = indiv;
		this.mutationRate = 0.01;
		this.setup();
	}

	private void setup() {
		this.setTitle((filePath != null) ? filePath : "Individual");
		this.add(new ChromosomeEditor(indiv), BorderLayout.CENTER);
		this.add(new EditorOptions(), BorderLayout.SOUTH);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
