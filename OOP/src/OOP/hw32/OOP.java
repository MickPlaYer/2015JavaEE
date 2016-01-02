package OOP.hw32;

public class OOP {

	public static void main(String[] args) {

		System.out.println("   << OOP(2) Inheriance(Ä~©Ó) >>");

		Porsche p = new Porsche();
		p.maxSpeed();
	}
}

class Car {
	String name;

	void maxSpeed() {
		System.out.println("180");
	}
}

class Porsche extends Car {
	void maxSpeed() {
		System.out.println("360");
	}
}