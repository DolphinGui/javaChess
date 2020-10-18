package vanillaChess;

import miscFunct.ArrayMan;

import java.util.ArrayList;

public class Pawn extends Piece {
	private final static String name = "Pawn";
	private final static char shorthand = 'p';
	private boolean hasMoved = false;
	private boolean passant = false;
	private int initialLoc;

	public Pawn(int loc, boolean fealty) {
		super(loc, fealty, "Pawn", 'p');
		initialLoc = loc;
	}

	private Integer[] advance(LinBoard board) {
		Integer[] direction = { 0, 1 };
		if (!isFirst)
			direction[1] = -1;
		if (board.getPiece(direction, board.locToVec(location)) == null) {
			Integer[] results = { location + board.vecToInteger(direction) };
			return results;
		}
		Integer[] results = {};
		return results;
	}

	private Integer[] capture(LinBoard board) {
		Integer[] west = { -1, 1 };
		Integer[] east = { 1, 1 };
		if (!isFirst) {
			west[1] = -1;
			east[1] = -1;
		}

		ArrayList<Integer> validX = new ArrayList<Integer>();
		if (board.checkFealty(location + board.vecToInteger(west), isFirst)
				&& board.getPiece(location + board.vecToInteger(west)) != null) {
			validX.add(board.vecToInteger(west) + location);
		}
		if (board.checkFealty(location + board.vecToInteger(east), isFirst)
				&& board.getPiece(location + board.vecToInteger(east)) != null) {
			validX.add(board.vecToInteger(east) + location);

		}
		return validX.toArray(new Integer[validX.size()]);
	}

	public Integer[] exception(LinBoard board) {
		return ArrayMan.concatAll(capture(board), initiative(board), advance(board));
	}

	public String getName() {
		return name;
	}

	public char getShort() {
		return shorthand;
	}

	private Integer[] initiative(LinBoard board) {
		if (!hasMoved) {
			Integer[] init = { 0 };
			if (isFirst)
				init[0] = location + board.getWidth() * 2;
			else
				init[0] = location - board.getWidth() * 2;
			return init;
		}
		return null;
	}

	public boolean passant() {
		return passant;
	}
	
	public void setLoc(int loc) {
		if(location == initialLoc) {
			passant = true;
		}
		location = loc;
		hasMoved = false;
		passant = false;
	}
}
