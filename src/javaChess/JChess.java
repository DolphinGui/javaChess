package javaChess;

import java.io.File;
import java.io.IOException;

import terminalChess.Display;
import vanillaChess.Game;

public class JChess {

	/*
	 * TODO: stockfish implementation?
	 */
	public static void main(String[] args) {
		Display screen;

		try {
			screen = new Display();

			char choice = screen.initStart();
			Session<Move> game;
			switch (choice) {
				case 'l' -> {
					game = new Session<>(screen, screen, new Game());
					game.play();
				}
				case 'b' -> {

					game = new Session<>(screen, true, screen.findFile(
							new File("assets/"),
							"Choose bot binary",
							(File f)-> f.canExecute()||!f.isHidden()).getPath(),
							new Game());
					game.play();
				}
			}
			screen.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
