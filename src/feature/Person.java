package feature;

public class Person extends Feature {

	public Person(String name) {
		super(name);
	}

	@Override
	public boolean accept(Item item) {
		System.out.println(this + "：我不要");
		return false;
	}
	
}
