package javaChess;

import java.io.IOException;

import terminalChess.ChessTerminal;
import vanillaChess.Game;

public class JChess {

	/*
	 * TODO: stockfish implementation?
	 */
	public static void main(String[] args) {
		Game chess = new Game();
		ChessTerminal screen;
		try {
			screen = new ChessTerminal();
			chess.init();

			char choice = screen.initStart();

			switch (Character.toLowerCase(choice)) {
			case 'l':
				gameloop(chess, screen);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void gameloop(Game chess, ChessTerminal screen) throws IOException {
		screen.initGame(chess.getCharBoard());
		NotationInterperter denote = new NotationInterperter(chess.getWidth(), chess.getHeight());
		int result = 0;
		while (true) {
			if (screen.resized()) {
				screen.refreshGame(chess.getCharBoard());
			}
			if (result == 3) { // check if previous loop's result was a checkmate
				screen.destroy(); // make this a victory screen
				break;
			}
			String move = screen.listenGame();
			screen.errorMessage("                   ");
			int loc = denote.denotate(move.substring(2, 4));
			int origin = denote.denotate(move.substring(0, 2));
			if (loc == -1 || origin == -1) { // -1 means out of bounds, -2 means bad input
				screen.errorMessage("Out of bounds");
				continue;
			}
			if (loc == -2 || origin == -2) { // -1 means out of bounds, -2 means bad input
				screen.errorMessage("Bad input");
				continue;
			}
			result = chess.turn(loc, origin); // 0 is successful, 1 is failed, 2 is check
			if (result == 1) { // -1 means out of bounds, -2 means bad input
				screen.errorMessage("Invalid move");
				continue;
			}
			screen.turn(chess.getCharBoard(), move);
		}
	}

}
