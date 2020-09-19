package terminalChess;

import java.io.IOException;
import java.util.ArrayList;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class GameState {

	private TextColor whiteTileColor;
	private TextColor whitePieceColor;
	private TextColor blackPieceColor;
	private TextColor blackTileColor;
	private TextColor foreground;
	private TextColor background;
	private TerminalPosition boardPosition;

	GameState() {
		boardPosition = new TerminalPosition(10, 3);
		whiteTileColor = TextColor.ANSI.YELLOW_BRIGHT;
		whitePieceColor = TextColor.ANSI.WHITE;
		foreground = TextColor.ANSI.WHITE;
		background = TextColor.ANSI.DEFAULT;
	}

	GameState(TextColor[] colors) {
		boardPosition = new TerminalPosition(10, 3);
		whiteTileColor = colors[1]; // the array is black tile, white tile, black piece, and white piece in this
		blackTileColor = colors[0]; // order.
		whitePieceColor = colors[3];
		blackPieceColor = colors[2];
		foreground = colors[4];
		background = colors[5];
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

	public void drawBoard(Screen screen, TextGraphics tGraphics, char[][] board) throws IOException {
		BasicTextImage boardImage = boardSet(board);
		tGraphics.drawImage(boardPosition, boardImage);
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

	private void initTime(Screen screen, TextGraphics tGraphics) throws IOException {
		tGraphics.putString(43, 4, "00:00");
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

	public void startGame(Screen screen, TextGraphics tGraphics, char[][] board) throws IOException {
		tGraphics.setForegroundColor(foreground);
		tGraphics.setBackgroundColor(background);
		screen.clear();
		String[][] moves = {};
		drawBoard(screen, tGraphics, board);
		initTime(screen, tGraphics);
		drawHistory(screen, tGraphics, moves);
	}

	public void error(Screen screen, TextGraphics tGraphics, String message) {
		tGraphics.putString(40, 16, message);
	}

	public void drawHistory(Screen screen, TextGraphics tGraphics, ArrayList<String[]> moves) throws IOException {
		drawHistory(screen, tGraphics, moves.toArray(new String[moves.size()][]));
	}

}
