package vanillaChess;
import miscFunct.ArrayMan;

import java.util.ArrayList;

import baseChess.LinBoard;
import baseChess.Piece;

public class Pawn extends Piece {
	public static final Integer[] move = {8};
	private final static String name = "Pawn";
	private final static char shorthand = 'p';
	
	public Pawn(boolean white){
		isFirst = white;
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
		if(board.getPiece(this.getLoc() + 8) == null) {
			return move;
		}
		return null;
	}
	
	private Integer[] initiative(LinBoard board) {
		if (this.getCol()==1 && board.getPiece(this.getLoc()+2)==null) {
			Integer[] init = {16};
			return init;
		}
		return new Integer[0];
	}
	
	private Integer[] capture(LinBoard board) {
		ArrayList<Integer> validX = new ArrayList<Integer>();
		if (!board.checkFealty(this.getLoc()+7, this.isFirst)){
			validX.add(7);
		}
		if (!board.checkFealty(this.getLoc()+9, this.isFirst)){
			validX.add(9);
		}
		return validX.toArray(new Integer[validX.size()]);
	}
}
