package OOP.hw31;

public class OOP {

	public static void main(String[] args) {
		
		System.out.println("   << OOP(1) Encapsulation(«Ê¸Ë) >>");

		Car c = new Car();
		c.maxSpeed();
	}
}

class Car {
	String name;
	
	void maxSpeed() {
		System.out.println("180");
	}
}