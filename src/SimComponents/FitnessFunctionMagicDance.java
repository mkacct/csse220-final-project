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
		char[] chromosome = indiv.getChromosome();
		for (int i = 0; i < FitnessFunctionMagicDance.DAYS; i++) {
			boolean allOnes = true;
			for(int j = 0; j < chromosome.length; j++) {
				if(chromosome[j] == '0') {
					return 1;
				}else if(chromosome[j]=='?'){
					if('0' == ((Math.random() >= 0.5) ? '1' : '0')) {
						allOnes = false;
						break;
					}
				}
			}
			if(allOnes == true) {
				return (int)(1 + (FitnessFunctionMagicDance.DAYS-i)*19.0/1000);
			}
		}
		return 1;
	}
}