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
	public static Individual load(String path) {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(path));
		} catch (FileNotFoundException err) {
			return null;
		}
		char[] charSet = scanner.nextLine().toCharArray();
		char[] chromosome = scanner.nextLine().toCharArray();
		scanner.close();
		return new Individual(chromosome, charSet);
	}

	public static void save(String path, Individual individual) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(path));
		} catch (FileNotFoundException err) {
			err.printStackTrace();
			return;
		}
		pw.println(String.join(individual.getChromosome()));
		pw.println(String.join(individual.getCharset()));
		pw.close();
	}
}