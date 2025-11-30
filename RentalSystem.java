import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
	
	private static RentalSystem instance = new RentalSystem(); //Holds the instance of the rental system and initialises it.
	private RentalSystem() {}
	
	public static RentalSystem getInstance() {
		return instance; //Returns the instance of rentalSystem to work with.
	}
	
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);  //saves the vehicle to a file.
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer); //Saves the customer to a file.
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            saveRecord(new RentalRecord(vehicle, customer, date, amount, "RENT")); //Saves the rent record a file.
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            saveRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN")); //Saves the return record to a file.
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
    
    //Saves Vehicle details
    public void saveVehicle(Vehicle vehicle) {
    	try {
			BufferedWriter writer  = new BufferedWriter(new FileWriter("vehicles.txt",true)); //Writes to the vehicles.txt in append mode.
			String type;
			
			if (vehicle instanceof Car) {
                type = "Car";
				Car car = (Car) vehicle;
				writer.write(type + "," + vehicle.getLicensePlate() + "," + vehicle.getMake() + "," +
								vehicle.getModel() + "," + vehicle.getYear() + "," + car.getNumSeats()
				);
		   }
		   else if (vehicle instanceof Minibus) {
                type = "Minibus";
                Minibus minibus = (Minibus) vehicle;
                writer.write(type + "," + vehicle.getLicensePlate() + "," +
						vehicle.getMake() + "," + vehicle.getModel() + "," + vehicle.getYear() + "," + minibus.getIsAccessible()
				);
		   }
		   else {
			   type = "PickupTruck";
			   
			   PickupTruck pickupTruck = (PickupTruck) vehicle;
			   
			   writer.write(type + "," + vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + 
					   vehicle.getModel() + "," + vehicle.getYear() +  "," + pickupTruck.getCargoSize() +  "," + pickupTruck.hasTrailer()
				);
			   
		   }
			writer.newLine(); // creates a newline after inserting the vehicle details.
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    //Saves the customers details to a file.
    public void saveCustomer(Customer customer) {
    	try {
			BufferedWriter writer  = new BufferedWriter(new FileWriter("customers.txt",true)); //saves the customers details to customers.txt in append mode
			writer.write(customer.getCustomerId() + "," + customer.getCustomerName() );
			writer.newLine(); // creates a newline after inserting the customer details.
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    	//Saves both the rent and return records to a file
     public void saveRecord(RentalRecord record) {
        try {
    		BufferedWriter writer  = new BufferedWriter(new FileWriter("rental_records.txt",true)); //saves the rental record details to rental_records in append mode.
    		//Gets both the vehicle and customer objects
    		Vehicle vehicle = record.getVehicle();
    		Customer customer = record.getCustomer();
    		
    		//Writes to the file by using licencePlate,customer id and record details
    		writer.write(vehicle.getLicensePlate()+ "," + customer.getCustomerId() +  "," + record.getRecordDate() +   "," +
    			record.getTotalAmount() + "," + record.getRecordType());
    			
    		
    		writer.newLine(); // creates a newline after inserting the rental details.
    		writer.close();
    			
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
        	
   }
    	
    

    
}