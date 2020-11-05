package javaChess;

import terminalChess.Display;

public class Session<T extends Move> {
	@SuppressWarnings("BusyWait")
	public static class ChessClock<T extends Move> implements Runnable{
		private boolean exists;
		private final Player<T> player1;
		private final Player<T> player2;
		private boolean isWhite;
		private int wTime;
		private int bTime;
		public ChessClock(Player<T> p1, Player<T> p2, int w, int b) {
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

	final Player<T> black;
	final Player<T> white;
	final Game<T> board;
	final ChessClock<T> time;

	public Session(Display db, Display dw, Game<T> g) {
		this(db, dw, -1, -1, g);
	}

	public Session(Display db, Display dw, int b, int w, Game<T> g) {
		board = g;
		board.init();
		black = new Human<>(false, board, db);
		white = new Human<>(true, board, dw);
		time = new ChessClock<>(white, black, w, b);
		black.setClock(time);
		white.setClock(time);
	}

	public Session(Display d, boolean isHumanWhite, String botPath, Game<T> g){
		this(d, -1, -1, isHumanWhite, botPath, g);
	}

	public Session(Display d, int b, int w, boolean isHumanWhite, String botPath, Game<T> g) {
		board = g;
		board.init();
		if(isHumanWhite) {
			black = new Bot<>(false,board, botPath);
			white = new Human<>(true, board, d);
		}else {
			white = new Bot<>(false,board, botPath);
			black = new Human<>(true, board, d);
		}
		time = new ChessClock<>(white, black, w, b);
		black.setClock(time);
		white.setClock(time);
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
			T move;
			if(whiteTurn) {
				move = white.onTurn();
			}else {
				move = black.onTurn();
			}
			white.stopPonder();
			black.stopPonder();
			T ponder = black.pondermove();
			
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
