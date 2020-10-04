package vanillaChess;

public class InvalidMoveException extends Exception {
	public InvalidMoveException() {
        super();
	}
	
	public InvalidMoveException(String s) {
        super(s);
	}
	
	public InvalidMoveException(String s, Throwable c) {
        super(s, c);
	}
	public InvalidMoveException(Throwable c) {
        super(c);
	}	
	/**
	 * More or less a wrapper class for other invalid moves. 
	 */
	private static final long serialVersionUID = 5408716596409657907L;
}
