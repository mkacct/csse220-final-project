package Main;

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
	 * or (double). If the string is not of these forms, returns -1. If the
	 * proportion is greater than 1, returns -2.
	 * 
	 * @param proportion
	 * @return a double from 0 to 1 (inclusive)
	 */
	public static double parseProportion(String proportion) {
		double prop;
		try {
			prop = Double.parseDouble(proportion);
		} catch (NumberFormatException err) {
			if (proportion.indexOf('/') != -1) {
				try {
					prop = (double) (Double.parseDouble(proportion.substring(0, proportion.indexOf('/')))
							/ Double.parseDouble(proportion.substring(proportion.indexOf('/') + 1)));
				} catch (NumberFormatException err2) {
					return -1;
				}
			} else if (proportion.indexOf('%') != -1) {
				try {
					prop = (double) (Double.parseDouble(proportion.substring(0, proportion.indexOf('%'))) / 100);
				} catch (NumberFormatException err3) {
					return -1;
				}
			} else {
				return -1;
			}
		}
		if (prop > 1 || prop < 0) {
			return -2;
		}
		return prop;
	}

}
