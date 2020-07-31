/**
 * 
 */
package terminalChess;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * @author Shin
 *
 */
public class ChessTerminal {

    Terminal terminal;
    Screen screen;
    TextGraphics tGraphics;
    
    TextColor tilecolor = TextColor.ANSI.YELLOW_BRIGHT;
    TextColor piececolor = TextColor.ANSI.WHITE;
    
    public void close() throws IOException {
	screen.close();
	terminal.close();
    }
    
    public ChessTerminal() throws IOException {
	terminal = new DefaultTerminalFactory().createTerminal();
	screen = new TerminalScreen(terminal);
	tGraphics = screen.newTextGraphics();
	screen.startScreen();
    }
    
    public char initGame() throws IOException {
	return start();
    }
    
    private TextColor swap(TextColor color) {
	HashMap<TextColor,TextColor> mapping = new HashMap<TextColor,TextColor>();
	mapping.put(TextColor.ANSI.YELLOW, TextColor.ANSI.YELLOW_BRIGHT);
	mapping.put(TextColor.ANSI.YELLOW_BRIGHT,TextColor.ANSI.YELLOW);
	mapping.put(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE);
	mapping.put(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
	return mapping.get(color);
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
    
    public void board() throws IOException {
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

    private char start() throws IOException {

	screen.clear();

	File text = new File("./assets/minirook.txt");
	Scanner background = new Scanner(text);

	for(int n = 0;background.hasNextLine();n++) {
	    String s = background.nextLine();
	    tGraphics.putString(Math.floorDiv((screen.getTerminalSize().getColumns()-s.length()), 2),n, s);
	}
	background.close();

	String welcome = "Welcome to Java Chess!";
	tGraphics.putString(Math.floorDiv(screen.getTerminalSize().getColumns()-welcome.length(), 2), Math.floorDiv(screen.getTerminalSize().getRows(), 2)-1, welcome);

	welcome = "N for local game, N for networked game,";
	tGraphics.putString(Math.floorDiv(screen.getTerminalSize().getColumns()-welcome.length(), 2), Math.floorDiv(screen.getTerminalSize().getRows(), 2), welcome);

	welcome = "and B for a bot game";
	tGraphics.putString(Math.floorDiv(screen.getTerminalSize().getColumns()-welcome.length(), 2), Math.floorDiv(screen.getTerminalSize().getRows(), 2)+1, welcome);

	screen.refresh();
	char choice = screen.readInput().getCharacter();
	return choice;

    }
    
    
}
