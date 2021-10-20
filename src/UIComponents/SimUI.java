package UIComponents;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import SimComponents.Individual;

import Util.FileUtil;

public class SimUI extends JFrame {
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
				public void actionPerformed(ActionEvent e) {new EditorUI(SimUI.this.loadPickedFile());}
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

	private Individual loadPickedFile() {
		JFileChooser picker = new JFileChooser();
		int returnVal = picker.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {return null;}
		return FileUtil.load(picker.getSelectedFile());
	}
}
