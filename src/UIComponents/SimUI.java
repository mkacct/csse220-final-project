package UIComponents;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main window of the application
 * @author R_002
 */
public class SimUI extends JFrame {
	/**
	 * Menu bar shown at the bottom of the main window
	 */
	private class SimOptions extends JPanel {
		SimOptions() {
			JButton newEditor = new JButton("New Indiv");
			JButton open = new JButton("Open Indiv (from file)");
			newEditor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {new EditorUI();}
			});
			open.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser picker = new JFileChooser();
					int returnVal = picker.showOpenDialog(SimUI.this);
					if (returnVal != JFileChooser.APPROVE_OPTION) {return;}
					File file = picker.getSelectedFile();
					new EditorUI(file);
				}
			});
			this.add(newEditor);
			this.add(open);
		}
	}

	public SimUI() {
		super();
		this.setTitle("Genetic Algorithm Sim");
		this.add(new Label("put ui here"), BorderLayout.CENTER);
		this.add(new SimOptions(), BorderLayout.SOUTH);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
