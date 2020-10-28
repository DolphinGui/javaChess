package javaChess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javaChess.Session.ChessClock;
import miscFunct.FileRead;
import vanillaChess.AlgebraicMove;
import vanillaChess.Game;

public class Bot extends Player {

	public static class UCIbot {
		private static class Setting {
			final UCITypes type;
			final String str;
			final int min;
			final int max;
			final String[] vars;
			final String name;

			// Constructs button
			public Setting(String n) {
				name = n;
				type = UCITypes.button;
				str = "";
				min = -1;
				max = -1;
				vars = new String[]{};
			}

			// Constructs check (boolean)
			public Setting(String n, boolean b) {
				name = n;
				type = UCITypes.check;
				str = String.valueOf(b);
				min = -1;
				max = -1;
				vars = new String[]{};
			}

			// Constructs spin
			public Setting(String n, int mn, int mx, int v) {
				name = n;
				type = UCITypes.spin;
				min = mn;
				max = mx;
				if (v >= mn && v <= mx) {
					str = String.valueOf(v);
				} else {
					throw new IndexOutOfBoundsException(v);
				}
				vars = new String[]{};
			}

			// Constructs string
			public Setting(String n, String t) {
				name = n;
				type = UCITypes.string;
				str = t;
				min = -1;
				max = -1;
				vars = new String[]{};
			}

			// Constructs combo
			public Setting(String n, String[] v, int i) {
				name = n;
				type = UCITypes.combo;
				vars = v;
				str = vars[i];
			min = -1;
			max = -1;
			}

			// Returns the command with a \n.
			public String parse() {
				String result = "setoption name " + name;
				if (type != UCITypes.button) {
					result = result.concat(" value " + str);
				}
				return result;
			}

		}
		static final private String nl = System.lineSeparator();
		private Process bot;
		private InputStream out;
		private OutputStreamWriter write;
		public String lastmove;
		public String pondermove;

		ArrayList<Setting> options;

		public UCIbot(String path) {
			try {
				bot = Runtime.getRuntime().exec(path);
				out = bot.getInputStream();
				OutputStream in = bot.getOutputStream();
				write = new OutputStreamWriter(in);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void close() {
			writeFlush("quit");
			bot.destroy();
		}

		public void init() throws IOException {
			readAll();
			writeFlush("uci");
			String buffer = readAll();
			ArrayList<Integer> nameIndex = new ArrayList<>();
			ArrayList<Integer> typeIndex = new ArrayList<>();
			options = new ArrayList<>();
			int lastindex = 0;
			do {
				nameIndex.add(buffer.indexOf("option name", lastindex + 1));
				lastindex = nameIndex.get(nameIndex.size() - 1);
			} while (lastindex != -1);
			nameIndex.remove(nameIndex.size() - 1);
			lastindex = 0;
			do {
				typeIndex.add(buffer.indexOf("type", lastindex + 1));
				lastindex = typeIndex.get(typeIndex.size() - 1);
			} while (lastindex != -1);
			typeIndex.remove(typeIndex.size() - 1);

			for (Integer l : nameIndex) {
				String name = buffer.substring(l + 12, typeIndex.get(nameIndex.indexOf(l)) - 1);
				String type = FileRead.wordParse(buffer, l + 18 + name.length());
				String def = FileRead.wordParse(buffer, l + 27 + type.length() + name.length());
				ArrayList<String> variations = new ArrayList<>();
				int st = l + 32 + type.length() + name.length() + def.length();
				String var;
				if (type.equals("combo")) {
					do {
						var = FileRead.wordParse(buffer, st);
						variations.add(var);
						if (!FileRead.wordParse(buffer, st + var.length() + 1).equals("var")) {
							break;
						}
						st += var.length() + 5;
					} while (true);
				}
				if (type.equals("spin")) {
					do {
						var = FileRead.wordParse(buffer, st);
						variations.add(var);
						String eval = FileRead.wordParse(buffer, st + var.length() + 1);
						if (!(eval.equals("min") || eval.equals("max"))) {
							break;
						}
						st += var.length() + 5;
					} while (true);
				}
				switch (parseType(type)) {
					case check -> options.add(new Setting(name, Boolean.parseBoolean(def)));
					case spin -> options.add(new Setting(name, Integer.parseInt(variations.get(0)), Integer.parseInt(variations.get(1)),
							Integer.parseInt(def)));
					case combo -> options.add(
							new Setting(name, variations.toArray(new String[0]), variations.indexOf(def)));
					case button -> options.add(new Setting(name));
					case string -> options.add(new Setting(name, def));
				}
			}
		}

		private UCITypes parseType(String s) {
			return switch (s.toLowerCase()) {
				case "check" -> UCITypes.check;
				case "button" -> UCITypes.button;
				case "combo" -> UCITypes.combo;
				case "spin" -> UCITypes.spin;
				case "string" -> UCITypes.string;
				default -> null;
			};
		}

		public void position(String fen) {
			writeFlush("position fen" + fen);
		}

		public void search(int wTime, int bTime) throws IOException {
			writeFlush("go wtime " + wTime + " " + "btime" + bTime);
			String ind = until(out);
			String answer = ind + readAll();
			lastmove = answer.substring(9, answer.indexOf("ponder "));
			pondermove = answer.substring(answer.indexOf("ponder ") + 7);
		}
		
		public void search(int sec) throws IOException {
			out.readAllBytes();
			writeFlush("go movetime " + sec);
			String ind = until(out);
			String answer = ind + readAll();
			lastmove = answer.substring(9, answer.indexOf("ponder "));
			pondermove = answer.substring(answer.indexOf("ponder ") + 7);
		}
		
		private static String until(InputStream out) throws IOException {
			String str = "";
			int index = -1;
			while (index == -1) {
				str = str.concat(String.valueOf((char) out.read()));
				index = str.indexOf("bestmove ");
			}
			return str.substring(index);
		}

		private String readAll() throws IOException {
			String str = "";
			str = str.concat(String.valueOf((char) out.read()));
			while (out.available() != 0) {
				str = str.concat(String.valueOf((char) out.read()));
			}
			return str;
		}
		private void writeFlush(String str) {
			try {
				write.write(str + nl);
				write.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	final UCIbot bot;

	public Bot(boolean white, Game game, ChessClock t, String path) {
		super(white, game, t);
		bot = new UCIbot(path);
		try {
			bot.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void error(String s) {}

	@Override
	AlgebraicMove onTurn() {
		try {
			if(time.time(myTurn)==-1) bot.search(5000);
			else bot.search(time.time(myTurn), time.time(!myTurn));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return board.decode(bot.lastmove);
	}

	@Override
	AlgebraicMove offTurn() {
		return new AlgebraicMove();
	}

	@Override
	void stopPonder() {
		// TODO Auto-generated method stub

	}

	@Override
	void ponderhit() {
		// TODO Auto-generated method stub

	}

	@Override
	void victoryScreen() {
		bot.close();
	}

	@Override
	void lossScreen() {
		bot.close();
	}


	@Override
	void updateScreen(AlgebraicMove m) {
		bot.position(board.getFen());
	}

	@Override
	void drawTime(int w, int b) {
		// TODO Auto-generated method stub
		
	}


}
enum UCITypes {
	check, spin, combo, button, string
	/*
	 * Check is boolean, spin is an integer with a range, combo is a combination of
	 * predefined variable strings, button is a command, and string is string.
	 */
}