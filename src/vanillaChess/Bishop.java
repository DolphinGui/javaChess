package vanillaChess;

import java.util.ArrayList;

import miscFunct.ArrayMan;

public class Bishop extends Piece {
	public static final Integer[] move = {};

	private final static String name = "Bishop";

	public Bishop(int loc, boolean fealty) {
		super(loc, fealty, "bishop", 'b');
	}

	private Integer[] diag(int i, boolean opposite) {
		Integer[] result = new Integer[2];
		if (!opposite) {
			result[0] = i;
			result[1] = i;
		} else {
			result[0] = i;
			result[1] = -i;
		}

		return result;
	}

	private int diagMov(int i, LinBoard board, boolean opp) {
		return board.vecToInteger(diag(i, opp));
	}

	public Integer[] exception(LinBoard board) {
		return ArrayMan.concatAll(move(board, true, true), move(board, true, false), move(board, false, true),
				move(board, false, false));
	}

	public String getName() {
		return name;
	}

	private Integer[] move(LinBoard board, boolean forward, boolean polarity) {
		ArrayList<Integer> moves = new ArrayList<Integer>();

		int increment = -1;
		if (forward)
			increment = 1;
		for (int i = increment; board.inBounds(board.locToVec(this.location), diag(i, polarity)); i += increment) {
			Piece p = board.getPiece(location + diagMov(i, board, polarity));
			if (p == null)
				moves.add(location + diagMov(i, board, polarity));
			else if (p.getFealty() != isFirst) {
				moves.add(location + diagMov(i, board, polarity));
				break;
			} else {
				break;
			}

		}
		return moves.toArray(new Integer[moves.size()]);
	}
}
