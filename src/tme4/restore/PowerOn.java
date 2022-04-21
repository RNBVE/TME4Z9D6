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
public class PowerOn implements Fixable {
	// Implements Fixable interface
	
	// Fixes power and zeros out error code
	@Override
	public void fix(GreenhouseControls gc) {		
		gc.setVariable("powerOn", "true");		
		gc.setErrorCode(0);
		gc.setFixMsg("\nPower has been fixed and turned back on at " + new Date()
				+ "\nPower On: true");
	}

	@Override
	public void log(GreenhouseControls gc) {
		System.out.println(gc.getFixMsg());
		System.out.println("Error code has been reset to: " + gc.getErrorCode());
		
		PrintWriter writer = null;
		
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
