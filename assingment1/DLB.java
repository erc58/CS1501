/**
 *@author Charles Kelly
 *@version 1.0
 *Last update: 11 Feb 16
 *de la Briandais Trie Implementation
 */
public class DLB{
	private Node root;
	private final char SENTINEL = '?';
	
	public DLB(){
		root = new Node();
	}
	/**
	 * Inserts a user-specified key and value into the DLB Trie. If
	 * the key already exists, data will not be overwritten.
	 * @param key Key to be added to the DLB Trie
	 * @param value The value to be associated with that key
	 */
	public void insert(String key){
		if(key != null){
			Node temp = this.root;
			for(int i = 0; i <= key.length(); i++){
				char character;
				if(i < key.length()){
					character = key.charAt(i);
				}
				else{
					character = SENTINEL;
				}
				if(temp.next == null){
					temp.next = new Node(character);
					temp = temp.next;
				}
				else{
					temp = temp.next;
					while(temp != null){
						if(temp.value.equals(character)){
							break;
						}
						else if(temp.sibling == null){
							temp.sibling = new Node(character);
							temp = temp.sibling;
							break;
						}
						else{
							temp = temp.sibling;
						}
					}
				}
			}
		}
	}
	/**
	 * Searches the trie for a substring of a key
	 * @param key The whole string
	 * @param start the inclusive starting index
	 * @param end the exclusive ending index
	 * @return True if found
	 */
	public boolean search(String key, int start, int end){
		assert(start > -1 && end < key.length() && start <= end);
		key = key.substring(start, end);
		return search(key);
	}
	/**
	 * Searches the trie for a key
	 * @param key The word being searched for
	 * @return True if found
	 */
	public boolean search(String key){
		if(root.next != null){
			Node temp = root.next;
			for(int i = 0; i <= key.length(); i++){
				char character;
				if(i < key.length()){
					character = key.charAt(i);
				}
				else{
					character = SENTINEL;
				}
				if(temp != null){
					temp = find(temp, character);
					if(temp == null){
						return false;
					}
					else{
						temp = temp.next;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Searches the level for a specific character
	 * @param start The starting node
	 * @param character The character being looked up
	 * @return The node containing that character or null
	 */
	public Node find(Node start, char character){
		Node temp = start;
		while(temp != null){
			if(temp.equals(character)){
				return temp;
			}
			else{
				temp = temp.sibling;
			}
		}
		return null;
	}
	
	/**
	 * Finds the longest prefix of a string
	 * @param key The string to examine
	 * @return the prefix
	 */
	public StringBuilder prefix(String key){
		StringBuilder string = new StringBuilder();
		Node node = root.next;
		for(int i = 0; i < key.length() && string.length() <= 5; i++){
			char character = key.charAt(i);
			node = find(node, character);
			if(node != null){
				string.append(character);
				node = node.next;
			}
			else{
				break;
			}
		}
		return string;
	}
	
	/**
	 * Gives the node containing the last character of a prefix
	 * @param prefix The prefix to look for
	 * @return the node of the last character from the prefix or null
	 */
	private Node prefixSearch(String prefix){
		Node node = root;
		int i = 0;
		while(node.next != null && i < prefix.length()){
			node = node.next;
			node = find(node, prefix.charAt(i));
			i++;
		}
		return node;
	}
	
	/**
	 * Creates a list of all words that branch off from this prefix
	 * @param prefix The prefix
	 * @return List of words with that prefix
	 */
	public LinkedList<String> prefixQuery(String prefix, LinkedList<String> list){
		StringBuilder prefixBuilder = new StringBuilder(prefix);
		return prefixQuery(prefixBuilder, list);
	}
	
	/**
	 * Creates a list of all words that branch off from this prefix
	 * @param prefix The prefix
	 * @return List of words with that prefix
	 */
	private LinkedList<String> prefixQuery(StringBuilder prefix, LinkedList<String> list){
		Node node = prefixSearch(prefix.toString());
		StringBuilder temp;
		Node tempNode = new Node();
		if(node != null){
			if(isEndOfWord(node)){
				list.add(prefix.toString());
			}
			if(list.size() >= 10){
				return list;
			}
			if(node.next.getValue() == (Character) '?'){
				temp = next(prefix, tempNode);
				tempNode = node.sibling;
			}
			else if(node.sibling != null && prefix.length() < 5){
				temp = extend(prefix, node);
				tempNode = node.next;
			}
			else if(node.sibling == null && prefix.length() < 5){
				temp = extend(prefix, node);
				tempNode = node.next;
			}
			else{
				temp = null;
				tempNode = null;
			}
			while(temp != null && tempNode != null){
				list = prefixQuery(temp, list);
				temp = next(temp, tempNode);
				tempNode = tempNode.sibling;
			}			
		}
		return list;
	}
	
	/**
	 * Checks if the currentNode's next node is a sentinel value
	 * @param currentNode The currentNode
	 * @return True if sentinel value is reached
	 */
	private boolean isEndOfWord(Node currentNode){
		return find(currentNode.next, SENTINEL) != null;
	}
	
	/**
	 * Extends a StringBuilder
	 * @param word The StringBuilder to be extended
	 * @param node The current node
	 * @return The word or null
	 */
	private StringBuilder extend(StringBuilder word, Node node){
		Node currentNode = new Node(node);
		StringBuilder temp = new StringBuilder(word);
		if(currentNode == null){
			return null;
		}
		if(!isEndOfWord(currentNode)){
			temp.append(currentNode.next.value);
			return temp;
		}
		return null;
	}
	
	/**
	 * Changes the last letter of a StringBuilder
	 * @param word The StringBuilder to be extended
	 * @param node The current node
	 * @return The word or null
	 */
	private StringBuilder next(StringBuilder word, Node node){
		Node currentNode = new Node(node);
		StringBuilder temp = new StringBuilder(word);
		if(currentNode == null){
			return null;
		}
		if(currentNode.sibling != null){
			currentNode = currentNode.sibling;
			int last = temp.length() - 1;
			temp.setCharAt(last, currentNode.value);
			return temp;
		}
		return null;
	}
	
	/**
	 * @author Charles Kelly
	 * @version 1.0
	 * DLB Node class
	 */
	private class Node{
		private Character value;
		private Node sibling;
		private Node next;
		
		public Node(){
			value = null;
			sibling = null;
			next = null;
		}
		
		public Node(char character){
			value = (Character) character;
			sibling = null;
			next = null;
		}
		
		/** 
		 * Copy COnstructor
		 * @param node The node to be copied
		 */
		public Node(Node node){
			setValue(node.getValue());
			setSibling(node.getSibling());
			setNext(node.getNext());			
		}
		
		public void setValue(Character character){
			this.value = character;
		}
		public void setSibling(Node sibling){
			this.sibling = sibling;
		}
		public void setNext(Node next){
			this.next = next;
		}
		
		public Character getValue(){
			if(value == null){
				return null;
			}
			return new Character(value);
		}
		public Node getSibling(){
			if(sibling == null){
				return null;
			}
			return new Node(sibling);
		}
		public Node getNext(){
			if(next == null){
				return null;
			}
			return new Node(next);
		}
		/**
		 * Compares the equality of this node to a character
		 * @return True if equality
		 */
		public boolean equals(char character){
			return value == (Character) character;
		}
		
		
	}
}