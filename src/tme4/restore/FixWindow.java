/**
 * 
 */
package tme4.restore;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

import tme4.GreenhouseControls;

/**
 * @author Chantal del Carmen
 * @date Mar 11, 2022
 *
 */
public class FixWindow implements Fixable {
	// Implements Fixable interface		  
	  
	// Fixes window and zeros out error codes
	@Override
	public void fix(GreenhouseControls gc) {
		gc.setVariable("windowOK", "true");		
		gc.setErrorCode(0);
		gc.setFixMsg("\nWindow has been fixed at " + new Date()
				+ "\nWindow OK: true");
	}

	// Logs to a text file in the current directory called fix.log
	// Prints to the console, and identify time and nature of
	// the fix
	@Override
	public void log(GreenhouseControls gc) {	
		System.out.println(gc.getFixMsg());
		System.out.println("Error code has been reset to: " + gc.getErrorCode());
		
		PrintWriter writer;
		
		try {
			writer = new PrintWriter("fix.log");
			writer.write(gc.getFixMsg());		
			writer.close();
			System.out.println("Fix has been logged in file fix.log.");
		} catch (FileNotFoundException e1) {
			System.out.println("File fix.log not found!");
		}	
	}
	  
	  
}
