import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashMap;

public class Airline{
	private static Scanner kb = new Scanner(System.in);
	private static NumberFormat dollars = new DecimalFormat("#0.00");
	public static void main(String[] args){
		System.out.println("-----------------------------------------------------------\n"
						 + "	                 Airline Program\n"
						 + "-----------------------------------------------------------\n\n");
		System.out.print("Please enter a filename: ");
		String filename = kb.nextLine();
		BufferedReader inputFile = getFile(filename);
		if(inputFile == null){
			System.out.println("The file " + filename + " does not exist. Airline program is terminating.");
			System.exit(0);
		}
		Graph routes = getGraph(inputFile); 
		while (true){
			System.out.println("-----------------------------------------------------------\n"
							 + "                        Main Menu                          \n"
							 + "-----------------------------------------------------------\n"
							 + "1. All Direct Routes\n"
							 + "2. Minimum Spanning Tree based on distances\n"
							 + "3. Shortest Path\n"
							 + "4. All trips less than specific amount\n"
							 + "5. Add a new route\n"
							 + "6. Remove a route\n"
							 + "7. Exit");
			System.out.print("User choice: ");
			int choice = 7; // initialize to exit value
			try {
			choice = Integer.parseInt(kb.nextLine());
			if (choice < 1 || choice > 7) throw new IllegalArgumentException();
			} catch (IllegalArgumentException iae) {
			choice = 7;
			}
			menuChoice(choice, filename, routes);
		}
	}
	
