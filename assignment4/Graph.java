import java.util.Arrays;
import java.util.Iterator;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class Graph{
	private final int V;
	private int E;
	private String[] vertices;
	private Bag[] adj;
	private static final NumberFormat dollars = new DecimalFormat("#0.00");
	
	/**
	 * Constructor
	 * @param V number of vertices
	 */
	public Graph(int V){
		this.V = V;
		E = 0;
		vertices = new String[V];
		adj = new Bag[V];
		for(int i = 0; i < V; i++){
			adj[i] = new Bag();
		}
	}
	
	/**
	 * Constructor
	 * @param V number of vertices
	 * @param vertices String[] of vertices
	 */
	public Graph(int V, String[] vertices){
		this.V = V;
		E = 0;
		if(vertices.length == V) this.vertices = vertices;
		else this.vertices = new String[V];
		adj  = new Bag[V];
		for(int i = 0; i < V; i++){
			adj[i] = new Bag();
		}
	}
	
	/**
	 * Returns the number of vertices in the graph
	 * @return the number of vertices
	 */
	public int numberOfVertices(){
		return V;
	}
	
	/**
	 * Returns an iterator to iterator over the edges in the adjacency
	 * list
	 * @return The iterator of Edge objects
	 */
	public Iterator adj(int vertex){
		if(vertex < 0 || vertex >= V) throw new IndexOutOfBoundsException();
		else return adj[vertex].getIterator();
	}
	
	/**
	 * Returns the String array of vertex names
	 * @return a copy of the String[]
	 */
	public String[] getVertices(){
		return Arrays.copyOf(vertices, V);
	}
	
	/**
	 * Add an edge to the graph
	 * @param vertexOne The first vertex
	 * @param vertexTwo The second vertex
	 * @param miles The distance in miles between the two vertices
	 * @param price The price to go between the two vertices
	 * @return True if the edge was added.
	 */
	public boolean addEdge(int vertexOne, int vertexTwo, int miles, double price){
		if(vertexOne == vertexTwo) return false; // prevent self edges
		Edge edge = new Edge(vertexOne, vertexTwo, miles, price);
		Edge edgeReverse = new Edge(vertexTwo, vertexOne, miles, price);
		adj[vertexOne].add(edge);
		return adj[vertexTwo].add(edgeReverse);
	}
	
	/**
	 * Remove an edge from the graph
	 * @param vertexOne The first vertex
	 * @param vertexTwo The second vertex
	 * @return True if the remove was successful
	 * @throws IndexOutOfBoundsException If either vertex is not between 0 and V - 1
	 */
	public boolean removeEdge(int vertexOne, int vertexTwo){
		boolean result = false;
		if(vertexOne < 0 || vertexOne >= V) throw new IndexOutOfBoundsException();
		if(vertexTwo < 0 || vertexTwo >= V) throw new IndexOutOfBoundsException();
		if(adj[vertexOne] != null) result = adj[vertexOne].remove(vertexTwo);
		if(adj[vertexTwo] != null) result = adj[vertexTwo].remove(vertexOne);
		return result;
	}
	
	/**
	 * Displays the direct routes in the graph
	 */
	public void displayGraph(){
		for (int i = 0; i < V; i++){
			Iterator currentBagIterator = adj[i].getIterator();
			while(currentBagIterator.hasNext()){
				Edge nextEdge = (Edge) currentBagIterator.next();
				if(nextEdge.getVertexTwo() > i) { 
					System.out.println(vertices[i] + " to " + vertices[nextEdge.getVertexTwo()] + ": " + nextEdge.getMiles() + " miles, $" + dollars.format(nextEdge.getPrice()));
				}
			}
		}
	}
	
	/** 
	 * Prints out the minimum spanning tree for the graph
	 */
	public void getMST(){
		MST minimumSpanningTree = new MST(this); 
		minimumSpanningTree.edges(this);
		
	}
	
	/**
	 * Returns a shortest path tree for the graph from a source based on
	 * distance
	 * @param source The starting vertex in the tree
	 * @return The SPT based on distance
	 */
	public SPT shortestPathDistance(int source){
		return new SPT(this, source, true);
	}
	
	/**
	 * Returns a shortest path tree for the graph from a source based on
	 * price
	 * @param source The starting vertex in the tree
	 * @return The SPT based on price
	 */
	public SPT shortestPathPrice(int source){
		return new SPT(this, source, false);
	}
	
	/**
	 * Returns a shortest path tree for the graph from a source based on
	 * hops
	 * @param source The starting vertex in the tree
	 * @return The SPT based on hops
	 */
	public SPTHops shortestPathHops(int source){
		return new SPTHops(this, source);
	}
	
}
