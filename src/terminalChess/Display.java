package terminalChess;

import java.io.IOException;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * @author Shin TODO:
 * 
 * 
 */
public class Display {
	

	public static void layers(Screen screen, TextGraphics tGraphics, String[][] layers) throws IOException {

		screen.clear();

		int width = screen.getTerminalSize().getColumns();
		int height = screen.getTerminalSize().getRows();

		for (String[] l : layers) {
			for (int i = 0; i < l.length; i++) {
				String s = l[i];
				tGraphics.putString(Math.floorDiv(width - s.length(), 2), Math.floorDiv(height - l.length, 2) + i, s);
			}
		}
		screen.refresh();
	}
	
	Terminal terminal;
	Screen screen;
	TextGraphics tGraphics;
	SimpleTerminalResizeListener resizeListener;
	
	
	public Display() throws IOException {
		terminal = new DefaultTerminalFactory().createTerminal();
		screen = new TerminalScreen(terminal);
		tGraphics = screen.newTextGraphics();
		screen.startScreen();
		resizeListener = new SimpleTerminalResizeListener(terminal.getTerminalSize());
		terminal.addResizeListener(resizeListener);
		terminal.setCursorVisible(false);
		resizeListener.isTerminalResized();
	}

	public void close() throws IOException {
		screen.close();
		terminal.close();
		System.exit(0);
	}

	public void destroy() throws IOException {
		screen.close();
		terminal.close();
	}

	public char initStart() throws IOException {
		layers(screen, tGraphics, GraphicsReader.readfiles("assets/start"));
		screen.refresh();
		return screen.readInput().getCharacter();
	}
	
	public boolean resized() {
		return resizeListener.isTerminalResized();
	}

}
