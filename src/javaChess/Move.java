package javaChess;

public abstract class Move<T> {
    public final T location;
    public final T origin;

    public Move(T loc, T o) {
        location = loc;
        origin = o;
    }
}
