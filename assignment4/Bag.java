import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag{
	private Node head;
	private int size;
	
	/**
	 * Constructs an empty bag
	 */
	public Bag(){
		head = null;
		size = 0;
	}
	
	/**
	 * Add an edge to the bag
	 * @param newItem The edge to add
	 * @return True if the add was successful
	 */
	public boolean add(Edge newItem){ // O(n) operation
		if(!contains(newItem.getVertexTwo())){
			Node temp = new Node(newItem);
			temp.setNext(head);
			head = temp;
			size++;
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if an edge to a vertex exists
	 * @param vertex The destination (second) vertex of an edge
	 * @return True if the Edge exists
	 */
	public boolean contains(int vertex){
		Node current = head;
		while(current != null){
			if(vertex == current.getData().getVertexTwo()) return true;
			current = current.getNext();
		}
		return false;
	}
	
	/**
	 * Remove an edge to the bag
	 * @param vertex The destination (second) vertex of an edge
	 * @return True if the remove was successful
	 */
	public boolean remove(int vertex){ // O(n) operation
		Node temp = getReference(vertex);
		if(temp != null){
			temp.setData(head.getData());
			head = head.getNext();
			size--;
			return true;
		}
		return false;
	}
	
	/**
	 * Get reference to an edge to the bag
	 * @param vertex The destination (second) vertex of an edge
	 * @return The node containing that edge
	 */
	public Node getReference(int vertex){
		Node current = head;
		while(current != null){
			if(vertex == current.getData().getVertexTwo()) return current;
			current = current.getNext();
		}
		return current;
	}
	
	/**
	 * Get reference to an edge to the bag using source
	 * @param vertex The source (first) vertex of an edge
	 * @return The node containing that edge
	 */
	public Node getSource(int vertex){
		Node current = head;
		while(current != null){
			if(vertex == current.getData().getVertexOne()) return current;
			current = current.getNext();
		}
		return current;
	}
	/**
	 * Get an edge in the bag
	 * @param node The node to get
	 * @return The Edge that was contained in the node
	 */
	public Edge getEdge(Node node){
		if(node != null) return node.getData();
		return null;
	}
	
	/**
	 * The size of the bag
	 * @return the size of the bag
	 */
	public int size(){
		return size;
	}
	
	/**
	 * Check if the bag is empty
	 * @return True if empty
	 */
	public boolean isEmpty(){
		return size == 0;
	}
	
	/**
	 * Get an iterator for the bag
	 * @return The iterator for the bag
	 */
	public Iterator getIterator(){
		return new BagIterator();
	}
	
	// Include iterator
	private class BagIterator implements Iterator{
		private Node next;
		
		public BagIterator(){
			next = head;
		}	
		
		public boolean hasNext(){
			return next != null;
		}
		
		public Edge next(){
			if(hasNext()){
				Edge temp = next.getData();
				next = next.getNext();
				return temp;
			}
			else throw new NoSuchElementException("No such element");
		}
		
		public void remove(){
			throw new UnsupportedOperationException("Remove is not supported");
		}
	}
}