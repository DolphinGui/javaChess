package vanillaChess;

import baseChess.LinBoard;
import baseChess.Piece;
import miscFunct.FileRead;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Game {
    private ArrayList<Character> boardDefault;
    private File config;
    private LinBoard internboard;
    private King whiteKing;
    private King blackKing;
    private boolean whiteTurn;
    
    public void init() throws IOException {
	config = new File("./assets/board.brd");
	boardDefault = FileRead.readFile(config, StandardCharsets.UTF_8);
	internboard = new LinBoard(8,8);
	this.boardSet();
    }

    
    private void boardSet() {
	for(int i=0; i<internboard.getLength();i++) {
	    boolean white = Character.isUpperCase(boardDefault.get(i)); //uppercases are white
	    char c = Character.toLowerCase(boardDefault.get(i));
	    if(c=='r') internboard.set(i, new Rook(i, white));
	    else if(c=='b') internboard.set(i, new Bishop(i, white));
	    else if(c=='k') internboard.set(i, new Knight(i, white));
	    else if(c=='x') internboard.set(i, new Queen(i, white));
	    else if(c=='p') internboard.set(i, new Pawn(i, white));
	    else if(c=='y') {
		internboard.set(i, new King(i, white));
		if(white) whiteKing= (King) internboard.getPiece(i);
		else blackKing = (King) internboard.getPiece(i);
	    }
	}
	whiteTurn = true;
    }

    public int turn(int loc, int origin) {
	//0 is successful, 1 is failed, 2 is check, and 3 is checkmate
	boolean mate = check(whiteTurn);
	if(mate && trap()) return 3;
	else if(mate)return 2;
	else if(move(loc, origin)) {
	    whiteTurn = !whiteTurn; 
	    return 0;
	} else return 1;
    }

    public boolean move(int loc, int origin) {
	Piece p = internboard.getPiece(origin);
	if(internboard.checkFealty(origin, whiteTurn)) return false; //checks if it's the piece's turn
	for(Integer m: p.getMoves(internboard)) { 
	    if(m+origin==loc) {      //validates that the resulting location is a valid move
		if(check(whiteTurn))return false; //validates no checkmate
		internboard.set(loc, p);
		p.setLoc(loc);
		return true;

	    }
	}
	return false;
    }
    /*
    @SuppressWarnings("unused")
    private void printInternboard() { //this is a debug function. comment out later.
	int i = 0;
	for(Piece p : internboard.getBoard()) {
	    i++;
	    if(p!=null) System.out.print(" "+p.getShort()+" ");
	    else System.out.print(" n ");
	    if(i%internboard.getWidth()==0) System.out.println();
	}
    } */
    
    private boolean check(boolean white) {
	for(Piece p : internboard.getPieces()) {
	    if(!white==p.getFealty()) {
		int k;
		if(white) k = whiteKing.getLoc();
		else k=blackKing.getLoc();
		for(int m : p.getMoves(internboard)) {
		    if(m==k) return true;
		}
	    }
	}
	return false;
    }
    private boolean trap() {
	if(whiteKing.getMoves(internboard).length==0)return true;
	if(blackKing.getMoves(internboard).length==0)return true;
	return false;
    }

}
