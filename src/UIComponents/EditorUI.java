package UIComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Exceptions.DomainException;
import Exceptions.FileFormatException;
import Main.FileUtil;
import Main.MiscUtil;
import SimComponents.Individual;

/**
 * Window for editing an individual, with ChromosomeEditor and menu options
 * 
 * @author R_002
 */
public class EditorUI extends JFrame {
	private Individual indiv;
	private File saveFile;
	private ChromosomeEditor editor;

	/**
	 * Menu bar shown at the bottom of the editor, with options for saving and mutation
	 */
	private class EditorOptions extends JPanel {
		public EditorOptions() {
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
			
			JTextField mutate = new JTextField("0", 5);
			ActionListener mutateListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EditorUI.this.mutate(mutate.getText());
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
	 * Construct with a new indiv of given size
	 * 
	 * @param size
	 */
	public EditorUI(int size) {
		this(new Individual(size));
	}

	/**
	 * Construct with an indiv to edit
	 * 
	 * @param indiv to edit
	 */
	public EditorUI(Individual indiv) {
		super();
		this.indiv = indiv;
		this.saveFile = null;
		this.setup();
	}

	/**
	 * Construct by loading an indiv from file
	 * If file is invalid, show an error dialog and close the window
	 * @param saveFile to load
	 */
	public EditorUI(File saveFile) {
		super();
		Individual indiv = null;
		try {
			indiv = FileUtil.loadIndiv(saveFile);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace(); // shouldn't happen
			this.dispose();
			return;
		} catch (FileFormatException ex) {
			JOptionPane.showMessageDialog(this, "Indiv file \"" + saveFile.getName() + "\" is incorrectly formatted");
			this.dispose();
			return;
		}
		this.indiv = indiv;
		this.saveFile = saveFile;
		this.setup();
	}

	/**
	 * Called by constructors after initializing properties
	 */
	private void setup() {
		this.editor = new ChromosomeEditor(indiv);
		this.updateWindowTitle();
		this.add(this.editor, BorderLayout.CENTER);
		this.add(new EditorUI.EditorOptions(), BorderLayout.SOUTH);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * Handles mutation at the given rate, and catches if the input was incorrect
	 * 
	 * @param rate
	 */
	public void mutate(String rate) {
		double parsedRate;
		try {
			parsedRate = MiscUtil.parseProportion(rate);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(EditorUI.this, "Input contained unrecognized character");
			return;
		} catch (DomainException ex) {
			JOptionPane.showMessageDialog(EditorUI.this, "Mutation rate must be between 0 and 1");
			return;
		}
		this.editor.handleMutate(parsedRate);
		// this.setTitle(this.getTitle().contains("mutated") ? this.getTitle() : this.getTitle() + " (mutated)");
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