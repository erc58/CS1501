import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class PriorityQueue{
	private final int INITIAL_SIZE = 16;
	private Car[] pqPrice;
	private Car[] pqMileage;
	private HashMap<String, Integer> indexPrice;
	private HashMap<String, Integer> indexMileage;
	private int size;
	// Need to figure out updating part
	
	/**
	 * No Args Constructor
	 */
	public PriorityQueue(){
		this(16);
	}
	
	/**
	 * General Constructor
	 * @param initialSize The initial size of the PQ
	 */
	public PriorityQueue(int initialSize){
		if (initialSize <= 0) throw new IllegalArgumentException("Initial size must be positive");
		pqPrice = new Car[initialSize];
		pqMileage = new Car[initialSize];
		indexPrice = new HashMap<String, Integer>(initialSize);
		indexMileage = new HashMap<String, Integer>(initialSize);
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
	public void insert(Car newCar){
		if (indexPrice.containsKey(newCar.getVIN())) throw new IllegalArgumentException(indexPrice.get(newCar.getVIN()).toString());
		pqPrice[size] = newCar;
		pqMileage[size] = newCar;
		indexPrice.put(newCar.getVIN(), size);
		indexMileage.put(newCar.getVIN(), size);
		swimPrice(size); 
		swimMileage(size++);
		if(arrayFull()){
			resize();
		}
	}
	
	/**
	 * Removes a specific Car from the PQ
	 * @param VIN The VIN of a Car object
	 */
	public void remove(String VIN){
		VIN = VIN.toUpperCase();
		boolean contains = indexPrice.containsKey(VIN);
		if(contains) {
			int priceIndex = indexPrice.get(VIN).intValue();
			size--;
			exchange(priceIndex, size , pqPrice, indexPrice);
			Car temp1 = pqPrice[priceIndex];
			Car temp2 = pqPrice[size];
			pqPrice[size] = null;
			indexPrice.remove(VIN);
			swimPrice(priceIndex);
			sinkPrice(priceIndex);
			int mileageIndex = indexMileage.get(VIN).intValue();
			exchange(mileageIndex, size, pqMileage, indexMileage);
			pqMileage[size] = null;
			indexMileage.remove(VIN);
			swimMileage(mileageIndex);
			sinkMileage(mileageIndex);
			if(arrayQuarter()){
				shrink();
			}
		}
	}
	
	/**
	 * Returns the Car object with the minimum price
	 * @return The car with the lowest price or null if PQ is empty
	 */
	public Car getMinPrice(){
		return pqPrice[0];
	}
	
	/**
	 * Returns the Car object with the minimum mileage
	 * @return The car with the lowest mileage or null if PQ is empty
	 */
	public Car getMinMileage(){
		return pqMileage[0];
	}	
	
	/**
	 * Returns the Car object with the minimum price by make and
	 * model
	 * @param make The make of a Car object
	 * @param model The model of a Car object
	 * @return The car with the lowest price by make and model
	 * or null if not found
	 */
	public Car getMinPriceMM(String make, String model){
		return searchPrice(0, make.toUpperCase(), model.toUpperCase());
	}
	
	/**
	 * Returns the Car object with the minimum mileage by make and
	 * model
	 * @param make The make of a Car object
	 * @param model The model of a Car object
	 * @return The car with the lowest mileage by make and model
	 * or null if not found
	 */
	public Car getMinMileageMM(String make, String model){
		return searchMileage(0, make.toUpperCase(), model.toUpperCase());
	}
	
	/**
	 * Searches for the minimum priced car by make and model
	 * @param parent The current index
	 * @param make The make of a Car object
	 * @param model The model of a Car object
	 * @return The car with the lowest price by make and model
	 * or null if not found
	 */	 
	private Car searchPrice(int parent, String make, String model){
		if(parent >= size) return null;
		if(pqPrice[parent] == null) return null;
		else if(make.equals(pqPrice[parent].getMake()) && model.equals(pqPrice[parent].getModel())) return pqPrice[parent];
		else {
			Car left = searchPrice(2 * parent + 1, make, model);
			Car right = searchPrice(2 * parent + 2, make, model);
			if (left == null && right == null) return null;
			else if (right == null) return left;
			else if (left == null) return right;
			else{
				if(left.getPrice() < right.getPrice()) return left;
				else return right;
			}
		}
	}
	
	/**
	 * Searches for the minimum mileage car by make and model
	 * @param parent The current index
	 * @param make The make of a Car object
	 * @param model The model of a Car object
	 * @return The car with the lowest mileage by make and model
	 * or null if not found
	 */	 
	private Car searchMileage(int parent, String make, String model){
		if (parent >= size) return null;
		if(pqMileage[parent] == null) return null;
		else if(make.equals(pqMileage[parent].getMake()) && model.equals(pqMileage[parent].getModel())) return pqMileage[parent];
		else {
			Car left = searchMileage(2 * parent + 1, make, model);
			Car right = searchMileage(2 * parent + 2, make, model);
			if (left == null && right == null) return null;
			else if (right == null) return left;
			else if (left == null) return right;
			else{
				if(left.getMileage() < right.getMileage()) return left;
				else return right;
			}
		}
	}
	
	/**
	 * Exchanges the object between two indexes
	 * @param firstIndex The first index
	 * @param secondIndex The second index
	 * @param heap The heap of Car objects
	 * @param index The HashMap of indexes
	 */
	private void exchange(int firstIndex, int secondIndex, Car[] heap, HashMap<String, Integer> index){
		Car temp = heap[firstIndex];
		heap[firstIndex] = heap[secondIndex];
		heap[secondIndex] = temp;
		index.replace(heap[firstIndex].getVIN(), Integer.valueOf(firstIndex));
		index.replace(heap[secondIndex].getVIN(), Integer.valueOf(secondIndex));
	}
	
	/**
	 * Brings a Car object up the heap based on price
	 * @param index The starting index
	 */
	private void swimPrice(int index){
		while(index > 0 && lowerPrice(index, (index - 1)/2)){
			exchange(index, (index - 1)/2, pqPrice, indexPrice);
			index = (index - 1)/2;
		}
	}
	
	/**
	 * Brings a Car object up the heap based on mileage
	 * @param index The starting index
	 */
	private void swimMileage(int index){
		while(index > 0 && lowerMileage(index, (index - 1)/2)){
			exchange(index, (index - 1)/2, pqMileage, indexMileage);
			index = (index - 1)/2;
		}
	}
	
	/**
	 * Brings a Car object down the heap based on price
	 * @param index The starting index
	 */
	private void sinkPrice(int index){
		while(2 * index + 1 < size){			
			int child = 2 * index + 1;
			if (child < size && lowerPrice(child + 1, child)) child++;
			if (!lowerPrice(child, index)) break;
			exchange(index, child, pqPrice, indexPrice);
			index = child;
		}
	}
	
	/**
	 * Brings a Car object down the heap based on mileage
	 * @param index The starting index
	 */
	private void sinkMileage(int index){
		while(2 * index + 1 < size){			
			int child = 2 * index + 1;
			if (child < size && lowerMileage(child + 1, child)) child++;
			if (!lowerMileage(child, index)) break;
			exchange(index, child, pqMileage, indexMileage);
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
	private boolean lowerPrice(int firstIndex, int secondIndex){
		if(pqPrice[firstIndex] == null || pqPrice[secondIndex] == null) return false;
		return pqPrice[firstIndex].getPrice() < pqPrice[secondIndex].getPrice();
	}
	
	/**
	 * Determines if a Car at one index has a lower mileage than a Car
	 * at another index
	 * @param firstIndex The first index
	 * @param secondIndex The second index
	 * @return True if the Car at firstIndex has a lower mileage than
	 * the Car at secondIndex
	 */
	private boolean lowerMileage(int firstIndex, int secondIndex){
		if(pqMileage[firstIndex] == null || pqMileage[secondIndex] == null) return false;
		return pqMileage[firstIndex].getMileage() < pqMileage[secondIndex].getMileage();
	}
	
	// Helper method to only be used with fetch(String id)
	private int getIndex(String id, HashMap<String, Integer> hashMap){
		Integer intIndex = hashMap.get(id);
		int index = -1;
		if (intIndex != null) index = intIndex.intValue();
		return index;
	}
	
	/**
	 * Fetches a Car based on its VIN
	 * @param VIN The VIN of the car
	 * @return The Car object or null
	 */
	public Car fetch(String VIN){
		int index = getIndex(VIN, indexPrice);
		if(index > -1) return pqPrice[index];
		else return null;
	}
	
	/**
	 * Updates the price of a specific car
	 * @param id The VIN of the Car to be updated
	 * @param price The price to be set
	 */
	public void updatePrice(String id, double price){
		int index = getIndex(id, indexPrice);
		if(index > -1){
			pqPrice[index].setPrice(price); // setPrice may throw an exception
			swimPrice(index);
			sinkPrice(index);
		}
	}
	
	/**
	 * Updates the mileage of a specific car
	 * @param id The VIN of the Car to be updated
	 * @param mileage The mileage to be set
	 */
	public void updateMileage(String id, double mileage){
		int index = getIndex(id, indexMileage);
		if(index > -1){
			pqMileage[index].setMileage(mileage); // setMileage may throw an exception
			swimMileage(index);
			sinkMileage(index);
		}
	}
	
	/**
	 * Updates the color of a specific car
	 * @param id The VIN of the Car to be updated
	 * @param color The color to be set
	 */
	public void updateColor(String id, String color){
		int index = getIndex(id, indexPrice);
		if(index > -1){
			pqPrice[index].setColor(color); // should update color in pqMileage as well
		}
	}
	
	/**
	 * Doubles the available space of the heap
	 */
	private void resize(){
		pqPrice = Arrays.copyOf(pqPrice, 2 * pqPrice.length);
		pqMileage = Arrays.copyOf(pqMileage, 2 * pqMileage.length);
	}
	
	/**
	 * Shrinks the heap in half
	 */
	private void shrink(){
		pqPrice = Arrays.copyOf(pqPrice, pqPrice.length / 2);
		pqMileage = Arrays.copyOf(pqMileage, pqMileage.length / 2);
	}
	
	/**
	 * Determines if the heap is full
	 * @return True if the heap is a full
	 */
	private boolean arrayFull(){
		return size == pqPrice.length;
	}
	/**
	 * Determines if the heap is only a quarter full
	 * @return True if the heap is a quarter full
	 */
	private boolean arrayQuarter(){
		if(pqPrice.length > INITIAL_SIZE) return size < (pqPrice.length / 4);
		else return false;
	}
	
	public static void main(String[] args){
		PriorityQueue cars = new PriorityQueue();
		Car fordFocus = new Car("AAAAAAAAAAAAAAAA1", "Ford", "Focus", 23454, 3463, "Blue");
		Car audiA6 = new Car("AAAAAAAAAAAAAAAA2", "Audi", "A6", 56000, 23000, "Grey");
		Car mustang = new Car("AAAAAAAAAAAAAAAA3", "Ford", "Mustang", 5000, 23454, "Yellow");
		Car mustang2 = new Car("AAAAAAAAAAAAAAAA5", "Ford", "Mustang", 49000, 38900, "Red");
		Car honda = new Car("AAAAAAAAAAAAAAAA4", "Honda", "Accord", 22000, 15432, "Teal");
		Car fordFocus2 = new Car("AAAAAAAAAAAAAAAB1", "Ford", "Focus", 23454, 3463, "Blue");
		Car audiA62 = new Car("AAAAAAAAAAAAAAAB2", "Audi", "A6", 56000, 23000, "Grey");
		Car mustang3 = new Car("AAAAAAAAAAAAAAAB3", "Ford", "Mustang", 5000, 23454, "Yellow");
		Car mustang4 = new Car("AAAAAAAAAAAAAAAB5", "Ford", "Mustang", 49000, 15500, "Red");
		Car honda2 = new Car("AAAAAAAAAAAAAAAB4", "Honda", "Accord", 22999, 30000, "Black");
		Car fordFocus3 = new Car("AAAAAAAAAAAAAAAC1", "Ford", "Focus", 23454, 3463, "Silver");
		Car audiA63 = new Car("AAAAAAAAAAAAAAAC2", "Audi", "A6", 43500, 73000, "White");
		Car mustang5 = new Car("AAAAAAAAAAAAAAAC3", "Ford", "Mustang", 50000, 23322, "Yellow");
		Car mustang6 = new Car("AAAAAAAAAAAAAAAC5", "Ford", "Mustang", 35000, 50000, "Red");
		Car honda3 = new Car("AAAAAAAAAAAAAAAC4", "Honda", "Accord", 3293, 14201, "Teal");
		Car vwRabbit = new Car("AAAAAAAAAAAAAAAD4", "Volkswagon", "Rabbit", 12900, 35001, "Black");
		Car vwRabbit2 = new Car("AAAAAAAAAAAAAAAD2", "Volkswagon", "Rabbit", 10293, 32194, "Black");
		Car vwRabbit3 = new Car("AAAAAAAAAAAAAAAD9", "Volkswagon", "Rabbit", 15392, 67859, "White");
		Car vwRabbit4 = new Car("AAAAAAAAAAAAAAAD6", "Volkswagon", "Rabbit", 9859, 45000, "Black");
		Car vwRabbit5 = new Car("AAAAAAAAAAAAAAAD1", "Volkswagon", "Rabbit", 14500, 23009, "Blue");
		Car vwRabbit6 = new Car("AAAAAAAAAAAAAAD10", "Volkswagon", "Rabbit", 11320, 17859, "Red");
		Car vwRabbit7 = new Car("AAAAAAAAAAAAAAAD7", "Volkswagon", "Rabbit", 15697, 39865, "Green");
		cars.insert(fordFocus);
		cars.insert(mustang);
		cars.insert(audiA6);
		cars.insert(honda);
		cars.insert(vwRabbit);
		cars.insert(vwRabbit2);
		cars.insert(vwRabbit3);
		cars.insert(vwRabbit4);
		cars.insert(vwRabbit5);
		cars.insert(vwRabbit6);
		cars.insert(vwRabbit7);
		cars.insert(mustang2);
		cars.insert(fordFocus2);
		cars.insert(mustang3);
		cars.insert(audiA62);
		cars.insert(honda2);
		cars.insert(mustang4);
		cars.insert(fordFocus3);
		cars.insert(mustang5);
		cars.insert(audiA63);
		cars.insert(honda3);
		cars.insert(mustang6);
		System.out.println("Lowest Price: \n" + cars.getMinPrice());
		System.out.println("Lowest Mileage: \n" + cars.getMinMileage());
		System.out.println("Lowest Price (Honda Accord): \n" + cars.getMinPriceMM("Honda", "Accord"));
		System.out.println("Lowest Mileage (Audi A6): \n" + cars.getMinMileageMM("Audi", "A6"));
		cars.remove(honda.getVIN());
		cars.remove(vwRabbit2.getVIN());
		cars.remove(honda3.getVIN());
		cars.remove(mustang5.getVIN());
		cars.remove(fordFocus.getVIN());
		cars.remove(audiA6.getVIN());
		cars.remove(mustang.getVIN());
		cars.remove(mustang4.getVIN());
		cars.remove(vwRabbit.getVIN());
		cars.remove(audiA62.getVIN());
		cars.remove(fordFocus3.getVIN());
		cars.remove(honda2.getVIN());
		cars.remove(mustang6.getVIN());
		cars.remove(vwRabbit7.getVIN());
		cars.remove(vwRabbit3.getVIN());
		cars.remove(fordFocus2.getVIN());
		cars.remove(vwRabbit4.getVIN());
		cars.remove(vwRabbit6.getVIN());
		System.out.println("Lowest Price: \n" + cars.getMinPrice());
		System.out.println("Lowest Mileage: \n" + cars.getMinMileage());
		Car temp1 = cars.getMinPriceMM("Honda", "Accord"); 
		if (temp1 != null) System.out.println("Lowest Price (Honda Accord): \n" + temp1);
		else System.out.println("There are no Honda Accords");
		System.out.println("Lowest Mileage (Ford Mustang): \n" + cars.getMinMileageMM("Audi", "A6"));
	}
}