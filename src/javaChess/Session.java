package javaChess;

import java.io.FileNotFoundException;

import vanillaChess.Game;

public class Session {
	Player black;
	Player white;
	Game board;
	
	public void main() {
		try {
			board.init();
		} catch (FileNotFoundException e) {
			System.exit(1);
		}
		
	}
}
