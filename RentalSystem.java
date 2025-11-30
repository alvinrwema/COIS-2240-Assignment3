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
	private RentalSystem() {
		loadData(); //Calls the loadData method so that all the saved details are loaded.
	}
	
	public static RentalSystem getInstance() {
		return instance; //Returns the instance of rentalSystem to work with.
	}
	
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    public boolean addVehicle(Vehicle vehicle) {
    	
    	//Checks for duplicate vehicle
    	if(findVehicleByPlate(vehicle.getLicensePlate() ) != null) {
    		System.out.printf("Error: A Vehicle with number plate %s is already in the system \n",vehicle.getLicensePlate());
    		return false;
    	}
    	
    	//Adds vehicle since no duplicate found
    	vehicles.add(vehicle);
    	saveVehicle(vehicle);
    	return true;
    	
    }

    public boolean addCustomer(Customer customer) {
    	//Checks for duplicate customer
    	if(findCustomerById(customer.getCustomerId()) != null) {
    		System.out.printf("Error:A customer with id %d is already in the system. \n",customer.getCustomerId());
    		return false;
    	}
    	
    	//Adds the customer since no duplicate found
        customers.add(customer);
        saveCustomer(customer); 
        return true;
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
     
     private void loadData() {
     	
     	loadVehicles();  //Calls this method so vehicles are loaded from vehicles.txt
     	loadCustomers();  //Calls this method so customers are loaded from customer.txt
     	loadRecords();  //Calls this method so records are loaded from rental_records.txt
     }
     
     private void loadVehicles() {
     	try {
 			BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"));
 			String line;
 			while((line = reader.readLine()) != null) {
 				//Moves to next line if the line is empty
 				 if (line.trim().isEmpty()) {
 				        continue;
 				    }
 				
 				String[] data = line.split(",");   //Reads in the data of the line into any array by splitting it with a comma
 				
 				//Gets the necessary fields of the vehicle using the order in which they were saved.
 				String type = data[0];  
 				String licensePlate = data[1];
 				String make = data[2];
 				String model = data[3];
 				int year =Integer.parseInt(data[4]);
 				Vehicle vehicle = null;
 				
 				if(type.equals("Car")) {
 					int numSeats = Integer.parseInt(data[5]);
 					vehicle = new Car(make,model,year,numSeats);
 				}
 				else if(type.equals("Minibus")) {
 					boolean isAccessible = Boolean.parseBoolean(data[5]);
 					vehicle = new Minibus(make,model,year,isAccessible);
 				}
 				else {
 					double cargoSize = Double.parseDouble(data[5]);
 					boolean hasTrailer = Boolean.parseBoolean(data[6]);
 					vehicle = new PickupTruck(make,model,year,cargoSize,hasTrailer);
 				}
 				
 				if(vehicle != null) {
 					vehicle.setLicensePlate(licensePlate);
 					vehicles.add(vehicle); //Adds the vehicle to the list 
 				}	
 			}
 			reader.close();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
     }
     private void loadCustomers() {
     	try {
 			BufferedReader reader = new BufferedReader(new FileReader("customers.txt"));
 			String line;
 			while((line = reader.readLine()) != null) {
 				//Moves to next line if the line is empty
 				 if (line.trim().isEmpty()) {
 				        continue;
 				    }
 				//Reads in the data of the line into any array by splitting it with a comma
 				String[] data = line.split(",");
 				
 				//Gets both the customer id and name details from the array
 				int customerId = Integer.parseInt(data[0]);
 				String customerName = data[1];
 				
 				Customer customer = new Customer(customerId,customerName);  //Creates a customer object using details read from the line
 				customers.add(customer); //Adds the customer to the list
 			}
 			reader.close();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
     }
     private void loadRecords() {
     	try {
 			BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"));
 			String line;
 			while((line = reader.readLine()) != null) {
 				//Moves to next line if the line is empty
 			    if (line.trim().isEmpty()) {
 			       continue;
 			    }
 				String[] data = line.split(",");
 				
 				//Finds the vehicle and customer objects using licensePlate and customerId.
 				Vehicle vehicle = findVehicleByPlate(data[0]);
 				Customer customer = findCustomerById(Integer.parseInt(data[1]));
 				
 				//Checks if either objects are null
 				if(vehicle == null || customer == null) {
 					continue; //Moves to next line to prevent any errors
 				}
 				LocalDate recordDate = LocalDate.parse(data[2]);
 				double amount = Double.parseDouble(data[3]);
 				String recordType = data[4];
 				
 				RentalRecord record =  new RentalRecord(vehicle,customer,recordDate,amount,recordType); //Creates record object using all the fields.
 				rentalHistory.addRecord(record); //Adds record to rentalHistory
 				
 				//Sets the status of the vehicle depending on the recordType
 	            if (recordType.equals("RENT")) {
 	                vehicle.setStatus(Vehicle.VehicleStatus.Rented);
 	            } 
 	            else if (recordType.equals("RETURN")) {
 	                vehicle.setStatus(Vehicle.VehicleStatus.Available);
 	            }
 				
 			
 			}
 			reader.close();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
     }
    	
    

    
}