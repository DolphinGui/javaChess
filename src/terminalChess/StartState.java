package terminalChess;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class StartState {

    public static char start(Screen screen, TextGraphics tGraphics) throws IOException {

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

	welcome = "L for local game, N for networked game,";
	tGraphics.putString(Math.floorDiv(screen.getTerminalSize().getColumns()-welcome.length(), 2), Math.floorDiv(screen.getTerminalSize().getRows(), 2), welcome);

	welcome = "and B for a bot game";
	tGraphics.putString(Math.floorDiv(screen.getTerminalSize().getColumns()-welcome.length(), 2), Math.floorDiv(screen.getTerminalSize().getRows(), 2)+1, welcome);

	screen.refresh();
	char choice = screen.readInput().getCharacter();
	return choice;

    }
}
