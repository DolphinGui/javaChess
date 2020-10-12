package javaChess;

import java.io.IOException;

import terminalChess.Display;
import terminalChess.ChessDisplay;
import vanillaChess.Game;

public class JChess {

	/*
	 * TODO: stockfish implementation?
	 */
	public static void main(String[] args) {
		Game chess = new Game();
		Display screen;
		try {
			screen = new Display();
			chess.init();

			char choice = screen.initStart();

			switch (choice) {
			case 'l':
				gameloop(chess, screen);
				screen.victoryScreen();
				break;
			}
			screen.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void gameloop(Game chess, Display s) throws IOException {
		ChessDisplay screen = new ChessDisplay(s);
		NotationInterperter denote = new NotationInterperter(chess.getWidth(), chess.getHeight());
		boolean result = true;
		screen.initGame(chess.getCharBoard());
		while (result) {
			if (screen.resized()) {
				screen.refreshGame(chess.getCharBoard());
			}
			String move = screen.listenGame();
			screen.errorMessage("");
			try {
				int loc = denote.denotate(move.substring(2, 4));
				int origin = denote.denotate(move.substring(0, 2));
				result = chess.turn(loc, origin);
			}catch(Exception e) {
				screen.errorMessage(e.getMessage());
			}
			screen.turn(chess.getCharBoard(), move);
		}
		screen.end();
	}

}
