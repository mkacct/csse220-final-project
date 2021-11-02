package SimComponents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class Sim {
	public static final String[] FF_NAMES = { "Simple" };
	public static final String[] SELECTOR_NAMES = { "Truncation", "Roulette wheel", "Ranked" };
	public static final String[] CROSSOVER_NAMES = { "None", "One point" };

	private ArrayList<Individual> pop;
	private FitnessFunction fitnessCalc;
	private Comparator<Individual> compare;
	private double mutationRate;
	String selectionType;
	boolean crossover;
	int elitism;

	/**
	 * Creates a new Sim with population 100 of chromosomes of size 100, with a
	 * mutation rate of rate, a simple fitness function, and a truncation selection
	 * type
	 * 
	 * @param rate <br>
	 *             Constraints: 0 <= rate <= 1
	 */
	public Sim(double rate) {
		pop = new ArrayList<Individual>();
		fitnessCalc = new FitnessFunctionSimple();
		for (int i = 0; i < 100; i++) {
			pop.add(new Individual(100, new Random()));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = "Truncation";
		this.mutationRate = rate;
	}

	/**
	 * Creates a new Sim with population popSize of chromosomes of size indivSize,
	 * with a mutation rate of rate, a simple fitness function, and a truncation
	 * selection type
	 * 
	 * @param popSize
	 * @param indivSize
	 * @param rate
	 */
	public Sim(int popSize, int indivSize, double rate) {
		pop = new ArrayList<Individual>();
		fitnessCalc = new FitnessFunctionSimple();
		for (int i = 0; i < popSize; i++) {
			pop.add(new Individual(indivSize, new Random()));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = "Truncation";
		this.mutationRate = rate;
	}

	/**
	 * Creates a new Sim with population 100 of chromosomes of size 100, with the
	 * given fitness function and selection type.
	 * 
	 * @param rate        <br>
	 *                    Constraints: 0 <= rate <= 1
	 * @param fitnessCalc
	 */
	public Sim(double rate, FitnessFunction fitnessCalc, String selectionType) {
		this.pop = new ArrayList<Individual>();
		this.fitnessCalc = fitnessCalc;
		for (int i = 0; i < 100; i++) {
			pop.add(new Individual(100, new Random()));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = selectionType;
		this.mutationRate = rate;
	}

	/**
	 * Creates a new Sim with population popSize of chromosomes of size indivSize,
	 * with a mutation rate of rate, and the given fitness function and selection
	 * type.
	 * 
	 * @param popSize       <br>
	 *                      Constraints: popSize >= 0
	 * @param indivSize     <br>
	 *                      Constraints: indivSize >= 0
	 * @param rate          <br>
	 *                      Constraints: 0 <= rate <= 1
	 * @param fitnessCalc
	 * @param selectionType
	 */
	public Sim(int popSize, int indivSize, double rate, FitnessFunction fitnessCalc, String selectionType) {
		this.pop = new ArrayList<Individual>();
		this.fitnessCalc = fitnessCalc;
		for (int i = 0; i < popSize; i++) {
			pop.add(new Individual(indivSize, new Random()));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = selectionType;
		this.mutationRate = rate;
	}

	/**
	 * Sets population to the next generation, acquired by selecting parents using
	 * the current selection method then creating mutated chldren. Will eventually
	 * include options for crossover and elitism.
	 */
	public void nextGen() {
		ArrayList<Individual> parents = new ArrayList<Individual>();
		switch (selectionType) {
		case "Truncation":
			parents = this.truncationSelection();
			break;
		case "Roulette wheel":
			parents = this.rouletteSelection();
			break;
		case "Ranked":
			parents = this.rankedSelection();
			break;
		}
		pop = new ArrayList<Individual>();
		for (Individual indiv : parents) {
			Individual child1 = indiv.create(mutationRate);
			Individual child2 = indiv.create(mutationRate);
			pop.add(child1);
			pop.add(child2);
		}
	}

	/**
	 * Cycles through the next n generations
	 * 
	 * @param n
	 */
	public void nextGens(int n) {
		for (int i = 0; i < n; i++) {
			this.nextGen();
		}
	}

	/**
	 * Cycles through the generations until the max fitness reaches the fitness
	 * entered.
	 * 
	 * @param fitness <br>
	 *                Constraints: 0 <= fitness <= 100
	 */
	public void nextGensUntil(int fitness) {
		while (this.getMaxFitness() < fitness) {
			this.nextGen();
		}
	}

	/**
	 * Returns the fitness of the most fit chromosome
	 * 
	 * @return
	 */
	public int getMaxFitness() {
		int max = 0;
		for (Individual indiv : pop) {
			if (fitnessCalc.calcFitness(indiv) > max) {
				max = fitnessCalc.calcFitness(indiv);
			}
		}
		return max;
	}

	/**
	 * Returns the average fitness of all the chromosomes
	 * 
	 * @return
	 */
	public double getAvgFitness() {
		int sum = 0;
		for (Individual indiv : pop) {
			sum += fitnessCalc.calcFitness(indiv);
		}
		return (double) (sum / pop.size());
	}

	/**
	 * Returns the min fitness of all the chromosomes
	 * 
	 * @return
	 */
	public int getMinFitness() {
		int min = 0;
		for (Individual indiv : pop) {
			if (fitnessCalc.calcFitness(indiv) < min) {
				min = fitnessCalc.calcFitness(indiv);
			}
		}
		return min;
	}

	/**
	 * Returns the top half of the population by fitness
	 * 
	 * @return
	 */
	private ArrayList<Individual> truncationSelection() {
		pop.sort(compare);
		ArrayList<Individual> parents = (ArrayList<Individual>) pop.subList(pop.size() / 2, pop.size());
		return parents;
	}

	/**
	 * Will return half of the population selected in proportion to their fitness
	 * 
	 * @return
	 */
	private ArrayList<Individual> rouletteSelection() {
		int num = 0;
		ArrayList<Individual> rouletteWheel = new ArrayList<Individual>();
		/*
		 * Constructs a HashMap where each individual is referenced a number of times
		 * equal to its fitness, so that when a number is randomly picked, the
		 * probability of each individual being picked is weighted based on its fitness.
		 */
		for (Individual indiv : pop) {
			int fitness = fitnessCalc.calcFitness(indiv);
			for (int i = 0; i < fitness; i++) {
				rouletteWheel.add(indiv);
				num++;
			}
		}
		ArrayList<Individual> parents = new ArrayList<Individual>();
		/*
		 * Randomly picks individuals with probabilities weighted by their fitness, adds
		 * each individual to the parents list if it doesn't have it, and if it does,
		 * prevents i from incrementing to ensure the right number of individuals end up
		 * in the parents list.
		 */
		for (int i = 0, size = pop.size() / 2; i < size; i++) {
			if (!parents.contains(rouletteWheel.get((int) (Math.random() * num)))) {
				parents.add(rouletteWheel.get((int) (Math.random() * num)));
			} else {
				i--;
			}
		}
		return parents;
	}

	/**
	 * Will implement ranked selection
	 * 
	 * @return
	 */
	private ArrayList<Individual> rankedSelection() {
		return pop;
	}
}


/*
 * Add a way to store past min, max, and average values
 * figure out ranked voting
 * work on displaying population
 * Work on graph if maddie hasn't
 * */
