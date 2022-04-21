/**
 * Title: ControllerException.java
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
 * 
 * public static void logException(Exception e) { - logs the exception, printing it to
 *  console and writing it to a text file
 */
  
/** 
 * PURPOSE OF MAIN NAMES USED FOR VARIABLES, METHODS
 * Class, method and variable names made following assignment instructions, or by their
 * function/purpose.
 */
  
/**
 * CODE...
 */

/* Based on code adapted from 
 * https://developervisits.wordpress.com/2017/02/26/define-custom-exception-with-error-code-in-java/ 
 */

package tme4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Logger;

public class GreenhouseControlsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private int errorCode = 0;
	private static Logger logger = Logger.getLogger("ControllerException");

	public GreenhouseControlsException(String errorMsg) {
		super(errorMsg);
//		this.errorCode = errorCode;
	}

	
	public static void logException(Exception e) {
	// Logs exception
		
		// Print exception log to console
		StringWriter trace = new StringWriter();
		e.printStackTrace(new PrintWriter(trace));		
		System.out.println();
		logger.severe(trace.toString());
		
		// Write exception log to text file "error.log"
		try {
			Date date = new Date(); 
			PrintWriter writer = new PrintWriter("error.log");
			writer.write(date.toString() + " ");
			writer.write(trace.toString());		
			writer.close();
			System.out.println("Exception logged in file error.log.");
		} catch (FileNotFoundException e1) {
			System.out.println("File error.log not found!");
		}			
	}
	
}
