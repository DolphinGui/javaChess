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

    static TextColor  tilecolor = TextColor.ANSI.YELLOW_BRIGHT;
    static TextColor piececolor = TextColor.ANSI.WHITE;

    private static TextCharacter pieceFactory(char item, boolean pieceWhite, boolean tileWhite) {
	TextColor piece;
	TextColor tile;
	if(pieceWhite) {
	    piece=piececolor;
	}else {piece=swap(piececolor);}
	if(tileWhite) {
	    tile=tilecolor;
	}else {tile=swap(tilecolor);}
	return new TextCharacter(item, piece, tile);
    }

    private static TextColor swap(TextColor color) {
	HashMap<TextColor,TextColor> mapping = new HashMap<TextColor,TextColor>();
	mapping.put(TextColor.ANSI.YELLOW, TextColor.ANSI.YELLOW_BRIGHT);
	mapping.put(TextColor.ANSI.YELLOW_BRIGHT,TextColor.ANSI.YELLOW);
	mapping.put(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE);
	mapping.put(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
	return mapping.get(color);
    }

    public static void start(Screen screen, TextGraphics tGraphics) throws IOException {
	screen.clear();
	final char[][] defaultBoard = {
		{'r','k','b','y','x','b','k','r'},
		{'p','p','p','p','p','p','p','p'},
		{'n','n','n','n','n','n','n','n'},
		{'n','n','n','n','n','n','n','n'},
		{'n','n','n','n','n','n','n','n'},
		{'n','n','n','n','n','n','n','n'},
		{'P','P','P','P','P','P','P','P'},
		{'R','K','B','Y','X','B','K','R'}};

	BasicTextImage board = new BasicTextImage(24, 8);
	boolean tileWhite = true;
	for(int rank = 0; rank<defaultBoard.length;rank++) {
	    for(int file = 0; file<defaultBoard[0].length*3;file+=3) {
		if(defaultBoard[rank][file/3]!='n') {
		    board.setCharacterAt(file+1, rank, pieceFactory(defaultBoard[rank][file/3], Character.isUpperCase(defaultBoard[rank][file/3]), tileWhite));
		}else {
		    board.setCharacterAt(file+1, rank, pieceFactory(' ',Character.isUpperCase(defaultBoard[rank][file/3]),tileWhite));
		}
		board.setCharacterAt(file, rank, pieceFactory(' ',Character.isUpperCase(defaultBoard[rank][file/3]),tileWhite));
		board.setCharacterAt(file+2, rank, pieceFactory(' ',Character.isUpperCase(defaultBoard[rank][file/3]),tileWhite));
		tileWhite = !tileWhite;
	    }
	    tileWhite = !tileWhite;
	}
	int column = Math.floorDiv(screen.getTerminalSize().getColumns()-board.getSize().getColumns(), 2);
	int row = Math.floorDiv(screen.getTerminalSize().getRows()-board.getSize().getRows(), 2);

	tGraphics.drawImage(new TerminalPosition(column,row), board);


	screen.refresh();
	screen.readInput();
    }
}
