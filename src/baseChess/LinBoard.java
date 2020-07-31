package baseChess;

//import vanillaChess.Pawn;
import vanillaChess.Rook;

public class LinBoard {
    private Piece[] board = new Piece[64];
    public void setBoard() {
	board[23] = new Rook(false, 23);
    }
    
    
    
    
    
    
    public boolean checkFealty(int loc, boolean fealty) {
	if(board[loc]!=null) return (board[loc].getFealty()==fealty);
	return true;
    }
    public boolean checkFealty(AlgNotation notate, boolean fealty) {
	if(board[notate.getLoc()]!=null) return (board[notate.getLoc()].getFealty()==fealty);
	return false;
    }
    
    public Piece getPiece(int loc) {
	return board[loc];
    }
    public Piece getPiece(AlgNotation notate) {
	return board[notate.getLoc()];
    }
    public void remove(int loc) {
	board[loc]=null;
    }
    public void remove(AlgNotation notate) {
	board[notate.getLoc()]=null;
    }
    public void set(int loc, Piece piece) {
	board[loc] = piece;
    }
    public boolean move(AlgNotation origin, AlgNotation dest) {
	for(int moves : board[origin.getLoc()].getMoves(this)) {
	    if((dest.getLoc()-origin.getLoc()) == moves) {
		return true;
	    }
	}
	return false;
    }
    public void set(AlgNotation notate, Piece piece) {
	board[notate.getLoc()] = piece;
    }
}
