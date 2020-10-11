package javaChess;

import terminalChess.ChessTerminal;
import vanillaChess.Game;

public class Human extends Player {

	ChessTerminal screen;
	
	public Human(boolean white, Game game, boolean turn, int t, ChessTerminal screen) {
		super(white, game, turn, t);
	}

	@Override
	AlgebraicMove onTurn(int t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	AlgebraicMove offTurn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void error(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void stopPonder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void ponderhit() {
		// TODO Auto-generated method stub
		
	}

}
