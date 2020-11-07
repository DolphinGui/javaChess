package javaChess;

import terminalChess.Display;
import vanillaChess.VanillaGame;

import java.io.File;
import java.io.IOException;

public class JChess {

	/*
	 * TODO: stockfish implementation?
	 */
	public static void main(String[] args) {
		Display screen;

		try {
			screen = new Display();

			char choice = screen.initStart();
			switch (choice) {
				case 'l' -> {
					Session<?,?> game = new Session<>(screen, screen, new VanillaGame());
					game.play();
				}
				case 'b' -> {
					Session<?,?> game = new Session<>(screen, true, screen.findFile(
							new File("."),
							"Choose bot binary",
							(File f)-> f.canExecute()||!f.isHidden()).getPath(),
							new VanillaGame());
					game.play();
				}
			}
			screen.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
