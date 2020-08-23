package javaChess;
import java.io.IOException;

//import terminalChess.ChessTerminal;
import vanillaChess.Game;

public class JChess {
	
	/* TODO:
	 * UI of some sort, maybe terminal or gui
	 * central game engine
	 * stockfish implementation?
	 * */
	public static void main(String[] args) throws IOException {
	    
		Game chess = new Game();
		//ChessTerminal screen = new ChessTerminal();
		
		chess.init();
		System.out.println(chess.move(21, 13));
		System.out.println(chess.move(36, 52));
		System.out.println(chess.move(30, 14));
		System.out.println(chess.move(31, 59));
		System.out.println(chess.checkmate());
		//screen.initStart();
		//screen.destroy();

	}

}
