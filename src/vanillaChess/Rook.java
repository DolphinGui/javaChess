package vanillaChess;

import java.util.ArrayList;

import baseChess.LinBoard;
import baseChess.Piece;
import miscFunct.ArrayMan;

public class Rook extends Piece {
    public static final Integer[] move = {};
    private final static String name = "Rook";
    private final static char shorthand = 'r';

    public Rook(boolean white, int loc){
	isFirst = white;
	location = loc;
    }

    public char getShort() {
	return shorthand;
    }

    public String getName() {
	return name;
    }
    
    private boolean compare(int x, int y, boolean greater) {
	if(greater) return (x>=y);
	else return (x<=y);
    }
    
    private Integer[] move(LinBoard board, boolean right, boolean rank) {
	ArrayList<Integer> moves = new ArrayList<Integer>();
	int inc;
	int end;
	int factor;
	if(rank) factor=1;
	else factor=8;
	if(right) {
	    inc=1;
	    end=7;
	}
	else {
	    inc=-1;
	    end=0;
	}
	for(int i = inc+this.getLoc(); compare(i, end+8*this.getRow(), !right); i+=inc) {
	    if(board.getPiece(i)==null) {
		moves.add(i-this.getLoc());
	    }else if(i==inc+this.getLoc()) {
		break;
	    }else if(!board.checkFealty(i, isFirst)) {
		moves.add(i-this.getLoc());
		break;
	    }else if(board.checkFealty(i, isFirst)) {
		break;
	    }
	}
	return moves.toArray(new Integer[moves.size()]);
    }

    public Integer[] exception(LinBoard board){
	return ArrayMan.concatAll(move(board, false, true));
    }

}
