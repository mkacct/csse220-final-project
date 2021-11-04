package SimComponents;

/**
 * Class: FitnessFunctionMatchTarget implements FitnessFunction
 * 
 * @author R_002 Stores a target chromosome and calculates fitness of
 *         individuals based on the percentage of bits that match the target
 */
public class FitnessFunctionMatchTarget implements FitnessFunction {
	private char[] target;

	public FitnessFunctionMatchTarget(char[] target) {
		this.target = target;
	}

	/**
	 * Returns a truncated percentage of bits in the individual's chromosome that
	 * match the target chromosome
	 */
	@Override
	public int calcFitness(Individual indiv) {
		char[] chromosome = indiv.getChromosome();
		if (chromosome.length != target.length) {
			return -1;
		}
		int fitness = 0;
		for (int i = 0; i < chromosome.length; i++) {
			if (chromosome[i] == target[i]) {
				fitness++;
			}
		}
		return (int) (fitness * 100 / target.length);
	}

}
