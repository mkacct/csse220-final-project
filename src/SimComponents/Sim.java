package SimComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import Exceptions.FileFormatException;
import Main.App;
import Main.FileUtil;

/**
 * Class: Sim
 * 
 * @author R_002 <br>
 *         Holds data for a population of individuals and allows simulation of
 *         evolution
 */
public class Sim {
	public static final String[] FF_NAMES = { "All 1s", "Match target", "Consecutive 1s", "Either extreme" };
	public static final String[] SELECTOR_NAMES = { "Truncation", "Roulette wheel", "Ranked" };
	public static final String[] CROSSOVER_NAMES = { "None", "One point" };

	private ArrayList<Individual> pop;
	private ArrayList<double[]> fitnessTracker;
	private FitnessFunction fitnessCalc;
	private Comparator<Individual> compare;
	private double mutationRate;
	String selectionType;
	String crossoverMode;
	double elitism;
	boolean includesQuestionMarks;

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
		fitnessCalc = new FitnessFunctionAll1s();
		for (int i = 0; i < 100; i++) {
			pop.add(new Individual(100, new Random(), false));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = "Truncation";
		this.mutationRate = rate;
		fitnessTracker = new ArrayList<double[]>();
		double[] fitness = { this.getMinFitness(), this.getAvgFitness(), this.getMaxFitness() };
		fitnessTracker.add(fitness);
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
		fitnessCalc = new FitnessFunctionAll1s();
		for (int i = 0; i < popSize; i++) {
			pop.add(new Individual(indivSize, new Random(), false));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = "Truncation";
		this.mutationRate = rate;
		fitnessTracker = new ArrayList<double[]>();
		double[] fitness = { this.getMinFitness(), this.getAvgFitness(), this.getMaxFitness() };
		fitnessTracker.add(fitness);
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
			pop.add(new Individual(100, new Random(), false));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = selectionType;
		this.mutationRate = rate;
		fitnessTracker = new ArrayList<double[]>();
		double[] fitness = { this.getMinFitness(), this.getAvgFitness(), this.getMaxFitness() };
		fitnessTracker.add(fitness);
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
	public Sim(int popSize, int indivSize, double rate, FitnessFunction fitnessCalc, String selectionType,
			double elitism, String crossoverMode, boolean isLearning) {
		this.pop = new ArrayList<Individual>();
		this.fitnessCalc = fitnessCalc;
		for (int i = 0; i < popSize; i++) {
			pop.add(new Individual(indivSize, new Random(), isLearning));
		}
		compare = new CompareIndividual(fitnessCalc);
		this.selectionType = selectionType;
		this.mutationRate = rate;
		fitnessTracker = new ArrayList<double[]>();
		double[] fitness = { this.getMinFitness(), this.getAvgFitness(), this.getMaxFitness() };
		fitnessTracker.add(fitness);
		this.elitism = elitism;
		this.crossoverMode = crossoverMode;
		this.includesQuestionMarks = isLearning;
	}

	/**
	 * Sets population to the next generation, acquired by selecting parents using
	 * the current selection method then creating mutated chldren. Will eventually
	 * include options for crossover and elitism.
	 */
	public void nextGen() {
		ArrayList<Individual> parents = new ArrayList<Individual>();

		ArrayList<Individual> copyOver = new ArrayList<Individual>();
		int numToCopy = (int) (this.elitism * pop.size());
		while (numToCopy > 0) {
			copyOver.add(this.getBestIndividual());
			pop.remove(this.getBestIndividual());
			numToCopy--;
		}

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

		boolean odd = pop.size() % 2 == 1;
		pop = copyOver;
		if (odd) {
			Individual parent1 = this.getWorstIndividual(parents);
			parents.remove(parent1);
			pop.add(parent1.create(mutationRate));
		}

		switch (this.crossoverMode) {
		case "None":
			break;
		case "One point":
			if (parents.size() % 2 == 1) {
				Individual parent1 = this.getWorstIndividual(parents);
				parents.remove(parent1);
				pop.add(parent1.create(mutationRate));
				pop.add(parent1.create(mutationRate));
			}
			parents = this.crossoverOnePoint(parents);
			break;
		default:
			break;
		}

		for (Individual indiv : parents) {
			Individual child1 = indiv.create(mutationRate);
			Individual child2 = indiv.create(mutationRate);
			pop.add(child1);
			pop.add(child2);
		}

		double[] fitness = { this.getMinFitness(), this.getAvgFitness(), this.getMaxFitness() };
		fitnessTracker.add(fitness);
	}

	/**
	 * Returns an arraylist of individuals that have been crossed together
	 * 
	 * @param parents <br>
	 *                Requires: parents is of even length
	 * @return
	 */
	private ArrayList<Individual> crossoverOnePoint(ArrayList<Individual> parents) {
		ArrayList<Individual> newParents = new ArrayList<Individual>();
		for (int i = 0; i < parents.size() / 2; i++) {
			newParents.addAll(parents.get(i * 2).onePointCrossoverWith(parents.get(i * 2 + 1)));
		}
		return newParents;
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
		int min = 100;
		for (Individual indiv : pop) {
			if (fitnessCalc.calcFitness(indiv) < min) {
				min = fitnessCalc.calcFitness(indiv);
			}
		}
		return min;
	}

	/**
	 * Returns the average number of times a given symbol appears in the chromosomes
	 * of this population
	 * 
	 * @param symbol
	 * @return
	 */
	public double getAverageOfSymbol(char symbol) {
		double numSymbol = 0;
		for (Individual indiv : pop) {
			for (char c : indiv.getChromosome()) {
				if (c == symbol) {
					numSymbol++;
				}
			}
		}
		return numSymbol / pop.size();
	}

	/**
	 * Returns the chromosome of the individual with the best fitness. If multiple
	 * have the same fitness, returns the first of them
	 * 
	 * @return
	 */
	public Individual getBestIndividual() {
		int max = 0;
		Individual bestIndividual = pop.get(0);
		for (Individual indiv : pop) {
			if (fitnessCalc.calcFitness(indiv) > max) {
				max = fitnessCalc.calcFitness(indiv);
				bestIndividual = indiv;
			}
		}
		return bestIndividual;
	}

	/**
	 * Returns the individual with the worst fitness. If multiple have the same
	 * fitness, returns the first of them
	 * 
	 * @return
	 */
	private Individual getWorstIndividual(ArrayList<Individual> input) {
		Individual worstIndiv = input.get(0);
		int min = fitnessCalc.calcFitness(input.get(0));
		for (Individual indiv : input) {
			if (fitnessCalc.calcFitness(indiv) < min) {
				min = fitnessCalc.calcFitness(indiv);
				worstIndiv = indiv;
			}
		}
		return worstIndiv;
	}

	/**
	 * Returns the top half of the population by fitness
	 * 
	 * @return
	 */
	private ArrayList<Individual> truncationSelection() {
		pop.sort(compare);
		ArrayList<Individual> parents = new ArrayList<Individual>(pop.subList(pop.size() / 2, pop.size()));
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
		for (int i = 0, size = (int) (pop.size() / 2.0 + 0.5); i < size; i++) {
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
		int num = 0;
		pop.sort(compare);
		ArrayList<Individual> rouletteWheel = new ArrayList<Individual>();
		/*
		 * Constructs a HashMap where each individual is referenced a number of times
		 * equal to its fitness, so that when a number is randomly picked, the
		 * probability of each individual being picked is weighted based on its fitness.
		 */
		for (int j = 0; j < pop.size(); j++) {
			for (int i = 0; i < j; i++) {
				rouletteWheel.add(pop.get(j));
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
		for (int i = 0, size = (int) (pop.size() / 2.0 + 0.5); i < size; i++) {
			if (!parents.contains(rouletteWheel.get((int) (Math.random() * num)))) {
				parents.add(rouletteWheel.get((int) (Math.random() * num)));
			} else {
				i--;
			}
		}
		return parents;
	}

	/**
	 * Returns an arraylist of chromosome char arrays
	 * 
	 * @return
	 */
	public ArrayList<char[]> getChromosomes() {
		ArrayList<char[]> chromosomes = new ArrayList<char[]>();
		for (int i = 0, size = pop.size(); i < size; i++) {
			chromosomes.add(pop.get(i).getChromosome());
		}
		return chromosomes;
	}

	/**
	 * Returns the Hamming distance of the population
	 * 
	 * @return
	 */
	public double hammingDistance() {
		double numPairs = 0;
		int[] numOnes = new int[pop.get(0).getChromosome().length];
		int[] numZeroes = new int[pop.get(0).getChromosome().length];
		int[] numQs = new int[pop.get(0).getChromosome().length];
		for (int i = 0; i < pop.get(0).getChromosome().length; i++) {
			for (Individual indiv : pop) {
				char[] chromosome = indiv.getChromosome();
				if (chromosome[i] == '0') {
					numZeroes[i] = numZeroes[i] + 1;
				} else if (chromosome[i] == '1') {
					numOnes[i] = numOnes[i] + 1;
				} else if (chromosome[i] == '?') {
					numQs[i] += 1;
				}
			}
		}
		for (int i = 0; i < numOnes.length; i++) {
			numPairs += (numOnes[i] * numZeroes[i] + numOnes[i] * numQs[i] + numZeroes[i] * numQs[i]);
		}
		double hammingDistance = numPairs / (pop.size() * pop.get(0).getChromosome().length * (pop.size() - 1) / 2);
		return hammingDistance * 100; // returns hamming distance scaled to be visible on a graph from 0 to 100
										// instead of 0 to 1
	}

	/**
	 * Utility method returning the fitness function with the given name Uncertain
	 * if this is permanent
	 * 
	 * @param name the name of the fitness function
	 * @return a new instance of the fitness function
	 */
	public static FitnessFunction ffByName(String name) throws FileNotFoundException, FileFormatException {
		switch (name) {
		case "All 1s":
			return new FitnessFunctionAll1s();
		case "Match target":
			char[] target;
			target = FileUtil.loadIndiv(new File(App.SAVE_DIR + "target")).getChromosome();
			return new FitnessFunctionMatchTarget(target);
		case "Consecutive 1s":
			return new FitnessFunctionConsecutive1s();
		case "Either extreme":
			return new FitnessFunctionEitherExtreme();
		case "Magic Dance": // for the research part
			return new FitnessFunctionMagicDance();
		}
		throw new IllegalArgumentException("Invalid fitness function name \"" + name + "\"");
	}
}
