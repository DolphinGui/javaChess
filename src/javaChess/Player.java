package javaChess;

import vanillaChess.Game;

public abstract class Player implements Runnable {
	public final boolean isWhite;
	public final Game board;
	private boolean myTurn;
	//private boolean exists;
	int time;
	AlgebraicMove lastmove;
	AlgebraicMove ponder;

	public Player(boolean white, Game game, boolean turn, int t) {
		isWhite = white;
		board = game;
		myTurn = turn;
		//exists = true;
		time = t;
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

	//what it does on its turn, like thinking
	abstract AlgebraicMove onTurn(int t);
 
	//what it does off its turn, like pondering. Must end in wait()
	abstract AlgebraicMove offTurn();

	public void switchTurns() {
		myTurn = !myTurn;
		this.notify();
	}

	//override and call this
	void close() {
		//exists = false;
	}

}
