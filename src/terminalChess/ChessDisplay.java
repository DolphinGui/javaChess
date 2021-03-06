package terminalChess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.input.KeyStroke;
import miscFunct.FileRead;

public class ChessDisplay {

	private class ChessClock implements Runnable {
		Thread clock;
		final String name;

		ChessClock() {
			name = "clock";
		}

		public void run() {
			while (exists) {
				try {
					drawTime();
					sec++;
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void start() {
			if (clock == null) {
				clock = new Thread(this, name);
				clock.start();
			}
		}
	}

	final ChessClock timer;

	boolean exists;
	int sec;

	private final TextColor whiteTileColor;
	private final TextColor whitePieceColor;
	private final TextColor blackPieceColor;
	private final TextColor blackTileColor;
	private final TextColor foreground;
	private final TextColor background;

	final ArrayList<String[]> history;
	private final TerminalPosition boardPosition;

	// --Commented out by Inspection (2020-11-06, 5:52 p.m.):private int rankSelect; //row
	// --Commented out by Inspection (2020-11-06, 5:52 p.m.):private int fileSelect; //file

	/*should just be a subclass,
	 * but for some reason it made
	 *  new terminals, so this is
	 *  now a box. Should debug later.*/
	private final Display chess;

	public ChessDisplay(Display c) {
		exists = true;
		timer = new ChessClock();
		history = new ArrayList<>();
		File f = new File("colors.cfg");
		chess = c;
		if(!f.exists()) {
			boardPosition = new TerminalPosition(10, 3);
			whiteTileColor = TextColor.ANSI.YELLOW_BRIGHT;
			whitePieceColor = TextColor.ANSI.WHITE;
			blackTileColor = TextColor.ANSI.YELLOW;
			blackPieceColor = TextColor.ANSI.BLACK;
			foreground = TextColor.ANSI.WHITE;
			background = TextColor.ANSI.DEFAULT;
		}else {
			TextColor[] colors = configure(f);
			boardPosition = new TerminalPosition(10, 3);
			whiteTileColor = colors[1]; // the array is black tile, white tile, black piece, and white piece in this
			blackTileColor = colors[0]; // order.
			whitePieceColor = colors[3];
			blackPieceColor = colors[2];
			foreground = colors[4];
			background = colors[5];
		}
	}


	private BasicTextImage boardSet(char[][] board) {
		BasicTextImage result = new BasicTextImage(board.length * 3 + 1, board[0].length + 1);
		boolean isWhite = false;
		char[] ab = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		for (int col = 0; col < board.length; col++) {
			for (int row = 0; row < board[col].length; row++) {
				char item = board[col][board[col].length - 1 - row];

				TextCharacter piece = pieceFactory(item, Character.isUpperCase(item), isWhite);
				TextCharacter space = pieceFactory(' ', isWhite, isWhite);
				result.setCharacterAt(col * 3, row, space);
				result.setCharacterAt(col * 3 + 1, row, piece);
				result.setCharacterAt(col * 3 + 2, row, space);
				isWhite = !isWhite;
			}
			isWhite = !isWhite;
		}
		for (int i = 0; i * 3 < result.getSize().getColumns(); i++) {
			result.setCharacterAt(i * 3 + 1, result.getSize().getRows() - 1,
					new TextCharacter(ab[i], foreground, background));
		}
		for (int i = 0; i < result.getSize().getRows() - 1; i++) {
			result.setCharacterAt(result.getSize().getColumns() - 1, result.getSize().getRows() - i - 2,
					new TextCharacter((char) (i + 49), foreground, background));
		}
		return result;
	}

	private TextColor[] configure(File f) {
		ArrayList<Character> text;
		text = FileRead.readFile(f);
		int i = 0;
		TextColor[] settings = new TextColor[6];
		String buffer = "";
		for (Character c : text) {
			if (c != ' ') {
				if (Character.isAlphabetic(c)) {
					boolean bright = Character.isLowerCase(c);
					switch (Character.toLowerCase(c)) {
					case 'd':
						settings[i] = TextColor.ANSI.DEFAULT;
						break;
					case 'b':
						if (bright)
							settings[i] = TextColor.ANSI.BLUE;
						else
							settings[i] = TextColor.ANSI.BLUE_BRIGHT;
						break;
					case 'c':
						if (bright)
							settings[i] = TextColor.ANSI.CYAN;
						else
							settings[i] = TextColor.ANSI.CYAN_BRIGHT;
						break;
					case 'g':
						if (bright)
							settings[i] = TextColor.ANSI.GREEN;
						else
							settings[i] = TextColor.ANSI.GREEN_BRIGHT;
						break;
					case 'm':
						if (bright)
							settings[i] = TextColor.ANSI.MAGENTA;
						else
							settings[i] = TextColor.ANSI.MAGENTA_BRIGHT;
						break;
					case 'r':
						if (bright)
							settings[i] = TextColor.ANSI.RED;
						else
							settings[i] = TextColor.ANSI.RED_BRIGHT;
						break;
					case 'w':
						if (bright)
							settings[i] = TextColor.ANSI.WHITE;
						else
							settings[i] = TextColor.ANSI.WHITE_BRIGHT;
						break;
					case 'y':
						if (bright)
							settings[i] = TextColor.ANSI.YELLOW;
						else
							settings[i] = TextColor.ANSI.YELLOW_BRIGHT;
						break;
					case 'l':
						if (bright)
							settings[i] = TextColor.ANSI.BLACK;
						else
							settings[i] = TextColor.ANSI.BLACK_BRIGHT;
						break;
					}
					i++;
				} else {
					buffer = buffer.concat(String.valueOf(c));
				}
			} else {
				if (!buffer.isEmpty()) {
					int red = Integer.parseInt(buffer.substring(0, 3));
					int blue = Integer.parseInt(buffer.substring(3, 6));
					int green = Integer.parseInt(buffer.substring(6, 9));
					settings[i] = new TextColor.RGB(red, blue, green);
				}
			}
		}
		return settings;
	}

	public void victoryScreen() throws IOException {
		chess.layers(GraphicsReader.readFiles("assets/victory"));
		chess.screen.refresh();
	}

	public void endClock() {
		exists = false;
	}

	private void drawBoard(char[][] board) {
		BasicTextImage boardImage = boardSet(board);
		chess.tGraphics.drawImage(boardPosition, boardImage);
	}

	private void drawHistory(ArrayList<String[]> moves) {
		drawHistory(moves.toArray(new String[moves.size()][]));
	}

	private void drawHistory(String[][] moves) {
		for (int i = 0; i < 5; i++) {
			chess.tGraphics.setCharacter(45, 6 + i, new TextCharacter('|', foreground, background));
		}
		if (moves.length != 0) {
			for (int i = 0; i < moves.length; i++) {
				chess.tGraphics.putString(45 - moves[i][0].length(), 6 + i, moves[i][0]);
				chess.tGraphics.putString(46, 6 + i, moves[i][1]);
			}
		}
	}

	private void drawMove(String move) {
		chess.tGraphics.putString(40, 15, move);
		try {
			chess.screen.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void drawTime() {
		chess.tGraphics.putString(44, 4, String.format("%d:%02d", sec / 60, sec));
		try {
			chess.screen.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void errorMessage(String message) {
		for(int i = 0; i < chess.screen.getTerminalSize().getColumns(); i++) {
			chess.screen.setCharacter(i, 16, new TextCharacter(' '));
		}
		chess.tGraphics.putString(40, 16, message);
	}


	public void initGame(char[][] board) {
		startGame(board);
		timer.start();
		chess.resizeListener.onResized(chess.terminal, null);
		try {
			chess.terminal.setCursorVisible(false);
			chess.screen.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String listenGame() {
		String display = "moves: ";
		String move = "";
		drawMove("moves:       ");
		while (move.length() < 5) {
			chess.screen.doResizeIfNecessary();
			KeyStroke input;
			try {
				input = chess.screen.readInput();
				switch(input.getKeyType()){
					case Enter, Escape-> move = "";
					case Character -> {
						if (input.getCharacter() == 'c' && input.isCtrlDown()) {
							System.exit(0);
						}else{
							move = move.concat(Character.toString(input.getCharacter()));
						}
					}
					case Backspace -> {
						if(move.length()!=0){
							move = move.substring(0, move.length()-1);
							drawMove("moves:       ");
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			drawMove(display.concat(move));
		}
		move = move.substring(0, 5);
		return move;
	}



	private TextCharacter pieceFactory(char item, boolean pieceWhite, boolean tileWhite) {
		TextColor piece;
		TextColor tile;
		if (pieceWhite) {
			piece = whitePieceColor;
		} else {
			piece = blackPieceColor;
		}
		if (tileWhite) {
			tile = whiteTileColor;
		} else {
			tile = blackTileColor;
		}
		return new TextCharacter(item, piece, tile);
	}

	public void startGame(char[][] board)  {
		chess.tGraphics.setForegroundColor(foreground);
		chess.tGraphics.setBackgroundColor(background);
		chess.screen.clear();
		String[][] moves = {};
		drawBoard(board);
		drawHistory(moves);
	}

	public void turn(char[][] board, String move) throws IOException {
		chess.screen.doResizeIfNecessary();
		drawBoard(board);
		history.add(new String[] { move, String.format("%d:%02d", sec / 60, sec) });
		drawHistory(history);
		chess.screen.refresh();
	}

}
