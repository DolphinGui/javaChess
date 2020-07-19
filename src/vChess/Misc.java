package vChess;

public class Misc {
	// is a miscellany of functions. Should never have an object.
	//private static char[] lookup = {'a','b','c','d','e','f','g','h'};
	private static String lookup = "abcdefgh";
	public static String notate(int loc) {
		return lookup.charAt(loc%8) + String.valueOf(Math.floorDiv(loc, 8)+1);
	}
	public static int parse(String notate) {
		return ((Character.getNumericValue(notate.charAt(1))-1)*8+lookup.indexOf(notate.charAt(0))); 
	}
}
