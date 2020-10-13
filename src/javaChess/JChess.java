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
		Display screen;
		Session game;
		try {
			screen = new Display();

			char choice = screen.initStart();

			switch (choice) {
			case 'l':
				game = new Session(screen, screen);
				game.localPlay();
				break;
			}
			screen.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
