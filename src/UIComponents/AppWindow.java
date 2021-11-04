package UIComponents;

import javax.swing.JFrame;

public abstract class AppWindow extends JFrame {
	private AppWindow parent;

	public AppWindow(AppWindow parent) {
		this.parent = parent;
	}

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

	public AppWindow getParentWindow() {return this.parent;}
}