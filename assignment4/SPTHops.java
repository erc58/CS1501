import java.util.Iterator;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

public class SPTHops{
	private boolean[] marked;
	private Edge[] edgeTo;
	private int[] distTo;
	
	/**
	 * Construct an SPT
	 * @param g The graph
	 * @param source The source vertex
	 */
	public SPTHops(Graph g, int source){
		marked = new boolean[g.numberOfVertices()];
		edgeTo = new Edge[g.numberOfVertices()];
		distTo = new int[g.numberOfVertices()];
		bfs(g, source);
	}
	
	/**
	 * Uses a bfs traversal to find shortest path based on hops to all 
	 * connected vertices
	 * @param g The graph
	 * @param source The source vertex
	 */
	private void bfs(Graph g, int source){
		Queue<Edge> queue = new LinkedList<Edge>();
		for(int i = 0; i < distTo.length; i++) distTo[i] = Integer.MAX_VALUE;
		distTo[source] = 0;
		marked[source] = true;
		queue.add(new Edge(source, source, 0, 0));
		
		while(!queue.isEmpty()) {
			int v = queue.poll().getVertexTwo();
			Iterator adj = g.adj(v);
			while (adj.hasNext()) {
				Edge e = (Edge) adj.next();
				int w = e.getVertexTwo();
				if (!marked[w]) {
					edgeTo[w] = e;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					queue.add(e);
				}
			}
		}
	}
	
	/**
	 * Get the distance to a vertex from the source
	 * @return The distance
	 */
	public int distTo(int v){
		return distTo[v];
	}
	
	/**
	 * Checks if there is a path to a vertex from the source
	 * @return True if a path exists
	 */
	public boolean hasPathTo(int v){
		return marked[v];
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