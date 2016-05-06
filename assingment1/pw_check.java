import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * pw_check class
 * @author Charlie Kelly
 */
public class pw_check{
	public static void main(String[] args){
		if(args.length == 1){
			if(args[0].equals("-g")){
				long startTime = System.currentTimeMillis();
				//Generate passwords
				DLB dictionary = createMyDictionary();
				long midTime = System.currentTimeMillis();
				System.out.println("Creating DLB took: " + ((double)(midTime - startTime)/60000) + " minutes");
				goodPasswords(dictionary);
			}
			else{
				System.out.println("Error: Not a recognized or accepted command line argument.");
				System.exit(0);
			}
		}
		else if(args.length > 1){
			System.out.println("Error: Not a recognized or accepted command line argument.");
			System.exit(0);
		}
		else{
			if(fileExists("good_passwords.txt")){
				//noArgsProgram(); // calls user program
				userProgram();
			}
			else{
				System.out.println("Error: You have not run the program with [-g] yet.");
				System.exit(0);
			}

		}
	}
	
	/**
	 * Creates the dictionary to be used for checking passwords
	 * @return The DLB containing all the dictionary words that are necessary
	 */
	public static DLB createMyDictionary(){
		String inputFilename = "dictionary.txt";
		String outputFilename = "my_dictionary.txt";
		DLB trie = new DLB();
		BufferedReader inputFile = null;
		FileWriter outputFile = null;
		StringBuilder words = new StringBuilder();
		String word = " ";
		if(fileExists(inputFilename)){
			try{
				inputFile = new BufferedReader(new FileReader(inputFilename));
				outputFile = new FileWriter(outputFilename);
				while(word != null){
					word = inputFile.readLine();
					if(word != null){
						if(word.length() < 6){
							word = word.toLowerCase();
							LinkedList<String> combinationList = enumCombinations(word);
							Iterator<String> iterator = combinationList.getIterator();
							while(iterator.hasNext()){
								String temp = iterator.next();
								trie.insert(temp);
								words.append(temp + "\n");
							}
							combinationList.clear();
						}
					}
				}
				outputFile.append(words);
				words = null;
				return trie;
			} catch (IOException ioe) {
				System.out.println("Error: The file(s) does not exist.\nEnding Program...");
				System.exit(0); // Quit Program
			} finally {
				try{
					inputFile.close();
					outputFile.close();
				} catch (IOException ioe) {
					System.out.println("Error: The file(s) cannot be properly written to.\nEnding Program...");
					System.exit(0); // Quit Program
				}
			}
		}
		return null;
	}
	
	/**
	 * Builds a list of all ways to spell this word
	 * @param word The word to inspect
	 * @return The list of different combinations of a word
	 */
	public static LinkedList<String> enumCombinations(String word){
		LinkedList<String> list = new LinkedList<String>();
		StringBuilder builder = new StringBuilder();
		return enumCombinations(word, list, builder);
	}
	
	/**
	 * Builds a list of all ways to spell this word
	 * @param word The word to inspect
	 * @param list The list of word combinations
	 * @param builder A string being built up
	 * @return The list of different combinations of a word
	 */
	private static LinkedList<String> enumCombinations(String word, LinkedList<String> list, StringBuilder builder){
		if(isFullWord(word, builder)){
			list.add(builder.toString());
		}
		StringBuilder temp = extendWord(word, builder);
		while(temp != null){
			list = enumCombinations(word, list, temp);
			temp = nextWord(temp);
		}
		return list;
	}
	
	/**
	 * Check if the StringBuilder word is a full word
	 * @param word The word 
	 * @param builder A word being built
	 * @return True if builder is a word
	 */
	public static boolean isFullWord(String word, StringBuilder builder){
		return builder.length() == word.length();
	}
	
	/**
	 * Attempt to build onto builder
	 * @param word The word 
	 * @param builder A word being built
	 * @return builder or null
	 */
	public static StringBuilder extendWord(String word, StringBuilder builder){
		StringBuilder temp = new StringBuilder(builder);
		int builderLength = temp.length();
		if(builderLength < word.length()){
			return temp.append(word.charAt(builderLength));
		}
		else{
			return null;
		}
	}
	
