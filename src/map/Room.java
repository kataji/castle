package map;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.regexp.internal.recompile;

import feature.Exit;
import feature.Feature;
import feature.Item;
import feature.OppoDirs;
import feature.Person;

public class Room {
    private String description;
    private ArrayList<Feature> persons = new ArrayList<>();
    private ArrayList<Feature> items = new ArrayList<>();
    private HashMap<String, Exit> exits = new HashMap<>();
    private static OppoDirs oppodirs = new OppoDirs();
    
    public Room(String description) 
    {
        this.description = description;
    }

    //给房间添加特性
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
    
	public void setExit(String dir, Room room){
		exits.put(dir, new Exit(room) );
	}
	
	public void setDoubleExit(String dir, Room room){
		exits.put(dir, new Exit(room));
		room.exits.put(oppodirs.get(dir), new Exit(this));
	}
	
	public void setExit(String dir, Room room, Item key){
		exits.put(dir, new Exit(room, key));
	}
    
    //从房间移除特性
    public Feature removeFeature(ArrayList<Feature> features, int index){
   		try{
   			return features.remove(index);
   		} catch (IndexOutOfBoundsException e) {
   			return null;
   		}
    }
    
    public Item removeItem(int index){
    	return (Item)removeFeature(items, index);
    }
    
    public Person removePerson(Person person){
    	persons.remove(person);
    	return person;
    }
    
    //查看房间相关信息
    @Override
    public String toString()
    {
        return description;
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
    
    public String showExits(){
    	StringBuffer sb = new StringBuffer();
    	for ( String dir : exits.keySet() ){
    		sb.append(dir);
    		sb.append("  ");
    	}
    	return sb.toString();
    }
    
    //获得房间特性的指针
    public Feature getFeature (ArrayList<Feature> features, int index){
    	try{
    		return features.get(index);
    	} catch (IndexOutOfBoundsException e){
    		return null;
    	}
    }    
    
    public Person getPerson(int index){
    	return (Person)getFeature(persons, index);
    }

    public Item getItem(int index) {
    	return (Item)getFeature(items, index);
    }
    
    public Exit getExit(String dir) {
    	return exits.get(dir);
    }
    
}
