package javaChess;

import java.io.FileNotFoundException;

import vanillaChess.Game;
import vanillaChess.InvalidMoveException;

public class Session {
	Player black;
	Player white;
	Game board;
	NotationInterperter denote;
	
	// h is human, b is bot, n is network
	public void init(char blackChoice, char whiteChoice) {
		try {
			board.init();
		} catch (FileNotFoundException e) {
			System.exit(1);
		}
		board = new Game();
		denote = new NotationInterperter(board.getWidth(), board.getHeight());
	}
	
	public void main() {
		boolean exists = true;
		do {
			white.run();
			black.run();
			AlgebraicMove move = white.onTurn(0);
			black.stopPonder();
			AlgebraicMove ponder = black.pondermove();
			
			try {
				board.turn(move.loc, move.origin);
				if(ponder.equals(move)) black.ponderhit();
				black.switchTurns();
				white.switchTurns();
			} catch (InvalidMoveException e) {
				white.error(e.getMessage());
			}
		}while(exists);
	}
}