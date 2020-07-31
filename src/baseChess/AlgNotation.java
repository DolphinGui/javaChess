package baseChess;
//This is a datatype class to simplify error handling.
public class AlgNotation {
	private int location;
	private static String lookup = "abcdefgh";
	
	public AlgNotation(int location) {
		this.set(location);
	}
	public AlgNotation(String notation) {
		this.set(notation);
	}
	
	
	
	public int getLoc() {
		return location;
	}
	public String get() {
		return lookup.charAt(location%8) + String.valueOf(Math.floorDiv(location, 8)+1);
	}
	
	public int getRow() {
		return Math.floorDiv(location, 8);
	}
	public int getCol() {
		return location % 8;
	}
	
	public void set(int loc) {
		location = loc;
	}
	public void set(String notate) {
		location = ((Character.getNumericValue(notate.charAt(1))-1)*8+lookup.indexOf(notate.charAt(0))); 
	}
}
