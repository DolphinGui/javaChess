package javaChess;

import terminalChess.Display;
import vanillaChess.AlgebraicMove;
import vanillaChess.Game;
import vanillaChess.InvalidMoveException;

public class Session {
	@SuppressWarnings("BusyWait")
	public static class ChessClock implements Runnable{
		private boolean exists;
		private final Player player1;
		private final Player player2;
		private boolean isWhite;
		private int wTime;
		private int bTime;
		public ChessClock(Player p1, Player p2, int w, int b) {
			player1 = p1;
			player2 = p2;
			wTime = w;
			bTime = b;
			isWhite = true;
		}
		
		public int time(boolean color) {
			if(color) return wTime;
			return bTime;
		}
		
		public void swap() {
			isWhite = !isWhite;
		}
		
		public void stop() {
			exists = false;
		}
		public void start() {
			exists = true;
		}
		@Override
		public void run() {
			start();
			if(wTime!=-1 && bTime!=-1) {
				while(exists) {
					if(isWhite) wTime--;
					else bTime--;
					player1.drawTime(wTime,bTime);
					player2.drawTime(wTime,bTime);
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	final Player black;
	final Player white;
	final Game board;
	ChessClock time;

	public Session(Display db, Display dw) {
		this(db, dw, -1, -1);
	}

	public Session(Display db, Display dw, int b, int w) {
		board = new Game();
		board.init();
		black = new Human(false, board, time, db);
		white = new Human(true, board, time, dw);
		time = new ChessClock(white, black, w, b);
	}

	public Session(Display d, boolean isHumanWhite, String botPath){
		this(d, -1, -1, isHumanWhite, botPath);
	}

	public Session(Display d, int b, int w, boolean isHumanWhite, String botPath) {
		board = new Game();
		board.init();
		if(isHumanWhite) {
			black = new Bot(false,board, time, botPath);
			white = new Human(true, board, time,  d);
		}else {
			white = new Bot(false,board, time, botPath);
			black = new Human(true, board, time, d);
		}
		time = new ChessClock(white, black, w, b);
	}



	public void play() {
		boolean exists = true;
		boolean whiteTurn = true;
		//Thread wThread = new Thread(white);
		//Thread bThread = new Thread(black);
		time.start();
		do {
			//wThread.start();
			//bThread.start();
			AlgebraicMove move;
			if(whiteTurn) {
				move = white.onTurn();
			}else {
				move = black.onTurn();
			}
			white.stopPonder();
			black.stopPonder();
			AlgebraicMove ponder = black.pondermove();
			
			try {
				exists = board.turn(move);
				black.updateScreen(move);
				white.updateScreen(move);
				if(ponder.equals(move)) black.ponderhit();
				black.switchTurns();
				white.switchTurns();
				whiteTurn = !whiteTurn;
			} catch (InvalidMoveException e) {
				white.error(e.getMessage());
			}
			time.swap();
		}while(exists);
		time.stop();
		if(board.checkmate(true)) {
			white.victoryScreen();
			black.lossScreen();
		}else {
			white.lossScreen();
			black.victoryScreen();
		}
	}
}
