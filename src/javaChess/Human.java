package javaChess;

import java.io.FileNotFoundException;
import java.io.IOException;

import terminalChess.ChessDisplay;
import terminalChess.Display;

public class Human<T extends Move> extends Player<T> {

	final ChessDisplay screen;
	
	public Human(boolean white, Game<T> game, Display s) {
		super(white, game);
		screen = new ChessDisplay(s);
		screen.initGame(game.getCharBoard());
	}

	@Override
	void updateScreen(T m) {
		try {
			screen.turn(board.getCharBoard(), board.notate(m));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	T onTurn() {
		return board.decode(screen.listenGame());
	}

	@SuppressWarnings("unchecked")
	@Override
	T offTurn() {
		return (T) new Move();
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
