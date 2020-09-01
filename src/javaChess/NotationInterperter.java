package javaChess;

public class NotationInterperter {
    public static int denotate(String notate) {
	final String ab = "abcdefghijklmnopqrstuvwxyz"; //abcdefghijklmnopqrstuvwxyz zyxwvutsrqpomnlkjihgfedcba
	int result;
	result = Character.getNumericValue(notate.charAt(1))*8-8;
	result+= ab.indexOf(notate.charAt(0));
	return result;
    }
}
