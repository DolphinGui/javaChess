package baseChess;

import java.util.ArrayList;

import miscFunct.ArrayMan;

public class Piece{
	protected int location;
	protected boolean isFirst = true;
	Integer[][] moves = {};
	
	public Piece(int loc, boolean fealty) {
	    location = loc;
	    isFirst = fealty;
	}
	
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

	public void setLoc(int loc) {
		location = loc;
	}
	public Integer[] exception(LinBoard a) {
		return null;
	}
	
	private Integer[] validator(LinBoard board) {
	    ArrayList<Integer> results = new ArrayList<Integer>();
	    for(Integer[] move: moves) {
		if(board.checkFealty(board.vecToInteger(move), isFirst))  results.add(board.vecToInteger(move));
	    }
	    return results.toArray(new Integer[results.size()]);
	}
	
	public Integer[] getMoves(LinBoard board) {
	    return ArrayMan.concatAll(validator(board), exception(board));
	}
	public boolean isKing() {
	    return false;
	}
}
