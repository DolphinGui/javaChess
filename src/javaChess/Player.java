package javaChess;


import javaChess.Session.ChessClock;

public abstract class Player<T extends Move<E>, E> implements Runnable {
	public final boolean isWhite;
	public final Game<T, E> board;
	protected boolean myTurn;
	//private boolean exists;
	protected final T lastmove;
	protected T ponder;
	protected ChessClock<T, E> time;

	public T lastMove() {
		return lastmove;
	}
	
	public T pondermove() {
		return ponder;
	}
	
	public Player(boolean white, Game<T, E> game) {
		isWhite = white;
		board = game;
		myTurn = white;
		ponder = null;
		lastmove = null;
	}

	public void setClock(ChessClock<T, E> c){
		time = c;
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
	
	abstract void drawTime(int w, int b);

	public abstract void error(String s);
	
	//what it does on its turn, like thinking
	abstract T onTurn();
 
	//what it does off its turn, like pondering.
	abstract T offTurn();

	abstract void stopPonder();
	abstract void ponderhit();
	abstract void victoryScreen();
	abstract void lossScreen();
	abstract void updateScreen(T m);
	
	public void switchTurns() {
		myTurn = !myTurn;
		stopPonder();
	}

	//override and call this
	void close() {
		//exists = false;
	}

}
