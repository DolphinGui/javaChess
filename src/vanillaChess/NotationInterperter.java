package vanillaChess;

public class NotationInterperter {
	private int width;
	private int height;
	final String ab = "abcdefghijklmnopqrstuvwxyz";

	NotationInterperter(int x, int y) {
		width = x;
		height = y;
	}

	public int denotate(String notatation) {
		 // abcdefghijklmnopqrstuvwxyz zyxwvutsrqpomnlkjihgfedcba
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
	
	public AlgebraicMove decode(String move) {
		if(move.length()==5)return new AlgebraicMove(denotate(move.substring(2, 4)), denotate(move.substring(0, 2)), move.charAt(4));
		return new AlgebraicMove(denotate(move.substring(2, 4)), denotate(move.substring(0, 2)));
	}
	
	public String notate(AlgebraicMove move) {
		return ab.charAt(move.origin % 8) + Integer.toString(1 + move.origin / 8) + ab.charAt(move.loc % 8) + Integer.toString(1 + move.loc / 8);
	}
	
}
