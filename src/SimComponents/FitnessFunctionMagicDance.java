package SimComponents;

/**
 * Fitness function used for the research project
 * Assuming the char set is "01?"
 * Question mark is used for learning thing
 * @author R_002
 */
public class FitnessFunctionMagicDance implements FitnessFunction {
	public static final int DAYS = 1000;

	private FitnessFunction usingFF;

	/**
	 * Construct the fitness function for the research project
	 * @param usingFF the fitness function to use this on top of
	 */
	public FitnessFunctionMagicDance(FitnessFunction usingFF) {
		this.usingFF = usingFF;
	}

	/**
	 * Returns the fitness based on how many "days" it takes to get 100% fitness with the FF used
	 */
	@Override
	public int calcFitness(Individual indiv) {
		for (int i = 0; i < FitnessFunctionMagicDance.DAYS; i++) {
			Individual guess = FitnessFunctionMagicDance.replaceQuestionMarks(indiv);
			if (this.usingFF.calcFitness(guess) == 100) {
				return (int) ((FitnessFunctionMagicDance.DAYS - i) * 100.0 / FitnessFunctionMagicDance.DAYS);
			}
		}
		return 0;
	}

	/**
	 * Return indiv but without question marks anymore
	 * @param indiv assuming char set is "01?"
	 * @return indiv but with question marks replaced with random binary (char set "01")
	 */
	public static Individual replaceQuestionMarks(Individual indiv) {
		char[] chromosome = indiv.getChromosome();
		for (int i = 0; i < chromosome.length; i++) {
			if (chromosome[i] == '?') {chromosome[i] = (Math.random() >= 0.5) ? '1' : '0';}
		}
		return new Individual(chromosome, "01".toCharArray());
	}
}