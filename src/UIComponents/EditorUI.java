package UIComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Main.FileUtil;
import Main.MiscUtil;
import SimComponents.Individual;

/**
 * Window for editing an individual, with ChromosomeEditor and menu options
 * 
 * @author R_002
 */
public class EditorUI extends JFrame {
	private final String DEFAULT_MUTATION_RATE = "0";

	private Individual indiv;
	private File saveFile;
	private double mutationRate;
	
	private ChromosomeEditor editor;

	/**
	 * Menu bar shown at the bottom of the editor, with options for saving and
	 * mutation
	 */
	private class EditorOptions extends JPanel {
		EditorOptions() {

			JButton save = new JButton("Save");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EditorUI.this.save();
				}
			});
			this.add(save);

			JButton saveAs = new JButton("Save as...");
			saveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EditorUI.this.saveAs();
				}
			});
			this.add(saveAs);

			this.add(new JLabel("Mutation rate:"));
			
			JTextField mutate = new JTextField(EditorUI.this.DEFAULT_MUTATION_RATE, 5);
			ActionListener mutateListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EditorUI.this.mutate(MiscUtil.parseProportion(mutate.getText()));
				}
			};
			mutate.addActionListener(mutateListener);
			this.add(mutate);
			JButton mutateButton = new JButton("Mutate");
			mutateButton.addActionListener(mutateListener);
			this.add(mutateButton);
		}
	}

	/**
	 * Construct with new indiv of given size
	 * 
	 * @param size
	 */
	public EditorUI(int size) {
		this(new Individual(size, false));
	}

	/**
	 * Construct with indiv to edit
	 * 
	 * @param indiv to edit
	 */
	public EditorUI(Individual indiv) {
		super();
		this.indiv = indiv;
		this.saveFile = null;
		this.mutationRate = MiscUtil.parseProportion(this.DEFAULT_MUTATION_RATE);
		this.setup();
	}

	/**
	 * Construct by loading from file
	 * If file is invalid, show an error dialog and close the window
	 * @param saveFile to load
	 */
	public EditorUI(File saveFile) {
		super();
		Individual indiv = FileUtil.loadIndiv(saveFile);
		if (indiv == null) {
			JOptionPane.showMessageDialog(this, "Indiv file \"" + saveFile.getName() + "\" is incorrectly formatted");
			this.dispose();
			return;
		}
		this.indiv = indiv;
		this.saveFile = saveFile;
		this.mutationRate = MiscUtil.parseProportion(this.DEFAULT_MUTATION_RATE);
		this.setup();
	}

	/**
	 * Called by constructors after initializing properties
	 */
	private void setup() {
		this.editor = new ChromosomeEditor(indiv);
		this.updateWindowTitle();
		this.add(this.editor, BorderLayout.CENTER);
		this.add(new EditorOptions(), BorderLayout.SOUTH);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * Handles mutation at the given rate, and catches if the input was incorrect
	 * 
	 * @param rate
	 */
	public void mutate(double rate) {
		if (rate == -1) {
			JOptionPane.showMessageDialog(EditorUI.this, "Input contained unrecognized character");
		} else if (rate == -2) {
			JOptionPane.showMessageDialog(EditorUI.this, "Mutation rate must be between 0 and 1");
		} else {
			this.mutationRate = rate;
			EditorUI.this.editor.handleMutate(EditorUI.this.mutationRate);
		}
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
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = saver.getSelectedFile();
		this.saveFile = file;
		this.updateWindowTitle();
		this.save();
	}
}