	/**
	 * Attempt to perform a letter to number switch
	 * @param word The word 
	 * @param builder A word being built
	 * @return builder or null
	 */
	public static StringBuilder nextWord(StringBuilder builder){
		StringBuilder temp = new StringBuilder(builder);
		int last = temp.length() - 1;
		char character = temp.charAt(last);
		if(character == 'a'){
			temp.setCharAt(last, '4');
			return temp;
		}
		else if(character == 'e'){
			temp.setCharAt(last, '3');
			return temp;
		}
		else if(character == 'i'){
			temp.setCharAt(last, '1');
			return temp;
		}
		else if(character == 'o'){
			temp.setCharAt(last, '0');
			return temp;
		}
		else if(character == 'l'){
			temp.setCharAt(last, '1');
			return temp;
		}
		else if(character == 's'){
			temp.setCharAt(last, '$');
			return temp;
		}
		else if(character == 't'){
			temp.setCharAt(last, '7');
			return temp;
		}
		return null;
	}
	
	/**
	 * Generate all good passwords
	 * @param dictionary The dictionary of words
	 */
	public static void goodPasswords(DLB dictionary){
		FileWriter output = null;
		StringBuilder password = new StringBuilder();
		StringBuilder passwords = new StringBuilder();
		try{
			output = new FileWriter("good_passwords.txt");
			long start = System.currentTimeMillis();
			passwords = solve(password, dictionary, passwords);
			dictionary = null;
			output.append(passwords);
			char[] startingCharacters = {'0', '2', '3', '5', '6', '7',
										'8', '9', 'b', 'c', 'd', 'e',
										'f', 'g', 'h', 'j', 'k', 'l', 
										'm', 'n', 'o', 'p', 'q', 'r', 
										's', 't', 'u', 'v', 'w', 'x',
										'y', 'z', '!', '@', '$', '^', 
										'_', '*'};
			for(char character: startingCharacters){
				save(passwords, character);
			}
			passwords = null;
			long end = System.currentTimeMillis();
			System.out.println("Enumerating passwords and writing to file took: " + ((double)(end - start)/60000) + " minutes");
		} catch (IOException ioe) {
			System.out.println("Catastrophic Failure");
			System.exit(0); // Quit Program
		} finally {
			try{
				output.close();
			} catch (IOException ioe) {
				System.out.println("Catastrophic Failure");
				System.exit(0); // Quit Program
			}
		}
	}
	
	/**
	 * Find all good passwords
	 * @param password The password in progress
	 * @param dictionary The dictionary of words
	 * @param passwords A StringBuilder Object of all the words
	 * @return passwords
	 */
	public static StringBuilder solve(StringBuilder password, DLB dictionary, StringBuilder passwords){
		int[] frequency = frequencyOfType(password);
		if(reject(password, dictionary, frequency)){
			return passwords;
		}
		if(isFullSolution(password)){
			return passwords.append(password + "\n");
		}
		StringBuilder attempt = extend(password);
		while(attempt != null){
			solve(attempt, dictionary, passwords);
			attempt = next(attempt, frequency);
		}
		return passwords;
	}
	
