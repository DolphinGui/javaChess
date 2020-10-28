package vanillaChess;

import java.util.ArrayList;

import miscFunct.ArrayMan;

public class Queen extends Piece {

	public Queen(int loc, boolean fealty) {
		super(loc, fealty, "Queen", 'q');
	}

	private Integer[] bishop(LinBoard board, boolean forward, boolean polarity) {
		ArrayList<Integer> moves = new ArrayList<>();

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
		return moves.toArray(new Integer[0]);
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
		return ArrayMan.concatAll(rook(board, true, true), rook(board, true, false), rook(board, false, true),
				rook(board, false, false), bishop(board, false, true), bishop(board, true, true),
				bishop(board, false, false), bishop(board, true, false));
	}

	private Integer[] rook(LinBoard board, boolean right, boolean rank) {
		ArrayList<Integer> moves = new ArrayList<>();
		int endpoint;
		Integer[] direction = { 0, 0 };
		if (rank) {
			endpoint = board.getHeight() * this.getRow() - 1; // calculates and sets endpoints and directions
			direction[0] = -1;
			if (right) {
				endpoint += board.getHeight();
				direction[0] = 1;
			}
		} else {
			endpoint = this.getCol() - board.getWidth();
			direction[1] = -1;
			if (right) {
				endpoint += (board.getHeight() + 1) * board.getWidth();
				direction[1] = 1;
			}
		}
		for (int i = this.getLoc() + board.vecToInteger(direction); i != endpoint; i += board.vecToInteger(direction)) {
			Piece p = board.getPiece(i);
			if (p == null) {
				moves.add(i);
			} else if (p.getFealty() != this.getFealty()) {
				moves.add(i);
				break;
			} else {
				break;
			}
		}
		return moves.toArray(new Integer[0]);
	}

}
