package SimComponents;

public class Individual {

	char[] chromosome;
	char[] charSet;
	
	public Individual(int size) {
		this.chromosome = new char[size];
		char[] charSet = {'0', '1'};
		this.charSet = charSet;
		this.initializeRandom();
	}

	public Individual(int size, char[] charSet) {
		this.chromosome = new char[size];
		this.charSet = charSet;
		this.initializeRandom();
	}
	
	public Individual(char[] chromosome, char[] charSet) {
		this.chromosome = chromosome;
		this.charSet = charSet;
	}
	
	public Individual create() {
		char[] chromosome1 = new char[chromosome.length];
		char[] charSet1;
		for(int i = 0; i < chromosome.length; i++) {
			chromosome1[i] = chromosome[i];
		}
		for(int i = 0; i < chromosome.length; i++) {
			chromosome1[i] = chromosome[i];
		}
		return new Individual(0);
	}
	
	private void initializeRandom() {
		for(int i = 0; i < this.chromosome.length; i++) {
			this.chromosome[i] = this.charSet[(int)(Math.random()*this.charSet.length)];
		}
	}
}
