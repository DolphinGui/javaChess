package timeChess;

import java.io.Serializable;

public class TimeAlgebraicMove implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8727231205734599052L;
	public final int loc;
	public final int origin;
	public final char promote;
	public final int turns;
	public TimeAlgebraicMove(int l, int o, char p, int t){
		loc = l;
		origin = o;
		promote = p;
		turns = t;
	}
	public TimeAlgebraicMove(int l, int o, int t){
		loc = l;
		origin = o;
		promote =' ';
		turns = t;
	}
	
	public TimeAlgebraicMove() {
		loc = 0;
		origin = 0;
		promote = '\n';
		turns = 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof TimeAlgebraicMove)) return false;
		TimeAlgebraicMove m = (TimeAlgebraicMove) o;
		return m.loc==loc && m.origin==origin && m.promote==promote && m.turns==turns;
	}
	
}
