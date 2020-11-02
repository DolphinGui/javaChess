package javaChess;

import java.io.FileNotFoundException;
import java.io.IOException;
import javaChess.Session.ChessClock;
import terminalChess.ChessDisplay;
import terminalChess.Display;
import vanillaChess.AlgebraicMove;
import vanillaChess.Game;

public class Human extends Player {

	final ChessDisplay screen;
	
	public Human(boolean white, Game game, Display s) {
		super(white, game);
		screen = new ChessDisplay(s);
		screen.initGame(game.getCharBoard());
	}

	@Override
	void updateScreen(AlgebraicMove m) {
		try {
			screen.turn(board.getCharBoard(), board.notate(m));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	AlgebraicMove onTurn() {
		
		return board.decode(screen.listenGame());
	}

	@Override
	AlgebraicMove offTurn() {
		return new AlgebraicMove();
	}

	@Override
	public void run() {}
	
	@Override
	public void error(String s) {
		screen.errorMessage(s);
	}

	@Override
	void stopPonder() {}

	@Override
	void ponderhit() {}

	@Override
	void victoryScreen() {
		try {
			screen.victoryScreen();
		} catch (FileNotFoundException e) {
			System.exit(2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		screen.endClock();
	}

	@Override
	void lossScreen() {
		// TODO Auto-generated method stub
		screen.endClock();
	}

	@Override
	void drawTime(int w, int b) {
		// TODO Auto-generated method stub
		//screen.
	}

	
}
