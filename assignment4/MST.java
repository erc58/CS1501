import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

public class MST{
	private Edge[] edgeTo;
	private int[] distTo;
	private boolean[] marked;
	private PriorityQueue pq;
	private HashMap<Integer, Integer> sourceDest;
	private LinkedList<Integer> source;
	
	/**
	 * Creates a MST given a Graph
	 * @param g The graph
	 */
	public MST(Graph g) {
		int V = g.numberOfVertices();
		edgeTo = new Edge[V];
		distTo = new int[V];
		marked = new boolean[V];
		sourceDest = new HashMap<Integer, Integer>(V);
		source = new LinkedList<Integer>();
		for(int i = 0; i < distTo.length; i++) distTo[i] = Integer.MAX_VALUE;		
		for (int i = 0; i < V; i++) {
			pq = new PriorityQueue(g.numberOfVertices(), true);
			if(!marked[i]){
				source.add(i);
				distTo[i] = 0;
				edgeTo[i] = new Edge(i, i, 0, 0);
				pq.insert(edgeTo[i]);
				while(!pq.isEmpty()) {
					int dest = pq.remove();
					sourceDest.put(dest, i);
					visit(g, dest);
				}
			}
		}
	}
	
	/**
	 * Visits a vertex and adds its adjacent vertices to the pq
	 * if the those adjacent vertices haven't been visited
	 * @param g The graph
	 * @param vertex The current vertex
	 */
	private void visit(Graph g, int vertex){
		marked[vertex] = true;
		Iterator adj = g.adj(vertex);
		while(adj.hasNext()){
			Edge e = (Edge) adj.next();
			int w = e.getVertexTwo();
			if (marked[w]) continue;
			if (e.getMiles() < distTo[w]) {
				edgeTo[w] = e;
				distTo[w] = e.getMiles();
				if(pq.contains(w)) pq.update(w, e);
				else pq.insert(e);
			}
		}
	}
	
	/**
	 * Returns an iterable object that represents the MST
	 * @return The iterable MST
	 */
	public void edges(Graph g){
		String[] cities = g.getVertices();
		@SuppressWarnings("unchecked")
		LinkedList<Edge>[] mst = new LinkedList[source.size()];
		for(int i = 0; i < mst.length; i++) mst[i] = new LinkedList<Edge>();
		for(int i = 0; i < edgeTo.length; i++){
			if(edgeTo[i].getVertexOne() != i){
				int s = sourceDest.get(i);
				int index = source.indexOf(s);
				mst[index].add(edgeTo[i]);
			}
		}
		for(int i = 0; i < mst.length; i++){
			System.out.println("MST of connected component " + (i + 1));
			LinkedList<Edge> temp = mst[i];
			if(temp.isEmpty()){
				int s = source.get(i);
				System.out.println(cities[s] + ": 0 miles");
			}
			for(Edge e : temp){
				int s = e.getVertexOne();
				int d = e.getVertexTwo();
				System.out.println(cities[s] + ", " + cities[d] + ": "
								  + e.getMiles() + " miles");
			}
			System.out.println();
		}
	}
}