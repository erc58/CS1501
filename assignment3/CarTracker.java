import java.util.Scanner;
import java.text.Format;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class CarTracker{
	private static Scanner kb = new Scanner(System.in);
	public static void main(String[] args){
		PriorityQueue cars = new PriorityQueue();
		System.out.println("================================================\n"
	  + "=                   Welcome                    =\n"
	  + "================================================");
		mainMenu(cars);
		kb.close();
	}
	/**
	 * Controls the display of the Main Menu and takes user input
	 * @param cars The PQ of cars
	 */
	public static void mainMenu(PriorityQueue cars){
		System.out.print("------------------------------------------------\n"
					   + "                   Main Menu\n"
					   + "1.  Add a Car\n"
					   + "2.  Update a Car\n"
					   + "3.  Remove a Car\n"
					   + "4.  Lowest Price Car\n"
					   + "5.  Lowest Mileage Car\n"
					   + "6.  Lowest Price Car by Make and Model\n"
					   + "7.  Lowest Mileage Car by Make and Model\n"
					   + "0.  Exit\n"
					   + "Please enter: ");
		int choice = 0;
		try {
			choice = Integer.parseInt(kb.nextLine());
		} catch (IllegalArgumentException iae) {
		
		}
		mainMenuChoice(choice, cars);
	}
	
	/**
	 * Takes user input and determines what method to call based on
	 * that input
	 * @param choice The user's choice
	 * @param cars The PQ of cars
	 */
	private static void mainMenuChoice(int choice, PriorityQueue cars){
		if (choice == 0) System.exit(0);
		else if (choice == 1) addCar(cars);
		else if (choice == 2) updateCar(cars);
		else if (choice == 3) removeCar(cars);
		else if (choice == 4) lowestPrice(cars);
		else if (choice == 5) lowestMileage(cars);
		else if (choice == 6) lowestPriceMM(cars);
		else if (choice == 7) lowestMileageMM(cars);
		else System.exit(0);
	}
	
	/**
	 * Allows the user to add a car to the PQ
	 * @param cars The PQ of cars
	 */
	private static void addCar(PriorityQueue cars){
		while (true){
			System.out.println("------------------------------------------------\n");
			System.out.println("             Add Car            ");
			System.out.print("Enter VIN: ");
			String VIN = kb.nextLine();
			System.out.print("Enter make: ");
			String make = kb.nextLine();
			System.out.print("Enter model: ");
			String model = kb.nextLine();
			System.out.print("Enter price: ");
			double price = 0;
			try{
				price = Double.parseDouble(kb.nextLine());
			} catch (IllegalArgumentException iae) {
				System.out.println("Price defaulted to 0");
			}
			System.out.print("Enter mileage: ");
			double mileage = 0;
			try{
				mileage = Double.parseDouble(kb.nextLine());
			} catch (IllegalArgumentException iae) {
				System.out.println("Mileage defaulted to 0");
			}
			System.out.print("Enter color: ");
			String color = kb.nextLine();
			Car newCar = null;
			while(true){
				try{ 
					newCar = new Car(VIN, make, model, price, mileage, color);
					cars.insert(newCar);
					break;
				} catch (IllegalArgumentException iae) {
					System.out.println("You have entered a VIN that is invalid or already in use.");
					System.out.println("A valid VIN contains 17 characters that are upper case"
									+ "\nletters or numbers except I (i), O (o), or Q (q).");
					System.out.print("Enter VIN: ");
					VIN = kb.nextLine();
				}
			}
			System.out.println("Car with VIN " + VIN + " has been added");
			System.out.print("\nAdd another car? ");
			String answer = kb.nextLine().toLowerCase();
			if (answer.equals("yes") || answer.equals("y")) {}
			else break;
		}
		mainMenu(cars);
	}
	
	/**
	 * Allows the user to remove a car from the PQ
	 * @param cars The PQ of cars
	 */
	private static void removeCar(PriorityQueue cars){
		System.out.println("------------------------------------------------\n");
		System.out.println("           Remove Car           ");
		System.out.print("Enter VIN: ");
		String VIN = kb.nextLine();
		Car temp = cars.fetch(VIN);
		if (temp == null) System.out.println("No cars with VIN: " + VIN);
		else{
			System.out.println("Removing:\n" + temp);
			cars.remove(VIN);
		}
		mainMenu(cars);
	}
	
	/**
	 * Allows the user to update a car in the PQ
	 * @param cars The PQ of cars
	 */
	private static void updateCar(PriorityQueue cars){
		System.out.println("------------------------------------------------\n");
		System.out.println("           Update Car           ");
		System.out.print("Please enter the VIN of the car you \nwish to update: ");
		String VIN = kb.nextLine().toUpperCase();
		updateCarMenu(VIN, cars);
	}
	
	/**
	 * Allows the user to choose an attribute of a car to update
	 * @param VIN The VIN of the car to update
	 * @param cars The PQ of cars
	 */
	private static void updateCarMenu(String VIN, PriorityQueue cars){
		// fetch car
		Car temp = cars.fetch(VIN);
		if(temp == null) { 
			System.out.println("No cars match the VIN: " + VIN);
			System.out.println();
			mainMenu(cars);
		}
		else{
			System.out.println(temp);
			System.out.print("1.  Change price\n"
						   + "2.  Change mileage\n"
						   + "3.  Change color\n"
						   + "0.  Main Menu\n"
						   + "Please enter: ");	
			int choice = 0;
			try{
				choice = Integer.parseInt(kb.nextLine());
			} catch (IllegalArgumentException iae) {
				
			}
			updateCarChoice(choice, temp, cars);
		}
	}
	
	/**
	 * Takes user input and allows the user to update that attribute
	 * @param choice The user's choice
	 * @param car The car to update
	 * @param cars The PQ of cars
	 */
	private static void updateCarChoice(int choice, Car car, PriorityQueue cars){
		if (choice == 0) mainMenu(cars);
		else if (choice == 1) {
			NumberFormat dollars = new DecimalFormat("#0.00");
			System.out.println("VIN: " + car.getVIN() + "\nCurrent Price: $" + dollars.format(car.getPrice()));
			System.out.print("Please enter new price: ");
			double newPrice = 0;
			try{
				newPrice = Double.parseDouble(kb.nextLine());
			} catch (IllegalArgumentException iae) {
				newPrice = car.getPrice(); // No change made
			}
			cars.updatePrice(car.getVIN(), newPrice);
		}
		else if (choice == 2) {
			NumberFormat mileage = new DecimalFormat("#0.0");
			System.out.println("VIN: " + car.getVIN() + "\nCurrent Mileage: " + mileage.format(car.getMileage()));
			System.out.print("Please enter new mileage: ");
			double newMileage = 0;
			try{
				newMileage = Double.parseDouble(kb.nextLine());
			} catch (IllegalArgumentException iae) {
				newMileage = car.getMileage(); // No change made
			}
			cars.updateMileage(car.getVIN(), newMileage);
		}
		else if (choice == 3) {
			System.out.println("VIN: " + car.getVIN() + "\nCurrent Color: " + car.getColor());
			System.out.print("Please enter new color: ");
			String newColor = kb.nextLine();
			cars.updateColor(car.getVIN(), newColor);
		}
		else mainMenu(cars);
		updateCarMenu(car.getVIN(), cars);
	}
	
	/**
	 * Displays the lowest priced car
	 * @param cars The PQ of cars
	 */
	private static void lowestPrice(PriorityQueue cars){
		System.out.println("------------------------------------------------\n");
		System.out.println("        Lowest Price Car        ");
		Car temp = cars.getMinPrice();
		if (temp == null) System.out.println("\nThere are no cars in the system.\n");
		else System.out.println("\n" + temp + "\n");
		mainMenu(cars);
	}
	
	/**
	 * Displays the car with the lowest mileage
	 * @param cars The PQ of cars
	 */
	private static void lowestMileage(PriorityQueue cars){
		System.out.println("------------------------------------------------\n");
		System.out.println("       Lowest Mileage Car       ");
		Car temp = cars.getMinMileage();
		if (temp == null) System.out.println("\nThere are no cars in the system.\n");
		else System.out.println("\n" + temp + "\n");
		mainMenu(cars);
	}
	
	/**
	 * Displays the lowest priced car by make and model
	 * @param cars The PQ of cars
	 */
	private static void lowestPriceMM(PriorityQueue cars){
		System.out.println("------------------------------------------------\n");
		System.out.println("        Lowest Price Car by Make and Model");
		while(true){
			System.out.print("Please enter the make: ");
			String make = kb.nextLine().toUpperCase();
			System.out.print("Please enter the model: ");
			String model = kb.nextLine().toUpperCase();
			Car temp = cars.getMinPriceMM(make, model);
			if (temp == null) System.out.println("\nThere are no " + make + " " + model + " vehicles found.");
			else System.out.println("\n" + temp);
			System.out.println();
			System.out.print("Would you like to search by another make and model? ");
			String answer = kb.nextLine().toLowerCase();
			if (answer.equals("yes") || answer.equals("y")) {}
			else break;
		}
		mainMenu(cars);
	}
	
	/**
	 * Displays the car with the lowest mileage by make and model
	 * @param cars The PQ of cars
	 */
	private static void lowestMileageMM(PriorityQueue cars){
		System.out.println("------------------------------------------------\n");
		System.out.println("       Lowest Mileage Car by Make and Model");
		while(true){
			System.out.print("Please enter the make: ");
			String make = kb.nextLine().toUpperCase();
			System.out.print("Please enter the model: ");
			String model = kb.nextLine().toUpperCase();
			Car temp = cars.getMinMileageMM(make, model);
			if (temp == null) System.out.println("\nThere are no " + make + " " + model + " vehicles found.");
			else System.out.println("\n" + temp);
			System.out.println();
			System.out.print("Would you like to search by another make and model? ");
			String answer = kb.nextLine().toLowerCase();
			if (answer.equals("yes") || answer.equals("y")) {}
			else break;
		}
		mainMenu(cars);
	}
}