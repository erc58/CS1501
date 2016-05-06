public class Edge{
	private int vertexOne;
	private int vertexTwo;
	private int miles;
	private double price;
	
	/**
	 * Constructs 
	 * @param vertexOne The first vertex
	 * @param vertexTwo The second vertex
	 * @param miles The distance weight between the two vertices in miles
	 * @param price The price weight between the two vertices
	 */
	public Edge(int vertexOne, int vertexTwo, int miles, double price){
		this.vertexOne = vertexOne;
		this.vertexTwo = vertexTwo;
		this.miles = miles;
		this.price = price;
	}
	
	/**
	 * Returns the first vertex in the edge
	 * @return the first vertex in the edge
	 */
	public int getVertexOne(){
		return vertexOne;
	}
	
	/**
	 * Returns the second vertex in the edge
	 * @return the second vertex in the edge
	 */
	public int getVertexTwo(){
		return vertexTwo;
	}
	
	/**
	 * Returns the distance weight for the edge
	 * @return the distance weight for the edge
	 */
	public int getMiles(){
		return miles;
	}
	
	/**
	 * Returns the price weight for the edge
	 * @return the price weight for the edge
	 */
	public double getPrice(){
		return price;
	}	
}