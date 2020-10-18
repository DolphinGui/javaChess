package javaChess;


import javaChess.Session.ChessClock;
import vanillaChess.AlgebraicMove;
import vanillaChess.Game;
import vanillaChess.NotationInterperter;

public abstract class Player implements Runnable {
	public final boolean isWhite;
	public final Game board;
	protected boolean myTurn;
	//private boolean exists;
	protected AlgebraicMove lastmove;
	protected AlgebraicMove ponder;
	protected ChessClock time;
	protected NotationInterperter denote;

	public AlgebraicMove lastMove() {
		return lastmove;
	}
	
	public AlgebraicMove pondermove() {
		return ponder;
	}
	
	public Player(boolean white, Game game, ChessClock c, NotationInterperter n) {
		isWhite = white;
		board = game;
		myTurn = white;
		denote = n;
		time = c;
		ponder = new AlgebraicMove();
		lastmove = new AlgebraicMove();
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
	abstract AlgebraicMove onTurn();
 
	//what it does off its turn, like pondering.
	abstract AlgebraicMove offTurn();

	abstract void stopPonder();
	abstract void ponderhit();
	abstract void victoryScreen();
	abstract void lossScreen();
	abstract void updateScreen(AlgebraicMove m);
	
	public void switchTurns() {
		myTurn = !myTurn;
		stopPonder();
	}

	//override and call this
	void close() {
		//exists = false;
	}

}
