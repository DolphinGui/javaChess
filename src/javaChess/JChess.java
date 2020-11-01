package javaChess;

import java.io.File;
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
				case 'l' -> {
					game = new Session(screen, screen);
					game.play();
				}
				case 'b' -> {

					game = new Session(screen, true, screen.findFile(
							new File("assets/"),
							"Choose bot binary",
							(File f)-> f.canExecute()||!f.isHidden()).getPath());
					game.play();
				}
			}
			screen.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
