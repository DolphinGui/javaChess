package javaChess;

import vanillaChess.Game;

public abstract class Player implements Runnable {
	public final boolean isWhite;
	public final Game board;
	private boolean myTurn;
	//private boolean exists;
	int time;
	private AlgebraicMove lastmove;
	private AlgebraicMove ponder;
	protected NotationInterperter denote;

	public AlgebraicMove lastMove() {
		return lastmove;
	}
	
	public AlgebraicMove pondermove() {
		return ponder;
	}
	
	public Player(boolean white, Game game, boolean turn, int t, NotationInterperter n) {
		isWhite = white;
		board = game;
		myTurn = turn;
		//exists = true;
		time = t;
		denote = n;
	}

	public boolean turn() {
		return myTurn;
	}

	@Override
	public void run() {
		while(!myTurn) {
			ponder = offTurn();
		}
	}

	public void updateTime(int t) {
		time = t;
	}

	public abstract void error(String s);
	
	//what it does on its turn, like thinking
	abstract AlgebraicMove onTurn(int t);
 
	//what it does off its turn, like pondering.
	abstract AlgebraicMove offTurn();

	abstract void stopPonder();
	abstract void ponderhit();
	abstract void victoryScreen();
	abstract void lossScreen();
	
	public void switchTurns() {
		myTurn = !myTurn;
		this.notify();
	}

	//override and call this
	void close() {
		//exists = false;
	}

}
