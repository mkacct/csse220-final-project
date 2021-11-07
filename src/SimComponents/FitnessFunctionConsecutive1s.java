package SimComponents;

/**
 * Fitness function based on the length of the longest sequence of 1s
 * @author R_002
 */
public class FitnessFunctionConsecutive1s implements FitnessFunction {
	/**
	 * Returns the percentage of the chromosome occupied by the longest sequence of ones
	 */
	@Override
	public int calcFitness(Individual indiv) {
		char[] chromosome = indiv.getChromosome();
		int ones = 0;
		int winner = 0;
		for (char c : chromosome) {
			if (c == '1')  {
				ones++;
				if (ones > winner) {winner = ones;}
			} else {
				ones = 0;
			}
		}
		return (int) (winner * 100.0 / chromosome.length);
	}
}