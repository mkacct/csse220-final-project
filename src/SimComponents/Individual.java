package SimComponents;
import Main.MiscUtil;
/**
 * Class: Individual
 * 
 * @author Team R002 <br>
 *         Purpose: Holds a chromosome and the set of possible bit values for
 *         that chromosome. <br>
 *         For example: <br>
 *         Individual me = new Individual(100);
 */
public class Individual {

	char[] chromosome;
	char[] charSet;

	/**
	 * Creates an individual with default potential characters 0 and 1 and
	 * initializes a chromosome of length size with random values of 0 and 1
	 * 
	 * @param size <br>
	 *             Constraints: size >= 0
	 */
	public Individual(int size) {
		this.chromosome = new char[size];
		char[] charSet = { '0', '1' };
		this.charSet = charSet;
		this.initializeRandom();
	}

	/**
	 * Creates an individual with potential characters contained in charSet.
	 * Individual is of length size and bits are ranomly initialized to characters
	 * from the charSet.
	 * 
	 * @param size    <br>
	 *                size >= 0
	 * @param charSet <br>
	 *                charSet is not empty
	 */
	public Individual(int size, char[] charSet) {
		this.chromosome = new char[size];
		this.charSet = charSet;
		this.initializeRandom();
	}

	/**
	 * Creates an individual with the given chromosome and set of potential
	 * characters.
	 * 
	 * @param chromosome <br>
	 *                   chromosome is not empty
	 * @param charSet    <br>
	 *                   charSet is not empty
	 */
	public Individual(char[] chromosome, char[] charSet) {
		for(char c:chromosome) {
			boolean contains = false;
			for(char s:charSet) {
				if(c == s) contains = true;
			}
			if(contains == false) {
				System.out.println("Chromosome and charSet do not match");
				return;
			}
		}
		this.chromosome = chromosome;
		this.charSet = charSet;
	}

	/**
	 * Initializes all bits in the chromosome to random values from the charSet
	 */
	private void initializeRandom() {
		for (int i = 0; i < this.chromosome.length; i++) {
			this.chromosome[i] = this.charSet[(int) (Math.random() * this.charSet.length)];
		}
	}

	/**
	 * Sets the given bit to a random value from the charSet excluding its previous
	 * value
	 * 
	 * @param bit <br>
	 *            bit is between 0 and the size of the chromosome
	 */
	private void flipBitRandom(int bit) {
		char set;
		do {
			set = charSet[(int) (Math.random() * charSet.length)];
		} while (set == chromosome[bit]);
		chromosome[bit] = set;
	}
	
	public void flipBit(int bit) {
		chromosome[bit] = charSet[(MiscUtil.arrayIndexOf(charSet, chromosome[bit])+1)%charSet.length];
	}
	/**
	 * Mutates the chromosome. Each bit has a chance of flipping equal to rate.
	 * @param rate
	 * <br> 0 <= rate <= 1
	 */
	public void mutate(double rate) {
		for(int i = 0; i < chromosome.length; i++) {
			if(Math.random() < rate) {
				this.flipBitRandom(i);
			}
		}
	}

	/**
	 * Returns a copy of the chromosome.
	 * @return char[]
	 */
	public char[] getChromosome() {
		char[] chromosome1 = new char[chromosome.length];
		for (int i = 0; i < chromosome.length; i++) {
			chromosome1[i] = chromosome[i];
		}
		return chromosome1;
	}
	
	/**
	 * Returns a copy of the set of potential characters.
	 * @return char[]
	 */
	public char[] getCharSet() {
		char[] charSet1 = new char[charSet.length];
		for (int i = 0; i < charSet.length; i++) {
			charSet1[i] = charSet[i];
		}
		return charSet1;
	}
	
	/**
	 * Returns a copy of the individual.
	 * 
	 * @return Individual
	 */
	public Individual create() {
		return new Individual(this.getChromosome(), this.getCharSet());
	}
}
