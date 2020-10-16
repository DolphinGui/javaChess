package miscFunct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class FileRead {
	public static ArrayList<Character> readFile(File file) {
		// This turns a file into an arraylist of characters.
		return readFile(file, Charset.defaultCharset());
	}

	public static ArrayList<Character> readFile(File file, Charset encoding) {
		try (InputStream in = new FileInputStream(file);
				Reader reader = new InputStreamReader(in, encoding);
				Reader buffer = new BufferedReader(reader)) {
			return handleCharacters(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ArrayList<Character> handleCharacters(Reader reader) {
		int r;
		ArrayList<Character> result = new ArrayList<Character>();
		try {
			while ((r = reader.read()) != -1) {
				char ch = (char) r;
				result.add(ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String wordParse(String s, int index) {
		String results = "";
		for (int i = index; !Character.isWhitespace(s.charAt(i)); i++) {
			results = results.concat(String.valueOf(s.charAt(i)));
		}
		return results;
	}
}
