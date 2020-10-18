package javaChess;

import java.io.IOException;

import terminalChess.Display;

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
				game.play();
				break;
			case 'b':
				game = new Session(screen, true, "assets/stockfish_20090216_x64_bmi2.exe");
				game.play();
				break;
			}
			screen.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
