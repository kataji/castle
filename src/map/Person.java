package map;

public class Person extends Feature {

	public Person(String description) {
		super(description);
		// TODO Auto-generated constructor stub
	}
	
	public void speak(){}
	
	public boolean receive(Item item){ 
		System.out.println(this + "：\n我不要");
		return false;
	}
}
