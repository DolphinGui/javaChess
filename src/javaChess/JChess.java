package javaChess;
import java.io.IOException;

//import baseChess.AlgNotation;
import baseChess.LinBoard;
//import terminalChess.ChessTerminal;

public class JChess {
	
	/* TODO:
	 * linear chess board
	 * move validator
	 * UI of some sort, maybe terminal or gui
	 * central game engine
	 * stockfish implementation?
	 * */
	public static void main(String[] args) throws IOException {
	    
		LinBoard board = new LinBoard(8,8);
		board.setBoard();
		for(Integer move: board.getPiece(12).getMoves(board)) System.out.println(move);
		

	}

}
