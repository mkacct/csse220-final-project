package UIComponents;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A window used in this app
 * Allows for simple initial positioning and close confirmatinons
 * @author R_002
 */
public abstract class AppWindow extends JFrame {
	private AppWindow parent;

	public AppWindow(AppWindow parent) {
		this.parent = parent;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (AppWindow.this.closeWindowCheck()) {AppWindow.this.dispose();}
			}
		});
	}

	/**
	 * Position the window correctly and display it
	 */
	public void showWindow() {
		this.pack();
		this.setResizable(false);
		if (this.parent != null) {
			this.setLocationRelativeTo(parent);
		} else {
			this.setLocationByPlatform(true);
		}
		this.setVisible(true);
	}

	/**
	 * Called when the user tries to close the window
	 * Override to make a close confirmation
	 * @return whether to actually close the window
	 */
	protected boolean closeWindowCheck() {return true;}

	public AppWindow getParentWindow() {return this.parent;}
}