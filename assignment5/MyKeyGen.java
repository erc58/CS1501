import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
public class MyKeyGen{
	private static final int bitlength = 512; // half of 1024
	private static final String TWO = "2";
	private static final String PUBLIC_KEY_FILENAME = "pubkey.rsa";
	private static final String PRIVATE_KEY_FILENAME = "privkey.rsa";
	public static void main(String[] args){
		SecureRandom randomOne = new SecureRandom();
		BigInteger P = BigInteger.probablePrime(bitlength, randomOne);
		SecureRandom randomTwo;
		BigInteger Q;
		do {
			randomTwo = new SecureRandom();
			Q = BigInteger.probablePrime(bitlength, randomTwo);
		} while (P.compareTo(Q) == 0);
		BigInteger N = P.multiply(Q);
		BigInteger phiN = (P.subtract(BigInteger.ONE)).multiply(Q.subtract(BigInteger.ONE));
		BigInteger E = new BigInteger(TWO);
		while(E.compareTo(phiN) < 0){
			if(phiN.gcd(E).equals(BigInteger.ONE)) break;
			E = E.add(BigInteger.ONE);
		}
		BigInteger[] xgcd = XGCD(phiN, E);
		BigInteger D = xgcd[2];
		if(D.compareTo(BigInteger.ZERO) < 0) {
			D = D.add(phiN); 
			/* CS 0441 came in handy here!
				D < 0
				E * D				= 1 mod phiN      			% Rules of E and D
				E * D 				= z * phiN + 1    			% Definition of modulo
				E * D + E * phiN 	= z * phiN + 1 + E * phiN   % Add E * phiN to both sides
				E * (D + phiN)		= phiN * (z + E) + 1		% Associative and Commutative
				E * (D + phiN)		= phiN * (z + E) + 1 mod phiN
				E * (D + phiN)		= 1 mod phiN
			*/
		}
		try {
			ObjectOutputStream savePub = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILENAME));
			savePub.writeObject(E);
			savePub.writeObject(N);
		} catch(IOException ioe){
			System.err.println("Error while saving to " + PUBLIC_KEY_FILENAME);
			ioe.printStackTrace();
		}
		try {
			ObjectOutputStream savePriv = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILENAME));
			savePriv.writeObject(D);
			savePriv.writeObject(N);
		} catch(IOException ioe){
			System.err.println("Error while saving to " + PRIVATE_KEY_FILENAME);
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Modified XGCD function to use BigInteger. Performs 
	 * the Extended Euclidean Algorithm
	 * GCD(a, b) = i = a*s + b*t
	 * @param a The first BigInteger
	 * @param b The second BigInteger
	 * @return A BigInteger array containing the following:
	 *		   answer[0] = i = GCD(a, b) 
	 *		   answer[1] = s
	 *		   answer[2] = t
	 */
	private static BigInteger[] XGCD(BigInteger a, BigInteger b){
		BigInteger[] answer = new BigInteger[3];
	
		BigInteger q;
		if (b.equals(BigInteger.ZERO)) {
			answer[0] = a;
			answer[1] = BigInteger.ONE;
			answer[2] = BigInteger.ZERO;
		}
		else {
			q = a.divide(b);
			answer = XGCD(b, a.mod(b));
			BigInteger temp = answer[1].subtract(answer[2].multiply(q));
			answer[1] = answer[2];
			answer[2] = temp;
		}
		return answer;
	}
}