/**
 * 
 */
package tme4.events;

/**
 * @author Chantal del Carmen
 * @date Feb 19, 2022
 *
 */

public class ThermostatNight extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThermostatNight(long delayTime) {
		super(delayTime);
	}
	
	public String toString() {
		return "Thermostat on night setting";
	}
	
	public void action() {
		System.out.println("In ThermostatNight.action()");

    	allowPause();
    	
		// Set key and value
		setKey("thermostat");
		setValue("night");	

		// Timing the event
		sleep(delayTime);  	

		// Print to GUI
		gc.appendToTextArea(this.toString());
		
		// Call function to add to List<TwoTuple>
		gc.addTwoTuple(key, value);
		
		// Remove event from event list
    	gc.removeEvent(this);		

	}
}
