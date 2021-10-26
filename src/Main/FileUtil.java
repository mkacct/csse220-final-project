package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
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
	 * @return the individual, or null if the file is of invalid format
	 */
	public static Individual loadIndiv(File file) {
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException err) {
			return null;
		}
		char[] charSet;
		char[] chromosome;
		try {
			charSet = scanner.nextLine().toCharArray();
			chromosome = scanner.nextLine().toCharArray();
		} catch (NoSuchElementException err) { // file too short
			scanner.close();
			return null;
		}
		if (scanner.hasNextLine()) { // file too long
			scanner.close();
			return null;
		}
		scanner.close();
		for (char c : chromosome) { // validation of chromosome
			if (MiscUtil.arrayIndexOf(charSet, c) == -1) {return null;}
		}
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