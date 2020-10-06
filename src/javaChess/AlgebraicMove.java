package javaChess;

import java.io.Serializable;

public class AlgebraicMove implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8727231205734599052L;
	public final int loc;
	public final int origin;
	public final char promote;
	public AlgebraicMove(int l, int o, char p){
		loc = l;
		origin = o;
		promote = p;
	}
	public AlgebraicMove(int l, int o){
		loc = l;
		origin = o;
		promote =' ';
	}
}
