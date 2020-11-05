package javaChess;


import javaChess.Session.ChessClock;

public abstract class Player<T extends Move> implements Runnable {
	public final boolean isWhite;
	public final Game<T> board;
	protected boolean myTurn;
	//private boolean exists;
	protected final T lastmove;
	protected T ponder;
	protected ChessClock<T> time;

	public T lastMove() {
		return lastmove;
	}
	
	public T pondermove() {
		return ponder;
	}
	
	@SuppressWarnings("unchecked")
	public Player(boolean white, Game<T> game) {
		isWhite = white;
		board = game;
		myTurn = white;
		ponder = (T) new Move();
		lastmove = (T) new Move();
	}

	public void setClock(ChessClock<T> c){
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
