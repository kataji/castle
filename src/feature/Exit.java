package feature;

import map.Room;

public class Exit extends Feature{
	private Room room;
	private boolean isLocked = false;
	private Item key;
	
	public Exit(Room room) {
		super("出口");
		this.room = room;
	}
	
	public Exit(Room room, Item key) {
		super("出口");
		this.room = room;
		this.isLocked = true;
		this.key = key;
	}
	
	public void lock( Item key ){
		if (isLocked == false){
			isLocked = true;
			this.key = key;
		}
	}
	
	public Room open(){
		if ( isLocked ){
			return null;
		}
		else{
			return room;
		}
	}
	
	public Room open ( Item item ){
		if ( !isLocked || item.toString().equals(key.toString())){
			return room;
		}
		else {
			return null;
		}
	}

	@Override
	public boolean accept(Item item) {
		if ( isLocked && item.toString().equals(key.toString())){
			isLocked = false;
			System.out.println(this + "：锁打开了");
			return true;
		}
		else {
			System.out.println(this + "：没有反应");
			return false;		
		}
	}

	@Override
	public void check() {
		if (!isLocked) 
			super.check();
		else
			System.out.println("上了锁的出口");
	}
	
	
}
