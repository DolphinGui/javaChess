package terminalChess;

import java.io.IOException;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * @author Shin
 * TODO:
 * 
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
    
    public char initStart() throws IOException {
	return StartState.start(screen, tGraphics);
    }
    public void initGame() throws IOException {
	GameState.start(screen, tGraphics);
    }
    
    public void destroy() throws IOException {
	screen.close();
	terminal.close();
    }
    
}
