package networkChess;

import java.util.BitSet;

public class HammingIntegrity {

    private static BitSet calculation(BitSet ar,int len, int r) {
	for (int i = 0; i < r; i++) { //for all parity bits
	    int parity = (int) Math.pow(2, i);
	    for (int j = 1; j <= len + r; j++) { // for all bits
		if (((j >> i) & 1) == 1) { // if part of the block
		    if (parity != j) //if not the parity bit
			ar.set(parity, ar.get(parity) ^ ar.get(j));
		}
	    }
	}
	ar.set(0, ar.cardinality() % 2 == 1);
	return ar;
    }

    private static BitSet generateCode(BitSet str, int M, int r) {
	BitSet ar = new BitSet(r + M + 1);
	int j = 0;
	for (int i = 1; i < r + M + 1; i++) {
	    if ((Math.ceil(Math.log(i) / Math.log(2)) - Math.floor(Math.log(i) / Math.log(2))) == 0) {
		ar.set(i, false);
	    } else {
		ar.set(i, str.get(j));
		j++;
	    }
	}

	return ar;
    }  
//    private static String printBitset(BitSet stuff, int len) {
//	String results = "";
//	for (int i = 0; i < len; i++) {
//	    if (stuff.get(i)) {
//		results += "1";
//	    } else {
//		results += "0";
//	    }
//	}
//	return results;
//    }

    public static BitSet encode(BitSet bits, int len) {
	int r = 1;

	for (; Math.pow(2, r) < len + r + 1; r++) {
	}

	BitSet ar = generateCode(bits, len, r);
	return calculation(ar, len, r);
    }

    public static BitSet decode(BitSet cypher, int len) {
	int buffer = 0;
	BitSet results = new BitSet();
	for(int i = cypher.nextSetBit(0); i >=0;i = cypher.nextSetBit(i+1)) {
	    buffer = buffer ^ i;
	}
	if(buffer!=0) 
	    cypher.flip(buffer);
	int j = 0;
	for(int i = 0; i < len; i++) {
	    if(i!=0&&(Math.ceil(Math.log(i) / Math.log(2)) - Math.floor(Math.log(i) / Math.log(2))) != 0) {
		results.set(j, cypher.get(i));
		j++;
	    }
	}
	
	return results;
    }
}
