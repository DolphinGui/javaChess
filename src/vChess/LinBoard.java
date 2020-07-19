package vChess;

public class LinBoard {
	private Piece[] board;
	public void setBoard() {
			board[3] = new Piece();
		//set the board
		
	}
	public Piece getPiece(int loc) {
		return board[loc];
	}
	public Piece getPiece(AlgNotation notate) {
		return board[notate.getLoc()];
	}
}
