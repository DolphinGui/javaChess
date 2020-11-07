package vanillaChess;

import javaChess.Move;

import java.io.Serializable;

public class LinearMove extends Move<Integer> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8727231205734599052L;
	public final char promote;

	public LinearMove(Integer loc, Integer o, char p) {
		super(loc, o);
		promote = p;
	}
	public LinearMove(Integer loc, Integer o) {
		super(loc, o);
		promote = ' ';
	}


	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof LinearMove)) return false;
		LinearMove m = (LinearMove) o;
		return m.location.equals(location) && m.origin.equals(origin) && m.promote==promote;
	}
	
}
