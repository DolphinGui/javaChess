package miscFunct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class FileRead {
	public static ArrayList<Character> readFile(File file) throws IOException {
		// This turns a file into an arraylist of characters.
		return readFile(file, Charset.defaultCharset());
	}

	public static ArrayList<Character> readFile(File file, Charset encoding) throws IOException, FileNotFoundException {
		try (InputStream in = new FileInputStream(file);
				Reader reader = new InputStreamReader(in, encoding);
				Reader buffer = new BufferedReader(reader)) {
			return handleCharacters(buffer);
		}
	}

	private static ArrayList<Character> handleCharacters(Reader reader) throws IOException {
		int r;
		ArrayList<Character> result = new ArrayList<Character>();
		while ((r = reader.read()) != -1) {
			char ch = (char) r;
			result.add(ch);
		}
		return result;
	}
}
