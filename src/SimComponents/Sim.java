package SimComponents;

import java.util.ArrayList;
import java.util.Random;

public class Sim {

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
