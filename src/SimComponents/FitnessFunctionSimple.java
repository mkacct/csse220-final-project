package SimComponents;

/**
 * Calculates the fitness of an individual based on the number of 1s in its
 * chromosome
 * 
 * @author R002
 *
 */
public class FitnessFunctionSimple implements FitnessFunction {

	public FitnessFunctionSimple() {
	}

	/**
	 * Returns the number of 1s in the chromosome
	 */
	@Override
	public int calcFitness(Individual indiv) {
		char[] chromosome = indiv.getChromosome();
		int fitness = 0;
		for (char c : chromosome) {
			if (c == '1') {
				fitness++;
			}
		}
		return fitness;
	}

}
