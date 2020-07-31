package javaChess;
import java.io.IOException;

//import baseChess.AlgNotation;
import baseChess.LinBoard;
//import terminalChess.ChessTerminal;

public class JChess {
	
	/* TODO:
	 * linear chess board
	 * move validator
	 * 	-chess pieces
	 * 		-rook
	 * 		-knight
	 * 		-bishop
	 * 		-queen
	 * 		-king
	 * UI of some sort, maybe terminal or gui
	 * */
	public static void main(String[] args) throws IOException {
		LinBoard board = new LinBoard();
		board.setBoard();
		Integer[] moves = board.getPiece(23).getMoves(board);
		for(int move: moves) System.out.println(move);
	}

}
