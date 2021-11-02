package Main;

import Exceptions.DomainException;

public class MiscUtil {

	/**
	 * Returns the index of char toFind in arr. If arr does not contain toFind,
	 * returns -1.
	 * 
	 * @param arr    <br>
	 *               Requires: arr is not empty
	 * @param toFind
	 * @return an int between 0 and arr.length (inclusive)
	 */
	public static int arrayIndexOf(char[] arr, char toFind) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == toFind) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Parses a double proportion from a string of the form (double)%, (int)/(int),
	 * or (double).
	 * 
	 * @param proportion
	 * @return a double from 0 to 1 (inclusive)
	 * @throws NumberFormatException if the string is not of one of the specified forms
	 * @throws DomainException if the proportion is greater than 1
	 */
	public static double parseProportion(String proportion) throws NumberFormatException, DomainException {
		double prop;
		try {
			prop = Double.parseDouble(proportion);
		} catch (NumberFormatException err) {
			if (proportion.indexOf('/') != -1) {
				try {
					prop = (double) (Double.parseDouble(proportion.substring(0, proportion.indexOf('/')))
							/ Double.parseDouble(proportion.substring(proportion.indexOf('/') + 1)));
				} catch (NumberFormatException err2) {
					throw new NumberFormatException();
				}
			} else if (proportion.indexOf('%') != -1) {
				try {
					prop = (double) (Double.parseDouble(proportion.substring(0, proportion.indexOf('%'))) / 100);
				} catch (NumberFormatException err3) {
					throw new NumberFormatException();
				}
			} else {
				throw new NumberFormatException();
			}
		}
		if (prop > 1 || prop < 0) {
			throw new DomainException();
		}
		return prop;
	}

}
