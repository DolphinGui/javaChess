package timeChess;

import javaChess.InvalidMoveException;
import miscFunct.FileRead;
import vanillaChess.Pawn;
import vanillaChess.King;
import vanillaChess.Rook;
import vanillaChess.Queen;
import vanillaChess.Bishop;
import vanillaChess.Knight;
import vanillaChess.Piece;
import vanillaChess.LinBoard;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class TimeGame implements javaChess.Game<TimeMove, Integer> {
	final String ab = "abcdefghijklmnopqrstuvwxyz";

	private ArrayList<Character> boardDefault;
	private LinBoard internboard;
	private King whiteKing;
	private King blackKing;
	private boolean whiteTurn;
	private ArrayList<TimeMove> history;
	private int halfMovesSinceAction;
	private int fullMoves;
	private String enPassantMove;



	private void boardSet() {
		int x = 0, y = internboard.getHeight()-1;
		for (int i = 0; boardDefault.get(i)!=' '; i++) {
			boolean white = Character.isUpperCase(boardDefault.get(i)); // uppercases are white
			char c = Character.toLowerCase(boardDefault.get(i));
			switch (c) {
			case 'r':
				internboard.set(y*8+x, new Rook(y*8+x, white));
				break;
			case 'b':
				internboard.set(y*8+x, new Bishop(y*8+x, white));
				break;
			case 'n':
				internboard.set(y*8+x, new Knight(y*8+x, white));
				break;
			case 'q':
				internboard.set(y*8+x, new Queen(y*8+x, white));
				break;
			case 'p':
				internboard.set(y*8+x, new Pawn(y*8+x, white));
				break;
			case 'k':
				internboard.set(y*8+x, new King(y*8+x, white));
				if (white)
					whiteKing = (King) internboard.getPiece(y*8+x);
				else
					blackKing = (King) internboard.getPiece(y*8+x);
				break;
			case '/':
				y--;
				x = 0;
			default:
				if(Character.isDigit(c)) 
					x-=Character.getNumericValue(c);
			}
			if(c!='/') x++;
		}
		whiteTurn = true;
		history = new ArrayList<>();
		halfMovesSinceAction = 0; // for the 50 move rule, necessary for FEN
		fullMoves = 1;
		enPassantMove = null;
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

	public boolean checkmate(boolean isWhiteTurn) {
		return trap(isWhiteTurn) && check(isWhiteTurn);
	}

	public String getFen() {
		String after =" ";
		if(whiteTurn) after += "w ";
		else after +="b ";

		after +=castleAvailibility(internboard) + " ";
		if(enPassantMove == null) after += "- ";
		else after +=enPassantMove + " ";

		after += halfMovesSinceAction + " ";
		after += fullMoves;

		return internboard.toFen() + after;
	}

	public boolean checkmate() {
		return trap(whiteTurn) && check(whiteTurn);
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

	public void init() {
		File config = new File("./assets/startingboard.brd");
		boardDefault = FileRead.readFile(config, StandardCharsets.UTF_8);
		internboard = new LinBoard(8, 8);
		boardSet();
	}

	//returns in FENotation
	private String castleAvailibility(LinBoard board) {
		String result = "";
		Rook wQueenside = null, wKingside = null, bQueenside = null, bKingside = null;
		King wKing = null, bKing = null;
		/*Technically implementation dependent on board,
		 * must be rewritten whenever board is changed.
		 * This can also have undefined behavior in cases of 
		 * 1 or less kings.*/
		for(Piece p : board.getBoard()) { 
			if(p instanceof Rook) {
				if(!p.hasMoved()) {
					if(p.isFirst) {
						if(wQueenside==null) wQueenside = (Rook) p;
						wKingside = (Rook) p;
					}else {
						if(bQueenside==null) bQueenside = (Rook) p;
						bKingside = (Rook) p;
					}	
				}	
			}else if(p instanceof King) {
				if(p.isFirst) wKing = (King) p;
				else bKing = (King) p;
			}
		}
		assert wKing != null;
		if(!wKing.hasMoved()) {
			assert wKingside != null;
			if(!wKingside.hasMoved()) result += "K";
			if(!wQueenside.hasMoved()) result += "Q";
		}
		if(!bKing.hasMoved()) {
			if(!bKingside.hasMoved()) result += "k";
			if(!bQueenside.hasMoved()) result += "w";
		}

		if(result.isEmpty()) return"-";

		return result;
	}

	/** (k)ing's position, then (r)ook's position.*/
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
	}

	private void move(int loc, int origin, char c) throws InvalidMoveException {
		LinBoard bufferboard = internboard.copy();
		Piece p = internboard.getPiece(origin);
		Piece r;
		if(p instanceof Pawn&&internboard.toRow(loc)==7||internboard.toRow(loc)==0){
			r = switch (c) {
				case 'q' -> new Queen(loc, whiteTurn);
				case 'n' -> new Knight(loc, whiteTurn);
				case 'r' -> new Rook(loc, whiteTurn);
				case 'b' -> new Bishop(loc, whiteTurn);
				default -> new Pawn(loc, whiteTurn);
			};
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
		}else if(board.getPiece(origin) instanceof Pawn && board.getPiece(loc + board.getWidth()) instanceof Pawn){
			set(loc, origin, board);
			board.set(loc + board.getWidth(), null);
		}else{
			throw new InvalidMoveException("Invalid Passant");
		}
	}


	@SuppressWarnings("unused")
	private void printInternboard() { // TODO: this is a debug function. comment out later.
		char shorthand;
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
	}  

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
		ArrayList<Integer> moves = new ArrayList<>(Arrays.asList(king.getMoves(internboard)));
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
		return moves.size() == 0;
	}

	private boolean turn(int loc, int origin, char piece) throws InvalidMoveException {
		boolean mate = check(whiteTurn);
		if (mate && trap(whiteTurn))
			return false;
		else {
			move(loc, origin, piece);
			return !(check(whiteTurn) && trap(whiteTurn));
		}
	}

	private boolean turn(int loc, int origin) throws InvalidMoveException {
		boolean mate = check(whiteTurn);
		if (mate && trap(whiteTurn))
			return false;
		else {
			move(loc, origin);
			return !(check(whiteTurn) && trap(whiteTurn));
		}
	}
	public TimeMove[] history() {
		return history.toArray(new TimeMove[0]);
	}
	public boolean turn(TimeMove m) throws InvalidMoveException {
		boolean result;
		if(m.promote!=' ')//noinspection UnusedAssignment
			result = turn(m.location, m.origin, m.promote);
		result = turn(m.location, m.origin);
		history.add(m);
		if(internboard.getPiece(m.location) instanceof Pawn
				|| internboard.getPiece(m.location) != null)
			halfMovesSinceAction = 0;
		else halfMovesSinceAction++;
		if(internboard.getPiece(m.location) instanceof Pawn) {
			int i;
			if(internboard.getPiece(m.location).isFirst) i = -internboard.getWidth();
			else i = internboard.getWidth();
			enPassantMove = code(m.location + i);
		}
		if(whiteTurn) fullMoves++;
		return result;
	}

	public int denotate(String notatation) {
		// abcdefghijklmnopqrstuvwxyz zyxwvutsrqpomnlkjihgfedcba
		int result;
		if (!Character.isDigit(notatation.charAt(1)))
			throw new IllegalArgumentException("Not Algabraic notation");
		if (ab.indexOf(notatation.charAt(0)) == -1 || ab.indexOf(notatation.charAt(0)) > internboard.getHeight())
			throw new IllegalArgumentException("Not Algabraic notation");
		result = (Character.getNumericValue(notatation.charAt(1)) - 1) * internboard.getWidth();
		result += ab.indexOf(Character.toLowerCase(notatation.charAt(0)));
		if (result >= internboard.getHeight() * internboard.getWidth()) 
			throw new IndexOutOfBoundsException("Out of bounds");
		return result;
	}

	public boolean qualifyMove(String move){
		if(move.length()<4) return false;
		if(Character.isAlphabetic(move.charAt(0))&&
				Character.isAlphabetic(move.charAt(2)))
			return false;
		return !Character.isDigit(move.charAt(1)) ||
				!Character.isDigit(move.charAt(3));
	}

	public TimeMove decode(String move) {
		if(move.length()==5)return new TimeMove(
				denotate(move.substring(2, 4)),
				denotate(move.substring(0, 2)),
				move.charAt(4),
				1);

		return new TimeMove(denotate(move.substring(2, 4)), denotate(move.substring(0, 2)), 1);
	}

	private String code(int m) {
		return ab.charAt(m % 8) + Integer.toString(1 + m / 8);
	}

	public String notate(TimeMove move) {
		return code(move.origin) + code(move.location);
	}

}
