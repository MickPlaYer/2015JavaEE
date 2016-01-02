package OOP.hw33;

public class OOP {

	public static void main(String[] args) {

		System.out.println("   << OOP(3) Cast(Âà«¬) >>");

		Car c = new Porsche();
		c.maxSpeed();
	}
}

class Car{
	String name;

	void maxSpeed(){
		System.out.println("180");
	}
}

class Porsche extends Car{
	void maxSpeed(){
		System.out.println("360");
	}
}