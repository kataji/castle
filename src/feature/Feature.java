package feature;

public class Feature {
	private String name;
	
	public Feature (String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean accept ( Item item ){
		System.out.println(name + "：没有反应");
		return false;
	}
	
	public void check(){ 
		System.out.println("普普通通的" + name);
	}
	
}
