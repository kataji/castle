package feature;

import java.util.HashMap;

public class OppoDirs {
	HashMap<String, String> oppoDirs = new HashMap<>();
	
	public OppoDirs(){
		oppoDirs.put("south", "north");
		oppoDirs.put("north", "south");
		oppoDirs.put("east", "west");
		oppoDirs.put("west", "east");
	}
	
	public String get(String dir){
		return oppoDirs.get(dir);
	}
}
