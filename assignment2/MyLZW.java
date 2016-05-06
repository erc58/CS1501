/*************************************************************************
 *  Compilation:  javac MyLZW.java
 *  Execution:    java MyLZW - mode < input.txt   (compress)
 *  Execution:    java MyLZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
import java.util.Arrays;
public class MyLZW {
	// Constants
	private static final int MAX_W = 16; 
	private static final int MIN_W = 9;
	private static final int MAX_L = 65536;
	private static final int MIN_L = 512;
    private static int R = 256;       	// number of input chars
	
	// Variables
    private static int L = MIN_L;     		// number of codewords = 2^W, initialized to 512
    private static int W = MIN_W;     		// codeword width, initialized to 9
	private static char mode = 'n';	  		// Do Nothing = 'n', Reset = 'r', Monitor = 'm'
	private static long bitsRead = 0;  		// bits read in from input file
	private static long bitsWritten = 0; 	// bits written out to output file
	private static double oldRatio = 0;
	private static double newRatio = 0;
	private static boolean monitoring = false; // determines if the compression ratios are being monitored

    public static void compress() { 
        StringBuilder input = new StringBuilder(BinaryStdIn.readString());
        TST<Integer> st = initializeTSTCodebook();
		writeMode(mode);
        int code = R+1;  // R is codeword for EOF
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input.toString());  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
			bitsRead += t * 8; // each char in String is a byte (8 bits)
			bitsWritten += W;
            if (t < input.length()) {  // try to add s to symbol table.
				if (code < L){ // Codebook is not full
					st.put(input.substring(0, t + 1), code++);
				}
				else{ // Codebook is full
					if(W < MAX_W){
							W++;
							L*=2;	
							st.put(input.substring(0, t + 1), code++);
					}
					else{
						if (mode == 'n') { // Do Nothing Mode
						}
						else if (mode == 'r') { // Reset Mode
							st = initializeTSTCodebook();
							W = MIN_W;
							L = MIN_L;
							code = R + 1;
							st.put(input.substring(0, t + 1), code++);
						}
						else { // Monitor Mode
							newRatio = bitsRead / (double) bitsWritten;
							if (!monitoring){
								oldRatio = newRatio;
								monitoring = true;
							}
							else if (oldRatio / newRatio > 1.1) { // reset if [(old ratio)/(new ration)] > 1.1
									st = initializeTSTCodebook();
									W = MIN_W;
									L = MIN_L;
									code = R + 1;
									oldRatio = 0;
									monitoring = false;
									st.put(input.substring(0, t + 1), code++);
							}
						}
					}
				}
            }
			input = new StringBuilder(input.substring(t));            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

	public static void expand() {
        String[] st = initializeArrayCodebook();
        int i = R + 1; // next available codeword value
		int temp = BinaryStdIn.readInt(2); // First two bits are reserved for the mode
		if(temp == 0) mode = 'n';
		else if(temp == 1) mode = 'r';
		else mode = 'm';
        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];
        while (true) {
			int t = val.length();
			bitsRead += W; // read in bit width
			bitsWritten += t * 8; // write 8 bit characters
            BinaryStdOut.write(val);
			if(i >= L){
				if (W < MAX_W) {
					W++;
					L*=2;
				}
				else {
					if (mode == 'n') {
						// Do nothing
					}
					else if (mode == 'r') {
						st = initializeArrayCodebook();
						i = R + 1;
						W = MIN_W;
						L = MIN_L;
					}
					else{
						newRatio = bitsWritten / (double) bitsRead; // flipped to achieve ratios measured during compression
						if (!monitoring) {
							oldRatio = newRatio;
							monitoring = true;
						}
						else if (oldRatio / newRatio > 1.1){ // reset if [(old ratio)/(new ration)] > 1.1
							st = initializeArrayCodebook();
							i = R + 1;
							W = MIN_W;
							L = MIN_L;
							oldRatio = 0;
							monitoring = false;
						}
					}
				}
			}
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L){
				st[i++] = val + s.charAt(0);
			}
            val = s;
        }
        BinaryStdOut.close();
    }
	
	/**
	 * Initializes TST<Integer> codebook when called 
	 * @return a TST<Integer> codebook with codes for each of the 256 characters
	 */
	public static TST<Integer> initializeTSTCodebook(){
		TST<Integer> st = new TST<Integer>();
		int i;
		for (i = 0; i < R; i++)
            st.put("" + (char) i, i);
		return st;
	}
	
	/**
	 * Initializes String[] codebook when called
	 * @return a String[] codebook of size 65536 with codes for each of the 256 characters
	 */
	public static String[] initializeArrayCodebook(){
		int i;
		String[] st = new String[MAX_L];
		for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";
		return st;
	}
	
	/**
	 * Writes the mode state to file using 2 bits.
	 */
	private static void writeMode(char mode){
		int temp = 0;
		if (mode == 'n') temp = 0;
		else if (mode == 'r') temp = 1;
		else temp = 2;
		int bitWidth = 2;
		BinaryStdOut.write(temp, bitWidth);
	}
    public static void main(String[] args) {
        if (args[0].equals("-")){
			String modeString = args[1].toLowerCase(); // handle uppercase and lowercase entry
			if(modeString.equals("n")) mode = 'n';
			else if (modeString.equals("r")) mode = 'r';
			else if (modeString.equals("m")) mode = 'm';
			else throw new IllegalArgumentException("Illegal command line argument");
			compress();
		}
        else if (args[0].equals("+")) expand();		
        else throw new IllegalArgumentException("Illegal command line argument");	
    }
}
