package baseChess;

import java.util.ArrayList;

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
    
    public LinBoard(Piece[] pieces, int rank, int file) {
	board = pieces;
	ranks = rank;
	files = file;
    }
    
    public boolean checkFealty(int loc, boolean fealty) {
	 //checks if piece is capturable, if null then defaults to yes.
	if(board[loc]!=null) return (board[loc].getFealty()!=fealty);
	return true;
    }
    
    public LinBoard copy() {
	return new LinBoard(board, ranks, files);
    }
    public Piece[] getBoard() {
	return board;
    }
    
    public int getHeight() {
	return ranks;
    }
    public int getLength() {
	return board.length;
    }
    
    public Piece getPiece(int loc) {
	if(!inBounds(loc)) return null;
	return board[loc];
    }
    public Piece getPiece(Integer[] vec, Integer[] coord) {
	return board[vecToInteger(vec) + vecToInteger(coord)];
    }
    
    public Piece[] getPieces() {
	ArrayList<Piece> results = new ArrayList<Piece>();
	for(Piece p:board) {
	    if(p!=null) results.add(p);
	}
	return results.toArray(new Piece[results.size()]);
    }
    
    public Piece[] getPieces(boolean fealty) {
	ArrayList<Piece> results = new ArrayList<Piece>();
	for(Piece p:board) {
	    if(p!=null) if(p.getFealty()==fealty) results.add(p);
	}
	return results.toArray(new Piece[results.size()]);
    }
    
    public int getWidth() {
	return files;
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
    
    public Integer[] locToVec(int loc) {
	//this turns an integer location to a vector
	Integer[] vector = new Integer[2];
	vector[0] = loc%files;
	vector[1] = Math.floorDiv(loc, ranks);
	return vector;
    }
    public void remove(int loc) {
	board[loc]=null;
    }
    public void set(int loc, Piece piece) {
	board[loc] = piece;
    }
    public void setBoard(Piece[] b) {
	board = b;
    }
    public int vecToInteger(Integer[] vector) {
	// This takes a 2 element vector and transforms it into an integer. it ignores all other elements.
	return vector[0]+vector[1]*files;
    }
}
