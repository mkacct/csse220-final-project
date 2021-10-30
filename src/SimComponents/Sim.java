package SimComponents;

import java.util.ArrayList;
import java.util.Random;

public class Sim {
	public static final String[] FF_NAMES = {"Simple"};
	public static final String[] SELECTOR_NAMES = {"Truncation", "Roulette wheel", "Ranked"};
	public static final String[] CROSSOVER_NAMES = {"None", "One point"};

	ArrayList<Individual> pop;
	
	public Sim() {
		pop = new ArrayList<Individual>();
		for(int i = 0; i < 100; i++) {
			pop.add(new Individual(100, new Random()));
		}
	}
	
	public Sim(int size) {
		pop = new ArrayList<Individual>();
		for(int i = 0; i < size; i++) {
			pop.add(new Individual(100, new Random()));
		}
	}
	
//	nextGen(){}
//	
//	nextGens(n){}
//	
//	nextGensUntil(fitness){}
//	
//	getMaxFitness(){}
//	
//	getAvgFitness(){}
//	
//	getMinFitness(){}
}
