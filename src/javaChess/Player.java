package javaChess;

public abstract class Player implements Runnable {
	public boolean isWhite;
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	abstract AlgebraicMove onTurn();
		
	
}
