package vanillaChess;

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

	private boolean check(boolean white, int loc, LinBoard board) {
		for (Piece p : board.getPieces(!white)) {
			for (int m : p.getMoves(board)) {
				if (m == loc)
					return true;
			}
		}
		return false;
	}

	private boolean check(boolean white, LinBoard board) {
		int k;
		if (white)
			k = whiteKing.getLoc();
		else
			k = blackKing.getLoc();
		return check(white, k, board);

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

	private int castle(int k, int r, LinBoard board) {
		Piece king = board.getPiece(k);
		Piece rook = board.getPiece(r);

		if(king.hasMoved()||rook.hasMoved()||(king.getFealty()!=rook.getFealty())) {
			return 1;
		}
		int interval = 0;
		if(king.getCol()==rook.getCol()) {
			if(king.getRow()>rook.getRow()) {
				interval = 8;
			}else if(king.getRow()<rook.getRow()) {
				interval = -8;
			}
		}else if(king.getRow()==rook.getRow()) {
			if(king.getCol()>rook.getCol()) {
				interval = 1;
			}else if(king.getCol()<rook.getCol()) {
				interval = -1;
			}
		}
		if(interval == 0) {
			return 1;
		}
		for(int i = k + interval; i!=r; i+=interval) {
			if(board.getPiece(i)!=null) 
				return 1;
			if(check(king.getFealty(), i, board))
				return 1;
		}
		set(k + 2 * interval, k, board);
		if (check(whiteTurn, board))
			return 2;// validates no checkmate
		whiteTurn = !whiteTurn;
		king.moved();
		rook.moved();
		return 0;
	}

	private int move(int loc, int origin, char c) {
		LinBoard bufferboard = internboard.copy();
		Piece p = internboard.getPiece(origin);
		Piece r;
		if(p instanceof Pawn&&internboard.toRow(loc)==7||internboard.toRow(loc)==0){
			switch(c) {
			case'q':
				r = new Queen(loc, whiteTurn);
				break;
			case'n':
				r = new Queen(loc, whiteTurn);
				break;
			case'r':
				r = new Queen(loc, whiteTurn);
				break;
			case'b':
				r = new Queen(loc, whiteTurn);
				break;
			default:
				r = new Pawn(loc, whiteTurn);
				break;
			}
			for (Integer m : p.getMoves(internboard)) {
				if (m == loc) { // validates that the resulting location is a valid move
					set(loc, origin, bufferboard);
					bufferboard.getBoard()[loc] = r;
					return resolve(bufferboard, p);
				}
			}
		}
		return move(loc, origin);
	}

	private int resolve(LinBoard board, Piece p) {
		if (check(whiteTurn, board))
			return 2;// validates no checkmate
		internboard.setBoard(board.getBoard());
		whiteTurn = !whiteTurn;
		p.moved();
		return 0;
	}
	
	private int move(int loc, int origin) {
		// 0 is successful, 1 is failed, 2 is check
		LinBoard bufferboard = internboard.copy();
		Piece p = internboard.getPiece(origin);
		if (internboard.checkFealty(origin, whiteTurn))
			return 1; // checks if piece is capturable

		if(p instanceof King&&internboard.getPiece(loc)instanceof Rook){
			return castle(origin, loc, bufferboard);
		}else if(p instanceof Pawn && internboard.getPiece(loc) instanceof Pawn){
			int result = enPassant(loc, origin, bufferboard);
			if(result==0) {
				return resolve(bufferboard, p);
			}
		}else{
			for (Integer m : p.getMoves(internboard)) {
				if (m == loc) { // validates that the resulting location is a valid move
					set(loc, origin, bufferboard);
					return resolve(bufferboard, p);
				}
			}
		}
		return 1;
	}

	
	
	private void set(int loc, int origin, LinBoard board) {
		if(loc<0||origin<0) {
			throw new IndexOutOfBoundsException();
		}
		board.set(loc, board.getPiece(origin));
		board.set(origin, null);
		board.getPiece(loc).setLoc(loc);
	}

	private int enPassant(int loc, int origin, LinBoard board) {
		Pawn p;
		Pawn l;
		if(board.getPiece(origin) instanceof Pawn && board.getPiece(loc) instanceof Pawn) {
			p = (Pawn) board.getPiece(origin);
			l = (Pawn) board.getPiece(loc);
		}else {
			return 1;
		}
		if(l.passant() && l.getRow()==p.getRow() && 
				Math.abs(l.getCol()-p.getCol())==1 && board.getPiece(loc)==null) {
			set(loc, origin - board.getWidth(), board);
			board.set(loc, null);
			return 0;
		}
		return 1;
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
			set(m, king.getLoc(), bufferboard);
			if (check(whiteTurn))
				return true;
		}
		if (moves.size() == 0)
			return true;
		return false;
	}

	public int turn(int loc, int origin, char piece) {
		// 0 is successful, 1 is failed, 2 is check, and 3 is checkmate
		boolean mate = check(whiteTurn);
		if (mate && trap(whiteTurn))
			return 3;
		else if (mate)
			return 2;
		else {
			return move(loc, origin, piece);
		}
	}
	
	public int turn(int loc, int origin) {
		// 0 is successful, 1 is failed, 2 is check, and 3 is checkmate
		boolean mate = check(whiteTurn);
		if (mate && trap(whiteTurn))
			return 3;
		else if (mate)
			return 2;
		else {
			return move(loc, origin);
		}
	}
}
