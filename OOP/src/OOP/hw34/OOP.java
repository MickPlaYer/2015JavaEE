package OOP.hw34;

public class OOP {

	public static void main(String[] args) {

		System.out.println("   << OOP(4) Abstract Function(©â¶H¨ç¦¡) >>");
		
		Car c = new Porsche();
		c.maxSpeed();

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