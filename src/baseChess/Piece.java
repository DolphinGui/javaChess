package baseChess;

import miscFunct.ArrayMan;

public class Piece{
	protected int location;
	protected boolean isFirst = true;
	
	public boolean getFealty() {
		return isFirst;
	}
	
	public int getLoc() {
		return location;
	}
	
	public int getCol() {
		return location%8;
	}
	
	public int getRow() {
		return Math.floorDiv(location, 8);
	}
	
	public AlgNotation getNotate() {
		AlgNotation notate = new AlgNotation(location);
		return notate;
	}

	public void setLoc(int loc) {
		location = loc;
	}
	public void setLoc(AlgNotation notate) {
		location = notate.getLoc();
	}
	public Integer[] exception(LinBoard a) {
		return null;
	}
	Integer[] moves = {};

	public Integer[] getMoves(LinBoard board) {
	    return ArrayMan.concatAll(moves, exception(board));
	};
	public boolean isKing() {
	    return false;
	};
}
