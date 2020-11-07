package javaChess;

public interface Game<T> {
    boolean checkmate(boolean turn);
    boolean checkmate();
    String getFen();
    char[][] getCharBoard();
    int getHeight();
    int getWidth();
    void init();
    T[] history();
    boolean turn(T m) throws InvalidMoveException;
    int denotate(String notation);
    T decode(String move);
    String notate(T move);
}
