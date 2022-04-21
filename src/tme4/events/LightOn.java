/**
 * 
 */
package tme4.events;

/**
 * @author Chantal del Carmen
 * @date Feb 19, 2022
 *
 */

public class LightOn extends Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LightOn(long delayTime) {
		super(delayTime);
	}
  
    public String toString() { return "Light is on"; }
	
	public void action() {
		System.out.println("In LightOn.action()");
		
    	allowPause();
    	
		// Set key and value
		setKey("light");
		setValue("true");
		
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
