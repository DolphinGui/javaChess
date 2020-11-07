package javaChess;

public interface Game<T extends  Move<E>, E> {
    boolean checkmate(boolean turn);
    boolean checkmate();
    String getFen();
// --Commented out by Inspection STOP (2020-11-06, 5:52 p.m.)
    char[][] getCharBoard();
    int getHeight();
    int getWidth();
    void init();
// --Commented out by Inspection START (2020-11-06, 5:52 p.m.):
//    T[] history();
    boolean turn(T m) throws InvalidMoveException;
// --Commented out by Inspection STOP (2020-11-06, 5:52 p.m.)
    int denotate(String notation);
    T decode(String move);
    String notate(T move);
}
