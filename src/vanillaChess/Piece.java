package vanillaChess;

import java.util.ArrayList;

import miscFunct.ArrayMan;

public class Piece {
	protected int location;
	protected final String name;
	protected final char shorthand;
	protected boolean isFirst;
	protected boolean hasMoved = false;
	final Integer[][] moves = {};

	public void moved() {
		hasMoved = true;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public Piece(int loc, boolean fealty, String nam, char shot) {
		location = loc;
		isFirst = fealty;
		name = nam;
		shorthand = shot;
	}

	public Integer[] exception(LinBoard a) {
		return null;
	}

	public int getCol() {
		return location % 8;
	}

	public boolean getFealty() {
		return isFirst;
	}

	public int getLoc() {
		return location;
	}

	public Integer[] getMoves(LinBoard board) {
		return ArrayMan.concatAll(validator(board), exception(board));
	}

	public String getName() {
		return name;
	}

	public int getRow() {
		return Math.floorDiv(location, 8);
	}

	public char getShort() {
		return shorthand;
	}

	public boolean isKing() {
		return false;
	}

	public void setLoc(int loc) {
		location = loc;
	}

	private Integer[] validator(LinBoard board) {
		ArrayList<Integer> results = new ArrayList<>();
		for (Integer[] move : moves) {
			if (board.checkFealty(board.vecToInteger(move), isFirst))
				results.add(board.vecToInteger(move));
		}
		return results.toArray(new Integer[0]);
	}
}
