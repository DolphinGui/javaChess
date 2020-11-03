package terminalChess;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

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

// --Commented out by Inspection START (28-Oct-20 15:20):
//	public void close() throws IOException {
//		screen.close();
//		terminal.close();
//		System.exit(0);
//	}
// --Commented out by Inspection STOP (28-Oct-20 15:20)

	public void destroy() throws IOException {
		screen.close();
		terminal.close();
	}

	public File findFile(File dir, String display, FileFilter filter) throws FileNotFoundException {
		try {
			terminal.setCursorVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		screen.clear();
		File result = null;
		LinkedList<File> files = new LinkedList<>();

		if(filter!=null){
			if(dir.listFiles(filter)==null) throw new FileNotFoundException();
			Collections.addAll(files, Objects.requireNonNull(dir.listFiles(filter)));
		}
		tGraphics.putString(
				(screen.getTerminalSize().getColumns() - display.length())/2,
				0,
				display);

		int row = 0;
		try {
			terminal.setCursorVisible(true);
			while(result==null||!filter.accept(result)){
				int pagelength = 1;
				if(screen.getTerminalSize().getRows()-1 > 0) pagelength = screen.getTerminalSize().getRows()-1;
				int page = row/pagelength;
				for(int i = page*pagelength; i<files.size()&&i < page*pagelength + pagelength; i++){
					tGraphics.putString(
							(terminal.getTerminalSize().getColumns()-files.get(i).getName().length())/2,
							1+i-page*pagelength,
							files.get(i).getName()
					);
				}
				screen.setCursorPosition(new TerminalPosition(
						((terminal.getTerminalSize().getColumns())-files.get(row).getName().length())/2,
						row%pagelength+1));
				screen.refresh();
				KeyStroke k = screen.readInput();
				switch(k.getKeyType()){
					case ArrowUp -> {
						if(row>0) row--;
					}
					case ArrowDown -> {
						if(row<files.size()) row++;
					}
					case Tab, Enter, ArrowLeft, ArrowRight -> result = files.get(row);
					case Character -> {
						if(k.getCharacter()=='c'&&k.isCtrlDown())System.exit(0);
					}
				}
			}
			terminal.setCursorVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


	public char initStart() throws IOException {
		layers(GraphicsReader.readFiles("assets/start"));
		screen.refresh();
		return screen.readInput().getCharacter();
	}

}
