package javaChess;

import java.io.FileNotFoundException;
import java.io.IOException;
import javaChess.Session.ChessClock;
import terminalChess.ChessDisplay;
import terminalChess.Display;
import vanillaChess.AlgebraicMove;
import vanillaChess.Game;

public class Human extends Player {

	ChessDisplay screen;
	
	public Human(boolean white, Game game, ChessClock time, Display s) {
		super(white, game,time);
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
		AlgebraicMove move = board.decode(screen.listenGame());
		
		return move;
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
