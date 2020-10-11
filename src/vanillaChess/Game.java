package vanillaChess;

import miscFunct.FileRead;

import java.io.File;
import java.io.FileNotFoundException;
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

	public void init() throws FileNotFoundException {
		config = new File("./assets/board.brd");
		boardDefault = FileRead.readFile(config, StandardCharsets.UTF_8);
		internboard = new LinBoard(8, 8);
		this.boardSet();
	}

	private void castle(int k, int r, LinBoard board) throws InvalidMoveException {
		Piece king = board.getPiece(k);
		Piece rook = board.getPiece(r);

		if(king.hasMoved()||rook.hasMoved()||(king.getFealty()!=rook.getFealty())) {
			throw new InvalidMoveException("Invalid Castle");
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
			throw new InvalidMoveException("Invalid Castle");
		}
		for(int i = k + interval; i!=r; i+=interval) {
			if(board.getPiece(i)!=null) 
				throw new InvalidMoveException("Invalid Castle");
			if(check(king.getFealty(), i, board))
				throw new InvalidMoveException("Invalid Castle");
		}
		set(k + 2 * interval, k, board);
		if (check(whiteTurn, board))
			throw new InvalidMoveException("Would Checkmate");// validates no checkmate
		whiteTurn = !whiteTurn;
		king.moved();
		rook.moved();
		return;
	}

	private void move(int loc, int origin, char c) throws InvalidMoveException {
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
					resolve(bufferboard, p);
					return;
				}
			}
		}
		move(loc, origin);
	}

	private void resolve(LinBoard board, Piece p) throws InvalidMoveException {
		if (check(whiteTurn, board))
			throw new InvalidMoveException("Checkmate");
		internboard.setBoard(board.getBoard());
		whiteTurn = !whiteTurn;
		p.moved();
	}

	private void move(int loc, int origin) throws InvalidMoveException {
		LinBoard bufferboard = internboard.copy();
		Piece p = internboard.getPiece(origin);
		if (internboard.checkFealty(origin, whiteTurn))// checks if piece is capturable
			throw new InvalidMoveException("Piece not capturable");
		if(p instanceof King&&internboard.getPiece(loc)instanceof Rook){
			castle(origin, loc, bufferboard);
			return;
		}else if(p instanceof Pawn && (internboard.getPiece(loc - internboard.getWidth()) instanceof Pawn||
				internboard.getPiece(loc + internboard.getWidth()) instanceof Pawn) &&
				(Math.abs(loc-origin)==7 || Math.abs(loc-origin)==9)){
			enPassant(loc, origin, bufferboard);
			resolve(bufferboard, p);
			return;
		}else{
			for (Integer m : p.getMoves(internboard)) {
				if (m == loc) { // validates that the resulting location is a valid move
					set(loc, origin, bufferboard);
					resolve(bufferboard, p);
					return;
				}
			}
		}
		throw new InvalidMoveException("Invalid Move");
	}



	private void set(int loc, int origin, LinBoard board) {
		if(loc<0||origin<0) {
			throw new IndexOutOfBoundsException();
		}
		board.set(loc, board.getPiece(origin));
		board.set(origin, null);
		board.getPiece(loc).setLoc(loc);
	}

	private void enPassant(int loc, int origin, LinBoard board) throws InvalidMoveException {
		if(board.getPiece(origin) instanceof Pawn && board.getPiece(loc - board.getWidth()) instanceof Pawn) {
			set(loc, origin, board);
			board.set(loc - board.getWidth(), null);
			return;
		}else if(board.getPiece(origin) instanceof Pawn && board.getPiece(loc + board.getWidth()) instanceof Pawn){
			set(loc, origin, board);
			board.set(loc + board.getWidth(), null);
			return;
		}else{
			throw new InvalidMoveException("Invalid Passant");
		}
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

	public boolean turn(int loc, int origin, char piece) throws InvalidMoveException {
		boolean mate = check(whiteTurn);
		if (mate && trap(whiteTurn))
			return false;
		else {
			move(loc, origin, piece);
			return !(check(whiteTurn) && trap(whiteTurn));
		}
	}

	public boolean turn(int loc, int origin) throws InvalidMoveException {
		boolean mate = check(whiteTurn);
		if (mate && trap(whiteTurn))
			return false;
		else {
			move(loc, origin);
			return !(check(whiteTurn) && trap(whiteTurn));
		}
	}
}
