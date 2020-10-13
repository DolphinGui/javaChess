package javaChess;

import java.io.FileNotFoundException;

import terminalChess.Display;
import vanillaChess.Game;
import vanillaChess.InvalidMoveException;

public class Session {
	Player black;
	Player white;
	Game board;
	NotationInterperter denote;
	
	public Session(Display db, Display dw) throws FileNotFoundException {
		board = new Game();
		board.init();
		denote = new NotationInterperter(board.getWidth(), board.getHeight());
		black = new Human(false, board, 0, denote, db);
		white = new Human(true, board, 0, denote, dw);
	}
	
	public void localPlay() {
		boolean exists = true;
		boolean whiteTurn = true;
		do {
			white.run();
			black.run();
			
			AlgebraicMove move;
			if(whiteTurn)move = white.onTurn(0);
			else move = black.onTurn(0);
			black.stopPonder();
			AlgebraicMove ponder = black.pondermove();
			try {
				exists = board.turn(move.loc, move.origin);
				black.updateScreen(move);
				if(ponder.equals(move)) black.ponderhit();
				black.switchTurns();
				white.switchTurns();
				whiteTurn = !whiteTurn;
			} catch (InvalidMoveException e) {
				white.error(e.getMessage());
			}
		}while(exists);
		if(board.checkmate(true)) white.victoryScreen();
		else black.victoryScreen();
	}
	public void remotePlay() {
		boolean exists = true;
		boolean whiteTurn = true;
		do {
			white.run();
			black.run();
			
			AlgebraicMove move;
			if(whiteTurn)move = white.onTurn(0);
			else move = black.onTurn(0);
			black.stopPonder();
			AlgebraicMove ponder = black.pondermove();
			try {
				exists = board.turn(move.loc, move.origin);
				black.updateScreen(move);
				white.updateScreen(move);
				if(ponder.equals(move)) black.ponderhit();
				black.switchTurns();
				white.switchTurns();
				whiteTurn = !whiteTurn;
			} catch (InvalidMoveException e) {
				white.error(e.getMessage());
			}
		}while(exists);
		if(board.checkmate(true)) {
			white.victoryScreen();
			black.lossScreen();
		}else {
			white.lossScreen();
			black.victoryScreen();
		}
	}
}
