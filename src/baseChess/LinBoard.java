package baseChess;

import java.util.ArrayList;

import vanillaChess.King;
import vanillaChess.Rook;

public class LinBoard {
    private Piece[] board;
    private int ranks; //length
    private int files; //width
    
    public LinBoard(int rank, int file) {
	if (rank==0 || file==0) throw new IndexOutOfBoundsException();
	board = new Piece[file*rank];
	ranks = rank; 
	files = file; 
    }
    
    public Piece[] getBoard() {
	return board;
    }
    
    public Piece[] getPieces() {
	ArrayList<Piece> results = new ArrayList<Piece>();
	for(Piece p:board) {
	    if(p!=null) results.add(p);
	}
	return results.toArray(new Piece[results.size()]);
    }
    
    public int getWidth() {
	return files;
    }
    public int getHeight() {
	return ranks;
    }
    
    public boolean inBounds(int loc) {
	if (loc>=this.board.length) return false;
	if (loc < 0) return false;
	return true;
    }
    
    public boolean inBounds(Integer[] location) {
	if (location[0]>= files) return false;
	if (location[1] >= ranks) return false;
	return true;
    }
    
    public boolean inBounds(Integer[] location,Integer[] vector) {
	if (location[0]+vector[0]>=files||location[0]+vector[0]<0) return false;
	if (location[1]+vector[1] >= ranks||location[1]+vector[1]<0) return false;
	return true;
    }
    
    
    public void setBoard() {
	this.set(12, new King(12, false)); //is a testing thing, remove later
	this.set(3, new Rook(3, true));
    }
    
    public int vecToInteger(Integer[] vector) {
	// This takes a 2 element vector and transforms it into an integer. it ignores all other elements.
	return vector[0]+vector[1]*files;
    }

    public Integer[] locToVec(int loc) {
	//this turns an integer location to a vector
	Integer[] vector = new Integer[2];
	vector[0] = loc%files;
	vector[1] = Math.floorDiv(loc, ranks);
	return vector;
    }
    
    
    public boolean checkFealty(int loc, boolean fealty) {
	 //checks if piece is capturable, if null then defaults to yes.
	if(board[loc]!=null) return (board[loc].getFealty()!=fealty);
	return true;
    }
    
    public Piece getPiece(int loc) {
	return board[loc];
    }
    public Piece getPiece(Integer[] vec) {
	return board[this.vecToInteger(vec)];
    }
    public void set(int loc, Piece piece) {
	board[loc] = piece;
    }
    public void remove(int loc) {
	board[loc]=null;
    }
}
