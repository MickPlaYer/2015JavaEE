package OOP.hw36;

public class OOP {

	public static void main(String[] args) {

		System.out.println("   << OOP(6) Polymorphism(¦h«¬) >>");

		Car c[] = { new Porsche(),
					new Buick(),
					new Toyota() };

		int size = c.length;
		for (int i = 0; i < size; i++)
			c[i].maxSpeed();
	}
}

abstract class Car {
	
	String name;
	abstract void maxSpeed();
	
}

class Porsche extends Car {
	
	void maxSpeed() {
		System.out.println("360");
	}
	
}

class Buick extends Car {
	
	void maxSpeed() {
		System.out.println("280");
	}
	
}

class Toyota extends Car {
	
	void maxSpeed() {
		System.out.println("220");
	}
	
}