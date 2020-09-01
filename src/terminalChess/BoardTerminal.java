package terminalChess;

import java.io.IOException;
import java.util.HashMap;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;

public class BoardTerminal extends ChessTerminal {

    TextColor tilecolor = TextColor.ANSI.YELLOW_BRIGHT;
    TextColor piececolor = TextColor.ANSI.WHITE;
    HashMap<TextColor, TextColor> swapMap = new HashMap<TextColor, TextColor>();
    
    char[][] initialBoard;
    
    public BoardTerminal(HashMap<TextColor, TextColor> colors, char[][] board) throws IOException {
	super();
	swapMap = colors;
	initialBoard = board;
    }
    
    public BoardTerminal() throws IOException {
	super();
	swapMap.put(TextColor.ANSI.YELLOW, TextColor.ANSI.YELLOW_BRIGHT);
	swapMap.put(TextColor.ANSI.YELLOW_BRIGHT,TextColor.ANSI.YELLOW);
	swapMap.put(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE);
	swapMap.put(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
	char[][] board = {
		{'r','k','b','y','x','b','k','r'},
		{'p','p','p','p','p','p','p','p'},
		{'n','n','n','n','n','n','n','n'},
		{'n','n','n','n','n','n','n','n'},
		{'n','n','n','n','n','n','n','n'},
		{'n','n','n','n','n','n','n','n'},
		{'P','P','P','P','P','P','P','P'},
		{'R','K','B','Y','X','B','K','R'}};
	initialBoard = board;
    }

    private TextCharacter pieceFactory(char item, boolean pieceWhite, boolean tileWhite) {
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

    private TextColor swap(TextColor color) {
	return swapMap.get(color);
    }

    public void start() throws IOException {
	screen.clear();
	
	BasicTextImage board = new BasicTextImage(initialBoard.length*3, initialBoard.length);
	boolean tileWhite = true;
	for(int rank = 0; rank<initialBoard.length;rank++) {
	    for(int file = 0; file<initialBoard[0].length*3;file+=3) {
		if(initialBoard[rank][file/3]!='n') {
		    board.setCharacterAt(file+1, rank, pieceFactory(initialBoard[rank][file/3], Character.isUpperCase(initialBoard[rank][file/3]), tileWhite));
		}else {
		    board.setCharacterAt(file+1, rank, pieceFactory(' ',Character.isUpperCase(initialBoard[rank][file/3]),tileWhite));
		}
		board.setCharacterAt(file, rank, pieceFactory(' ',Character.isUpperCase(initialBoard[rank][file/3]),tileWhite));
		board.setCharacterAt(file+2, rank, pieceFactory(' ',Character.isUpperCase(initialBoard[rank][file/3]),tileWhite));
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
