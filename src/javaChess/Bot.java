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
		private class Setting {
			UCITypes type;
			String str;
			int min;
			int max;
			String[] vars;
			String name;

			// Constructs button
			public Setting(String n) {
				name = n;
				type = UCITypes.button;
			}

			// Constructs check (boolean)
			public Setting(String n, boolean b) {
				name = n;
				type = UCITypes.check;
				str = String.valueOf(b);
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
			}

			// Constructs string
			public Setting(String n, String t) {
				name = n;
				type = UCITypes.string;
				str = t;
			}

			// Constructs combo
			public Setting(String n, String[] v, int i) {
				name = n;
				type = UCITypes.combo;
				vars = v;
				str = vars[i];
			}

			// Returns the command with a \n.
			public String parse() {
				String result = "setoption name " + name;
				if (type != UCITypes.button) {
					result = result.concat(" value " + str);
				}
				return result;
			}

			// Updates values and does validation.
			public void update(String s) {
				switch(type) {
				case button:
					throw new IllegalCallerException("Buttons have no values to update");
				case check:
					str = String.valueOf(Boolean.valueOf(s));
					break;
				case combo:
					boolean er = true;
					for(String n : vars) {
						if(n.equals(s)) {
							er = false;
							break;
						}
					}
					if(!er) {
						str = s;
					}else {
						throw new IllegalArgumentException("Not a valid value");
					}
					break;
				case spin:
					if(Integer.valueOf(s)>max||Integer.valueOf(s)<min) {
						throw new IllegalArgumentException("Out of bounds");
					}
					str = s;
					break;
				case string:
					str = s;
					break;
				}	
			}
		}
		static final private String nl = System.lineSeparator();
		private Process bot;
		private InputStream out;
		private OutputStream in;
		private OutputStreamWriter write;
		public String lastmove;
		public String pondermove;

		ArrayList<Setting> options;

		public UCIbot(String path) {
			try {
				bot = Runtime.getRuntime().exec(path);
				out = bot.getInputStream();
				in = bot.getOutputStream();
				write = new OutputStreamWriter(in);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		//
		public void changeSetting(String id, String value) throws IOException {
			int index = 0;
			for(Setting s : options) {
				if(s.name.equals(id)) {
					index = options.indexOf(s);
					break;
				}
			}
			options.get(index).update(value);
			writeFlush(options.get(index).parse());
			if(out.available() !=0) {
				readAll();
			}
		}

		public void close() throws IOException {
			writeFlush("quit");
			bot.destroy();
		}

		public void init() throws IOException {
			readAll();
			writeFlush("uci");
			String buffer = readAll();
			ArrayList<Integer> nameIndex = new ArrayList<Integer>();
			ArrayList<Integer> typeIndex = new ArrayList<Integer>();
			options = new ArrayList<Setting>();
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
				ArrayList<String> variations = new ArrayList<String>();
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
				case check:
					options.add(new Setting(name, Boolean.valueOf(def)));
					break;
				case spin:
					options.add(new Setting(name, Integer.valueOf(variations.get(0)), Integer.valueOf(variations.get(1)),
							Integer.valueOf(def)));
					break;
				case combo:
					options.add(
							new Setting(name, variations.toArray(new String[variations.size()]), variations.indexOf(def)));
					break;
				case button:
					options.add(new Setting(name));
					break;
				case string:
					options.add(new Setting(name, def));
				}
			}
		}

		private UCITypes parseType(String s) {
			switch (s.toLowerCase()) {
			case "check":
				return UCITypes.check;
			case "button":
				return UCITypes.button;
			case "combo":
				return UCITypes.combo;
			case "spin":
				return UCITypes.spin;
			case "string":
				return UCITypes.string;
			}
			return null;
		}

		public void position(String fen) throws IOException {
			writeFlush("position fen" + fen);
		}

		public void position(String fen, AlgebraicMove[] moves) throws IOException {
			String move = "";
			for(AlgebraicMove m: moves) {
				move += m.toString() + " ";
			}
			writeFlush("position fen" + fen + " moves" + move);
		}

		public void search(int wTime, int bTime) throws IOException {
			writeFlush("go wtime " + wTime + " " + "btime" + bTime);
			String ind = until("bestmove ");
			String answer = ind + readAll();
			lastmove = answer.substring(9, answer.indexOf("ponder "));
			pondermove = answer.substring(answer.indexOf("ponder ") + 7);
		}
		
		public void search(int sec) throws IOException {
			writeFlush("go movetime " + sec);
			String ind = until("bestmove ");
			String answer = ind + readAll();
			lastmove = answer.substring(9, answer.indexOf("ponder "));
			pondermove = answer.substring(answer.indexOf("ponder ") + 7);
		}
		
		private String until(String target) throws IOException {
			String str = "";
			int index = -1;
			while (index == -1) {
				str = str.concat(String.valueOf((char) out.read()));
				index = str.indexOf(target);
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

	UCIbot bot;

	public Bot(boolean white, Game game, ChessClock t, String path) {
		super(white, game, t);
		bot = new UCIbot(path);
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
		//bot.
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
		try {
			bot.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	void lossScreen() {
		try {
			bot.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	void updateScreen(AlgebraicMove m) {
		try {
			bot.position(board.getFen());
		} catch (IOException e) {
			e.printStackTrace();
		}
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