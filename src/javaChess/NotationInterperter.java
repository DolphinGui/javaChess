package javaChess;

public class NotationInterperter {
    private int width;
    private int height;
    
    NotationInterperter(int x, int y){
	width = x;
	height = y;
    }
    
    public int denotate(String notate) {
	final String ab = "abcdefghijklmnopqrstuvwxyz"; //abcdefghijklmnopqrstuvwxyz zyxwvutsrqpomnlkjihgfedcba
	int result;
	if(!Character.isDigit(notate.charAt(1))) return -2;
	if(ab.indexOf(notate.charAt(0))==-1||ab.indexOf(notate.charAt(0))>height) return -2;
	result = (Character.getNumericValue(notate.charAt(1))-1)*width;
	result+= ab.indexOf(Character.toLowerCase(notate.charAt(0)));
	if(result>=height*width) return -1;
	return result;
    }
}
