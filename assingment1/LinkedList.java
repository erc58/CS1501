import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * @author Charles Kelly
 * @version 1.0
 * LinkedList<T> class
 */
public class LinkedList<T>{
	private Node head;
	private int size;
	
	public LinkedList(){
		head = null;
		size = 0;
	}
	public LinkedList(T newObject){
		head = new Node(newObject);
		size = 1;
	}
	/**
	 * Adds an object to a list
	 * @param newObject Object to be added
	 * @return True if add was successful
	 */
	public boolean add(T newObject){
		if(size == 0){
			head = new Node(newObject);
			size++;
			return true;
		}
		else if(!contains(newObject)){
			Node newNode = new Node(newObject);
			newNode.next = head;
			head = newNode;
			size++;
			return true;
		}
		return false;
	}
	/**
	 * Checks if an object is in the list
	 * @param object Object to be looked for
	 * @return True if object was found
	 */
	public boolean contains(T object){
		Node temp = head;
		while(temp != null){
			if(temp.value.equals(object)){
				return true;
			}
			temp = temp.next;
		}
		return false;
	}
	/**
	 * Finds the node that an item is located
	 * @param object Object to be looked for
	 * @return the node if found or null
	 */
	public Node find(T object){
		Node temp = head;
		while(temp != null){
			if(temp.value.equals(object)){
				return temp;
			}
			temp = temp.next;
		}
		return null;
	}
	/**
	 * Removes an object from a list
	 * @param object Object to be removed
	 * @return True if remove was successful
	 */
	public boolean remove(T object){
		Node temp = head;
		boolean found = false;
		if(!isEmpty()){
			while(!found && temp != null){
				if(temp.value.equals(object)){
					found = true;					
				}
				else{
					temp = temp.next;
				}
			}
			if(found){
				temp.value = head.value;
				head = head.next;
				size--;
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	/**
	 * Checks if list is empty
	 * @return True if empty
	 */
	public boolean isEmpty(){
		return size == 0;
	}
	/**
	 * Returns the size of the list
	 * @return The size of the list
	 */
	public int size(){
		return size;
	}
	/**
	 * Clears the list
	 */
	public void clear(){
		head = null;
	}
	/**
	 * Get an iterator for this list
	 * @return an iterator
	 */
	public Iterator<T> getIterator(){
		return new LLIterator();
	}
	private class LLIterator implements Iterator<T>{
		private Node nextNode;
		
		public LLIterator(){
			nextNode = head;
		}
		/**
		 * Sees if the iterator has another item
		 * @return True if there is another item
		 */
		public boolean hasNext(){
			return nextNode != null;
		}
		/**
		 * Gives the next item
		 * @return The next object
		 */
		public T next(){
			if(hasNext()){
				T temp = nextNode.value;
				nextNode = nextNode.next;
				return temp;
			}
			else{
				throw new NoSuchElementException("Index out of bounds.");
			}
		}
		/**
		 * Removes the last item that was returned
		 * @throws java.lang.UnsupportedOperationException if this method is called
		 */
		public void remove(){
			throw new java.lang.UnsupportedOperationException();
		}
	}
	private class Node{
		private T value;
		private Node next;
					
		public Node(T value){
			this.value = value;
			next = null;
		}
	}
}