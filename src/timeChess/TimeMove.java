package timeChess;

import javaChess.Move;

import java.io.Serializable;

public class TimeMove extends Move implements Serializable {
	private static final long serialVersionUID = -9138583168838054804L;
	/**
	 * 
	 */

	public final int loc;
	public final int origin;
	public final char promote;
	public final int turns;
	public TimeMove(int l, int o, char p, int t){
		loc = l;
		origin = o;
		promote = p;
		turns = t;
	}
	public TimeMove(int l, int o, int t){
		loc = l;
		origin = o;
		promote =' ';
		turns = t;
	}
	
	public TimeMove() {
		loc = 0;
		origin = 0;
		promote = '\n';
		turns = 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof TimeMove)) return false;
		TimeMove m = (TimeMove) o;
		return m.loc==loc && m.origin==origin && m.promote==promote && m.turns==turns;
	}
	
}
