package terminalChess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import miscFunct.FileRead;

public class GameState {

	private class ChessClock implements Runnable {
		Thread clock;
		String threadname;

		ChessClock(String name) {
			threadname = name;
		}

		public void run() {
			while (exists) {
				try {
					drawTime(chess.screen, chess.tGraphics, sec);
					sec++;
					TimeUnit.SECONDS.sleep(1);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void start() {
			if (clock == null) {
				clock = new Thread(this, threadname);
				clock.start();
			}
		}
	}

	ChessClock timer;
	
	boolean exists;
	int sec;
	
	private TextColor whiteTileColor;
	private TextColor whitePieceColor;
	private TextColor blackPieceColor;
	private TextColor blackTileColor;
	private TextColor foreground;
	private TextColor background;
	
	ArrayList<String[]> history;
	
	private TerminalPosition boardPosition;
	private ChessTerminal chess;
	
	public GameState(ChessTerminal c) throws IOException {
		exists = true;
		timer = new ChessClock("clock");
		history = new ArrayList<String[]>();
		File f = new File("colors.cfg");
		chess = c;
		if(!f.exists()) config();
		else config(configure(f));
	}
	
	
	private BasicTextImage boardSet(char[][] board) throws IOException {
		BasicTextImage result = new BasicTextImage(board.length * 3 + 1, board[0].length + 1);
		boolean isWhite = false;
		char[] ab = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		for (int col = 0; col < board.length; col++) {
			for (int row = 0; row < board[col].length; row++) {
				char item = board[col][board[col].length - 1 - row];

				if (item == 'n')
					item = ' ';
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
	private void config() {
		boardPosition = new TerminalPosition(10, 3);
		whiteTileColor = TextColor.ANSI.YELLOW_BRIGHT;
		whitePieceColor = TextColor.ANSI.WHITE;
		foreground = TextColor.ANSI.WHITE;
		background = TextColor.ANSI.DEFAULT;
	}

	private void config(TextColor[] colors) {
		boardPosition = new TerminalPosition(10, 3);
		whiteTileColor = colors[1]; // the array is black tile, white tile, black piece, and white piece in this
		blackTileColor = colors[0]; // order.
		whitePieceColor = colors[3];
		blackPieceColor = colors[2];
		foreground = colors[4];
		background = colors[5];
	}

	private TextColor[] configure(File f) {
		ArrayList<Character> text = null;
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
					int red = Integer.valueOf(buffer.substring(0, 3));
					int blue = Integer.valueOf(buffer.substring(3, 6));
					int green = Integer.valueOf(buffer.substring(6, 9));
					settings[i] = new TextColor.RGB(red, blue, green);
				}
			}
		}
		return settings;
	}
	
	public void destroy() throws IOException {
		exists = false;
		chess.destroy();
	}
	
	public void drawBoard(Screen screen, TextGraphics tGraphics, char[][] board) throws IOException {
		BasicTextImage boardImage = boardSet(board);
		tGraphics.drawImage(boardPosition, boardImage);
	}

	public void drawHistory(Screen screen, TextGraphics tGraphics, ArrayList<String[]> moves) throws IOException {
		drawHistory(screen, tGraphics, moves.toArray(new String[moves.size()][]));
	}

	private void drawHistory(Screen screen, TextGraphics tGraphics, String[][] moves) {
		for (int i = 0; i < 5; i++) {
			tGraphics.setCharacter(45, 6 + i, new TextCharacter('|', foreground, background));
		}
		if (moves.length != 0) {
			for (int i = 0; i < moves.length; i++) {
				tGraphics.putString(45 - moves[i][0].length(), 6 + i, moves[i][0]);
				tGraphics.putString(46, 6 + i, moves[i][1]);
			}
		}
	}
	
	public void drawMove(Screen screen, TextGraphics tGraphics, String move) throws IOException {
		tGraphics.putString(40, 15, move);
		screen.refresh();
	}

	public void drawTime(Screen screen, TextGraphics tGraphics, int seconds) throws IOException {
		tGraphics.putString(47, 4, Integer.toString(seconds % 10));
		tGraphics.putString(46, 4, Integer.toString((seconds / 10) % 6));
		tGraphics.putString(44, 4, Integer.toString((seconds / 60) % 10));
		tGraphics.putString(43, 4, Integer.toString((seconds / 600)));
		screen.refresh();
	}

	public void error(Screen screen, TextGraphics tGraphics, String message) {
		tGraphics.putString(40, 16, message);
	}

	public void initGame(char[][] board) throws IOException {
		startGame(chess.screen, chess.tGraphics, board);
		timer.start();
		chess.resizeListener.onResized(chess.terminal, null);
		chess.screen.refresh();
	}

	private void initTime(Screen screen, TextGraphics tGraphics) throws IOException {
		tGraphics.putString(43, 4, "00:00");
	}

	public String listenGame() throws IOException {
		String display = "moves: ";
		String move = "";
		drawMove(chess.screen, chess.tGraphics, "moves:       ");
		while (move.length() < 5) {
			KeyStroke input = chess.screen.readInput();
			if (input.getKeyType() == KeyType.Enter||input.getKeyType()==KeyType.Escape) {
				if (move.length() == 5)
					break;
				else
					move = "";
			}
			if (input.getCharacter() == 'c' && input.isCtrlDown()) {
				System.exit(0);
			}
			if(input.getKeyType()==KeyType.Backspace && move.length()!=0) {
				move = move.substring(0, move.length()-1);
				drawMove(chess.screen, chess.tGraphics, "moves:       ");
			}else if(input.getKeyType()==KeyType.Character){
				move = move.concat(Character.toString(input.getCharacter()));
			}
			drawMove(chess.screen, chess.tGraphics, display.concat(move));
		}
		move = move.substring(0, move.length() - 1);
		return move;
	}

	public void errorMessage(String message) {
		for(int i = 0; i < chess.screen.getTerminalSize().getColumns(); i++) {
			chess.screen.setCharacter(i, 16, new TextCharacter(' '));
		}
		error(chess.screen, chess.tGraphics, message);
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

	public void refreshGame(char[][] board) throws IOException {
		startGame(chess.screen, chess.tGraphics, board);
		drawMove(chess.screen, chess.tGraphics, "moves:       ");
		chess.screen.refresh();
	}

	public void startGame(Screen screen, TextGraphics tGraphics, char[][] board) throws IOException {
		tGraphics.setForegroundColor(foreground);
		tGraphics.setBackgroundColor(background);
		screen.clear();
		String[][] moves = {};
		drawBoard(screen, tGraphics, board);
		initTime(screen, tGraphics);
		drawHistory(screen, tGraphics, moves);
	}

	public boolean resized() {
		return chess.resized();
	}
	
	public void turn(char[][] board, String move) throws IOException {
		drawBoard(chess.screen, chess.tGraphics, board);
		String[] addition = { move, Integer.toString(sec) };
		history.add(addition);
		drawHistory(chess.screen, chess.tGraphics, history);
		chess.screen.refresh();
	}

}
