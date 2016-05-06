public class Node{
	private Edge data;
	private Node next;
	
	/**
	 * Constructs a node
	 * @param data The edge
	 */
	public Node(Edge data){
		this.data = data;
	}
	
	/**
	 * Sets the data of the node
	 * @param data The edge
	 */
	public void setData(Edge data){
		this.data = data;
	}
	
	/**
	 * Sets the next Node
	 * @param next The next node
	 */
	public void setNext(Node next){
		this.next = next;
	}
	
	/**
	 * Returns the data stored in the node
	 * @return The Edge stored in the node
	 */
	public Edge getData(){
		return data;
	}
	
	/**
	 * Returns the next node
	 * @return the next node
	 */
	public Node getNext(){
		return next;
	}
}