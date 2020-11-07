package timeChess;

import javaChess.Move;

import java.io.Serializable;

public class TimeMove extends Move<Integer> implements Serializable {
	private static final long serialVersionUID = -9138583168838054804L;
	/**
	 * 
	 */
	public final char promote;
	public final int turns;
	public TimeMove(int l, int o, char p, int t){
		super(l,o);
		promote = p;
		turns = t;
	}
	public TimeMove(int l, int o, int t){
		super(l,o);
		promote =' ';
		turns = t;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof TimeMove)) return false;
		TimeMove m = (TimeMove) o;
		return m.location.equals(location) && m.origin.equals(origin) && m.promote==promote && m.turns==turns;
	}
	
}
