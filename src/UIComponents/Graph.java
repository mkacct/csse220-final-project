package UIComponents;

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
	Color[] colors;
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
	public Graph(double[] initialData, Color[] colors) {
		dataPoints = new ArrayList<double[]>();
		dataPoints.add(initialData);
		this.setPreferredSize(new Dimension(110, 100));
		this.colors = colors;
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
		Graphics2D g2d = (Graphics2D) g;
		this.removeAll();
		this.setPreferredSize(new Dimension(130, 30 + 20 * (Math.max(dataPoints.size(), 10))));
		this.drawAxes(g2d);
		for (int j = 0; j < dataPoints.size() - 1; j++) {
			this.drawMyLine(j, g2d);
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
		int tickSize = 3;
		g.drawRect(originX, 10, graphWidth, graphHeight);
		int numVerticalTicks = 10;
		for (int i = 0; i <= numVerticalTicks; i++) {
			g.drawString(Integer.toString(i * 10), 0, originY + 10 - (int) (i * graphHeight / numVerticalTicks));
			g.drawLine(originX-tickSize, originY - (int) (i * graphHeight / numVerticalTicks), originX+tickSize, originY - (int) (i * graphHeight / numVerticalTicks));
		}
		int tickWidth = Math.max(dataPoints.size()/10, 10)-Math.max(dataPoints.size()/10, 10)%10;
		for (int i = 1; i*tickWidth <= Math.max(100,dataPoints.size()); i++) {
			g.drawString(Integer.toString(i * tickWidth),
					originX + i*tickWidth * graphWidth / (Math.max(dataPoints.size(), 100))-10, this.getHeight());
			g.drawLine(originX + i*tickWidth * graphWidth / (Math.max(dataPoints.size(), 100)), originY - 3,
					originX + i*tickWidth * graphWidth / (Math.max(dataPoints.size(), 100)), originY + 3);
		}
	}

	/**
	 * Draws the lines on the graph
	 * 
	 * @param index
	 */
	private void drawMyLine(int index, Graphics2D g2d) {
		for(int line = 0; line < colors.length; line++) {
			double startFitness = this.dataPoints.get(index)[line];
			double endFitness = this.dataPoints.get(index + 1)[line];
			int xInitial = originX + index * graphWidth / (Math.max(dataPoints.size(), 100));
			int xFinal = originX + (index + 1) * graphWidth / (Math.max(dataPoints.size(), 100));
			int yInitial = originY - (int) (startFitness * graphHeight / 100);
			int yFinal = originY - (int) (endFitness * graphHeight / 100);
			Line2D lineToDraw = new Line2D.Double(xInitial, yInitial, xFinal, yFinal);
			g2d.setColor(colors[line]);
			g2d.draw(lineToDraw);
		}
	}
}

