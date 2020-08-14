package javaChess;
import java.io.IOException;

import baseChess.LinBoard;
import terminalChess.ChessTerminal;
import vanillaChess.Game;

public class JChess {
	
	/* TODO:
	 * UI of some sort, maybe terminal or gui
	 * central game engine
	 * stockfish implementation?
	 * */
	public static void main(String[] args) throws IOException {
	    
		Game chess = new Game();
		chess.init();
		System.in.read();

	}

}
