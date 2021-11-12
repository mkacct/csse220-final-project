package UIComponents;

import java.awt.Color;
import java.io.FileNotFoundException;

import Exceptions.FileFormatException;
import Main.App;
import SimComponents.Sim;

public class LearningSimUI extends SimUI {

	public LearningSimUI(AppWindow parent)
			throws FileNotFoundException, FileFormatException {
		super(parent, 1000, 20, "Magic Dance", "Truncation", "One point", 0, 0);
	}
	
	@Override
	public double[] updateData(Sim sim) {
		double[] data = { sim.getMinFitness(), sim.getAvgFitness(), sim.getMaxFitness(), sim.hammingDistance(), sim.getAverageOfSymbol('0'), sim.getAverageOfSymbol('1'), sim.getAverageOfSymbol('?') };
		return data;
	}
	
	@Override
	public Color[] graphColors() {
		Color[] colors = {Color.red, Color.blue, Color.green, Color.yellow, App.ZERO_COLOR, App.ONE_COLOR, App.Q_COLOR};
		return colors;
	}

	@Override
	public String[] graphLabels() {
		String[] labels = {"Min fitness", "Avg fitness", "Max fitness", "Hamming distance", "Number of 0s", "Number of 1s", "Number of ?s"};
		return labels;
	}
	
	@Override
	public boolean includesQuestionMarks() {
		return true;
	}
}
