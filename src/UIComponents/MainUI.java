package UIComponents;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Main.App;

/**
 * Main window of the application
 * @author R_002
 */
public class MainUI extends JFrame {
	/**
	 * Titles and such
	 */
	private class Header extends JPanel {
		Header() {
			super();
			this.setLayout(new GridLayout(3, 1));
			JLabel title = new JLabel(App.TITLE, JLabel.CENTER);
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
		MainMenu() {
			super();
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
					int returnVal = picker.showOpenDialog(MainUI.this);
					if (returnVal != JFileChooser.APPROVE_OPTION) {return;}
					File file = picker.getSelectedFile();
					new EditorUI(file);
				}
			});
			this.add(newEditor);
			this.add(open);
			this.setLayout(new GridLayout(this.getComponentCount(), 1));
		}
	}

	public MainUI() {
		super();
		this.setTitle(App.TITLE);
		this.add(new Header(), BorderLayout.NORTH);
		this.add(new MainMenu(), BorderLayout.SOUTH);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