	public static BufferedReader getFile(String filename){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(filename));
		} catch (IOException ioe){
			System.out.println("The file, " + filename + ", could not be found.");
		}
		return br;
	}
	
	public static Graph getGraph(BufferedReader br){
		int V = 0;
		try {
			V = Integer.parseInt(br.readLine());
		} catch (IOException ioe) {
			System.out.println("IOException in assignment of V");
		}
		String[] cities = new String[V];
		for (int i = 0; i < V; i++) { 
			try {
				cities[i] = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IOException in for-loop");
			}
		}
		Graph temp = new Graph(V, cities);
		Scanner remainderOfFile = new Scanner(br);
		while (remainderOfFile.hasNextLine() && remainderOfFile.hasNextInt()) {
			int vertexOne = remainderOfFile.nextInt() - 1;
			int vertexTwo = remainderOfFile.nextInt() - 1; 
			int mileage = remainderOfFile.nextInt();
			double price = remainderOfFile.nextDouble();
			temp.addEdge(vertexOne, vertexTwo, mileage, price);
		}
		remainderOfFile.close();
		try {
			br.close();
		} catch (IOException ioe) {
			System.out.println("IOException on closing file.");
		}
		return temp; // return the Graph
	}

	public static void menuChoice(int choice, String filename, Graph routes){ // pass Graph object
		if (choice == 1) listDirectRoutes(routes);
		else if (choice == 2) minimumSpanningTree(routes);
		else if (choice == 3) shortestPathMenu(routes);
		else if (choice == 4) superSaver(routes);
		else if (choice == 5) addRoute(routes);
		else if (choice == 6) removeRoute(routes);
		else saveAndExit(filename, routes);
	}
	
	public static void listDirectRoutes(Graph routes){// pass Graph object
		System.out.println("-----------------------------------------------------------\n"
						 + "                    All Direct Routes\n"
						 + "-----------------------------------------------------------");
		routes.displayGraph();
		System.out.println();
	}
	
	public static void minimumSpanningTree(Graph routes){// pass Graph object
		System.out.println("-----------------------------------------------------------\n"
						 + "                 Minimum Spanning Tree\n"
						 + "-----------------------------------------------------------");
		routes.getMST();
		System.out.println();		
	}
	
	public static void shortestPathMenu(Graph routes){// pass Graph object
		System.out.println("-----------------------------------------------------------\n"
						 + "                    Shortest Path                           ");
		boolean keepRunning = true;
		while(keepRunning){
			System.out.println("-----------------------------------------------------------\n"
							 + "1. By Distance\n"
							 + "2. By Price\n"
							 + "3. By Hops\n"
							 + "4. Main Menu");
			System.out.print("User choice: ");
			int choice = 4; // initialize to exit value
			try {
				choice = Integer.parseInt(kb.nextLine());
				if (choice < 1 || choice > 4) throw new IllegalArgumentException();
			} catch (IllegalArgumentException iae) {
				choice = 4;
			}
			keepRunning = shortestPathMenuChoice(routes, choice);
		}
		
	}
	public static boolean shortestPathMenuChoice(Graph routes, int choice){
		if (choice == 1) {
			shortestPathDistance(routes);
			return true;
		}
		else if (choice == 2) {
			shortestPathPrice(routes);
			return true;
		}
		else if (choice == 3) {
			shortestPathHops(routes);
			return true;
		}
		else return false;
	}
	
	public static void shortestPathDistance(Graph routes){
		String[] cities = routes.getVertices();
		int V = cities.length;
		for(int i = 0; i < V; i++){
			if(i % 2 == 0 && i > 0) System.out.println();
			System.out.print((i+1) + ". " + cities[i] + "\t\t");
		}
		HashMap<String, Integer> cityMap = new HashMap<String, Integer>(V);
		for(int i = 0; i < V; i++) cityMap.put(cities[i], i);
		System.out.println();
		System.out.print("Please enter a source: ");
		int vertexOne, vertexTwo;
		String source = kb.nextLine();
		if(cityMap.containsKey(source)){
			vertexOne = Integer.valueOf(cityMap.get(source));
		}
		else{
			System.out.println("Invalid input. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		System.out.print("Please enter a destination: ");
		String destination = kb.nextLine();
		if(cityMap.containsKey(destination)){
			vertexTwo = Integer.valueOf(cityMap.get(destination));
		}
		else{
			System.out.println("Invalid input. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		if(vertexTwo == vertexOne){
			System.out.println("The source and destination vertices may not be the same. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		SPT paths = routes.shortestPathDistance(vertexOne);
		Iterable<Edge> path = paths.pathTo(vertexTwo);
		if(path != null) { 
			System.out.println("The shortest path by distance from "
								+ cities[vertexOne] + " to " 
								+ cities[vertexTwo] + ": " 
								+ ((int)paths.distTo(vertexTwo))
								+ " miles\n"
								+ "-----------------------------------------------------------");
			System.out.print("(Reverse order) " + cities[vertexTwo] + " ");
		
			for(Edge e : path){
				System.out.print(e.getMiles() + " " + cities[e.getVertexOne()] + " ");
			}
			System.out.println();
		}
		else System.out.println("There is no path from "
								+ cities[vertexOne] + " to "
								+ cities[vertexTwo] + ".");
		System.out.println();
	}
	
	public static void shortestPathPrice(Graph routes){
		String[] cities = routes.getVertices();
		int V = cities.length;
		for(int i = 0; i < V; i++){
			if(i % 2 == 0 && i > 0) System.out.println();
			System.out.print((i+1) + ". " + cities[i] + "\t\t");
		}
		HashMap<String, Integer> cityMap = new HashMap<String, Integer>(V);
		for(int i = 0; i < V; i++) cityMap.put(cities[i], i);
		System.out.println();
		System.out.print("Please enter a source: ");
		int vertexOne, vertexTwo;
		String source = kb.nextLine();
		if(cityMap.containsKey(source)){
			vertexOne = Integer.valueOf(cityMap.get(source));
		}
		else{
			System.out.println("Invalid input. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		System.out.print("Please enter a destination: ");
		String destination = kb.nextLine();
		if(cityMap.containsKey(destination)){
			vertexTwo = Integer.valueOf(cityMap.get(destination));
		}
		else{
			System.out.println("Invalid input. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		if(vertexTwo == vertexOne){
			System.out.println("The source and destination vertices may not be the same. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		SPT paths = routes.shortestPathPrice(vertexOne);
		Iterable<Edge> path = paths.pathTo(vertexTwo);
		if(path != null) { 
			System.out.println("The shortest path by price from "
								+ cities[vertexOne] + " to " 
								+ cities[vertexTwo] + ": $" 
								+ dollars.format(paths.distTo(vertexTwo))
								+ "\n-----------------------------------------------------------");
			System.out.print("(Reverse order) " + cities[vertexTwo] + " ");
		
			for(Edge e : path){
				System.out.print("$" + dollars.format(e.getPrice()) + " " + cities[e.getVertexOne()] + " ");
			}
			System.out.println();
		}
		else System.out.println("There is no path from "
								+ cities[vertexOne] + " to "
								+ cities[vertexTwo] + ".");
		System.out.println();
	}
	
	public static void shortestPathHops(Graph routes){
		String[] cities = routes.getVertices();
		int V = cities.length;
		for(int i = 0; i < V; i++){
			if(i % 2 == 0 && i > 0) System.out.println();
			System.out.print((i+1) + ". " + cities[i] + "\t\t");
		}
		HashMap<String, Integer> cityMap = new HashMap<String, Integer>(V);
		for(int i = 0; i < V; i++) cityMap.put(cities[i], i);
		System.out.println();
		System.out.print("Please enter a source: ");
		int vertexOne, vertexTwo;
		String source = kb.nextLine();
		if(cityMap.containsKey(source)){
			vertexOne = Integer.valueOf(cityMap.get(source));
		}
		else{
			System.out.println("Invalid input. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		System.out.print("Please enter a destination: ");
		String destination = kb.nextLine();
		if(cityMap.containsKey(destination)){
			vertexTwo = Integer.valueOf(cityMap.get(destination));
		}
		else{
			System.out.println("Invalid input. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		if(vertexTwo == vertexOne){
			System.out.println("The source and destination vertices may not be the same. Returning to Shortest Path Menu.");
			System.out.println();
			return;
		}
		SPTHops paths = routes.shortestPathHops(vertexOne);
		Iterable<Edge> path = paths.pathTo(vertexTwo);
		if(path != null) { 
			System.out.println("The shortest path by hops from "
								+ cities[vertexOne] + " to " 
								+ cities[vertexTwo] + ": " 
								+ paths.distTo(vertexTwo)
								+ " hops\n"
								+ "-----------------------------------------------------------");
			System.out.print("(Reverse order) " + cities[vertexTwo]);
		
			for(Edge e : path){
				System.out.print(" " + cities[e.getVertexOne()]);
			}
			System.out.println();
		}
		else System.out.println("There is no path from "
								+ cities[vertexOne] + " to "
								+ cities[vertexTwo] + ".");
		System.out.println();
	}
	
	public static void superSaver(Graph routes){// pass Graph object
		System.out.println("-----------------------------------------------------------\n"
						 + "                    Super Saver\n"
						 + "-----------------------------------------------------------");
		System.out.print("Please enter a price (in dollars): ");
		double price = Double.parseDouble(kb.nextLine());
		System.out.println("\nAll routes under $" + dollars.format(price) + " from source to destination:\n-----------------------------------------------------------");
		String[] cities = routes.getVertices();	
		int V = cities.length;
		double priceSum;
		Stack<Edge> path;
		boolean[] marked;
		for (int i = 0; i < V; i++){
			marked = new boolean[V];
			priceSum = 0.0;
			path = new Stack<Edge>();
			path.push(new Edge(i, i, 0, 0.0));
			marked[i] = true;
			SuperSaver(routes, path,i, i, priceSum, price, marked);
		}
	}
	
	public static void SuperSaver(Graph routes, Stack<Edge> path, int source, int vertex, double priceSum, double price, boolean[] marked){
		if(priceSum <= price && priceSum != 0.0){
			@SuppressWarnings("unchecked")
			Stack<Edge> temp = (Stack<Edge>) path.clone();
			String[] cities = routes.getVertices();
			System.out.print("Cost: $" + dollars.format(priceSum) + "; " + cities[source]);
			for(Edge e : temp){
				if(e.getPrice() != 0.0)System.out.print(" $" + dollars.format(e.getPrice()) + " " + cities[e.getVertexTwo()]);
			}
			System.out.println();
			System.out.println();
		}
		Iterator adj = routes.adj(vertex);
		while(adj.hasNext()){
			Edge e = (Edge) adj.next();
			int v = e.getVertexTwo();
			if(!marked[v]){
				boolean[] newMarked = Arrays.copyOf(marked, marked.length);
				newMarked[v] = true;
				@SuppressWarnings("unchecked")
				Stack<Edge> newPath = (Stack<Edge>) path.clone();
				newPath.push(e);
				double newPriceSum = priceSum + e.getPrice();
				SuperSaver(routes, newPath, source, v, newPriceSum, price, newMarked);
			}
		}
	}
	
	public static void addRoute(Graph routes){// pass Graph object
		System.out.println("-----------------------------------------------------------\n"
						 + "                    Add a Route\n"
						 + "-----------------------------------------------------------");
		String[] cities = routes.getVertices();
		int V = cities.length;
		for(int i = 0; i < V; i++){
			if(i % 2 == 0 && i > 0) System.out.println();
			System.out.print((i+1) + ". " + cities[i] + "\t\t");
		}
		HashMap<String, Integer> cityMap = new HashMap<String, Integer>(V);
		for(int i = 0; i < V; i++) cityMap.put(cities[i], i);
		System.out.println();
		System.out.print("Please enter a source: ");
		int vertexOne, vertexTwo;
		String source = kb.nextLine();
		if(cityMap.containsKey(source)){
			vertexOne = Integer.valueOf(cityMap.get(source));
		}
		else{
			System.out.println("Invalid input. Returning to Main Menu.");
			System.out.println();
			return;
		}
		System.out.print("Please enter a destination: ");
		String destination = kb.nextLine();
		if(cityMap.containsKey(destination)){
			vertexTwo = Integer.valueOf(cityMap.get(destination));
		}
		else{
			System.out.println("Invalid input. Returning to Main Menu.");
			System.out.println();
			return;
		}
		if(vertexTwo == vertexOne){
			System.out.println("The source and destination vertices may not be the same.");
			System.out.println();
			return;
		}
		System.out.print("Please enter the mileage: ");
		int dist = Integer.parseInt(kb.nextLine());
		System.out.print("Please enter the price: ");
		double price = Double.parseDouble(kb.nextLine());
		if (routes.addEdge(vertexOne, vertexTwo, dist, price))
			System.out.println("Successfully added a route from "
							   + cities[vertexOne] + " to " 
							   + cities[vertexTwo] +".\n");
		else 
			System.out.println("Unable to add a route from "
							   + cities[vertexOne] + " to " 
							   + cities[vertexTwo] +" because one already exists.\n");
	}
	
	public static void removeRoute(Graph routes){// pass Graph object
		System.out.println("-----------------------------------------------------------\n"
						 + "                    Remove a Route\n"
						 + "-----------------------------------------------------------");
		String[] cities = routes.getVertices();
		int V = cities.length;
		for(int i = 0; i < V; i++){
			if(i % 2 == 0 && i > 0) System.out.println();
			System.out.print((i+1) + ". " + cities[i] + "\t\t");
		}
		HashMap<String, Integer> cityMap = new HashMap<String, Integer>(V);
		for(int i = 0; i < V; i++) cityMap.put(cities[i], i);
		System.out.println();
		System.out.print("Please enter a source: ");
		int vertexOne, vertexTwo;
		String source = kb.nextLine();
		if(cityMap.containsKey(source)){
			vertexOne = Integer.valueOf(cityMap.get(source));
		}
		else{
			System.out.println("Invalid input. Returning to Main Menu.");
			System.out.println();
			return;
		}
		System.out.print("Please enter a destination: ");
		String destination = kb.nextLine();
		if(cityMap.containsKey(destination)){
			vertexTwo = Integer.valueOf(cityMap.get(destination));
		}
		else{
			System.out.println("Invalid input. Returning to Main Menu.");
			System.out.println();
			return;
		}
		if(vertexTwo == vertexOne){
			System.out.println("The source and destination vertices may not be the same.");
			System.out.println();
			return;
		}
		if (routes.removeEdge(vertexOne, vertexTwo))
			System.out.println("Successfully removed the route from "
							   + cities[vertexOne] + " to " 
							   + cities[vertexTwo] +".\n");
		else 
			System.out.println("Unable to remove the route from "
							   + cities[vertexOne] + " to " 
							   + cities[vertexTwo] +" because one does not exist.\n");
	}
	
	public static void saveAndExit(String filename, Graph routes){// pass Graph object and filename
		PrintWriter output = getOutput(filename);
		if(output != null) {
			int V = routes.numberOfVertices();
			output.print(V);
			String[] vertices = routes.getVertices();
			for(String s : vertices){
				output.println();
				output.print(s);
			}
			for(int i = 0; i < V; i++){
				Iterator it = routes.adj(i);
				while(it.hasNext()){
					Edge temp = (Edge) it.next();
					if (temp.getVertexTwo() > i) {
						output.println();
						output.print((temp.getVertexOne() + 1) + " "
									 + (temp.getVertexTwo() + 1) 
									 + " " + temp.getMiles()
									 + " " + dollars.format(temp.getPrice()));
					}
				}
			}
			output.close();
			System.out.println("Data successfully outputted to " + filename);
		}
		System.exit(0);
	}
	
	public static PrintWriter getOutput(String filename){
		PrintWriter output = null;
		try {
			output = new PrintWriter(new FileWriter(filename));
		} catch (IOException ioe) {
		
		}
		return output;
	}
	
}