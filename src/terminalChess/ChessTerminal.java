package terminalChess;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;

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
	
	public void start() {
	    if(clock == null) {
		clock = new Thread(this, threadname);
		clock.start();
	    }
	}
	
	public void run() {
	    int sec = 0;
	    while(exists) {
		try {
		    GameState.update(screen, tGraphics, sec);
		    sec++;
		    TimeUnit.SECONDS.sleep(1);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}

    }




    Terminal terminal;
    Screen screen;
    TextGraphics tGraphics;
    SimpleTerminalResizeListener resizeListener;
    ChessClock timer;
    boolean exists;
    
    TextColor tilecolor = TextColor.ANSI.YELLOW_BRIGHT;
    TextColor piececolor = TextColor.ANSI.WHITE;



    public void close() throws IOException {
	screen.close();
	terminal.close();
	System.exit(0);
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
    }

    public char initStart() throws IOException {
	DisplayLayers.start(screen, tGraphics, GraphicsReader.readfiles("assets/start"));
	return screen.readInput().getCharacter();
    }

    public boolean resized() {
	return resizeListener.isTerminalResized();
    }

    public void initGame(char[][] board) throws IOException {
	GameState.startGame(screen, tGraphics, board);
	
	timer.start();
	resizeListener.onResized(terminal, null);
    }

    public void boardUpdate(char[][] board) throws IOException {
	GameState.update(screen, tGraphics, board);
    }

    public String listenGame() throws IOException {
	String notate = "moves: ";
	String input = "";
	GameState.update(screen, tGraphics, "moves:     ");
	while(input.length()<5) {
	    input = input.concat( Character.toString( screen.readInput().getCharacter() ) );
	    GameState.update(screen, tGraphics, notate.concat(input));
	}
	input = input.substring(0, input.length()-1);
	return input;
    }

    public void destroy() throws IOException {
	screen.close();
	terminal.close();
	exists = false;
    }

}
