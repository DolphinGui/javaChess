package vanillaChess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import baseChess.LinBoard;
import baseChess.Piece;

public class King extends Piece {
    Integer[][] moves = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
    
    
    public King(int loc, boolean fealty) {
	super(loc, fealty);
    }

    private Integer[] validator(LinBoard board) {
	ArrayList<Integer> results = new ArrayList<Integer>();
	for(Integer[] move: moves) {
	    if(board.inBounds(board.locToVec(location),move)) {
		if(board.checkFealty(board.vecToInteger(move)+location, isFirst))  results.add(board.vecToInteger(move)+location);
	    }
	}
	
	ArrayList<Piece> stuff = new ArrayList<Piece>();
	for(Piece p: board.getPieces()) {
	    if(p!=this&&p.getFealty()!=this.isFirst)stuff.add(p);
	}
	for(Piece p: stuff) {
	    List<Integer> m = Arrays.asList(p.getMoves(board));
	    results.removeAll(m);
	}
	ArrayList<Integer> cleaned = new ArrayList<Integer>();
	for(Integer m: results) {
	    m -=location;
	    cleaned.add(m);
	}
	
	return cleaned.toArray(new Integer[cleaned.size()]);
    }
    
    public Integer[] getMoves(LinBoard board) {
	return validator(board);
    }
    
}
