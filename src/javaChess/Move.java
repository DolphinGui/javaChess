package javaChess;

import java.io.Serializable;

public class Move implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8727231205734599052L;
	public final int loc;
	public final int origin;
	public final char promote;
	public Move(int l, int o, char p){
		loc = l;
		origin = o;
		promote = p;
	}
	public Move(int l, int o){
		loc = l;
		origin = o;
		promote =' ';
	}
	
	public Move() {
		loc = 0;
		origin = 0;
		promote = '\n';
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof Move)) return false;
		Move m = (Move) o;
		return m.loc==loc && m.origin==origin && m.promote==promote;
	}
	
}
