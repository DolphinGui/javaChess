package miscFunct;

import java.util.Arrays;

public class ArrayMan {
    @SafeVarargs
    public static <T> T[] concatAll(T[] first, T[]... rest) {

	int totalLength = first.length;
	for (T[] array : rest) {
	    totalLength += array.length;
	}
	T[] result = Arrays.copyOf(first, totalLength);
	int offset = first.length;
	for (T[] array : rest) {
	    System.arraycopy(array, 0, result, offset, array.length);
	    offset += array.length;
	}
	return result;
    }
    public static <T> T[] append(T[] array, T addition) {
	T[] result = Arrays.copyOf(array, array.length+1);
	result[result.length] = addition;
	return result;
    }
}
