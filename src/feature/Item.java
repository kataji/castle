package feature;

public class Item extends Feature {

	protected boolean canBeInBag = true;
	
	public Item(String name) {
		super(name);
	}
	
	public boolean canBeInBag(){
		return canBeInBag;
	}
	
	public Item get(){ return this;}

}
