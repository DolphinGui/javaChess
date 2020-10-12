package javaChess;

import terminalChess.ChessDisplay;
import terminalChess.Display;
import vanillaChess.Game;

public class Human extends Player {

	ChessDisplay screen;
	
	public Human(boolean white, Game game, boolean turn, int t, NotationInterperter n, Display s) {
		super(white, game, turn, t, n);
		screen = new ChessDisplay(s);
		screen.initGame(game.getCharBoard());
	}

	@Override
	AlgebraicMove onTurn(int t) {
		return denote.decode(screen.listenGame());
	}

	@Override
	AlgebraicMove offTurn() {
		return null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	void lossScreen() {
		// TODO Auto-generated method stub
		
	}

}
