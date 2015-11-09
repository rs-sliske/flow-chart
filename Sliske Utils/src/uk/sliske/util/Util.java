package uk.sliske.util;

import java.util.ArrayList;

public class Util {
	public static boolean checkChar(final char c, final String s) {
		for (Character ch : s.toCharArray())
			if (ch == c) return true;
		return false;
	}
	public static String[] removeEmptyStrings(String[] data) {
		ArrayList<String> result = new ArrayList<String>();
		
		for (int i = 0; i < data.length; i++)
			if (!data[i].equals("")) result.add(data[i]);
		
		String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];
		
		for (int i = 0; i < data.length; i++)
			result[i] = data[i].intValue();
		
		return result;
	}
	


}
