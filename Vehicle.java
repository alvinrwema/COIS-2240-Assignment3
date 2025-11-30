
public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }
    public Vehicle(String make, String model, int year) {
    	
    	this.make = capitalize(make);
    	this.model = capitalize(model);
    	
        this.year = year;
        this.status = VehicleStatus.Available;
        this.licensePlate = null;  
        
    }
    
    private String capitalize(String input) {
    	//Checks if input is null or empty
    	if(input == null || input.isEmpty()) 
    		return null;
    	else
    		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase(); //Returns the capitalized input
    }

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
    	//Throws exception if the plate is not valid 
    	if(isValid(plate)) { 
    		 this.licensePlate = plate == null ? null : plate.toUpperCase();
    	}else {
    		throw new IllegalArgumentException("Invalid License Plate entered.");  //Throws exception since plate is not valid .
    	}
    }
    private boolean isValid(String plate) { 
    	//Checks if the plate is null,empty or has a length that is not equal to 6.
    	if(plate == null || plate.isEmpty() || plate.length() != 6) {
    		return false;
    	 }
    	//Loops through first three items to check if they are characters
    	for(int i =0;i < 3;i++){
    		char item = plate.charAt(i);
    		//Checks if the item is a character
    		if(!Character.isLetter(item)) {
    			return false; 
    		}
    	 } 
    	//Loops through the last three to check if they are numbers.
    	for(int i =3;i < 6;i++){ 
    		char item = plate.charAt(i); 
    		//Checks if the item is a digit
    		if(!Character.isDigit(item)) {
    			return false; 
    		} 
    	}

    	return true;  //Returns true since the plate is valid 
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }

}
