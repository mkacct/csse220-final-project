package SimComponents;

/**
 * Fitness function in which the most fit indivs have either the most or least 1s
 * @author R_002
 */
public class FitnessFunctionEitherExtreme implements FitnessFunction {
	/**
	 * Returns the percentage by which either ones or not ones are more frequent than the other
	 */
	@Override
	public int calcFitness(Individual indiv) {
		char[] chromosome = indiv.getChromosome();
		int ones = 0;
		for (char c : chromosome) {
			if (c == '1')  {ones++;}
		}
		return (int)((Math.abs(ones - (chromosome.length - ones)))*100/chromosome.length);
	}	
}
