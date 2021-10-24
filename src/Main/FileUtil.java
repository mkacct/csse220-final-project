package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import SimComponents.Individual;

/**
 * File save/load utility methods
 * @author R_002
 */
public class FileUtil {
	/**
	 * Load an individual from file
	 * @param file to load
	 * @return the individual
	 */
	public static Individual loadIndiv(File file) {
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException err) {
			return null;
		}
		char[] charSet = scanner.nextLine().toCharArray();
		char[] chromosome = scanner.nextLine().toCharArray();
		scanner.close();
		return new Individual(chromosome, charSet);
	}

	/**
	 * Save an individual to file (overwriting)
	 * @param file to save to
	 * @param indiv the invdividual
	 */
	public static void saveIndiv(File file, Individual indiv) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException err) {
			err.printStackTrace();
			return;
		}
		pw.println(new String(indiv.getCharSet()));
		pw.println(new String(indiv.getChromosome()));
		pw.close();
	}
}