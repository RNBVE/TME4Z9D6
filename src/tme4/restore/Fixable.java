package tme4.restore;
/**
 * Title: Fixable.java
 * Author: Chantal del Carmen
 * Student ID: 3514814
 * Date: 2022.02.15
 * Tested by: GreenhouseControls.java
 */

import tme4.GreenhouseControls;

/**
 * DOCUMENTATION...
 */

/** 
 * METHODS
 * 
 * void fix() - not implemented here
 * void log() - not implemented here
 */
  
/** 
 * PURPOSE OF MAIN NAMES USED FOR VARIABLES, METHODS
 * Class, method and variable names made following assignment instructions, or by their
 * function/purpose.
 */
  
/**
 * CODE...
 */

public interface Fixable {

	// turns Power on, fix window and zeros out error codes
	void fix(GreenhouseControls gc);

	// logs to a text file in the current directory called fix.log
	// prints to the console, and identify time and nature of
	// the fix
	void log(GreenhouseControls gc);
}

