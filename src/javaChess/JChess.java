package javaChess;
import java.io.IOException;

import terminalChess.ChessTerminal;
import vanillaChess.Game;

public class JChess {
	
	/* TODO:
	 * UI of some sort, maybe terminal or gui
	 * central game engine
	 * stockfish implementation?
	 * */
	public static void main(String[] args) throws IOException {///*
		Game chess = new Game();
		ChessTerminal screen = new ChessTerminal();
		chess.init();
		screen.initGame(chess.getCharBoard());
		int result = 0;
		while(true) {
		    if(result == 3) {
			screen.destroy();
		    	break;
		    }
		    String out = screen.listenGame();
		    int loc = NotationInterperter.denotate(out.substring(2, 4));
		    int origin = NotationInterperter.denotate(out.substring(0, 2));
		    result = chess.turn(loc, origin);
		    screen.boardUpdate(chess.getCharBoard());
		}
		//*/

	}

}
