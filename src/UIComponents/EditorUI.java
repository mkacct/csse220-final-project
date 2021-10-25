package UIComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Main.FileUtil;
import SimComponents.Individual;

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
			mutate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {indiv.mutate(EditorUI.this.mutationRate);}
			});
			this.add(mutate);

			JButton save = new JButton("Save");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {EditorUI.this.save();}
			});
			this.add(save);

			JButton saveAs = new JButton("Save as...");
			saveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {EditorUI.this.saveAs();}
			});
			this.add(saveAs);
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
		this.updateWindowTitle();
		this.add(new ChromosomeEditor(indiv), BorderLayout.CENTER);
		this.add(new EditorOptions(), BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Update the window title in accordance with the file name
	 */
	private void updateWindowTitle() {
		this.setTitle((saveFile != null) ? saveFile.getName() : "Individual");
	}

	/**
	 * Save, or if there is no file yet, save as
	 */
	private void save() {
		if (this.saveFile != null) {
			FileUtil.saveIndiv(this.saveFile, this.indiv);
		} else {
			this.saveAs();
		}
	}

	/**
	 * Save as (show save dialog)
	 */
	private void saveAs() {
		JFileChooser saver = new JFileChooser();
		int returnVal = saver.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {return;}
		File file = saver.getSelectedFile();
		this.saveFile = file;
		this.updateWindowTitle();
		this.save();
	}
}
