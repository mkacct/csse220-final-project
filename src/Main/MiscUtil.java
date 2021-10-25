package Main;

public class MiscUtil {

	public static int arrayIndexOf(char[] arr, char toFind) {
		for(int i = 0; i<arr.length; i++) {
			if(arr[i] == toFind) {
				return i;
			}
		}
		return -1;
	}
	
	public static double parseProportion(String proportion) {
		double prop;
		try{
			prop = Double.parseDouble(proportion);
		}catch(NumberFormatException err) {
			if(proportion.indexOf('/')!= -1) {
				try {
					prop = (double) (Integer.parseInt(proportion.substring(0, proportion.indexOf('/')))/Integer.parseInt(proportion.substring(proportion.indexOf('/')+1)));
				}catch(NumberFormatException err2) {
					return -1;
				}
			} else if(proportion.indexOf('%')!=-1) {
				try {
					prop = (double)(Double.parseDouble(proportion.substring(0, proportion.indexOf('%')))/100);
				}catch(NumberFormatException err3) {
					return -1;
				}
			} else {
				return -1;
			}
		}
		if(prop >1) {
			return -2;
		}
		return prop;
	}

}
