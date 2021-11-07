package SimComponents;

public class FitnessFunctionEitherExtreme implements FitnessFunction {
	@Override
	public int calcFitness(Individual indiv) {
		char[] chromosome = indiv.getChromosome();
		int ones = 0;
		for (char c : chromosome) {
			if (c == '1')  {ones++;}
		}
		return Math.abs(ones - (chromosome.length - ones));
	}	
}
