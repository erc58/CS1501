import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MySign{
	private static final String PUBLIC_KEY_FILENAME = "pubkey.rsa";
	private static final String PRIVATE_KEY_FILENAME = "privkey.rsa";
	
	public static void main(String[] args){
		if (args.length == 2) {
			String flag = args[0];
			String filename = args[1];
			if (flag.equals("s")) {
				try {
					sign(filename);
				} catch (FileNotFoundException fnfe) {
					System.out.println(fnfe.getMessage());
				}
			}
			else if (flag.equals("v")){
				try { 
					verify(filename);
				} catch (FileNotFoundException fnfe) {
					System.out.println(fnfe.getMessage());
				}
			}
			else throw new IllegalArgumentException("User did not choose either 's' or 'v'.");
		}	
		else throw new IllegalArgumentException("The expected arguments were not provided.");
	}
	
	/**
	 * Signs a file using a private key
	 * @param filename The filename of the file to sign
	 * @throws FileNotFoundException if privkey.rsa does not exist
	 */
	public static void sign(String filename) throws FileNotFoundException {
		BigInteger D, N, hash, decrypt;
		ObjectInputStream priv;
		ObjectOutputStream output;
		MessageDigest md;
		Path path;
		byte[] data, digest;
		String outputFilename = filename + ".signed";
		if (checkFile(PRIVATE_KEY_FILENAME)) {
			try {
				priv = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILENAME));
				D = (BigInteger) priv.readObject();
				N = (BigInteger) priv.readObject();
				md = MessageDigest.getInstance("SHA-256");
				path = Paths.get(filename);
				data = Files.readAllBytes(path);
				md.update(data);
				digest = md.digest();
				hash = new BigInteger(1, digest);
				decrypt = hash.modPow(D, N);
				output = new ObjectOutputStream(new FileOutputStream(outputFilename));
				output.writeObject(data);
				output.writeObject(decrypt);
			} catch (NoSuchFileException nsfe) {
				System.out.println("The file, " + filename + ", does not exist.");
				System.exit(0);
			} catch (ClassCastException cce) {
				cce.printStackTrace(); 
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (NoSuchAlgorithmException nsae) {
				nsae.printStackTrace();
			}
		}
		else throw new FileNotFoundException(PRIVATE_KEY_FILENAME + " not found in the current directory.");
	}
	
	/**
	 * Verifies that a provided file is properly signed
	 * @param filename The filename of a SIGNED file (*.signed)
	 * @throws FileNotFoundException if pubkey.rsa does not exist
	 */
	public static void verify(String filename) throws FileNotFoundException{
		BigInteger E, N, hash, decrypted, encrypt;
		ObjectInputStream pub, signed;
		MessageDigest md;
		byte[] data, digest;
		if (checkFile(PUBLIC_KEY_FILENAME)) {
			try {
				pub = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILENAME));
				E = (BigInteger) pub.readObject();
				N = (BigInteger) pub.readObject();
				md = MessageDigest.getInstance("SHA-256");
				signed = new ObjectInputStream(new FileInputStream(filename));
				data = (byte[]) signed.readObject();
				decrypted = (BigInteger) signed.readObject();
				md.update(data);
				digest = md.digest();
				hash = new BigInteger(1, digest);
				encrypt = decrypted.modPow(E, N);
				if(hash.equals(encrypt)) System.out.println("The signature is valid.");
				else System.out.println("The signature is invalid.");
			} catch (ClassCastException cce) {
				cce.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} catch (StreamCorruptedException sce) {
				System.out.println(filename + " is not a SIGNED file.");
				System.exit(0);
			} catch (NoSuchFileException nsfe) {
				System.out.println("The file, " + filename + ", does not exist.");
				System.exit(0);
			} catch (FileNotFoundException fnfe) {
				System.out.println("The file, " + filename + ", does not exist.");
				System.exit(0);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (NoSuchAlgorithmException nsae) {
				nsae.printStackTrace();
			}
		}
		else throw new FileNotFoundException(PUBLIC_KEY_FILENAME + " not found in the current directory.");
	}
	
	/**
	 * Performs a quick check of a file's existence
	 * @param filename The name of the file
	 * @return True if the file exists.
	 */
	public static boolean checkFile(String filename){
		File f = new File(filename);
		return f.exists() && !f.isDirectory();
	}
	
}