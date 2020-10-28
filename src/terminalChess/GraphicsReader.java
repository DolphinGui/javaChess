package terminalChess;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GraphicsReader {

	private static String[] load(File f) throws FileNotFoundException {
		Scanner layer = new Scanner(f);
		ArrayList<String> result = new ArrayList<>();
		while (layer.hasNextLine()) {
			result.add(layer.nextLine());
		}
		layer.close();
		return result.toArray(new String[0]);
	}

	/* This reads layer files and loads them into an array of string arrays. */
	public static String[][] readfiles(String dir) throws FileNotFoundException {
		File l = new File(dir);
		if (!l.exists())
			throw new FileNotFoundException();
		FileFilter hidden = f -> !f.isHidden();
		File[] layerDir = l.listFiles(hidden);

		assert layerDir != null;
		Arrays.sort(layerDir);

		ArrayList<String[]> results = new ArrayList<>();

		for (File f : layerDir) {
			results.add(load(f));
		}
		return results.toArray(new String[results.size()][]);
	}
}
