package OOP.hw37;

public class OOP {

	public static void main(String[] args) {
		
		System.out.println("   << OOP(7) Interface(¤¶­±) >>");
		
		Engine e[] = { new Porsche(),
						new Buick(),
						new Toyota() };

		int size = e.length;
		for (int i = 0; i < size; i++)
			e[i].maxSpeed();
		
	}
}

class Car {
	String name;
}

interface Engine {
	abstract void maxSpeed();
}

class Porsche extends Car implements Engine {

	public  void maxSpeed() {
		System.out.println("360");
	}

}

class Buick extends Car implements Engine {

	public void maxSpeed() {
		System.out.println("280");
	}

}

class Toyota extends Car implements Engine {

	public void maxSpeed() {
		System.out.println("220");
	}
	
}