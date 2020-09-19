package terminalChess;

import java.io.IOException;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class DisplayLayers {

	public static void start(Screen screen, TextGraphics tGraphics, String[][] layers) throws IOException {

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
}
