package map;

public class Feature {
	private String description;
	
	public Feature (String description){
		this.description = description;
	}
	
	@Override
	public String toString(){
		return description;
	}
}
