package OOP.hw35;

public class OOP {

	public static void main(String[] args) {

		System.out.println("   << OOP(5) Instances(ª«¥ó®a±Ú) >>");

		Porsche p = new Porsche();
		Buick b = new Buick();
		Toyota t = new Toyota();

		p.maxSpeed();
		b.maxSpeed();
		t.maxSpeed();

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