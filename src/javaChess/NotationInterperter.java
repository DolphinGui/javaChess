package javaChess;

public class NotationInterperter {
	private int width;
	private int height;

	NotationInterperter(int x, int y) {
		width = x;
		height = y;
	}

	public int denotate(String notate) {
		final String ab = "abcdefghijklmnopqrstuvwxyz"; // abcdefghijklmnopqrstuvwxyz zyxwvutsrqpomnlkjihgfedcba
		int result;
		if (!Character.isDigit(notate.charAt(1)))
			throw new IllegalArgumentException("Not Algabraic notation");
		if (ab.indexOf(notate.charAt(0)) == -1 || ab.indexOf(notate.charAt(0)) > height)
			throw new IllegalArgumentException("Not Algabraic notation");
		result = (Character.getNumericValue(notate.charAt(1)) - 1) * width;
		result += ab.indexOf(Character.toLowerCase(notate.charAt(0)));
		if (result >= height * width) 
			throw new IndexOutOfBoundsException("Out of bounds");
		return result;
	}
}
