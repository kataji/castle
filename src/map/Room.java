package map;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String description;
    private HashMap<String, Room> exits = new HashMap<>(); 
    private ArrayList<Feature> persons = new ArrayList<>();
    private ArrayList<Feature> items = new ArrayList<>();
    private static HashMap<String, String> oppoDirs = new HashMap<>();
    static {
    	oppoDirs.put("south", "north");
    	oppoDirs.put("north", "south");
    	oppoDirs.put("east", "west");
    	oppoDirs.put("west", "east");    	
    }

    public Room(String description) 
    {
        this.description = description;
    }

    public void setExit(String dir, Room room){
    	exits.put(dir, room);
    }
    
    public void setDoubleExit(String dir, Room room){
    	exits.put(dir, room);
    	room.setExit(getOppoDir(dir), this);
    }
    
    private String getOppoDir(String dir) {
    	return oppoDirs.get(dir);
    }

	private void addFeatures(ArrayList<Feature> featureList, Feature[] features){
    	for ( Feature c : features ){
    		featureList.add(c);
    	}
    }

    public void addPersons(Person...persons){
    	addFeatures(this.persons, persons);
    }
    
    public void addItems(Item...items){
    	addFeatures(this.items, items);
    }
    
    public Item removeItem(int index){
   		return (Item) items.remove(index);
    }
    
    @Override
    public String toString()
    {
        return description;
    }
    
    public String showExits(){
    	StringBuffer sb = new StringBuffer();
    	for ( String dir : exits.keySet() ){
    		sb.append(dir);
    		sb.append(' ');
    	}
    	return sb.toString();
    }
    
    private String showFeatures(ArrayList<Feature> features){
       	StringBuffer sb = new StringBuffer();
    	for ( int i = 0; i < features.size(); i++ ){
    		sb.append("[");
    		sb.append( i );
    		sb.append("]");
    		sb.append(features.get(i));
    		sb.append("  ");
    	}
    	return sb.toString();
    }
    
    public String showPersons(){
    	return showFeatures(persons);
    }
    
    public String showItems(){
    	return showFeatures(items);
    }
    
    public Room getExit(String direction){
    	return exits.get(direction);
    }
    
    public Person getPerson(int index){
    	return (Person) persons.get(index);
    }

    public Item getItem(int index) {
    	return (Item) items.get(index);
    }
}
