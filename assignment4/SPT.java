import java.util.Iterator;
import java.util.Stack;
public class SPT{
	private final boolean type;
	private Edge[] edgeTo;
	private double[] distTo; // int may be cast to double
	private PriorityQueue pq;
	
	/**
	 * Construct an SPT
	 * @param g The graph
	 * @param vertex The source vertex
	 * @param type True if based on distance, false if based on price
	 */
	public SPT(Graph g, int vertex, boolean type){
		edgeTo = new Edge[g.numberOfVertices()];
		distTo = new double[g.numberOfVertices()];
		this.type = type;
		for(int i = 0; i < distTo.length; i++) distTo[i] = Double.POSITIVE_INFINITY;
		pq = new PriorityQueue(g.numberOfVertices(), type);
		distTo[vertex] = 0.0;
		pq.insert(new Edge(vertex, vertex, 0, 0));
		while(!pq.isEmpty()){
			relax(g, pq.remove());
		}
	}
	
	/**
	 * Using a vertex, see if a path to its adjacent vertices creates
	 * a shorter path than currently exists.
	 * @param g The graph
	 * @param vertex The current vertex
	 */
	private void relax(Graph g, int vertex){
		Iterator adj = g.adj(vertex);
		Edge e = null;
		while(adj.hasNext()){
			e = (Edge) adj.next();
			int w = e.getVertexTwo();
			if (type) { // distance
				if(distTo[w] > distTo[vertex] + e.getMiles()) {
					distTo[w] = distTo[vertex] + e.getMiles();
					edgeTo[w] = e;
					if(pq.contains(w)) pq.update(w, e);
					else pq.insert(e);
				}
			}
			else { // price
				if(distTo[w] > distTo[vertex] + e.getPrice()) {
					distTo[w] = distTo[vertex] + e.getPrice();
					edgeTo[w] = e;
					if(pq.contains(w)) pq.update(w, e);
					else pq.insert(e);
				}
			}
		}
	}
	
	/**
	 * Get the distance to a vertex from the source
	 * @return The distance
	 */
	public double distTo(int v){
		return distTo[v];
	}
	
	/**
	 * Checks if there is a path to a vertex from the source
	 * @return True if a path exists
	 */
	public boolean hasPathTo(int v){
		return distTo[v] < Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Return the path from source to a specified vertex
	 * @return An iterable object representing the path, or Null if no path exists
	 */
	public Iterable<Edge> pathTo(int v){
		if(!hasPathTo(v)) return null;
		Stack<Edge> path = new Stack<Edge>();
		for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.getVertexOne()]) path.push(e);
		return path;
	}
}