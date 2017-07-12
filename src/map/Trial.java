package map;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Trial {
	public static void main(String[] args){
    	try {
			BufferedReader inF = new BufferedReader(
					new InputStreamReader(
							new FileInputStream("rooms.txt"), "utf8"));
			
	    	String line;
	    	while((line = inF.readLine()) != null){
	    		System.out.println(line);
	    	}
	    	
	    	inF.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

	}
}
