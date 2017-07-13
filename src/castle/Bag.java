package castle;

import java.util.ArrayList;

import feature.Item;

public class Bag extends ArrayList<Item> {
	private int bagSizeLimit;
	private static final long serialVersionUID = -8498270186232603798L;
	
	public Bag ( int bagSizeLimit ){
		this.bagSizeLimit = bagSizeLimit;
	}

	@Override
	public boolean add(Item e) {
		if ( this.size() < bagSizeLimit ){
			return super.add(e);
		}
		else return false;
	}
//
//	@Override
//	public Item remove(int index) {
//		try{
//			return super.remove(index);
//		} catch (IndexOutOfBoundsException e) {
//			return null;
//		}
//	}
//	
//	@Override
//	public Item get(int index) {
//		try {
//			return super.get(index);
//		} catch (IndexOutOfBoundsException e) {
//			return null;
//		}
//	}

	@Override
	public String toString() {		
       	StringBuffer sb = new StringBuffer();
       	sb.append('(');
       	sb.append(this.size());
       	sb.append('/');
       	sb.append(bagSizeLimit);
       	sb.append(")  ");
    	for ( int i = 0; i < this.size(); i++ ){
    		sb.append('[');
    		sb.append( i );
    		sb.append(']');
    		sb.append(this.get(i));
    		sb.append("  ");
    	}
    	return sb.toString();
	}

	public void extend(int sizeInc) {
		bagSizeLimit += sizeInc;
	}
	
}
