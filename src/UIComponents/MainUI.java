package UIComponents;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

import Exceptions.FileFormatException;
import Main.App;

/**
 * Main window of the application, with title text and main menu buttons
 * @author R_002
 */
public class MainUI extends AppWindow {
	/**
	 * Titles and such
	 */
	private class Header extends JPanel {
		public Header() {
			super();
			this.setLayout(new GridLayout(3, 1));
			JLabel title = new JLabel(App.LONG_TITLE, JLabel.CENTER);
			title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			this.add(title);
			this.add(new JLabel("by " + App.TEAM_ID, JLabel.CENTER));
			this.add(new JLabel("(" + App.AUTHOR + ")", JLabel.CENTER));
		}
	}

	/**
	 * Menu buttons
	 */
	private class MainMenu extends JPanel {
		public MainMenu() {
			super();

			JButton newSim = new JButton("New Sim");
			newSim.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {MainUI.this.newSim();}
			});
			this.add(newSim);

			JButton newResearchSim = new JButton("New Sim (Research)");
			newResearchSim.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {MainUI.this.newResearchSim();}
			});
			this.add(newResearchSim);

			JButton newEditor = new JButton("New Indiv");
			newEditor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {MainUI.this.newIndiv();}
			});
			this.add(newEditor);

			JButton open = new JButton("Open Indiv (from file)");
			open.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {MainUI.this.openIndiv();}
			});
			this.add(open);
			
			this.setLayout(new GridLayout(this.getComponentCount(), 1));
		}
	}

	public MainUI() {
		super(null);
		this.setTitle(App.TITLE);
		this.add(new MainUI.Header(), BorderLayout.NORTH);
		this.add(new MainUI.MainMenu(), BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.showWindow();
	}

	/**
	 * Open a sim config window, asking the user for info to make a new sim
	 */
	private void newSim() {
		new SimConfigUI(this);
	}

	/**
	 * Open the research sim
	 */
	private void newResearchSim() {
		try {
			new LearningSimUI(this);
		} catch (FileFormatException | FileNotFoundException ex) {
			// should never happpen
			ex.printStackTrace();
			return;
		}
	}

	/**
	 * Prompt the user for a size and make a new indiv
	 */
	private void newIndiv() {
		String s = JOptionPane.showInputDialog(this, "Enter size of chromosome:");
		if (s == null) {return;}
		int size = 0;
		try {
			size = Integer.parseInt(s);
		} catch (NumberFormatException err) {
			JOptionPane.showMessageDialog(this, "Invalid size \"" + s + "\"");
			return;
		}
		if (size > 0) {
			if (size > 255) {
				int res = JOptionPane.showConfirmDialog(this, "Size is a bit large, you sure?", "Warning", JOptionPane.OK_CANCEL_OPTION);
				if (res != JOptionPane.OK_OPTION) {return;}
			}
			new EditorUI(this, size);
		} else {
			JOptionPane.showMessageDialog(this, "Size must be at least 1");
		}
	}

	/**
	 * Have the user pick a file to open an indiv
	 */
	private void openIndiv() {
		JFileChooser picker = new JFileChooser(App.SAVE_DIR);
		int returnVal = picker.showOpenDialog(MainUI.this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {return;}
		File file = picker.getSelectedFile();
		new EditorUI(this, file);
	}
}