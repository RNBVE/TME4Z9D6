/**
 * 
 */
package tme4.events;

import java.util.concurrent.TimeUnit;

/**
 * @author Chantal del Carmen
 * @date Feb 19, 2022
 *
 */

public class Terminate extends Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Terminate(long delayTime) {
		super(delayTime);
	}

	public String toString() { return "Terminating";  }

	@Override
	public void run() {
		action();
	}
//	@Override
	public void action() {
		System.out.println("In Terminate.action()");
		
		sleep(delayTime);  	

		// Print to GUI
		gc.appendToTextArea(this.toString());
		
		// Call function to add to List<TwoTuple>
		gc.addTwoTuple(key, value);
		
    	gc.removeEvent(this);
    	    	
    	try {
			executor.awaitTermination(900, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
		// Exit program
		System.exit(0);
    	
	}
}
