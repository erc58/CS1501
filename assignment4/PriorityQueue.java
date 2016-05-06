import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class PriorityQueue{
	private final int INITIAL_SIZE = 16;
	private Edge[] pq;
	private HashMap<Integer, Integer> indexMap;
	private boolean attribute;
	private int size;	
	
	/**
	 * General Constructor
	 * @param initialSize The initial size of the PQ
	 */
	public PriorityQueue(int initialSize, boolean attribute){
		if (initialSize <= 0) throw new IllegalArgumentException("Initial size must be positive");
		pq = new Edge[initialSize];
		indexMap = new HashMap<Integer, Integer>(initialSize);
		this.attribute = attribute;
		size = 0;		
	}
	
	/**
	 * Check if the PQ is empty
	 * @return True if the PQ is empty
	 */
	public boolean isEmpty(){
		return size == 0;
	}
	
	/**
	 * Returns the number of elements in the PQ
	 * @return The size of the PQ
	 */	 
	public int size(){
		return size;
	}
	
	/**
	 * Inserts a new Car object into the PQ
	 * @param newCar the Car to be added to the PQ
	 * @throws IllegalArgumentException If a Car with the same VIN already exists
	 */
	public void insert(Edge newEdge){
		pq[size] = newEdge;
		indexMap.put(newEdge.getVertexTwo(), size);
		swim(size++); 
		if(arrayFull()){
			resize();
		}
	}
	
	/**
	 * Removes a specific Car from the PQ
	 * @param VIN The VIN of a Car object
	 */
	public int remove(){
		size--;
		exchange(0, size);
		Edge temp = pq[size];
		pq[size] = null;
		sink(0);
		indexMap.remove(temp.getVertexTwo());
		if(arrayQuarter()){
			shrink();
		}
		return temp.getVertexTwo();
	}
	
	public boolean contains(int vertex){
		return indexMap.containsKey(vertex);
	}
	
	public void update(int vertex, Edge e){
		int index = Integer.valueOf(indexMap.get(vertex));
		pq[index] = e;
		swim(index);
		sink(index);
	}
	
	/**
	 * Returns the Edge object with the minimum attribute
	 * @return The edge with the minimum attribute or null if PQ is empty
	 */
	public Edge getMin(){
		return pq[0];
	}
	
	
	
	/**
	 * Exchanges the object between two indexes
	 * @param firstIndex The first index
	 * @param secondIndex The second index
	 * @param heap The heap of Car objects
	 * @param index The HashMap of indexes
	 */
	private void exchange(int firstIndex, int secondIndex){
		Edge temp = pq[firstIndex];
		pq[firstIndex] = pq[secondIndex];
		pq[secondIndex] = temp;
		indexMap.replace(pq[firstIndex].getVertexTwo(), firstIndex);
		indexMap.replace(pq[secondIndex].getVertexTwo(), secondIndex);
	}
	
	/**
	 * Brings an Edge object up the heap based on price
	 * @param index The starting index
	 */
	private void swim(int index){
		while(index > 0 && lower(index, (index - 1)/2)){
			exchange(index, (index - 1)/2);
			index = (index - 1)/2;
		}
	}
	
	/**
	 * Brings an Edge object down the heap based on price
	 * @param index The starting index
	 */
	private void sink(int index){
		while(2 * index + 1 < size){			
			int child = 2 * index + 1;
			if (child < size && lower(child + 1, child)) child++;
			if (!lower(child, index)) break;
			exchange(index, child);
			index = child;
		}
	}
	
	/**
	 * Determines if a Car at one index has a lower price than a Car
	 * at another index
	 * @param firstIndex The first index
	 * @param secondIndex The second index
	 * @return True if the Car at firstIndex has a lower price than
	 * the Car at secondIndex
	 */
	private boolean lower(int firstIndex, int secondIndex){
		if(attribute){ // based on distance if true
			if(pq[firstIndex] == null || pq[secondIndex] == null) return false;
			return pq[firstIndex].getMiles() < pq[secondIndex].getMiles();
		}
		else{
			if(pq[firstIndex] == null || pq[secondIndex] == null) return false;
			return pq[firstIndex].getPrice() < pq[secondIndex].getPrice();
		}
	}	
		
	/**
	 * Doubles the available space of the heap
	 */
	private void resize(){
		pq = Arrays.copyOf(pq, 2 * pq.length);
	}
	
	/**
	 * Shrinks the heap in half
	 */
	private void shrink(){
		pq = Arrays.copyOf(pq, pq.length / 2);
	}
	
	/**
	 * Determines if the heap is full
	 * @return True if the heap is a full
	 */
	private boolean arrayFull(){
		return size == pq.length;
	}
	/**
	 * Determines if the heap is only a quarter full
	 * @return True if the heap is a quarter full
	 */
	private boolean arrayQuarter(){
		if(pq.length > INITIAL_SIZE) return size < (pq.length / 4);
		else return false;
	}
}