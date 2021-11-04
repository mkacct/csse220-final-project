package SimComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * Shows line graph of min, average, and max fitness over time
 * 
 * @author R_002
 *
 */
public class Graph extends JComponent {
	ArrayList<double[]> dataPoints;
	ArrayList<Line2D> minLine;
	ArrayList<Line2D> avgLine;
	ArrayList<Line2D> maxLine;
	int originX;
	int originY;
	int graphHeight;
	int graphWidth;

	/**
	 * Constructs a graph
	 * 
	 * @param initialData <br>
	 *                    a double array of length 3 that has the initial min, max,
	 *                    and average fitnesses for the population
	 */
	public Graph(double[] initialData) {
		dataPoints = new ArrayList<double[]>();
		dataPoints.add(initialData);
		this.setPreferredSize(new Dimension(110, 100));
		minLine = new ArrayList<Line2D>();
		avgLine = new ArrayList<Line2D>();
		maxLine = new ArrayList<Line2D>();
	}

	/**
	 * Adds a set of min, avg, and max data to the graph
	 * 
	 * @param data
	 */
	public void updateGraph(double[] data) {
		dataPoints.add(data);
	}

	/**
	 * Clears all the lines from the graph
	 */
	public void clearGraph() {
		dataPoints = new ArrayList<double[]>();
	}

	/**
	 * Draws the graph based on its stored lines
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		minLine = new ArrayList<Line2D>();
		avgLine = new ArrayList<Line2D>();
		maxLine = new ArrayList<Line2D>();
		Graphics2D g2d = (Graphics2D) g;
		this.removeAll();
		this.setPreferredSize(new Dimension(130, 30 + 20 * (Math.max(dataPoints.size(), 10))));
		this.drawAxes(g2d);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < dataPoints.size() - 1; j++) {
				this.addLine(j, i);
			}
		}
		for (int i = 0; i < this.minLine.size(); i++) {
			g2d.setColor(Color.red);
			g2d.draw(this.minLine.get(i));
			g2d.setColor(Color.blue);
			g2d.draw(this.avgLine.get(i));
			g2d.setColor(Color.green);
			g2d.draw(this.maxLine.get(i));
		}
	}

	/**
	 * Draws axes for the graph
	 * 
	 * @param g
	 */
	private void drawAxes(Graphics2D g) {
		g.setColor(Color.black);
		graphWidth = this.getWidth() - 30;
		graphHeight = this.getHeight() - 20;
		originX = 20;
		originY = graphHeight + 10;
		g.drawRect(20, 10, graphWidth, graphHeight);
		for (int i = 0; i <= 10; i++) {
			g.drawString(Integer.toString(i * 10), 0, originY + 10 - (int) (i * graphHeight / 10));
			g.drawLine(17, originY - (int) (i * graphHeight / 10), 23, originY - (int) (i * graphHeight / 10));
		}
		for (int i = 1; i <= Math.max(dataPoints.size(), 10); i++) {
			g.drawString(Integer.toString(i * 10),
					10 + (int) (i * graphWidth / Math.max(dataPoints.size() / 10, 10)), this.getHeight());
			g.drawLine(20 + (int) (i * graphWidth / Math.max(dataPoints.size() / 10, 10)), originY - 3,
					20 + (int) (i * graphWidth / Math.max(dataPoints.size() / 10, 10)), originY + 3);
		}
	}

	/**
	 * Draws the lines on the graph
	 * 
	 * @param index
	 * @param line
	 */
	private void addLine(int index, int line) {
		double startFitness = this.dataPoints.get(index)[line];
		double endFitness = this.dataPoints.get(index + 1)[line];
		int xInitial = originX + index * graphWidth / (Math.max(dataPoints.size(), 100));
		int xFinal = originX + (index + 1) * graphWidth / (Math.max(dataPoints.size(), 100));
		int yInitial = originY - (int) (startFitness * graphHeight / 100);
		int yFinal = originY - (int) (endFitness * graphHeight / 100);
		Line2D lineToAdd = new Line2D.Double(xInitial, yInitial, xFinal, yFinal);
		switch (line) {
		case 0:
			minLine.add(lineToAdd);
			break;
		case 1:
			avgLine.add(lineToAdd);
			break;
		case 2:
			maxLine.add(lineToAdd);
			break;
		}
	}
}

