package terminalChess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;

import miscFunct.FileRead;

/**
 * @author Shin
 * TODO:
 * 
 * 
 */
public class ChessTerminal{
    private class ChessClock implements Runnable{
	Thread clock;
	String threadname;
	ChessClock(String name){
	    threadname = name;
	}

	public void run() {
	    while(exists) {
		try {
		    game.drawTime(screen, tGraphics, sec);
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
	    if(clock == null) {
		clock = new Thread(this, threadname);
		clock.start();
	    }
	}
    }

    GameState game;
    Terminal terminal;
    Screen screen;
    TextGraphics tGraphics;
    SimpleTerminalResizeListener resizeListener;
    ChessClock timer;
    boolean exists;
    int sec;
    boolean init;

    TextColor tilecolor = TextColor.ANSI.YELLOW_BRIGHT;
    TextColor piececolor = TextColor.ANSI.WHITE;
    ArrayList<String[]> history;

    private GameState configure(File f) {
	ArrayList<Character> text = null;
	try {
	    text = FileRead.readFile(f);
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
	int i = 0;
	boolean isAnsi = false;
	TextColor[] settings = new TextColor[6];
	String buffer = "";
	for(Character c : text) {
	    if(c==' ') { 
		if(i>0&&!isAnsi) {
		    if(buffer.charAt(0)=='d') {
			settings[i-1] = TextColor.ANSI.DEFAULT;
			buffer = "";
		    }else {
			int red = Integer.valueOf(buffer.substring(0, 3));
			int green = Integer.valueOf(buffer.substring(3, 6));
			int blue = Integer.valueOf(buffer.substring(6, 9));
			settings[i-1] = new TextColor.RGB(red, green, blue);
			buffer = "";
		    }
		}
		i++;
	    }
	    else if(i==0) {
		isAnsi = (c=='0');
	    }else{
		String s = String.valueOf(c);
		if(isAnsi) {
		    boolean bright = Character.isLowerCase(c);
		    switch(Character.toLowerCase(c)) {
		    case 'd':
			settings[i-1] = TextColor.ANSI.DEFAULT;
			break;
		    case 'b':
			if(bright) settings[i-1] = TextColor.ANSI.BLUE;
			else settings[i-1] = TextColor.ANSI.BLUE_BRIGHT;
			break;
		    case 'c':
			if(bright) settings[i-1] = TextColor.ANSI.CYAN;
			else settings[i-1] = TextColor.ANSI.CYAN_BRIGHT;
			break;
		    case 'g':
			if(bright) settings[i-1] = TextColor.ANSI.GREEN;
			else settings[i-1] = TextColor.ANSI.GREEN_BRIGHT;
			break;
		    case 'm':
			if(bright) settings[i-1] = TextColor.ANSI.MAGENTA;
			else settings[i-1] = TextColor.ANSI.MAGENTA_BRIGHT;
			break;
		    case 'r':
			if(bright) settings[i-1] = TextColor.ANSI.RED;
			else settings[i-1] = TextColor.ANSI.RED_BRIGHT;
			break;
		    case 'w':
			if(bright) settings[i-1] = TextColor.ANSI.WHITE;
			else settings[i-1] = TextColor.ANSI.WHITE_BRIGHT;
			break;
		    case 'y':
			if(bright) settings[i-1] = TextColor.ANSI.YELLOW;
			else settings[i-1] = TextColor.ANSI.YELLOW_BRIGHT;
			break;
		    case 'l':
			if(bright) settings[i-1] = TextColor.ANSI.BLACK;
			else settings[i-1] = TextColor.ANSI.BLACK_BRIGHT;
			break;
		    }
		}else {
		    buffer = buffer.concat(s);
		}
	    }
	}
	return new GameState(settings);
    }

    public ChessTerminal() throws IOException {
	terminal = new DefaultTerminalFactory().createTerminal();
	screen = new TerminalScreen(terminal);
	tGraphics = screen.newTextGraphics();
	screen.startScreen();
	resizeListener = new SimpleTerminalResizeListener(terminal.getTerminalSize());
	terminal.addResizeListener(resizeListener);
	terminal.setCursorVisible(false);
	timer = new ChessClock("chessClock");
	exists = true;
	history = new ArrayList<String[]>();
	resizeListener.isTerminalResized();
	game = configure(new File("colors.cfg"));
    }

    public void close() throws IOException {
	screen.close();
	terminal.close();
	System.exit(0);
    }

    public void destroy() throws IOException {
	screen.close();
	terminal.close();
	exists = false;
    }

    public void initGame(char[][] board) throws IOException {
	game.startGame(screen, tGraphics, board);
	timer.start();
	resizeListener.onResized(terminal, null);
	screen.refresh();
    }

    public void refreshGame(char[][] board) throws IOException {
	game.startGame(screen, tGraphics, board);
	game.drawMove(screen, tGraphics, "moves:       ");
	screen.refresh();
    }

    public char initStart() throws IOException {
	DisplayLayers.start(screen, tGraphics, GraphicsReader.readfiles("assets/start"));
	screen.refresh();
	
	KeyStroke input = null;
	
	while(input==null) {
	    input = screen.readInput();
	}
	
	if(input.getCharacter()==null) {
	    System.exit(0);
	}
	return input.getCharacter();
    }

    public String listenGame() throws IOException {
	String display = "moves: ";
	String move = "";
	game.drawMove(screen, tGraphics, "moves:       ");
	while(move.length()<5) {
	    KeyStroke input = screen.readInput();
	    if(input.getKeyType()==KeyType.Enter) {
		if(move.length()==4) 
		    break;
		else 
		    move = "";
	    }
	    if(input.getCharacter()=='c'&&input.isCtrlDown()) {
		System.exit(0);
	    }
	    move = move.concat( Character.toString( input.getCharacter() ) );
	    game.drawMove(screen, tGraphics, display.concat(move));
	}
	move = move.substring(0, move.length()-1);
	return move;
    }

    public boolean resized() {
	return resizeListener.isTerminalResized();
    }

    public void errorMessage(String message) {
	game.error(screen, tGraphics, message);
    }

    public void turn(char[][] board, String move) throws IOException {
	game.drawBoard(screen, tGraphics, board);
	String[] addition = {move, Integer.toString(sec)};
	history.add(addition);
	game.drawHistory(screen, tGraphics, history);
	screen.refresh();
    }

}
