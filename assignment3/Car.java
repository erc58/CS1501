import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.Format;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class Car {
	private String VIN;
	private String make;
	private String model;
	private double price;
	private double mileage;
	private String color;
	
	// Constructor
	/**
	 * Constructor
	 * @param VIN The Vehicle ID of the car
	 * @param make The make of the car
	 * @param model The model of the car
	 * @param price The price of the car in dollars
	 * @param mileage The mileage on the car
	 * @param color The color of the car
	 */
	public Car(String VIN, String make, String model, double price, double mileage, String color){
		setVIN(VIN);
		setMake(make);
		setModel(model);
		setPrice(price);
		setMileage(mileage);
		setColor(color);
	}
	
	// Setters
	/**
	 * Sets the car's VIN
	 * @param VIN The Vehicle ID of the car
	 * @throws IllegalArgumentException If the VIN is invalid.
	 */
	public void setVIN(String VIN){
		VIN = VIN.toUpperCase();
		Pattern pattern = Pattern.compile("[A-Z0-9&&[^IOQ]]{17}");
		Matcher m = pattern.matcher(VIN);
		if(VIN.length() != 17) throw new IllegalArgumentException("VIN must contain 17 characters exactly");
		if(!m.matches()) throw new IllegalArgumentException("VIN may not contain I (i), O (o), or Q (q)");
		else this.VIN = VIN;
		// add more VIN checks
	}
	
	/**
	 * Sets the car's make
	 * @param make The make of the car
	 */
	public void setMake(String make){
		this.make = make.toUpperCase();
	}
	
	/**
	 * Sets the car's model
	 * @param model The model of the car
	 */
	public void setModel(String model){
		this.model = model.toUpperCase();
	}
	
	/**
	 * Sets the car's price
	 * @param price The price of the car
	 */
	public void setPrice(double price){
		if(price < 0) this.price = 0;
		else this.price = price;
	}
	
	/**
	 * Sets the car's mileage
	 * @param mileage The mileage on the car
	 */
	public void setMileage(double mileage){
		if (mileage < 0) this.mileage = 0;
		else this.mileage = mileage;
	}
	
	/**
	 * Sets the car's color
	 * @param color The color of the car
	 */
	public void setColor(String color){
		this.color = color.toUpperCase();
	}
	
	// Getters
	
	/**
	 * Gets the car's VIN
	 * @return The VIN of the car
	 */
	public String getVIN(){
		return VIN;
	}	
	
	/**
	 * Gets the car's make
	 * @return The make of the car
	 */
	public String getMake(){
		return make;
	}
	
	/**
	 * Gets the car's model
	 * @return The model of the car
	 */
	public String getModel(){
		return model;
	}
	
	/**
	 * Gets the car's price
	 * @return The price of the car
	 */
	public double getPrice(){
		return price;
	}
	
	/**
	 * Gets the car's mileage
	 * @return The mileage on the car
	 */
	public double getMileage(){
		return mileage;
	}
	
	/**
	 * Gets the car's color
	 * @return The color of the car
	 */
	public String getColor(){
		return color;
	}
		
	public String toString(){
		NumberFormat dollars = new DecimalFormat("#0.00");
		NumberFormat miles = new DecimalFormat("#0.0");
		StringBuilder string = new StringBuilder();
		string.append("VIN: " + VIN + "\n");
		string.append("Make: " + make + "\n");
		string.append("Model: " + model + "\n");
		string.append("Color: " + color + "\n");
		string.append("Mileage: " + miles.format(mileage) + "\n");
		string.append("Price: $" + dollars.format(price) + "\n");
		return string.toString();
	}
}