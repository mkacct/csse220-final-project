package SimComponents;

import java.util.Comparator;

/**
 * Class: CompareIndividual
 * 
 * @author R002 stores criteria to compare individuals
 */
public class CompareIndividual implements Comparator<Individual> {

	FitnessFunction fitnessCalc;

	/**
	 * Creates a new CompareIndividual with the default of a simple fitness function
	 */
	public CompareIndividual() {
		fitnessCalc = new FitnessFunctionSimple();
	}

	/**
	 * Creates a new CompareIndividual with the given fitness function
	 * 
	 * @param fitnessCalc
	 */
	public CompareIndividual(FitnessFunction fitnessCalc) {
		this.fitnessCalc = fitnessCalc;
	}

	/**
	 * Returns a negative number if o2 > o1, zero if they are equal, and a positive
	 * number if o1 > o2
	 */
	@Override
	public int compare(Individual o1, Individual o2) {
		return fitnessCalc.calcFitness(o1) - fitnessCalc.calcFitness(o2);
	}

}
