package tme4.restore;
/**
 * Title: Restore.java
 * Author: Chantal del Carmen
 * Student ID: 3514814
 * Date: 2022.02.15
 * Tested by: GreenhouseControls.java
 */
 
/**
 * DOCUMENTATION...
 */

/** 
 * METHODS
 * public GreenhouseControls deserialize() { - deserializes the object and restores it
 * private void printSystemState() { - prints to console the state of the deserialized object
 * 
 */
  
/** 
 * PURPOSE OF MAIN NAMES USED FOR VARIABLES, METHODS
 * Class, method and variable names made following assignment instructions, or by their
 * function/purpose.
 */
  
/**
 * CODE...
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

import tme4.GreenhouseControls;
import tme4.TwoTuple;
import tme4.events.Event;

public class Restore {
	
	private String filename = "";
//	private GreenhouseControls gc = null;
	
	public Restore() {}
	
	public Restore(String filename) {
	    this.filename = filename;			
	}

	// Deserializes the GreenhouseControls object from file and restores the object
	public GreenhouseControls deserialize() {
		
		GreenhouseControls gc = null;
		FileInputStream fileIn = null;
		ObjectInputStream objIn = null;
		
		try {
			fileIn = new FileInputStream(filename);
			objIn = new ObjectInputStream(fileIn);

			System.out.println("Deserializing file \"" + filename + "\" at " + new Date());
			gc = (GreenhouseControls)objIn.readObject();
			
			System.out.println("GC: " + gc);
			
			objIn.close();
			fileIn.close();
			
			System.out.println("Deserialization complete.");
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e1) {
			System.out.println("GreenhouseControls class not found.");
			e1.printStackTrace();
		}
		
		
		printSystemState(gc);
		return gc;
	}

	private void printSystemState(GreenhouseControls gc) {
		// Prints the current state of the saved system after deserialization
	
		System.out.println("\nSTATE OF GREENHOUSECONTROLS SYSTEM - EVENT LIST");
		
		for(Event e : gc.getEventList()) {
			System.out.println(e.getClass().getSimpleName());
		}
		
//		System.out.println("\nSTATE OF GREENHOUSECONTROLS SYSTEM - TT LIST");
//
//		for(TwoTuple tt : gc.getTwoTupleList()) {
//			System.out.println(tt);
//		}
	}

}

