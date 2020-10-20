package vanillaChess;

import java.util.ArrayList;

public class King extends Piece {
	Integer[][] moves = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 }, { -1, -1 } };

	public King(int loc, boolean fealty) {
		super(loc, fealty, "King", 'k');
	}

	public Integer[] getMoves(LinBoard board) {
		return validator(board);
	}

	public boolean isKing() {
		return true;
	}
	
	private Integer[] validator(LinBoard board) {
		ArrayList<Integer> results = new ArrayList<Integer>();
		for (Integer[] move : moves) {
			if (board.inBounds(board.locToVec(location), move)) {
				if (board.checkFealty(board.vecToInteger(move) + location, isFirst))
					results.add(board.vecToInteger(move) + location);
			}
		}
		return results.toArray(new Integer[results.size()]);
	}

}
