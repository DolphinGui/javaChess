package terminalChess;

import java.io.IOException;
import java.util.HashMap;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class GameState {

    static TextColor tilecolor = TextColor.ANSI.YELLOW_BRIGHT;
    static TextColor piececolor = TextColor.ANSI.WHITE;
    static HashMap<TextColor, TextColor> swapMap = new HashMap<TextColor, TextColor>();

    static {
	swapMap.put(TextColor.ANSI.YELLOW, TextColor.ANSI.YELLOW_BRIGHT);
	swapMap.put(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.YELLOW);
	swapMap.put(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE);
	swapMap.put(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
    }

    private static BasicTextImage boardSet(char[][] board) throws IOException {
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
		    new TextCharacter(ab[i], TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
	}
	for (int i = 0; i < result.getSize().getRows() - 1; i++) {
	    result.setCharacterAt(result.getSize().getColumns() - 1, result.getSize().getRows() - i - 2,
		    new TextCharacter((char) (i + 49), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
	}
	return result;
    }

    private static void drawBoard(Screen screen, TextGraphics tGraphics, char[][] board) throws IOException {

	BasicTextImage boardImage = boardSet(board);

	int column = 10;// Math.floorDiv(screen.getTerminalSize().getColumns()-boardImage.getSize().getColumns(),
			// 2);
	int row = 3;// Math.floorDiv(screen.getTerminalSize().getRows()-boardImage.getSize().getRows(),
		    // 2);

	tGraphics.drawImage(new TerminalPosition(column, row), boardImage);
    }

    private static void drawHistory(Screen screen, TextGraphics tGraphics, String[][] moves) {
	for (int i = 0; i < 5; i++) {
	    tGraphics.setCharacter(45, 6 + i, '|');
	}
	for (int i = 0; i < moves.length; i++) {
	    tGraphics.putString(45 - moves[i][0].length(), 6  + i, moves[i][0]);
	    tGraphics.putString(46, 6 + i, moves[i][1]);
	}
    }

    private static void drawMove(Screen screen, TextGraphics tGraphics, String move) {
	tGraphics.putString(40, 15, move);
    }

    private static void drawTime(Screen screen, TextGraphics tGraphics, int seconds) throws IOException {
	String time = "00:00";
	time = Integer.toString(Math.floorDiv(seconds, 60)) + ":" + Integer.toString(seconds % 60);
	tGraphics.putString(40, 4, time);
    }

    private static TextCharacter pieceFactory(char item, boolean pieceWhite, boolean tileWhite) {
	TextColor piece;
	TextColor tile;
	if (pieceWhite) {
	    piece = piececolor;
	} else {
	    piece = swapMap.get(piececolor);
	}
	if (tileWhite) {
	    tile = tilecolor;
	} else {
	    tile = swapMap.get(tilecolor);
	}
	return new TextCharacter(item, piece, tile);
    }

    public static void startGame(Screen screen, TextGraphics tGraphics, char[][] board) throws IOException {
	screen.clear();
	String[][] moves = { { "nana", "20:30" }, { "nana", "20:30" }, { "nana", "20:30" }, { "nana", "20:30" } };
	drawBoard(screen, tGraphics, board);
	drawTime(screen, tGraphics, 0);
	drawHistory(screen, tGraphics, moves);
	screen.refresh();
    }

    public static void update(Screen screen, TextGraphics tGraphics, char[][] board) throws IOException {
	drawBoard(screen, tGraphics, board);
	screen.refresh();
    }

    public static void update(Screen screen, TextGraphics tGraphics, int time) throws IOException {
	drawTime(screen, tGraphics, time);
	screen.refresh();
    }

    public static void update(Screen screen, TextGraphics tGraphics, String move) throws IOException {
	drawMove(screen, tGraphics, move);
	screen.refresh();
    }

    public static void update(Screen screen, TextGraphics tGraphics, String[][] moves) throws IOException {
	drawHistory(screen, tGraphics, moves);
	screen.refresh();
    }

}
