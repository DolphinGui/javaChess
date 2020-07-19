package vChess;

public class Piece {
	private int location = 3;
	
	public int getLoc() {
		return location;
	}
	public AlgNotation getNotate() {
		AlgNotation notate = new AlgNotation(location);
		return notate;
	}

	public void set(int loc) {
		location = loc;
	}
	public void set(AlgNotation notate) {
		location = notate.getLoc();
	}
}
