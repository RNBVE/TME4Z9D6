//: innerclasses/controller/Event.java
// The common methods for any control event.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @date  : Oct. 21, 2006
 *
 * Description: Event abstract class
 *
 */

package tme4.events;

import java.io.Serializable;

import tme4.GreenhouseControls;
import tme4.PauseResume;

public class Event extends PauseResume implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String key = null;
	protected String value = null;	
	protected long delayTime = 0;

	public Event() {}
	
	public Event(long delayTime) {
		this.delayTime = delayTime;
	}
	
	// Event.run() method to (1) time the Event,
	// (2) suspend/resume mechanism, and (3) call
	// action() method of the Event
	public void run() {	
		
		System.out.println("In Event.run() method, thread " + this.getClass().getSimpleName() + 
			     " is waiting to get lock");
		
	    // Acquiring lock
	    lock.lock();
	    
	    try {
	    	System.out.println("Thread " + this.getClass().getSimpleName() + " has got the lock");
	    	action();
	    } finally{
	    	// Always release the lock at the end
	    	lock.unlock();
	    	System.out.println("Thread " + this.getClass().getSimpleName() + " has released the lock");
	    }  
	}
	
	public void action() {}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
} ///:~
