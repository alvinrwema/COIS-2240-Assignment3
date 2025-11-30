import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleRentalTest {
	
	private RentalSystem rentalSystem;
	
	@BeforeEach
	public void setUp() {
		//Instantiates the rental system so it can be used by all other tests
			rentalSystem = RentalSystem.getInstance();
	}

	@Test
	public void testLicensePlate() {
		
			//Instantiating multiple vehicles to use.
			Vehicle v1 = new Car("Honda","Civic",2022,4);
			Vehicle v2= new Car("Honda","Odyssey",2023,4);
			Vehicle v3=  new Car("Toyota","Camry",2023,2);
			Vehicle v4 = new Car("Tesla","Model 3",2022,2);
			Vehicle v5= new Car("Hyundai","Elantra",2022,2);
			Vehicle v6=  new Car("Toyota","Prius",2025,3);
			Vehicle v7=  new Car("Toyota","Corolla",2023,5);
			
			//Setting valid number plates for the 3 vehicles
			v1.setLicensePlate("AAA100");	
			v2.setLicensePlate("ABC567");
			v3.setLicensePlate("ZZZ999");
			
			//Asserting that they were properly set without issues since they are valid.
			
			assertTrue(v1.getLicensePlate().equals("AAA100"));
			assertTrue(v2.getLicensePlate().equals("ABC567"));
			assertTrue(v3.getLicensePlate().equals("ZZZ999"));
		     
		     //Asserting that an exption is thrown for invalid number plates.
		   
			 assertThrows(IllegalArgumentException.class, () -> v4.setLicensePlate( ""));
			 assertThrows(IllegalArgumentException.class, () -> v5.setLicensePlate(null));
			 assertThrows(IllegalArgumentException.class, () -> v6.setLicensePlate("AAA1000"));
			 assertThrows(IllegalArgumentException.class, () -> v7.setLicensePlate("ZZZ99"));
			 	 
     
	}
	

}
