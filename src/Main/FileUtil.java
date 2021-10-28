package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Exceptions.FileFormatException;
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
	 * @throws FileNotFoundException if file does not exist
	 * @throws FileFormatException if file is not of correct format for an indiv
	 */
	public static Individual loadIndiv(File file) throws FileNotFoundException, FileFormatException {
		Scanner scanner;
		scanner = new Scanner(file);
		char[] charSet;
		char[] chromosome;
		try {
			charSet = scanner.nextLine().toCharArray();
			chromosome = scanner.nextLine().toCharArray();
		} catch (NoSuchElementException err) { // file too short
			scanner.close();
			throw new FileFormatException();
		}
		if (scanner.hasNextLine()) { // file too long
			scanner.close();
			throw new FileFormatException();
		}
		scanner.close();
		for (char c : chromosome) { // validation of chromosome
			if (MiscUtil.arrayIndexOf(charSet, c) == -1) {throw new FileFormatException();}
		}
		return new Individual(chromosome, charSet);
	}

	/**
	 * Save an individual to file (overwriting)
	 * @param file to save to (assumed to be valid)
	 * @param indiv the invdividual
	 */
	public static void saveIndiv(File file, Individual indiv) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException err) {
			err.printStackTrace(); // should not happen
			return;
		}
		pw.println(new String(indiv.getCharSet()));
		pw.println(new String(indiv.getChromosome()));
		pw.close();
	}
}