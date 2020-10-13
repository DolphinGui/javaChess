package terminalChess;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
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


	public void layers(String[][] layers) throws IOException {

		screen.clear();

		int col = screen.getTerminalSize().getColumns();
		int row = screen.getTerminalSize().getRows();

		BasicTextImage[] image = new BasicTextImage[layers.length];


		for (int i = 0; i < layers.length; i++) {
			String[] l = layers[i];

			int width = l[0].length();
			int height = l.length;

			for(String s : l) {
				if(width < s.length()) width = s.length();
			}

			image[i] = new BasicTextImage(width, height);

			for(int y = 0; y < l.length; y++) {
				String s = l[y];
				for (int x = 0; x < s.length(); x++) {
					image[i].setCharacterAt(x, y, new TextCharacter(s.charAt(x)));
				} //TODO: fix this
			}
		}
		for(BasicTextImage l : image) {
			TerminalPosition pos = new TerminalPosition((col - l.getSize().getColumns())/2, (row - l.getSize().getRows())/2);
			tGraphics.drawImage(pos, l);
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

	public char initStart() throws FileNotFoundException, IOException {
		layers(GraphicsReader.readfiles("assets/start"));
		screen.refresh();
		return screen.readInput().getCharacter();
	}

	public boolean resized() {
		return resizeListener.isTerminalResized();
	}

}