	/**
	 * Check if a good password
	 * @param password The password in progress
	 * @return True if a good password
	 */
	public static boolean isFullSolution(StringBuilder password){
		if(password.length() < 5){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Check if a password should be pruned
	 * @param password The password in progress
	 * @param dictionary The dictionary of words
	 * @param frequency An array of frequencies
	 * @return True if a password should be rejected
	 */
	public static boolean reject(StringBuilder password, DLB dictionary, int[] frequency){
		int letters = frequency[0];
		int numbers = frequency[1];
		int specials = frequency[2];
		if(letters > 3 || numbers > 2 || specials > 2){
			return true; // reject because too many of one type
		}
		else if(password.length() == 5 && (numbers == 0 || specials == 0)){
			return true; // reject because not enough of one type
		}
		else{
			String temp = password.toString();
			for(int i = 0; i < temp.length()-1; i++){
				if(dictionary.search(temp, i, temp.length())){
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Next the last character of a password in progress
	 * @param password The password in progress
	 * @param frequency An array of frequencies
	 * @return password or null
	 */
	public static StringBuilder next(StringBuilder password, int[] frequency){
		// make more efficient
		int letters = frequency[0];
		int numbers = frequency[1];
		int specials = frequency[2];
		StringBuilder temp = new StringBuilder(password);
		if(temp != null){
			int last = temp.length() - 1;
			int character = (int) temp.charAt(last);
			if(character != 42){
				if (character >= 48 && character <= 57){
					if(numbers <= 2){
						if(character + 1 == 49 || character + 1 == 52){
							character += 2;
						}
						else if(character == 57){
							character = 98; // set to b
						}
						else{
							character++;
						}
					}
					else if(letters <= 3){
						character = 98;
					}
					else if(specials <= 2){
						character = 33;
					}
					else{
						return null;
					}
				}
				else if(character >= 98 && character <= 122){
					if(numbers <= 3){
						if(character + 1 == 105){
							character += 2;
						}
						else if(character == 122){
							character = 33; // set to !
						}
						else{
							character++;
						}
					}
					else if(specials <= 2){
						character = 33;
					}
				}
				else{
					if(specials <= 2){
						switch(character){
							case 33: // !
								character = 64; // set to '@'
								break;
							case 64: // @
								character = 36; // set to '$'
								break;
							case 36: // $
								character = 94; // set to '^'
								break;
							case 94: // ^
								character++; // set to '_'
								break;
							case 95: // _
								character = 42; // set to '*'
								break;
						}
					}
					else{
						return null;
					}
				}
				temp.setCharAt(last, (char) character);
				return temp;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}
	
	/**
	 * Extend the password in progress
	 * @param password The password in progress
	 * @return password or null
	 */
	public static StringBuilder extend(StringBuilder password){
		// next character over
		StringBuilder temp = new StringBuilder(password);
		if(temp.length() < 5){
			temp.append('0');
			return temp;
		}
		else{
			return null; // cannot extend
		}
	}
	
	/**
	 * Return a count of the three (3) types of characters
	 * @param string The string to be looked
	 * @return An array of frequencies
	 */
	public static int[] frequencyOfType(StringBuilder string){
		int[] frequency = {0,0,0};
		for(int i = 0; i < string.length(); i++){
			int charInt = (int) string.charAt(i);
			if(charInt >= 98 && charInt <= 122){
				frequency[0]++; // count letter
			}
			else if(charInt >= 48 && charInt <= 57){
				frequency[1]++; // count number
			}
			else{
				frequency[2]++; // count special character
			}
		}
		return frequency;
	}
	
	/**
	 * Runs the user program
	 */
	public static void userProgram(){
		Scanner keyboard = new Scanner(System.in);
		DLB passwords = new DLB();
		char currentDLB = '\n';
		System.out.println("Welcome!" + 
						 "\nPassword Rules" + 
						 "\n\t* Exactly five (5) characters long" +
						 "\n\t* 1-3 characters must be letters [a-z]" +
						 "\n\t* 1-2 must be numbers [0-9]" +
						 "\n\t* 1-2 must be symbols (\'!\', \'@\'" +
						 ", \'$\', \'^\', \'_\', or \'*\')" +
						 "\n\t* Passwords are not case-sensitive" +
						 "\nTo exit the program, press the \'enter\' key prior to entering a character");
		String input = "";
		System.out.print("Please enter a 5 character password: ");
		input = (keyboard.nextLine()).toLowerCase();
		while(!input.equals("") && !input.equals("\n")){
			char character = input.charAt(0);
			if(currentDLB != character){
				passwords = openSave(character);
				currentDLB = characterCheck(character);
			}
			if(passwords.search(input)){
				System.out.println(input + " is a good password. Congratulations!");
			}
			else{
				StringBuilder prefix = passwords.prefix(input);
				System.out.println(input + " is not a valid password.");
				LinkedList<String> passwordList = new LinkedList<String>();
				LinkedList<String> usedPassword = new LinkedList<String>();
				System.out.println("10 closest passwords: ");
				int count = usedPassword.size();
				if(prefix.length() == 5){
					usedPassword.add(prefix.toString());
					count = usedPassword.size();
					System.out.println("\t" + count + ". " + prefix);
					prefix.deleteCharAt(prefix.length()-1);
				}
				// Slow but works
				while(count < 10){
					passwordList = passwords.prefixQuery(prefix.toString(), passwordList);
					Iterator<String> it = passwordList.getIterator();
					while(it.hasNext() && count < 10){
						String stringToCheck = it.next();
						if(!usedPassword.contains(stringToCheck)){
							usedPassword.add(stringToCheck);
							count = usedPassword.size();
							System.out.println("\t" + count + ". " + stringToCheck);
						}
					}
					if(prefix.length() >= 2){
						prefix = prefix.deleteCharAt(prefix.length() - 1);
					}
				}
			}
			System.out.print("Please enter a 5 character password: ");
			input = (keyboard.nextLine()).toLowerCase();
		}
		keyboard.close();
	}
	
	/**
	 * Checks if a file exists
	 * @param filename The name of the file
	 * @return True if the file exists and is a file
	 */
	public static boolean fileExists(String filename){
		File file = new File(filename);
		return file.exists() && file.isFile();
	}
	
	/**
	 * Write a group of passwords to a file based on starting character
	 * @param str The StringBuilder containing all good passwords
	 * @param startCharacter the starting character of passwords to save to file
	 */
	public static void save(StringBuilder str, char startCharacter){
		new File("../output").mkdir();
		String start = fileSpecifier(startCharacter);
		StringBuilder filename = new StringBuilder("../output/output-char-" + start + ".txt");
		StringBuilder toAppend = new StringBuilder();
		FileWriter file = null;
		for(int i = 0; i < str.length(); i += 6){
			if(str.charAt(i) == startCharacter){
				toAppend.append(str.substring(i, i + 6));
			}
		}
		try{
			file = new FileWriter(filename.toString());
			file.append(toAppend);
		}
		catch(IOException ioe){
			System.err.println("Error while saving to " + filename);
			ioe.printStackTrace();
		} finally {
			try{
				file.close();
			} catch (IOException ioe){
			
			}
		}
	}
	
	/**
	 * Generate a DLB from a saved file
	 * @param startCharacter the starting character of passwords of interest
	 * @return The DLB of passwords with that starting character
	 */
	public static DLB openSave(char startCharacter){
		String start = fileSpecifier(startCharacter);
		StringBuilder filename = new StringBuilder("../output/output-char-" + start + ".txt");
		BufferedReader file = null;
		DLB trie = new DLB();
		try{
			file = new BufferedReader(new FileReader(filename.toString()));
			String word = " ";
			while(word != null){
				word = file.readLine();
				trie.insert(word);
			}
		}
		catch(FileNotFoundException fnfe){
			System.err.println(filename + " does not exist.");
			return null;
		}
		catch(IOException ioe){
			System.err.println("Error resuming from " + filename);
			return null;
		} finally {
			try{
				file.close();
			} catch (IOException ioe){
			
			}
		}
		return trie;
	}
	
	/**
	 * Facilitates the checks for various start characters
	 * @param startCharacter the starting character of passwords of interest
	 * @return a String with the file name add on
	 */
	public static String fileSpecifier(char startCharacter){
		String string = new String();
		if(startCharacter == '!'){
			string = "exclamation";
		}
		else if(startCharacter == '@'){
			string = "atsymbol";
		}
		else if(startCharacter == '$'){
			string = "dollarsign";
		}
		else if(startCharacter == '^'){
			string = "carrot";
		}
		else if(startCharacter == '_'){
			string = "underscore";
		}
		else if(startCharacter == '*'){
			string = "asterisk";
		}
		else if((startCharacter >= 48 && startCharacter <= 57) || (startCharacter >= 97 && startCharacter <= 122)) {
			if(startCharacter == 'a' || startCharacter == 'i' ||
				startCharacter == '1' || startCharacter == '4'){
					startCharacter++;
			}				
			string += startCharacter;
		}
		else{
			string += '0';
		}
		return string;
	}
	public static char characterCheck(char character){
		if(character == '!'){
			// don't change character
		}
		else if(character == '@'){
			// don't change character
		}
		else if(character == '$'){
			// don't change character
		}
		else if(character == '^'){
			// don't change character
		}
		else if(character == '_'){
			// don't change character
		}
		else if(character == '*'){
			// don't change character
		}
		else if((character >= 48 && character <= 57) || (character >= 97 && character <= 122)) {
			if(character == 'a' || character == 'i' ||
				character == '1' || character == '4'){
					character++;
			}
		}
		else{
			character = '0';
		}
		return character;
	}
}
