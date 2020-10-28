package vanillaChess;

import java.util.ArrayList;

public class Knight extends Piece {

	final Integer[][] moves = { { -1, -2 }, { -2, -1 }, { -2, 1 }, { -1, 2 }, { 1, -2 }, { 2, -1 }, { 2, 1 }, { 1, 2 } };

	public Knight(int loc, boolean fealty) {
		super(loc, fealty, "Knight", 'n');
	}

	public Integer[] getMoves(LinBoard board) {
		return validator(board);
	}

	private Integer[] validator(LinBoard board) {
		ArrayList<Integer> results = new ArrayList<>();
		for (Integer[] move : moves) {
			if (board.inBounds(board.locToVec(location), move)) {
				if (board.checkFealty(board.vecToInteger(move) + location, isFirst))
					results.add(board.vecToInteger(move) + location);
			}
		}
		return results.toArray(new Integer[0]);
	}

}
