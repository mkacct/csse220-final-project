package UIComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import SimComponents.Individual;
import Util.FileUtil;

/**
 * Window for editing an individual
 * @author R_002
 */
public class EditorUI extends JFrame {
	private Individual indiv;
	private File saveFile;
	private double mutationRate;

	/**
	 * Menu bar shown at the bottom of the editor
	 */
	private class EditorOptions extends JPanel {
		EditorOptions() {
			JButton mutate = new JButton("Mutate");
			JButton save = new JButton("Save");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileUtil.saveIndiv(EditorUI.this.saveFile, EditorUI.this.indiv);
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

	/**
	 * Construct with indiv to edit
	 * @param indiv to edit
	 */
	public EditorUI(Individual indiv) {
		super();
		this.indiv = indiv;
		this.saveFile = null;
		this.mutationRate = 0.01;
		this.setup();
	}

	/**
	 * Construct by loading from file
	 * @param saveFile to load
	 */
	public EditorUI(File saveFile) {
		super();
		this.indiv = FileUtil.loadIndiv(saveFile);
		this.saveFile = saveFile;
		this.mutationRate = 0.01;
		this.setup();
	}

	/**
	 * Called by constructors after initializing properties
	 */
	private void setup() {
		this.setTitle((saveFile != null) ? saveFile.getName() : "Individual");
		this.add(new ChromosomeEditor(indiv), BorderLayout.CENTER);
		this.add(new EditorOptions(), BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
}
