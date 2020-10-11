package javaChess;

public class NotationInterperter {
	private int width;
	private int height;

	NotationInterperter(int x, int y) {
		width = x;
		height = y;
	}

	public int denotate(String notatation) {
		final String ab = "abcdefghijklmnopqrstuvwxyz"; // abcdefghijklmnopqrstuvwxyz zyxwvutsrqpomnlkjihgfedcba
		int result;
		if (!Character.isDigit(notatation.charAt(1)))
			throw new IllegalArgumentException("Not Algabraic notation");
		if (ab.indexOf(notatation.charAt(0)) == -1 || ab.indexOf(notatation.charAt(0)) > height)
			throw new IllegalArgumentException("Not Algabraic notation");
		result = (Character.getNumericValue(notatation.charAt(1)) - 1) * width;
		result += ab.indexOf(Character.toLowerCase(notatation.charAt(0)));
		if (result >= height * width) 
			throw new IndexOutOfBoundsException("Out of bounds");
		return result;
	}
	
	public AlgebraicMove decode(String notatation) {
		return new AlgebraicMove(denotate(notatation.substring(2, 4)), denotate(notatation.substring(0, 2)));
	}
	
}
