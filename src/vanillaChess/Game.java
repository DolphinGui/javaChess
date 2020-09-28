package vanillaChess;

import baseChess.LinBoard;
import baseChess.Piece;
import miscFunct.FileRead;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	private ArrayList<Character> boardDefault;
	private File config;
	private LinBoard internboard;
	private King whiteKing;
	private King blackKing;
	private boolean whiteTurn;

	private void boardSet() {
		for (int i = 0; i < internboard.getLength(); i++) {
			boolean white = Character.isUpperCase(boardDefault.get(i)); // uppercases are white
			char c = Character.toLowerCase(boardDefault.get(i));
			switch (c) {
			case 'r':
				internboard.set(i, new Rook(i, white));
				break;
			case 'b':
				internboard.set(i, new Bishop(i, white));
				break;
			case 'k':
				internboard.set(i, new Knight(i, white));
				break;
			case 'x':
				internboard.set(i, new Queen(i, white));
				break;
			case 'p':
				internboard.set(i, new Pawn(i, white));
				break;
			case 'y':
				internboard.set(i, new King(i, white));
				if (white)
					whiteKing = (King) internboard.getPiece(i);
				else
					blackKing = (King) internboard.getPiece(i);
				break;
			}
		}
		whiteTurn = true;
	}

	private boolean check(boolean white) {
		return check(white, internboard);
	}

	private boolean check(boolean white, LinBoard board) {
		for (Piece p : board.getPieces()) {
			if (!white == p.getFealty()) {
				int k;
				if (white)
					k = whiteKing.getLoc();
				else
					k = blackKing.getLoc();
				for (int m : p.getMoves(board)) {
					if (m == k)
						return true;
				}
			}
		}
		return false;
	}

	public boolean checkmate() {
		if (trap(whiteTurn) && check(whiteTurn))
			return true;
		return false;
	}

	public char[][] getCharBoard() {
		return internboard.getCharBoard();
	}

	public int getHeight() {
		return internboard.getHeight();
	}

	public int getWidth() {
		return internboard.getWidth();
	}

	public void init() throws IOException {
		config = new File("./assets/board.brd");
		boardDefault = FileRead.readFile(config, StandardCharsets.UTF_8);
		internboard = new LinBoard(8, 8);
		this.boardSet();
	}

	private int move(int loc, int origin) {
		// 0 is successful, 1 is failed, 2 is check
		LinBoard bufferboard = internboard.copy();
		Piece p = internboard.getPiece(origin);
		if (internboard.checkFealty(origin, whiteTurn))
			return 1; // checks if it's the piece's turn
		for (Integer m : p.getMoves(internboard)) {
			if (m == loc) { // validates that the resulting location is a valid move
				mv(loc, origin, bufferboard);
				if (check(whiteTurn, bufferboard))
					return 2;// validates no checkmate
				internboard.setBoard(bufferboard.getBoard());
				whiteTurn = !whiteTurn;
				return 0;
			}
		}
		return 1;
	}

	private void mv(int loc, int origin, LinBoard board) {
		board.set(loc, board.getPiece(origin));
		board.set(origin, null);
		board.getPiece(loc).setLoc(loc);
	}

	/*
	@SuppressWarnings("unused")
	private void printInternboard() { // this is a debug function. comment out later.
		char shorthand = ' ';
		for (int n = internboard.getHeight() - 1; n >= 0; n--) {
			for (int i = 0; i <= internboard.getWidth() - 1; i++) {
				Piece p = internboard.getPiece(i + n * 8);
				if (p == null) {
					shorthand = 'n';
				} else {
					shorthand = p.getShort();
					if (p.getFealty())
						shorthand = Character.toUpperCase(shorthand);
				}
				System.out.print(" " + shorthand + " ");
			}
			System.out.println();
		}
	}  */

	private boolean trap(boolean whiteTurn) {
		LinBoard bufferboard = internboard.copy();
		King king;
		int oppKing;
		if (whiteTurn) {
			king = whiteKing;
			oppKing = blackKing.getLoc();
		} else {
			king = blackKing;
			oppKing = whiteKing.getLoc();
		}
		ArrayList<Integer> moves = new ArrayList<Integer>(Arrays.asList(king.getMoves(internboard)));
		for (Piece p : internboard.getPieces(!whiteTurn)) {
			moves.removeAll(Arrays.asList(p.getMoves(internboard)));
		}
		for (Integer m : moves) {
			if (oppKing == m) {
				if (moves.size() == 1)
					return true;
			}
			mv(m, king.getLoc(), bufferboard);
			if (check(whiteTurn))
				return true;
		}
		if (moves.size() == 0)
			return true;
		return false;
	}

	public int turn(int loc, int origin) {
		// 0 is successful, 1 is failed, 2 is check, and 3 is checkmate
		boolean mate = check(whiteTurn);
		if (mate && trap(whiteTurn))
			return 3;
		else if (mate)
			return 2;
		else
			return move(loc, origin);
	}
}
