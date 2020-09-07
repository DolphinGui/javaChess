package vanillaChess;

import java.util.ArrayList;

import baseChess.LinBoard;
import baseChess.Piece;
import miscFunct.ArrayMan;

public class Rook extends Piece {
    public static final Integer[] move = {};

    private final static String name = "Rook";
    private final static char shorthand = 'r';
    public Rook(int loc, boolean fealty) {
	super(loc, fealty, "Rook", 'r');
    }

    private boolean comp(int n, int m, boolean greater) {
	if(greater) return (n>m);
	else return (n<m);
    }

    public Integer[] exception(LinBoard board){
	return ArrayMan.concatAll(move(board, false, true),move(board, true, true),move(board, false, false),move(board, true, false));
    }

    public String getName() {
	return name;
    }
    
    public char getShort() {
	return shorthand;
    }

    private Integer[] move(LinBoard board, boolean right, boolean rank) {
	ArrayList<Integer> moves = new ArrayList<Integer>();
	int endpoint;
	Integer[] direction = {0,0};
	if(rank) {
	    endpoint=board.getHeight()*this.getRow()-1; //calculates and sets endpoints and directions
	    direction[0] = -1;
	    if(right) {
		endpoint+=board.getHeight();
		direction[0] = 1;
	    }
	}else {
	    endpoint = this.getCol()-board.getWidth();
	    direction[1] = -1;
	    if(right) {
		endpoint+=(board.getHeight()+1)*board.getWidth();
		direction[1] = 1;
	    }
	}
	for(int i=this.getLoc()+board.vecToInteger(direction); comp(i, endpoint, !rank); i+=board.vecToInteger(direction)) {
	    Piece p = board.getPiece(i);
	    if(p==null) {
		moves.add(i);
	    }else if (p.getFealty()!=this.getFealty()) {
		moves.add(i);
		break;
	    }else {
		break;
	    }
	}
	return moves.toArray(new Integer[moves.size()]);
    }

}
