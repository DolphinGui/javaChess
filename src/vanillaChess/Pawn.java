package vanillaChess;
import miscFunct.ArrayMan;

import java.util.ArrayList;

import baseChess.LinBoard;
import baseChess.Piece;

public class Pawn extends Piece {
    public Pawn(int loc, boolean fealty) {
	super(loc, fealty, "Pawn", 'p');
    }
    private final static String name = "Pawn";
    private final static char shorthand = 'p';
    private boolean hasMoved = false;

    public void setLoc(int loc) {
	location = loc;
	hasMoved = false;
    }
    
    public String getName() {
	return name;
    }
    public char getShort() {
	return shorthand;
    }

    public Integer[] exception(LinBoard board){
	return ArrayMan.concatAll(capture(board), initiative(board), advance(board)) ;
    }

    private Integer[] advance(LinBoard board) {
	Integer[] direction = {0,1};
	if(board.getPiece(direction) == null) {
	    Integer[] results = {board.vecToInteger(direction)};
	    return results;
	}
	return null;
    }

    private Integer[] initiative(LinBoard board) {
	if (!hasMoved) {
	    Integer[] init = {board.getWidth()*2};
	    return init;
	}
	return null;
    }

    private Integer[] capture(LinBoard board) {
	Integer[] NW = {-1,1};
	Integer[] NE = {1,1};
	
	ArrayList<Integer> validX = new ArrayList<Integer>();
	if (!board.checkFealty(this.getLoc()+board.vecToInteger(NW), this.isFirst)){
	    validX.add(board.vecToInteger(NW));
	}
	if (!board.checkFealty(this.getLoc()+board.vecToInteger(NE), this.isFirst)){
	    validX.add(board.vecToInteger(NE));
	}
	return validX.toArray(new Integer[validX.size()]);
    }
}
